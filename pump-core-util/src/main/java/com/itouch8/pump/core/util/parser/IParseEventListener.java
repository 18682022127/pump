package com.itouch8.pump.core.util.parser;

import java.util.Collection;

import org.springframework.core.io.Resource;


public interface IParseEventListener {

    
    public void onBeforeParse(IParserContext context, Collection<Resource> resources);

    
    public void onFinishParse(IParserContext context, Collection<Resource> resources);

    
    public void onBeforeParseOneResource(IParserContext context, Resource resource);

    
    public void onFailureParseOneResource(IParserContext context, Resource resource, Exception e);

    
    public void onSuccessParseOneResource(IParserContext context, Resource resource);
}
