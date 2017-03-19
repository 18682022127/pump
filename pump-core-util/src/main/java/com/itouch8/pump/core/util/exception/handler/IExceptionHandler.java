package com.itouch8.pump.core.util.exception.handler;


public interface IExceptionHandler {

    
    public boolean ignoreHandlerException();

    
    public Object handler(Throwable throwable);
}
