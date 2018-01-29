package com.itouch8.pump.core.util.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.env.EnvConsts;
import com.itouch8.pump.core.util.exception.handler.IExceptionHandler;
import com.itouch8.pump.core.util.exception.level.ExceptionLevel;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;
import com.itouch8.pump.core.util.exception.meta.IExceptionMeta;
import com.itouch8.pump.core.util.exception.meta.IExceptionMetaLoader;
import com.itouch8.pump.core.util.track.Tracker;

public class Throw {

    public static IExceptionMeta lookupExceptionMeta(String code, Throwable cause) {
        IExceptionMetaLoader loader = BaseConfig.getExceptionMetaLoader();
        return null == loader ? null : loader.lookup(code, cause);
    }

    public static String getExceptionLocalMessage(String code, Object... args) {
        return getExceptionLocalMessage(code, null, args);
    }

    public static String getExceptionLocalMessage(Throwable cause, Object... args) {
        return getExceptionLocalMessage(null, cause, args);
    }

    public static String getExceptionLocalMessage(String code, Throwable cause, Object... args) {
        PumpExceptionInnerProxy proxy = createRuntimeException(code, cause, args).getProxy();
        return proxy.getMessage();
    }

    public static void throwRuntimeException(String code, Object... args) {
        throw createRuntimeException(code, null, args);
    }

    public static void throwRuntimeException(Throwable e) {
        throw createRuntimeException(null, e);
    }

    public static void throwRuntimeException(String code, Throwable e, Object... args) {
        throw createRuntimeException(code, e, args);
    }

    public static PumpRuntimeException createRuntimeException(String code, Object... args) {
        return createRuntimeException(code, null, args);
    }

    public static PumpRuntimeException createRuntimeException(Throwable e) {
        return createRuntimeException(null, e);
    }

    public static PumpRuntimeException createRuntimeException(String code, Throwable e, Object... args) {
        if (e instanceof PumpRuntimeException) {
            return (PumpRuntimeException) e;
        } else if (e instanceof PumpException) {
            return new PumpRuntimeException((PumpException) e);
        } else if (e instanceof InvocationTargetException) {
            return createRuntimeException(code, ((InvocationTargetException) e).getTargetException(), args);
        } else {
            PumpExceptionInnerProxy proxy = new PumpExceptionInnerProxy(code, e, args);
            return new PumpRuntimeException(proxy);
        }
    }

    public static String getMessageWithoutTrackId(Throwable e) {
        PumpExceptionInnerProxy proxy = createRuntimeException(e).getProxy();
        return proxy.getMessage();
    }

    public static String getShortMessage(Throwable e) {
        return getShortMessage0(e, EnvConsts.LINE_SEPARATOR);
    }

    public static String getShortMessage(Throwable e, String lineSeparator) {
        return getShortMessage0(e, lineSeparator);
    }

    public static String getMessage(Throwable e) {
        return getExceptionMessage(e, EnvConsts.LINE_SEPARATOR, false);
    }

    public static String getMessage(Throwable e, String lineSeparator) {
        return getExceptionMessage(e, lineSeparator, false);
    }

    public static String getStackMessage(Throwable e) {
        return getExceptionMessage(e, EnvConsts.LINE_SEPARATOR, true);
    }

    public static String getStackMessage(Throwable e, String lineSeparator) {
        return getExceptionMessage(e, lineSeparator, true);
    }

    private static String getExceptionMessage(Throwable e, String lineSeparator, boolean forcePrintStack) {
        StringBuffer sb = new StringBuffer(getShortMessage0(e, lineSeparator));
        if (forcePrintStack || ExceptionMonitor.isMonitoring()) {
            StackTraceElement[] trace = e.getStackTrace();
            for (int i = 0; i < trace.length; i++) {
                sb.append("\tat ").append(trace[i]).append(lineSeparator);
            }
            Throwable t = e.getCause();
            if (null != t) {
                setTrace(t, sb, trace, lineSeparator);
            }
        }
        if (!CoreUtils.isBlank(lineSeparator)) {
            sb.delete(sb.lastIndexOf(lineSeparator), sb.length());
        }
        return sb.toString();
    }

    private static String getShortMessage0(Throwable e, String lineSeparator) {
        StringBuffer sb = new StringBuffer();
        PumpExceptionInnerProxy proxy = createRuntimeException(e).getProxy();
        if (null != proxy.getTrackId()) {
            //sb.append("TrackId:" + proxy.getTrackId()).append(lineSeparator);
        }
        String message = proxy.getMessage();
        String code = proxy.getCode();
        if (null != code && !code.equals(message)) {
            //sb.append("Code:" + code).append(lineSeparator);
        }
        // String pCode = proxy.getParentCode();
        // if(null != pCode && !pCode.startsWith("##")){
        // sb.append("ParentCode:"+pCode).append(lineSeparator);
        // }
        // if(null != proxy.getLevel()){
        // sb.append(proxy.getLevel().getDescription()).append(lineSeparator);
        // }
        if (null != message) {
            sb.append(message).append(lineSeparator);
        }
        Throwable cause = getRootCause(e);
        if (null != cause && !cause.equals(e)) {
            String c = cause.getMessage();
            if (!CoreUtils.isBlank(c)) {
                sb.append(c).append(lineSeparator);
            }
        }
        return sb.toString();
    }

