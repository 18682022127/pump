package com.itouch8.pump.core.util.parser;

import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.core.env.Environment;


public interface IParserComponentFactory {

    
    public IParserContext newParserContext();

    
    public ProblemReporter newProblemReporter();

    
    public IParseEventListener newEventListener();

    
    public SourceExtractor newSourceExtractor();

    
    public Environment newEnvironment();
}
