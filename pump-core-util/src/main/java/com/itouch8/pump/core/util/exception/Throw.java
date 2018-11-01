package com.itouch8.pump.core.util.exception;

import java.lang.reflect.InvocationTargetException;

import com.itouch8.pump.ReturnCodes;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.env.EnvConsts;

/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 异常工具类<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public class Throw {

    /**
     * 抛出异常
     * 
     * @param code
     * @param message
     * @param args
     */
    public static void throwRuntimeException(String code,String message, Object... args) {
        throw createRuntimeException(code,message, null, args);
    }
    
    /**
     * 抛出异常
     * 
     * @param message
     * @param args
     */
    public static void throwRuntimeException(String message, Object... args) {
       throw createRuntimeException(ReturnCodes.SYSTEM_ERROR.code,message, null, args);
    }

    /**
     * 抛出异常
     * 
     * @param e
     */
    public static void throwRuntimeException(Throwable e) {
        throw createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, e.getMessage(), e);
    }

    /**
     * 抛出异常
     * 
     * @param code
     * @param e
     * @param args
     */
    public static void throwRuntimeException(String code, Throwable e, Object... args) {
        throw createRuntimeException(code,e.getMessage(), e, args);
    }

    /**
     * 创建异常
     * 
     * @param code
     * @param message
     * @param args
     * @return
     */
    public static PumpRuntimeException createRuntimeException(String code,String message, Object... args) {
        return createRuntimeException(code, message, args);
    }
    
    public static PumpRuntimeException createRuntimeException(String message, Throwable e,Object... args) {
        return createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, message, args);
    }

    public static PumpRuntimeException createRuntimeException(Throwable e) {
        return createRuntimeException(ReturnCodes.SYSTEM_ERROR.code,e.getMessage(), e);
    }

    public static PumpRuntimeException createRuntimeException(String code,String message, Throwable e, Object... args) {
        if (e instanceof PumpRuntimeException) {
            return (PumpRuntimeException) e;
        } else if (e instanceof PumpException) {
            return new PumpRuntimeException((PumpException) e);
        } else if (e instanceof InvocationTargetException) {
            return createRuntimeException(code,e.getMessage(), ((InvocationTargetException) e).getTargetException(), args);
        } else {
            PumpExceptionInnerProxy proxy = new PumpExceptionInnerProxy(code,message, e, args);
            return new PumpRuntimeException(proxy);
        }
    }

    //内部代理类
    static class PumpExceptionInnerProxy {
        private String code;
        private String message;
        private Throwable e;
        private Object[] args;

        PumpExceptionInnerProxy(String code,String message, Throwable cause, Object... args) {
            this.code = code;
            this.e = cause;
            this.args = args;
            this.message = message;
        }
        
        public String getCode() {
            return code;
        }

        public String getMessage() {
        	String localMessage = CoreUtils.getMessage(this.message , args);
            if (CoreUtils.isBlank(localMessage)) {
            	localMessage = this.message;
            }
            return localMessage;
        }
        
        public  String getShortMessage() {
            return getShortMessage0(EnvConsts.LINE_SEPARATOR);
        }

        public String getShortMessage(String lineSeparator) {
            return getShortMessage0(lineSeparator);
        }

        public String getMessage(String lineSeparator) {
            return getExceptionMessage(lineSeparator, false);
        }

        public String getStackMessage() {
            return getExceptionMessage(EnvConsts.LINE_SEPARATOR, true);
        }

        public String getStackMessage(String lineSeparator) {
            return getExceptionMessage(lineSeparator, true);
        }

        private String getExceptionMessage(String lineSeparator, boolean forcePrintStack) {
            StringBuffer sb = new StringBuffer(getShortMessage0(lineSeparator));
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

        private String getShortMessage0(String lineSeparator) {
            PumpExceptionInnerProxy proxy = createRuntimeException(this.code,this.message,e,args).getProxy();
            String message = proxy.getMessage();
            String code = proxy.getCode();
            StringBuffer sb = new StringBuffer();
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

        Throwable getCause() {
            return e;
        }
    }
}
