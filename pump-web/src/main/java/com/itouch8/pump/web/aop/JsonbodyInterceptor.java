package com.itouch8.pump.web.aop;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.core.MethodParameter;

import com.itouch8.pump.core.util.aop.AopInterceptorSupport;
import com.itouch8.pump.web.annotation.JsonBodySupport;
import com.itouch8.pump.web.annotation.JsonBodySupport.JsonBodyInfo;

public class JsonbodyInterceptor extends AopInterceptorSupport {

    @Override
    public boolean after(Map<String, Object> context, Object target, Method method, Object[] args, Object result) {
        MethodParameter returnType = new MethodParameter(method, -1);
        boolean isJsonBody = void.class != returnType.getParameterType() && JsonBodySupport.hasJsonBodyAnnotation(returnType);
        if (isJsonBody) {
            JsonBodyInfo jsonBodyInfo = JsonBodySupport.getJsonBodyInfo(result, returnType, null);
            JsonBodySupport.setJsonBodyInfoToContext(jsonBodyInfo);
        }
        return super.after(context, target, method, args, result);
    }
}
