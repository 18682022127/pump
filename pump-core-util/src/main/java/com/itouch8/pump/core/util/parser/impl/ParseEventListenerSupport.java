package com.itouch8.pump.core.util.parser.impl;

import java.util.Collection;

import org.springframework.core.io.Resource;

import com.itouch8.pump.core.util.parser.IParseEventListener;
import com.itouch8.pump.core.util.parser.IParserContext;


public class ParseEventListenerSupport implements IParseEventListener {

    
    @Override
    public void onBeforeParse(IParserContext context, Collection<Resource> resources) {

    }

    
    @Override
    public void onFinishParse(IParserContext context, Collection<Resource> resources) {

    }

    
    @Override
    public void onBeforeParseOneResource(IParserContext context, Resource resource) {

    }

    
    @Override
    public void onFailureParseOneResource(IParserContext context, Resource resource, Exception e) {

    }

    
    @Override
    public void onSuccessParseOneResource(IParserContext context, Resource resource) {

    }
}
