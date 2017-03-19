package com.itouch8.pump.core.util.parser;

import java.io.InputStream;

import org.springframework.core.io.Resource;


public interface IParser<E extends IParserContext> {

    
    public void parse(E parserContext, String locationPattern);

    
    public void parse(E parserContext, String[] locationPatterns);

    
    public void parse(E parserContext, InputStream inputStream);

    
    public void parse(E parserContext, Resource resource);
}