    private static Throwable getRootCause(Throwable e) {
        if (null == e) {
            return null;
        }
        Throwable rootCause = null;
        Throwable cause = e.getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            if (e instanceof InvocationTargetException) {
                cause = ((InvocationTargetException) cause).getTargetException();
            } else {
                cause = cause.getCause();
            }
        }
        return rootCause;
    }

    private static void setTrace(Throwable ourCause, StringBuffer info, StackTraceElement[] causedTrace, String lineSeparator) {
        StackTraceElement[] trace = ourCause.getStackTrace();
        int m = trace.length - 1, n = causedTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }
        int framesInCommon = trace.length - 1 - m;

        info.append("Caused by: ").append(ourCause).append(lineSeparator);
        for (int i = 0; i <= m; i++) {
            info.append("\tat ").append(trace[i]).append(lineSeparator);
        }

        if (framesInCommon != 0) {
            info.append("\t... ").append(framesInCommon).append(" more").append(lineSeparator);
        }

        // Recurse if we have a cause
        Throwable cause = ourCause.getCause();
        if (cause != null) {
            setTrace(cause, info, trace, lineSeparator);
        }
    }

    static class PumpExceptionInnerProxy {
        private String trackId;
        private String parentCode;
        private String code;
        private String message;
        private String view;
        private ExceptionLevel level;
        private List<IExceptionHandler> handlers;
        private Throwable cause;
        private AtomicBoolean load = new AtomicBoolean(false);
        private Object[] args;

        PumpExceptionInnerProxy(String code, Throwable cause, Object... args) {
            this.trackId = Tracker.getCurrentTrackId();
            this.code = code;
            this.cause = cause;
            this.args = args;
        }

        private boolean doMessageResolver(IExceptionMeta meta, Object... args) {
            String localeMessageKey = meta.getMessageKey();
            if (!CoreUtils.isBlank(localeMessageKey)) {// 国际化key值存在
                this.message = CoreUtils.getMessage(localeMessageKey, args);
                if (CoreUtils.isBlank(this.message)) {// 但是国际化配置文件中不存在对应的key配置
                    throw new IllegalArgumentException("not found the message key[" + localeMessageKey + "] in exception property files, exception code is " + this.code + ".");
                }
                return true;
            }
            return false;
        }

        private boolean doViewResolver(IExceptionMeta meta) {
            if (!CoreUtils.isBlank(meta.getView())) {
                this.view = meta.getView();
                return true;
            }
            return false;
        }

        private boolean doLevelResolver(IExceptionMeta meta) {
            if (null != meta.getLevel()) {
                this.level = meta.getLevel();
                return true;
            }
            return false;
        }

        private boolean doHandlersResolver(IExceptionMeta meta) {
            if (null != meta.getHandlers() && !meta.getHandlers().isEmpty()) {
                this.handlers = meta.getHandlers();
                return true;
            }
            return false;
        }

        private void doResolver() {
            if (!load.get()) {
                synchronized (load) {
                    if (!load.get()) {
                        try {
                            boolean messageStatus = false, viewStatus = false, levelStatus = false, handlerStatus = false;
                            IExceptionMeta meta = lookupExceptionMeta(code, cause);
                            if (null == meta) {// 未找到元信息
                                this.parentCode = null;
                                this.code = CoreUtils.isBlank(code) ? BaseConfig.getExceptionCode() : code;
                            } else {
                                this.parentCode = meta.getParentCode();
                                this.code = meta.getCode();
                                boolean status = false;
                                while (!status) {
                                    messageStatus = messageStatus || this.doMessageResolver(meta, args);
                                    viewStatus = viewStatus || this.doViewResolver(meta);
                                    levelStatus = levelStatus || this.doLevelResolver(meta);
                                    handlerStatus = handlerStatus || this.doHandlersResolver(meta);
                                    String type = meta.getParentCode();
                                    if (!CoreUtils.isBlank(type)) {
                                        meta = lookupExceptionMeta(type, null);
                                    } else {
                                        meta = null;
                                    }
                                    status = (meta == null) || (messageStatus && viewStatus && levelStatus && handlerStatus);
                                }
                            }
                            if (!messageStatus) {
                                this.message = CoreUtils.getMessage(this.code, args);
                                if (CoreUtils.isBlank(this.message)) {
                                    this.message = this.code;
                                    this.code = ExceptionCodes.YT000000;
                                }
                            }
                            if (!viewStatus) {
                                this.view = BaseConfig.getExceptionView();
                            }
                            if (!levelStatus) {
                                this.level = BaseConfig.getExceptionLevel();
                            }
                            if (!handlerStatus) {
                                this.handlers = BaseConfig.getExceptionHandlers();
                            }
                        } finally {
                            load.set(true);
                        }
                    }
                }
            }
        }

        String getTrackId() {
            return trackId;
        }

        String getParentCode() {
            doResolver();
            return parentCode;
        }

        String getCode() {
            doResolver();
            return code;
        }

        String getMessage() {
            doResolver();
            return message;
        }

        String getView() {
            doResolver();
            return view;
        }

        ExceptionLevel getLevel() {
            doResolver();
            return level;
        }

        List<IExceptionHandler> getHandlers() {
            doResolver();
            return handlers;
        }

        Throwable getCause() {
            return cause;
        }
    }
}
