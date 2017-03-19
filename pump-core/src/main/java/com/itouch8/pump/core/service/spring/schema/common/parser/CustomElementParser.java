package com.itouch8.pump.core.service.spring.schema.common.parser;

import org.w3c.dom.Element;

import com.itouch8.pump.core.util.CoreUtils;


public class CustomElementParser extends AbstractParser {

    public static final CustomElementParser CUSTOM_ELEMENT_PARSER = new CustomElementParser();

    @Override
    protected String getBeanClassName(Element element) {
        String cls = element.getAttribute("class");
        if (!CoreUtils.isBlank(cls)) {
            return cls;
        }
        return null;
    }
}
