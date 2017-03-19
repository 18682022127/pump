package com.itouch8.pump.web.springmvc.returnvaluehandler;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.itouch8.pump.core.util.page.IPage;
import com.itouch8.pump.util.Tool;
import com.itouch8.pump.web.WebPumpConfig;


@Deprecated
public class PageReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        Method method = returnType.getMethod();
        if (Collection.class.isAssignableFrom(method.getReturnType())) {// 返回集合类型，并且含有分页参数
            for (Class<?> pt : method.getParameterTypes()) {
                if (IPage.class.isAssignableFrom(pt)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("application/json;charset=UTF-8");
        Object target = WebPumpConfig.getPageJsonWrapper().wrap(returnValue);
        Tool.JSON.serialize(response.getOutputStream(), target);
        response.getOutputStream().flush();
    }

}
