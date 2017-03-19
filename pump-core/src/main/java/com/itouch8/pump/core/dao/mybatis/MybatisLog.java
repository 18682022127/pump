package com.itouch8.pump.core.dao.mybatis;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.core.util.logger.LogMonitor;
import com.itouch8.pump.core.util.logger.level.LogLevel;


public class MybatisLog extends Slf4jImpl {

    private Logger logger;

    private static final Map<String, Logger> map = new HashMap<String, Logger>();

    
    public MybatisLog(String clazz) {
        super(clazz);
        try {
            Log log = (Log) FieldUtils.readField(this, "log", true);
            try {
                logger = (Logger) FieldUtils.readField(log, "logger", true);// Slf4jLocationAwareLoggerImpl
            } catch (Exception e) {
                logger = (Logger) FieldUtils.readField(log, "log", true);// Slf4jLoggerImpl
            }
        } catch (Exception i) {
            logger = LoggerFactory.getLogger(clazz);
        }
    }

    
    @Override
    public boolean isDebugEnabled() {
        return super.isDebugEnabled();
    }

    
    @Override
    public boolean isTraceEnabled() {
        return super.isTraceEnabled();
        // return true;
    }

    
    @Override
    public void error(String s, Throwable e) {
        if (isEnabled(s, LogLevel.ERROR)) {
            CommonLogger.error(s, e, logger);
        }
    }

    
    @Override
    public void error(String s) {
        if (isEnabled(s, LogLevel.ERROR)) {
            CommonLogger.error(s, null, logger);
        }
    }

    
    @Override
    public void debug(String s) {
        if (isEnabled(s, LogLevel.DEBUG)) {
            CommonLogger.debug(s, null, logger);
        }
    }

    
    @Override
    public void trace(String s) {
        if (isEnabled(s, LogLevel.DEBUG)) {
            CommonLogger.debug(s, null, logger);
        }
    }

    
    @Override
    public void warn(String s) {
        if (isEnabled(s, LogLevel.WARN)) {
            CommonLogger.warn(s, null, logger);
        }
    }

    
    private boolean isEnabled(String msg, LogLevel level) {
        if (LogMonitor.isMonitoring()) {
            return true;
        } else {
            String type = getType(msg);
            if (!CoreUtils.isBlank(type)) {
                Logger l = getLogger(type);
                if (null == l) {
                    return true;
                } else {
                    switch (level) {
                        case DEBUG:
                            return l.isDebugEnabled();
                        case INFO:
                            return l.isInfoEnabled();
                        case WARN:
                            return l.isWarnEnabled();
                        case ERROR:
                            return l.isErrorEnabled();
                        default:
                            return true;
                    }
                }
            }
            return true;
        }

    }

    
    private String getType(String msg) {
        Map<Pattern, String> mybatisLogTypeMapping = PumpConfig.getMybatisLogTypeMapping();
        if (null != mybatisLogTypeMapping && !CoreUtils.isBlank(msg)) {
            for (Pattern p : mybatisLogTypeMapping.keySet()) {
                if (p.matcher(msg).find()) {
                    return mybatisLogTypeMapping.get(p);
                }
            }
        }
        return null;
    }

    
    private Logger getLogger(String type) {
        Logger logger = map.get(type);
        if (null == logger) {
            synchronized (map) {
                logger = map.get(type);
                if (null == logger) {
                    logger = LoggerFactory.getLogger(type);
                    map.put(type, logger);
                }
            }
        }
        return logger;
    }
}
