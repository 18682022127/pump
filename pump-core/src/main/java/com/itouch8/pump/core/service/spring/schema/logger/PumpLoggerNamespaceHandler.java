package com.itouch8.pump.core.service.spring.schema.logger;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.itouch8.pump.core.service.spring.schema.logger.parser.LoggerParser;


public class PumpLoggerNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("logger", new LoggerParser());
    }

}
