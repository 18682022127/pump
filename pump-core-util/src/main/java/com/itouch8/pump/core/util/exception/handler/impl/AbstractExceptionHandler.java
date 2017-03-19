package com.itouch8.pump.core.util.exception.handler.impl;

import com.itouch8.pump.core.util.exception.PumpRuntimeException;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.handler.IExceptionHandler;


public abstract class AbstractExceptionHandler implements IExceptionHandler {

    
    private boolean ignoreHandlerException = true;

    @Override
    public boolean ignoreHandlerException() {
        return ignoreHandlerException;
    }

    public void setIgnoreHandlerException(boolean ignoreHandlerException) {
        this.ignoreHandlerException = ignoreHandlerException;
    }

    
    @Override
    public Object handler(Throwable throwable) {
        PumpRuntimeException exception = Throw.createRuntimeException(throwable);
        return this.handlerPumpRuntimeException(exception);
    }

    
    protected abstract Object handlerPumpRuntimeException(PumpRuntimeException exception);
}
