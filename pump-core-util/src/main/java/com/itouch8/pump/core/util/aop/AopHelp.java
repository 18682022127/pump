package com.itouch8.pump.core.util.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AopHelp {

    
    public static Logger getLogger(JoinPoint point) {
        Signature signature = point.getSignature();
        String className = signature.getDeclaringTypeName();
        Logger logger = LoggerFactory.getLogger(className);
        return logger;
    }

    
    public static StackTraceElement getStackTraceElement(JoinPoint point) {
        Signature signature = point.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        StackTraceElement stack = null;
        try {// 这里由于Spring根本没有实现SourceLocation接口，获取文件名称会抛出异常
            SourceLocation source = point.getSourceLocation();
            stack = new StackTraceElement(className, methodName, source.getFileName(), source.getLine());
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            if (null != stackTrace) {
                for (StackTraceElement st : stackTrace) {
                    if (st.getClassName().startsWith(className) && methodName.equals(st.getMethodName())) {
                        stack = st;
                        break;
                    }
                }
            }
        }
        return stack;
    }

    
    public static Method getPointMethod(JoinPoint point) {
        Signature signature = point.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature m = (MethodSignature) signature;
            return m.getMethod();
        }
        return null;
    }
}
