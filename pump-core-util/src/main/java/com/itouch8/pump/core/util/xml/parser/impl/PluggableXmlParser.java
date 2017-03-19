package com.itouch8.pump.core.util.xml.parser.impl;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.xml.XmlHelper;
import com.itouch8.pump.core.util.xml.context.IXmlParserContext;
import com.itouch8.pump.core.util.xml.parser.INamespaceParser;


public class PluggableXmlParser<E extends IXmlParserContext> extends AbstractXmlParser<E> {

    @Override
    protected void parseDocument(E parserContext, Document document, Resource resource) {
        Element root = document.getDocumentElement();
        INamespaceParser<E> parser = getNamespaceParser(document.getDocumentElement());
        if (null == parser) {
            Throw.throwRuntimeException("not found the namespace parser. [" + XmlHelper.getNamespaceURI(root) + "] ");
        }
        parser.parse(parserContext, document, resource);
    }
}
