package com.itouch8.pump.core.util.exception.meta.impl;

import java.util.List;

import com.itouch8.pump.core.util.exception.handler.IExceptionHandler;
import com.itouch8.pump.core.util.exception.level.ExceptionLevel;
import com.itouch8.pump.core.util.exception.meta.IExceptionMeta;


public class ExceptionMeta implements IExceptionMeta {

    
    private String parentCode;

    
    private String code;

    
    private String messageKey;

    
    private ExceptionLevel level;

    
    private String view;

    
    private String description;

    
    private List<IExceptionHandler> handlers;

    
    private List<Class<? extends Throwable>> causes;

    @Override
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public ExceptionLevel getLevel() {
        return level;
    }

    public void setLevel(ExceptionLevel level) {
        this.level = level;
    }

    @Override
    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<IExceptionHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<IExceptionHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public List<Class<? extends Throwable>> getCauses() {
        return causes;
    }

    public void setCauses(List<Class<? extends Throwable>> causes) {
        this.causes = causes;
    }
}
