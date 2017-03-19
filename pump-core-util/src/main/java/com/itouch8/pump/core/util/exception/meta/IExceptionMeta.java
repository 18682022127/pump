package com.itouch8.pump.core.util.exception.meta;

import java.util.List;

import com.itouch8.pump.core.util.exception.handler.IExceptionHandler;
import com.itouch8.pump.core.util.exception.level.ExceptionLevel;


public interface IExceptionMeta {

    
    public String getParentCode();

    
    public String getCode();

    
    public String getMessageKey();

    
    public ExceptionLevel getLevel();

    
    public String getView();

    
    public List<IExceptionHandler> getHandlers();

    
    public List<Class<? extends Throwable>> getCauses();
}
