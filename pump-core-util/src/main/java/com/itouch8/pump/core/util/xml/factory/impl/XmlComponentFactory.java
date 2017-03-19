package com.itouch8.pump.core.util.xml.factory.impl;

import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.itouch8.pump.core.util.xml.BaseErrorHandler;
import com.itouch8.pump.core.util.xml.IniConfigEntityResolver;
import com.itouch8.pump.core.util.xml.context.IXmlParserContext;
import com.itouch8.pump.core.util.xml.context.impl.XmlParserContext;
import com.itouch8.pump.core.util.xml.factory.IXmlComponentFactory;
import com.itouch8.pump.core.util.xml.parser.INamespaceParser;
import com.itouch8.pump.core.util.xml.parser.IXmlParser;
import com.itouch8.pump.core.util.xml.parser.impl.CallbackXmlParser;
import com.itouch8.pump.core.util.xml.parser.impl.PluggableXmlParser;


public class XmlComponentFactory implements IXmlComponentFactory {

    
    @Override
    public IXmlParserContext newXmlParserContext() {
        return new XmlParserContext();
    }

    
    @Override
    public <E extends IXmlParserContext> IXmlParser<E> newXmlParser() {
        return newXmlParser(null);
    }

    
    @Override
    public <E extends IXmlParserContext> IXmlParser<E> newXmlParser(INamespaceParser<E> parser) {
        if (null != parser) {
            return new CallbackXmlParser<E>(parser);
        } else {
            return new PluggableXmlParser<E>();
        }
    }

    
    @Override
    public DocumentLoader newDocumentLoader() {
        return new DefaultDocumentLoader();
    }

    
    @Override
    public EntityResolver newEntityResolver() {
        return new IniConfigEntityResolver();
    }

    
    @Override
    public ErrorHandler newErrorHandler() {
        return new BaseErrorHandler();
    }
}
