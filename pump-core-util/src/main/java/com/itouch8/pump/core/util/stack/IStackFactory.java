package com.itouch8.pump.core.util.stack;

import org.slf4j.Logger;


public interface IStackFactory {

    
    public IStack getStack(Logger logger, StackTraceElement stack, String message, Throwable throwable);
}
