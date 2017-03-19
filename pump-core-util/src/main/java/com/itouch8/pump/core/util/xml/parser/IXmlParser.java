package com.itouch8.pump.core.util.xml.parser;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

import com.itouch8.pump.core.util.parser.IParser;
import com.itouch8.pump.core.util.xml.context.IXmlParserContext;


public interface IXmlParser<E extends IXmlParserContext> extends IParser<E> {

    
    public List<Document> buildDocuments(E parserContext, String locationPattern);

    
    public List<Document> buildDocuments(E parserContext, String[] locationPatterns);

    
    public Document buildDocument(E parserContext, InputStream inputStream);

    
    public Document buildDocument(E parserContext, Resource resource);
}
