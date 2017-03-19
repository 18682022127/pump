package com.itouch8.pump.core.util.stack.impl;

import org.slf4j.Logger;

import com.itouch8.pump.core.util.stack.IStack;
import com.itouch8.pump.core.util.stack.IStackFactory;


public class SimpleStackFactory implements IStackFactory {

    
    public IStack getStack(Logger logger, StackTraceElement stack, String message, Throwable throwable) {
        SimpleStack ss = new SimpleStack(logger, stack, message, throwable);
        return ss;
    }
}
