package com.itouch8.pump.util.json.serial.wrapper;

import java.util.Map;

import org.springframework.validation.BindException;


public interface IAjaxBindExceptionHandler {

    
    public void handler(Map<String, Object> context, BindException exception);
}
