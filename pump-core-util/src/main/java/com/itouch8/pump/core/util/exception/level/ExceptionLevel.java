package com.itouch8.pump.core.util.exception.level;


public enum ExceptionLevel {
    
    INFO,
    
    ERROR,
    
    FATAL;

    
    public static ExceptionLevel instance(String name) {
        for (ExceptionLevel el : values()) {
            if (el.name().equalsIgnoreCase(name)) {
                return el;
            }
        }
        return FATAL;
    }

    
    public String getDescription() {
        return "Exception-Level:" + this.name();
    }
}
