package com.itouch8.pump.web;

import java.util.List;

import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.util.annotation.Configable;
import com.itouch8.pump.core.util.aop.IAopInterceptor;
import com.itouch8.pump.util.json.serial.wrapper.IJsonWrapper;
import com.itouch8.pump.web.path.IPathResolver;
import com.itouch8.pump.web.request.log.IParamConvert;
import com.itouch8.pump.web.request.log.IRequestLog;
import com.itouch8.pump.web.view.IViewMapping;


public class WebPumpConfig extends PumpConfig {

    
    @Configable
    private static IJsonWrapper pageJsonWrapper;

    
    @Configable
    private static IJsonWrapper defaultJsonWrapper;

    
    @Configable
    private static IJsonWrapper exceptionWrapper;

    
    @Configable
    private static List<IViewMapping> viewMappings;

    
    @Configable
    private static List<IRequestLog> requestLogs;

    
    @Configable
    private static IParamConvert paramConvert;

    
    @Configable
    private static List<IAopInterceptor> controllerAopInterceptors;

    
    @Configable
    private static IPathResolver pathResolver;

    
    @Configable
    private static List<HandlerMethodReturnValueHandler> priorReturnValueHandlers;

    
    @Override
    public void validate() {
        super.validate();

        // 执行当前配置类中的校验
    }

    public static IJsonWrapper getPageJsonWrapper() {
        return getComponent(pageJsonWrapper, IJsonWrapper.class, "page");
    }

    public void setPageJsonWrapper(IJsonWrapper pageJsonWrapper) {
        WebPumpConfig.pageJsonWrapper = pageJsonWrapper;
    }

    public static IJsonWrapper getDefaultJsonWrapper() {
        return getComponent(defaultJsonWrapper, IJsonWrapper.class, "default");
    }

    public void setDefaultJsonWrapper(IJsonWrapper defaultJsonWrapper) {
        WebPumpConfig.defaultJsonWrapper = defaultJsonWrapper;
    }

    public static IJsonWrapper getExceptionWrapper() {
        return getComponent(exceptionWrapper, IJsonWrapper.class, "exception");
    }

    public void setExceptionWrapper(IJsonWrapper exceptionWrapper) {
        WebPumpConfig.exceptionWrapper = exceptionWrapper;
    }

    public static List<IViewMapping> getViewMappings() {
        return getComponents(viewMappings, IViewMapping.class);
    }

    public void setViewMappings(List<IViewMapping> viewMappings) {
        WebPumpConfig.viewMappings = viewMappings;
    }

    public static List<IRequestLog> getRequestLogs() {
        return getComponents(requestLogs, IRequestLog.class);
    }

    public void setRequestLogs(List<IRequestLog> requestLogs) {
        WebPumpConfig.requestLogs = requestLogs;
    }

    public static IParamConvert getParamConvert() {
        return paramConvert;
    }

    public void setParamConvert(IParamConvert paramConvert) {
        WebPumpConfig.paramConvert = paramConvert;
    }

    public static List<IAopInterceptor> getControllerAopInterceptors() {
        return getComponents(controllerAopInterceptors, IAopInterceptor.class, "Controller");
    }

    public void setControllerAopInterceptors(List<IAopInterceptor> controllerAopInterceptors) {
        WebPumpConfig.controllerAopInterceptors = controllerAopInterceptors;
    }

    public static IPathResolver getPathResolver() {
        return getComponent(pathResolver, IPathResolver.class);
    }

    public void setPathResolver(IPathResolver pathResolver) {
        WebPumpConfig.pathResolver = pathResolver;
    }

    public static List<HandlerMethodReturnValueHandler> getPriorReturnValueHandlers() {
        return priorReturnValueHandlers;
    }

    public void setPriorReturnValueHandlers(List<HandlerMethodReturnValueHandler> priorReturnValueHandlers) {
        WebPumpConfig.priorReturnValueHandlers = priorReturnValueHandlers;
    }
}
