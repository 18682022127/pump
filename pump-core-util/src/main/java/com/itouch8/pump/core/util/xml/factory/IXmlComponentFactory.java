package com.itouch8.pump.core.util.xml.factory;

import org.springframework.beans.factory.xml.DocumentLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.itouch8.pump.core.util.xml.context.IXmlParserContext;
import com.itouch8.pump.core.util.xml.parser.INamespaceParser;
import com.itouch8.pump.core.util.xml.parser.IXmlParser;


public interface IXmlComponentFactory {

    
    public IXmlParserContext newXmlParserContext();

    
    public <E extends IXmlParserContext> IXmlParser<E> newXmlParser();

    
    public <E extends IXmlParserContext> IXmlParser<E> newXmlParser(INamespaceParser<E> parser);

    
    public DocumentLoader newDocumentLoader();

    
    public EntityResolver newEntityResolver();

    
    public ErrorHandler newErrorHandler();
}
