package com.itouch8.pump.core.util.parser.impl;

import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.parser.IParseEventListener;
import com.itouch8.pump.core.util.parser.IParserContext;
import com.itouch8.pump.core.util.parser.ParserComponents;


public class ParserContext implements IParserContext {

    private ResourceLoader resourceLoader;

    private ProblemReporter problemReporter;

    private IParseEventListener eventListener;

    private SourceExtractor sourceExtractor;

    private Environment environment;

    
    public ResourceLoader getResourceLoader() {
        if (null == this.resourceLoader) {
            this.resourceLoader = BaseConfig.getResourcePatternResolver();
        }
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    
    public ProblemReporter getProblemReporter() {
        if (null == this.problemReporter) {
            this.problemReporter = ParserComponents.getFactory().newProblemReporter();
        }
        return problemReporter;
    }

    public void setProblemReporter(ProblemReporter problemReporter) {
        this.problemReporter = problemReporter;
    }

    
    public IParseEventListener getEventListener() {
        if (null == this.eventListener) {
            this.eventListener = ParserComponents.getFactory().newEventListener();
        }
        return eventListener;
    }

    public void setEventListener(IParseEventListener eventListener) {
        this.eventListener = eventListener;
    }

    
    public SourceExtractor getSourceExtractor() {
        if (null == this.sourceExtractor) {
            this.sourceExtractor = ParserComponents.getFactory().newSourceExtractor();
        }
        return sourceExtractor;
    }

    public void setSourceExtractor(SourceExtractor sourceExtractor) {
        this.sourceExtractor = sourceExtractor;
    }

    
    public Environment getEnvironment() {
        if (null == this.environment) {
            this.environment = ParserComponents.getFactory().newEnvironment();
        }
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
