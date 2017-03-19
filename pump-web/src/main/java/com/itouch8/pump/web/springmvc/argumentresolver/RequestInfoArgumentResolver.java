package com.itouch8.pump.web.springmvc.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.web.servlet.ServletHelp;


public class RequestInfoArgumentResolver implements HandlerMethodArgumentResolver {

    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return IRequestInfo.class.isAssignableFrom(paramType);
    }

    
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return ServletHelp.getRequestInfo();
    }
}
