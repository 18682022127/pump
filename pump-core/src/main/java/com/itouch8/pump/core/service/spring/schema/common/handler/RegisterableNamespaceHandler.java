package com.itouch8.pump.core.service.spring.schema.common.handler;

import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public abstract class RegisterableNamespaceHandler extends NamespaceHandlerSupport {

    public void doRegisterBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
        super.registerBeanDefinitionParser(elementName, parser);
    }

    public void doRegisterBeanDefinitionDecorator(String elementName, BeanDefinitionDecorator dec) {
        super.registerBeanDefinitionDecorator(elementName, dec);
    }

    public void doRegisterBeanDefinitionDecoratorForAttribute(String attrName, BeanDefinitionDecorator dec) {
        super.registerBeanDefinitionDecoratorForAttribute(attrName, dec);
    }
}
