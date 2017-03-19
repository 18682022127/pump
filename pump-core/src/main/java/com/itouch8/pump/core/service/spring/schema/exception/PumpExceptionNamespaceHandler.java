package com.itouch8.pump.core.service.spring.schema.exception;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.itouch8.pump.core.service.spring.schema.exception.parser.ExceptionParser;


public class PumpExceptionNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("exception", new ExceptionParser());
        // registerBeanDefinitionParser("exception-config", new ExceptionParser());
    }

}
