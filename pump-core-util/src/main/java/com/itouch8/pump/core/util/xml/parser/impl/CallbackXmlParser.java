package com.itouch8.pump.core.util.xml.parser.impl;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

import com.itouch8.pump.core.util.xml.context.IXmlParserContext;
import com.itouch8.pump.core.util.xml.parser.INamespaceParser;


public class CallbackXmlParser<E extends IXmlParserContext> extends AbstractXmlParser<E> {

    private final INamespaceParser<E> parser;

    public CallbackXmlParser(final INamespaceParser<E> parser) {
        super();
        this.parser = parser;
    }

    public INamespaceParser<E> getParser() {
        return parser;
    }

    @Override
    protected void parseDocument(E parserContext, Document document, Resource resource) {
        parser.parse(parserContext, document, resource);
    }
}
