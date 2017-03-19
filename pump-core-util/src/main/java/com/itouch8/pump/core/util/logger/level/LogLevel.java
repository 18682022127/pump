package com.itouch8.pump.core.util.logger.level;


public enum LogLevel {
    DEBUG, INFO, WARN, ERROR;

    
    public String getDescription() {
        switch (this) {// 这里为了打印的时候格式一致，补上相应的空格
            case DEBUG:
                return "DEBUG";
            case INFO:
                return "INFO ";
            case WARN:
                return "WARN ";
            case ERROR:
                return "ERROR";
        }
        return null;
    }
}
