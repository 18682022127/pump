package com.itouch8.pump.web.springmvc.resourceresolver;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.path.IPathResolver;
import com.itouch8.pump.web.servlet.ServletHelp;


public class StaticResourceResolver extends PathResourceResolver {

    final protected Resource getResource(String resourcePath, Resource location) throws IOException {
        return super.getResource(resolverResourcePath(resourcePath), location);
    }

    protected String resolverResourcePath(String resourcePath) {
        HttpServletRequest request = ServletHelp.getRequest();
        IPathResolver pathResolver = WebPumpConfig.getPathResolver();
        if (null != request && null != pathResolver) {
            return pathResolver.resolver(request, resourcePath);
        }
        return resourcePath;
    }
}
