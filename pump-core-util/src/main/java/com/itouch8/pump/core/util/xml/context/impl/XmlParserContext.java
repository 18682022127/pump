package com.itouch8.pump.core.util.xml.context.impl;

import org.springframework.beans.factory.xml.DocumentLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.itouch8.pump.core.util.parser.impl.ParserContext;
import com.itouch8.pump.core.util.xml.context.IXmlParserContext;
import com.itouch8.pump.core.util.xml.parser.XmlParserUtils;


public class XmlParserContext extends ParserContext implements IXmlParserContext {

    private DocumentLoader documentLoader;

    private EntityResolver entityResolver;

    private ErrorHandler errorHandler;

    private XmlValidationMode xmlValidationMode;

    
    @Override
    public DocumentLoader getDocumentLoader() {
        if (null == this.documentLoader) {
            this.documentLoader = XmlParserUtils.getFactory().newDocumentLoader();
        }
        return this.documentLoader;
    }

    
    @Override
    public EntityResolver getEntityResolver() {
        if (null == this.entityResolver) {
            this.entityResolver = XmlParserUtils.getFactory().newEntityResolver();
        }
        return this.entityResolver;
    }

    
    @Override
    public ErrorHandler getErrorHandler() {
        if (null == this.errorHandler) {
            this.errorHandler = XmlParserUtils.getFactory().newErrorHandler();
        }
        return this.errorHandler;
    }

    
    @Override
    public XmlValidationMode getXmlValidationMode() {
        return xmlValidationMode;
    }

    public void setDocumentLoader(DocumentLoader documentLoader) {
        this.documentLoader = documentLoader;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void setXmlValidationMode(XmlValidationMode xmlValidationMode) {
        this.xmlValidationMode = xmlValidationMode;
    }
}
