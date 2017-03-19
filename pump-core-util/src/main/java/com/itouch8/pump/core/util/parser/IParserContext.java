package com.itouch8.pump.core.util.parser;

import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.core.env.Environment;


public interface IParserContext {

    
    public ProblemReporter getProblemReporter();

    
    public IParseEventListener getEventListener();

    
    public SourceExtractor getSourceExtractor();

    
    public Environment getEnvironment();
}
