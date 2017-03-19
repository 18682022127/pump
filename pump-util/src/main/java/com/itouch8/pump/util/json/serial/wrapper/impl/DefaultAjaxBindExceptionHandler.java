package com.itouch8.pump.util.json.serial.wrapper.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.itouch8.pump.util.json.serial.wrapper.IAjaxBindExceptionHandler;


public class DefaultAjaxBindExceptionHandler implements IAjaxBindExceptionHandler {

    @Override
    public void handler(Map<String, Object> context, BindException exception) {
        BindingResult result = exception.getBindingResult();
        List<Map<String, String>> msgs = new ArrayList<Map<String, String>>();
        if (result.hasFieldErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", error.getField());
                map.put("type", "field");
                map.put("message", error.getDefaultMessage());
                msgs.add(map);
            }
        }
        if (result.hasGlobalErrors()) {
            for (ObjectError error : result.getGlobalErrors()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", error.getObjectName());
                map.put("type", "global");
                map.put("message", error.getDefaultMessage());
                msgs.add(map);
            }
        }
        context.put("errors", msgs);
    }
}
