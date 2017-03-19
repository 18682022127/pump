package com.itouch8.pump.core.util.parser.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.core.io.Resource;

import com.itouch8.pump.core.util.parser.IParseEventListener;
import com.itouch8.pump.core.util.parser.IParserContext;


public class ParseEventListenerChain implements IParseEventListener {

    private List<? extends IParseEventListener> listeners;

    public void setListeners(List<? extends IParseEventListener> listeners) {
        this.listeners = listeners;
    }

    
    @Override
    public void onBeforeParse(IParserContext context, Collection<Resource> resources) {
        if (null != listeners) {
            for (IParseEventListener listener : listeners) {
                listener.onBeforeParse(context, resources);
            }
        }
    }

    
    @Override
    public void onFinishParse(IParserContext context, Collection<Resource> resources) {
        if (null != listeners) {
            for (int i = listeners.size() - 1; i >= 0; i--) {
                IParseEventListener listener = listeners.get(i);
                listener.onFinishParse(context, resources);
            }
        }
    }

    
    @Override
    public void onBeforeParseOneResource(IParserContext context, Resource resource) {
        if (null != listeners) {
            for (IParseEventListener listener : listeners) {
                listener.onBeforeParseOneResource(context, resource);
            }
        }
    }

    
    @Override
    public void onFailureParseOneResource(IParserContext context, Resource resource, Exception e) {
        if (null != listeners) {
            for (int i = listeners.size() - 1; i >= 0; i--) {
                IParseEventListener listener = listeners.get(i);
                listener.onFailureParseOneResource(context, resource, e);
            }
        }
    }

    
    @Override
    public void onSuccessParseOneResource(IParserContext context, Resource resource) {
        if (null != listeners) {
            for (int i = listeners.size() - 1; i >= 0; i--) {
                IParseEventListener listener = listeners.get(i);
                listener.onSuccessParseOneResource(context, resource);
            }
        }
    }
}
