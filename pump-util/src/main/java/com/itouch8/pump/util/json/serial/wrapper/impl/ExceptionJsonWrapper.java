package com.itouch8.pump.util.json.serial.wrapper.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.validation.BindException;

import com.itouch8.pump.core.service.spring.SpringHelp;
import com.itouch8.pump.core.util.exception.PumpRuntimeException;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.util.json.serial.wrapper.IAjaxBindExceptionHandler;


public class ExceptionJsonWrapper extends MapJsonWrapper {

    private final IAjaxBindExceptionHandler defaultIAjaxBindExceptionHandler = new DefaultAjaxBindExceptionHandler();

    @Override
    protected boolean addOriginalData() {
        return false;
    }

    @Override
    protected boolean getStatus(Object original) {
        return false;
    }

    @Override
    protected void doWrap(Map<String, Object> wrapper, Object original) {
        if (original instanceof BindException) {
            Map<String, Object> ex = getExceptionInfoMap(wrapper);
            IAjaxBindExceptionHandler b = getAjaxBindExceptionHandler();
            b.handler(ex, (BindException) original);
        } else if (original instanceof Throwable) {
            PumpRuntimeException be = Throw.createRuntimeException((Throwable) original);
            Map<String, Object> ex = getExceptionInfoMap(wrapper);
            ex.put("code", be.getCode());
            ex.put("trackId", be.getTrackId());
            ex.put("level", be.getLevel());
            ex.put("message", be.getShortMessage());
            ex.put("detail", be.getMessage());
        }
    }

    private IAjaxBindExceptionHandler getAjaxBindExceptionHandler() {
        try {
            IAjaxBindExceptionHandler d = SpringHelp.getBean(IAjaxBindExceptionHandler.class);
            return null == d ? defaultIAjaxBindExceptionHandler : d;
        } catch (Exception e) {
            return defaultIAjaxBindExceptionHandler;
        }
    }

    private Map<String, Object> getExceptionInfoMap(Map<String, Object> wrapper) {
        Map<String, Object> ex = new LinkedHashMap<String, Object>();
        wrapper.put(getDataJsonPropertyName(), ex);
        return ex;
    }
}
