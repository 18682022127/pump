package com.itouch8.pump.core.util.parser.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.parser.IParseEventListener;
import com.itouch8.pump.core.util.parser.IParser;
import com.itouch8.pump.core.util.parser.IParserContext;


public abstract class AbstractParser<E extends IParserContext> implements IParser<E> {

    
    private final ThreadLocal<Set<Resource>> resourcesCurrentlyBeingLoaded = new ThreadLocal<Set<Resource>>();

    
    private final ThreadLocal<E> currentParserContext = new ThreadLocal<E>();

    
    private final ParseCallback<E, Void> defaultParseCallback = new ParseCallback<E, Void>() {
        @Override
        public Void callback(E parserContext, EncodedResource resource) throws IOException {
            AbstractParser.this.doParse(parserContext, resource);
            return null;
        }
    };

    
    @Override
    public void parse(E parserContext, String locationPattern) {
        parse(parserContext, new String[] {locationPattern});
    }

    
    @Override
    public void parse(E parserContext, String[] locationPatterns) {
        Environment environment = extractEnvironment(parserContext);
        Set<Resource> resources = CoreUtils.getResources(environment, locationPatterns);
        doParse(parserContext, resources, defaultParseCallback);
    }

    
    @Override
    public void parse(E parserContext, InputStream inputStream) {
        parse(parserContext, new InputStreamResource(inputStream));
    }

    
    @Override
    public void parse(E parserContext, Resource resource) {
        doParse(parserContext, Arrays.asList(resource), defaultParseCallback);
    }

    
    protected abstract void doParse(E parserContext, EncodedResource resource) throws IOException;

    
    protected <R> List<R> doParse(E parserContext, Collection<Resource> resources, ParseCallback<E, R> callback) {
        if (null != resources && !resources.isEmpty()) {
            IParseEventListener listener = parserContext.getEventListener();// 侦听器
            try {
                if (null != listener) {
                    listener.onBeforeParse(parserContext, resources);
                }
                currentParserContext.set(parserContext);// 将执行环境存到当前线程
                List<R> rs = new ArrayList<R>();
                for (Resource resource : resources) {
                    R r = nonRepeatedParse(parserContext, resource, listener, callback);
                    rs.add(r);
                }
                return rs;
            } finally {
                try {
                    if (null != listener) {
                        listener.onFinishParse(parserContext, resources);
                    }
                } finally {
                    currentParserContext.remove();// 从当前线程移除执行环境
                }
            }
        }
        return null;
    }

    
    protected E getParserContext() {
        return currentParserContext.get();
    }

    
    private <R> R nonRepeatedParse(E parserContext, Resource resource, IParseEventListener listener, ParseCallback<E, R> callback) {
        Set<Resource> currentResources = this.resourcesCurrentlyBeingLoaded.get();// 当前线程已经解析的资源对象
        if (currentResources == null) {
            currentResources = new HashSet<Resource>(4);
            this.resourcesCurrentlyBeingLoaded.set(currentResources);
        }
        if (!currentResources.add(resource)) {
            throw new RuntimeException("Detected cyclic loading of " + resource + " !");
        }
        try {
            if (null != listener) {
                listener.onBeforeParseOneResource(parserContext, resource);
            }
            EncodedResource encodedResource = decorateEncodedResource(resource);
            R rs = callback.callback(parserContext, encodedResource);// 执行真正的解析操作
            if (null != listener) {
                listener.onSuccessParseOneResource(parserContext, resource);
            }
            return rs;
        } catch (Exception ex) {
            if (null != listener) {
                listener.onFailureParseOneResource(parserContext, resource, ex);
            }
            throw new RuntimeException("IOException parsing from " + resource, ex);
        } finally {
            currentResources.remove(resource);
            if (currentResources.isEmpty()) {
                this.resourcesCurrentlyBeingLoaded.remove();
            }
        }
    }

    
    protected EncodedResource decorateEncodedResource(Resource resource) {
        EncodedResource encodedResource = null;
        if (!(resource instanceof EncodedResource)) {
            encodedResource = new EncodedResource(resource);
        } else {
            encodedResource = (EncodedResource) resource;
        }
        return encodedResource;
    }

    
    protected String resolvePath(Environment environment, String path) {
        return environment == null ? path : environment.resolveRequiredPlaceholders(path);
    }

    
    protected Environment extractEnvironment(E parserContext) {
        Environment environment = parserContext.getEnvironment();
        return environment;
    }

    
    protected interface ParseCallback<E, R> {
        R callback(E parserContext, EncodedResource resource) throws IOException;
    }
}
