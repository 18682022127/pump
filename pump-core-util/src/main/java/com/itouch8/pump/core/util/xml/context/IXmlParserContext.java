package com.itouch8.pump.core.util.xml.context;

import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import com.itouch8.pump.core.util.parser.IParserContext;


public interface IXmlParserContext extends IParserContext {

    
    public DocumentLoader getDocumentLoader();

    
    public EntityResolver getEntityResolver();

    
    public ErrorHandler getErrorHandler();

    
    public XmlValidationMode getXmlValidationMode();

    
    public enum XmlValidationMode {
        
        NONE(XmlValidationModeDetector.VALIDATION_NONE),
        
        AUTO(XmlValidationModeDetector.VALIDATION_AUTO),
        
        DTD(XmlValidationModeDetector.VALIDATION_DTD),
        
        XSD(XmlValidationModeDetector.VALIDATION_XSD);

        private final int mode;

        private XmlValidationMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }
    }
}
