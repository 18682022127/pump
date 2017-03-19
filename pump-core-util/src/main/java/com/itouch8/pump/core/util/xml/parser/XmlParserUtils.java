package com.itouch8.pump.core.util.xml.parser;

import java.util.List;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

import com.itouch8.pump.core.util.parser.IParser;
import com.itouch8.pump.core.util.xml.context.IXmlParserContext;
import com.itouch8.pump.core.util.xml.factory.IXmlComponentFactory;
import com.itouch8.pump.core.util.xml.factory.impl.XmlComponentFactory;


public class XmlParserUtils {

    private static IXmlComponentFactory factory = new XmlComponentFactory();

    
    public static IXmlComponentFactory getFactory() {
        return factory;
    }

    
    public void setFactory(IXmlComponentFactory factory) {
        if (null != factory) {
            XmlParserUtils.factory = factory;
        }
    }

    
    public static void parseXml(String locationPattern) {
        parseXml(new String[] {locationPattern});
    }

    
    public static void parseXml(INamespaceParser<IXmlParserContext> parser, String locationPattern) {
        parseXml(parser, new String[] {locationPattern});
    }

    
    public static void parseXml(IXmlParserContext context, String locationPattern) {
        parseXml(context, new String[] {locationPattern});
    }

    
    public static void parseXml(String[] locationPatterns) {
        parseXml((INamespaceParser<IXmlParserContext>) null, locationPatterns);
    }

    
    public static void parseXml(INamespaceParser<IXmlParserContext> parser, String[] locationPatterns) {
        IXmlParserContext parserContext = factory.newXmlParserContext();
        IParser<IXmlParserContext> xp = factory.newXmlParser(parser);
        xp.parse(parserContext, locationPatterns);
    }

    
    public static void parseXml(IXmlParserContext context, String[] locationPatterns) {
        IParser<IXmlParserContext> xp = factory.newXmlParser();
        xp.parse(context, locationPatterns);
    }

    
    public static void parseXml(Resource resource) {
        parseXml((INamespaceParser<IXmlParserContext>) null, resource);
    }

    
    public static void parseXml(INamespaceParser<IXmlParserContext> parser, Resource resource) {
        IXmlParserContext parserContext = factory.newXmlParserContext();
        IParser<IXmlParserContext> xp = factory.newXmlParser(parser);
        xp.parse(parserContext, resource);
    }

    
    public static void parseXml(IXmlParserContext context, Resource resource) {
        IParser<IXmlParserContext> xp = factory.newXmlParser();
        xp.parse(context, resource);
    }

    
    public static List<Document> buildDocuments(String locationPattern) {
        return buildDocuments(new String[] {locationPattern});
    }

    
    public static List<Document> buildDocuments(IXmlParserContext context, String locationPattern) {
        return buildDocuments(context, new String[] {locationPattern});
    }

    
    public static List<Document> buildDocuments(String[] locationPatterns) {
        return buildDocuments(null, locationPatterns);
    }

    
    public static List<Document> buildDocuments(IXmlParserContext context, String[] locationPatterns) {
        if (null == context) {
            context = factory.newXmlParserContext();
        }
        IXmlParser<IXmlParserContext> xp = factory.newXmlParser();
        return xp.buildDocuments(context, locationPatterns);
    }

    
    public static Document buildDocument(Resource resource) {
        return buildDocument(null, resource);
    }

    
    public static Document buildDocument(IXmlParserContext context, Resource resource) {
        if (null == context) {
            context = factory.newXmlParserContext();
        }
        IXmlParser<IXmlParserContext> xp = factory.newXmlParser();
        return xp.buildDocument(context, resource);
    }
}
