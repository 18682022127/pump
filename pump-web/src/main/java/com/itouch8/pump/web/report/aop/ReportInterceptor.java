package com.itouch8.pump.web.report.aop;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.core.MethodParameter;

import com.itouch8.pump.core.util.aop.AopInterceptorSupport;
import com.itouch8.pump.web.report.annotation.ReportSupport;

public class ReportInterceptor extends AopInterceptorSupport {

    @Override
    public boolean after(Map<String, Object> context, Object target, Method method, Object[] args, Object result) {

        MethodParameter returnType = new MethodParameter(method, -1);
        boolean isReport = void.class != returnType.getParameterType() && ReportSupport.hasReportAnnotation(returnType);
        if (isReport) {
            ReportSupport.setReportDataContext(result);
        }
        return super.after(context, target, method, args, result);
    }
}
