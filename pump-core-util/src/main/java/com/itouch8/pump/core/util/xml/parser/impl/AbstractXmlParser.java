package com.itouch8.pump.core.util.xml.parser.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.core.util.parser.impl.AbstractParser;
import com.itouch8.pump.core.util.xml.XmlHelper;
import com.itouch8.pump.core.util.xml.context.IXmlParserContext;
import com.itouch8.pump.core.util.xml.context.IXmlParserContext.XmlValidationMode;
import com.itouch8.pump.core.util.xml.parser.INamespaceParser;
import com.itouch8.pump.core.util.xml.parser.IXmlParser;


public abstract class AbstractXmlParser<E extends IXmlParserContext> extends AbstractParser<E> implements IXmlParser<E> {

    private static final String PROFILE_ATTRIBUTE = "profile";

    private static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";

    private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();

    
    private final ParseCallback<E, Document> defaultXmlParseCallback = new ParseCallback<E, Document>() {
        @Override
        public Document callback(E parserContext, EncodedResource resource) throws IOException {
            InputStream inputStream = null;
            try {
                inputStream = resource.getResource().getInputStream();
                InputSource inputSource = new InputSource(inputStream);
                if (resource.getEncoding() != null) {
                    inputSource.setEncoding(resource.getEncoding());
                }
                return doLoadDocument(parserContext, inputSource, resource.getResource());
            } finally {
                CoreUtils.closeQuietly(inputStream);
            }
        }
    };

    
    @Override
    public List<Document> buildDocuments(E parserContext, String locationPattern) {
        return buildDocuments(parserContext, new String[] {locationPattern});
    }

    
    @Override
    public List<Document> buildDocuments(E parserContext, String[] locationPatterns) {
        Environment environment = extractEnvironment(parserContext);
        Set<Resource> resources = CoreUtils.getResources(environment, locationPatterns);
        return doBuildDocuments(parserContext, resources);
    }

    
    @Override
    public Document buildDocument(E parserContext, InputStream inputStream) {
        return buildDocument(parserContext, new InputStreamResource(inputStream));
    }

    
    @Override
    public Document buildDocument(E parserContext, Resource resource) {
        List<Document> documents = doBuildDocuments(parserContext, Arrays.asList(resource));
        if (null == documents || documents.isEmpty()) {
            return null;
        }
        return documents.get(0);
    }

    
    @Override
    protected void doParse(E parserContext, EncodedResource resource) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = resource.getResource().getInputStream();
            InputSource inputSource = new InputSource(inputStream);
            if (resource.getEncoding() != null) {
                inputSource.setEncoding(resource.getEncoding());
            }
            doParse(parserContext, inputSource, resource.getResource());
        } finally {
            CoreUtils.closeQuietly(inputStream);
        }
    }

    
    protected void afterBuildDocument(E parserContext, Document document, Resource resource) {

    }

    
    protected abstract void parseDocument(E parserContext, Document document, Resource resource);

    
    protected INamespaceParser<E> getNamespaceParser(Node node) {
        return XmlHelper.getNamespaceParser(node);
    }

    
    private void doParse(E parserContext, InputSource inputSource, Resource resource) {
        Document doc = doLoadDocument(parserContext, inputSource, resource);
        Element root = doc.getDocumentElement();
        Environment environment = extractEnvironment(parserContext);

        String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
        if (null != environment && !CoreUtils.isBlank(profileSpec)) {
            String[] specifiedProfiles = StringUtils.tokenizeToStringArray(profileSpec, MULTI_VALUE_ATTRIBUTE_DELIMITERS);
            if (!environment.acceptsProfiles(specifiedProfiles)) {
                CommonLogger.info("Skipped XML file due to specified profiles [" + profileSpec + "] not matching: " + resource);
                return;
            }
        }
        afterBuildDocument(parserContext, doc, resource);
        parseDocument(parserContext, doc, resource);
    }

    
    private Document doLoadDocument(E parserContext, InputSource inputSource, Resource resource) {
        DocumentLoader loader = extractDocumentLoader(parserContext);
        if (null == loader) {
            Throw.throwRuntimeException("DocumentLoader is null when parsing XML document from " + resource.getDescription());
        }
        EntityResolver entityResolver = extractEntityResolver(parserContext);
        ErrorHandler errorHandler = extractErrorHandler(parserContext);
        XmlValidationMode mode = parserContext.getXmlValidationMode();
        int validationMode;
        if (null == mode || XmlValidationMode.AUTO.equals(mode)) {
            validationMode = detectValidationMode(resource);
        } else {
            validationMode = mode.getMode();
        }
        // 如果是XSD，加载器会强制设置namespaceAware为true
        Document document = null;
        try {
            document = loader.loadDocument(inputSource, entityResolver, errorHandler, validationMode, false);
        } catch (SAXParseException ex) {
            Throw.throwRuntimeException("Line " + ex.getLineNumber() + " in XML document from " + resource.getDescription() + " is invalid", ex);
        } catch (SAXException ex) {
            Throw.throwRuntimeException("XML document from " + resource.getDescription() + " is invalid", ex);
        } catch (ParserConfigurationException ex) {
            Throw.throwRuntimeException("Parser configuration exception parsing XML from " + resource.getDescription(), ex);
        } catch (IOException ex) {
            Throw.throwRuntimeException("IOException parsing XML document from " + resource.getDescription(), ex);
        } catch (Throwable ex) {
            Throw.throwRuntimeException("Unexpected exception parsing XML document from " + resource.getDescription(), ex);
        }
        return document;
    }

    protected ErrorHandler extractErrorHandler(E parserContext) {
        ErrorHandler errorHandler = parserContext.getErrorHandler();
        return errorHandler;
    }

    protected EntityResolver extractEntityResolver(E parserContext) {
        EntityResolver entityResolver = parserContext.getEntityResolver();
        return entityResolver;
    }

    protected DocumentLoader extractDocumentLoader(E parserContext) {
        return parserContext.getDocumentLoader();
    }

    
    private int detectValidationMode(Resource resource) {
        if (resource.isOpen()) {
            throw Throw.createRuntimeException("Passed-in Resource [" + resource + "] contains an open stream: " + "cannot determine validation mode automatically.");
        }

        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            return validationModeDetector.detectValidationMode(inputStream);
        } catch (IOException ex) {
            throw Throw.createRuntimeException("Unable to determine validation mode for [" + resource + "]: an error occurred whilst reading from the InputStream.", ex);
        } finally {
            CoreUtils.closeQuietly(inputStream);
        }
    }

    
    private List<Document> doBuildDocuments(E parserContext, Collection<Resource> resources) {
        return super.doParse(parserContext, resources, defaultXmlParseCallback);
    }
}
