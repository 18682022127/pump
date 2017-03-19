package com.itouch8.pump.core.util.xml.parser;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

import com.itouch8.pump.core.util.xml.context.IXmlParserContext;


public interface INamespaceParser<E extends IXmlParserContext> {

    
    public void parse(E parserContext, Document document, Resource resource);
}
