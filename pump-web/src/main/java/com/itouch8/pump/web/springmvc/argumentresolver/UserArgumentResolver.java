package com.itouch8.pump.web.springmvc.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.security.core.login.user.IUser;
import com.itouch8.pump.web.annotation.User;


public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return IUser.class.isAssignableFrom(parameter.getParameterType()) || parameter.hasParameterAnnotation(User.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        IUser user = getUser(webRequest);
        if (null != user) {
            if (IUser.class.isAssignableFrom(parameter.getParameterType())) {
                return user;
            } else {
                User anno = parameter.getParameterAnnotation(User.class);
                String property = anno.value();
                if (CoreUtils.isBlank(property) || "id".equalsIgnoreCase(property) || "userId".equalsIgnoreCase(property)) {
                    if (String.class.isAssignableFrom(parameter.getParameterType())) {
                        return user.getUserId();
                    } else {
                        return CoreUtils.getProperty(user, "userId", parameter.getParameterType());
                    }
                } else {
                    return CoreUtils.getProperty(user, property, parameter.getParameterType());
                }
            }
        }
        return user;
    }

    protected IUser getUser(NativeWebRequest webRequest) {
        return null;
    }
}
