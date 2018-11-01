package com.itouch8.pump.core.util.exception;

import java.lang.reflect.InvocationTargetException;

import com.itouch8.pump.ReturnCodes;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.env.EnvConsts;
import com.itouch8.pump.core.util.track.Tracker;

public class Throw {

    public static String getExceptionLocalMessage(String message, Object... args) {
        return getExceptionLocalMessage(ReturnCodes.SYSTEM_ERROR.code,message, null, args);
    }

    public static String getExceptionLocalMessage(Throwable cause, Object... args) {
        return getExceptionLocalMessage(ReturnCodes.SYSTEM_ERROR.code, ReturnCodes.SYSTEM_ERROR.msg,cause, args);
    }

    public static String getExceptionLocalMessage(String code,String message, Throwable cause, Object... args) {
        PumpExceptionInnerProxy proxy = createRuntimeException(code,message, cause, args).getProxy();
        return proxy.getMessage();
    }

    public static void throwRuntimeException(String code, Object... args) {
        throw createRuntimeException(code, ReturnCodes.SYSTEM_ERROR.msg,null, args);
    }

    public static void throwRuntimeException(Throwable e) {
        throw createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, ReturnCodes.SYSTEM_ERROR.msg,e);
    }

    public static void throwRuntimeException(String code, Throwable e, Object... args) {
        throw createRuntimeException(code, ReturnCodes.SYSTEM_ERROR.msg,e, args);
    }

    public static PumpRuntimeException createRuntimeException(String code,String message, Object... args) {
        return createRuntimeException(code, message,null, args);
    }
    
    public static PumpRuntimeException createRuntimeException(String message,Throwable e, Object... args) {
        return createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, message,e, args);
    }
    
    public static PumpRuntimeException createRuntimeException(Throwable e,Object... args) {
        return createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, ReturnCodes.SYSTEM_ERROR.msg,e,args);
    }
    
    public static PumpRuntimeException createRuntimeException(String message,Throwable e) {
        return createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, message,e);
    }

    public static PumpRuntimeException createRuntimeException(Throwable e) {
        return createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, ReturnCodes.SYSTEM_ERROR.msg, e);
    }

    public static PumpRuntimeException createRuntimeException(String code,String message, Throwable e, Object... args) {
        if (e instanceof PumpRuntimeException) {
            return (PumpRuntimeException) e;
        } else if (e instanceof PumpException) {
            return new PumpRuntimeException((PumpException) e);
        } else if (e instanceof InvocationTargetException) {
            return createRuntimeException(code,message, ((InvocationTargetException) e).getTargetException(), args);
        } else {
            PumpExceptionInnerProxy proxy = new PumpExceptionInnerProxy(code,message, e, args);
            return new PumpRuntimeException(proxy);
        }
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
            sb.append("Code:" + code).append(lineSeparator);
        }
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
        private String code;
        private String message;
        private Throwable cause;
        private Object[] args;

        PumpExceptionInnerProxy(String code,String message, Throwable cause, Object... args) {
            this.trackId = Tracker.getCurrentTrackId();
            this.code = code;
            this.cause = cause;
            this.args = args;
            this.message = message;
        }


        String getTrackId() {
            return trackId;
        }


        String getCode() {
            return code;
        }

        String getMessage() {
        	String localMessage = CoreUtils.getMessage(this.message , args);
            if (CoreUtils.isBlank(localMessage)) {
            	localMessage = this.message;
            }
            return localMessage;
        }

        Throwable getCause() {
            return cause;
        }
    }
}
