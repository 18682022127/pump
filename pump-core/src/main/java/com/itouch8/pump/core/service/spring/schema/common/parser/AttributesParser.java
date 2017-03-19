package com.itouch8.pump.core.service.spring.schema.common.parser;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import com.itouch8.pump.core.service.spring.schema.common.handler.RegisterableNamespaceHandler;


public class AttributesParser extends AbstractParser {

    private Map<String, Class<?>> beanClassMap;

    public AttributesParser() {
        this.beanClassMap = new HashMap<String, Class<?>>();
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        if (beanClassMap.containsKey(element.getLocalName())) {
            return beanClassMap.get(element.getLocalName());
        }
        return super.getBeanClass(element);
    }

    public AttributesParser registerBeanDefinitionParser(RegisterableNamespaceHandler namespace, String elementName, Class<?> beanClass) {
        this.beanClassMap.put(elementName, beanClass);
        namespace.doRegisterBeanDefinitionParser(elementName, this);
        return this;
    }
}
