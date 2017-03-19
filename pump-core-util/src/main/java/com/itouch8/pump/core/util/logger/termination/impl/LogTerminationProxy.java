package com.itouch8.pump.core.util.logger.termination.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.logger.level.LogLevel;
import com.itouch8.pump.core.util.logger.termination.ILogTermination;
import com.itouch8.pump.core.util.stack.IStack;


public class LogTerminationProxy implements ILogTermination {

    
    @Override
    public void write(LogLevel level, IStack stack) {
        Logger logger = getLogger(stack);
        String message = stack.getMessage();
        Throwable e = stack.getThrowable();
        switch (level) {
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug(message, e);
                }
                break;
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info(message, e);
                }
                break;
            case WARN:
                if (logger.isWarnEnabled()) {
                    logger.warn(message, e);
                }
                break;
            case ERROR:
                if (logger.isErrorEnabled()) {
                    logger.error(message, e);
                }
                break;
        }
    }

    
    protected Logger getLogger(IStack stack) {
        Logger logger = stack.getLogger();
        if (null == logger) {
            String key = BaseConfig.getScanPackage();
            if (null != stack && null != stack.getStack()) {
                key = stack.getStack().getClassName();
            }
            logger = map.get(key);
            if (null == logger) {
                synchronized (map) {
                    logger = map.get(key);
                    if (null == logger) {
                        logger = LoggerFactory.getLogger(key);
                        map.put(key, logger);
                    }
                }
            }
        }
        return logger;
    }

    private static final Map<String, Logger> map = new HashMap<String, Logger>();
}
