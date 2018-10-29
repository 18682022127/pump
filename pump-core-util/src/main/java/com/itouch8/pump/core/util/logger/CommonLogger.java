package com.itouch8.pump.core.util.logger;

import com.itouch8.pump.core.util.CoreUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonLogger {

    public static void debug(String msg, Object... args) {
    	if(log.isDebugEnabled()) {
    		log.debug(format(msg, args), args);
        }
    }
    
    public static void debug(Throwable e) {
        if(log.isDebugEnabled()) {
        	log.debug(e.getMessage(), e);
        }
    }
    
    public static void info(String msg, Object... args) {
    	if(log.isInfoEnabled()) {
    		log.info(format(msg, args), args);
        }
    }

    
    public static void info(Throwable e) {
    	 if(log.isInfoEnabled()) {
         	log.info(e.getMessage(), e);
         }
    }
    
    public static void warn(String msg, Object... args) {
    	if(log.isWarnEnabled()) {
    		log.warn(format(msg, args), args);
        }
    }
    
    public static void warn(Throwable e) {
    	 if(log.isWarnEnabled()) {
          	log.warn(e.getMessage(), e);
          }
    }
    
    public static void error(String msg, Object... args) {
    	if(log.isErrorEnabled()) {
    		log.error(format(msg, args), args);
        }
    }

    
    public static void error(Throwable e) {
    	if(log.isErrorEnabled()) {
          	log.error(e.getMessage(), e);
          }
    }
    
    private static String format(String pattern, Object... arguments) {
        if (null == pattern) {
            return null;
        }
        if (null == arguments) {
            return pattern;
        }
        Object[] args = new Object[arguments.length];
        for (int i = arguments.length - 1; i >= 0; i--) {
            args[i] = arguments[i];
        }
        return CoreUtils.format(pattern, args);
    }

}
