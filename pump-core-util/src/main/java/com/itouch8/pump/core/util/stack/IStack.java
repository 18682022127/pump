package com.itouch8.pump.core.util.stack;

import java.io.Serializable;

import org.slf4j.Logger;


public interface IStack extends Serializable {

    
    public String getTrackId();

    
    public Logger getLogger();

    
    public StackTraceElement getStack();

    
    public String getMessage();

    
    public Throwable getThrowable();

}
