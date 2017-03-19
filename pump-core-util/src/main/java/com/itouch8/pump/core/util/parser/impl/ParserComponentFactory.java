package com.itouch8.pump.core.util.parser.impl;

import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.beans.factory.parsing.NullSourceExtractor;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

import com.itouch8.pump.core.util.parser.IParseEventListener;
import com.itouch8.pump.core.util.parser.IParserComponentFactory;
import com.itouch8.pump.core.util.parser.IParserContext;


public class ParserComponentFactory implements IParserComponentFactory {

    
    @Override
    public IParserContext newParserContext() {
        return new ParserContext();
    }

    
    @Override
    public ProblemReporter newProblemReporter() {
        return new FailFastProblemReporter();
    }

    
    @Override
    public IParseEventListener newEventListener() {
        return null;
    }

    
    @Override
    public SourceExtractor newSourceExtractor() {
        return new NullSourceExtractor();
    }

    
    @Override
    public Environment newEnvironment() {
        return new StandardEnvironment();
    }
}
