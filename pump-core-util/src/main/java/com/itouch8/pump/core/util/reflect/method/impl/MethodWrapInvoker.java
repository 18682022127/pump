package com.itouch8.pump.core.util.reflect.method.impl;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.reflect.method.IMethodInvoker;
import com.itouch8.pump.core.util.reflect.method.IParamExtractor;


public class MethodWrapInvoker implements IMethodInvoker, Serializable {

    
    private static final long serialVersionUID = 713289286180221048L;

    private Method method;

    private String[] argNames;

    private int order;

    private IParamExtractor extractor;

    public MethodWrapInvoker(Method method, int order, String[] args) {
        this(method, order, args, null);
    }

    public MethodWrapInvoker(Method method, int order, String[] args, IParamExtractor extractor) {
        this.method = method;
        this.order = order;
        if (null != extractor) {
            this.extractor = extractor;
        }
        if (null != args) {
            // 计算可能的参数数组长度
            int length = args.length;
            for (String arg : args) {
                if (-1 != arg.indexOf('=')) {
                    int t = Integer.parseInt(arg.split("=")[0]);
                    length = length > t ? length : t;
                }
            }
            this.argNames = new String[length];
            // 按位置对应
            for (int i = 0, l = args.length; i < l; i++) {
                if (-1 == args[i].indexOf('=')) {
                    this.argNames[i] = args[i].trim();
                }
            }
            // 按指定索引对应，如有相同，会覆盖位置对应
            for (String arg : args) {
                if (-1 != arg.indexOf('=')) {
                    this.argNames[Integer.parseInt(arg.split("=")[0])] = arg.split("=")[1].trim();
                }
            }
        }
    }

    
    @Override
    public Object invoke(Object param) {
        return invoke(param, extractor);
    }

    
    @Override
    public Object invoke(Object param, IParamExtractor extractor) {
        Method handlerMethod = getMethod();
        try {
            Object handler = handlerMethod.getDeclaringClass().newInstance();
            Class<?>[] parameterTypes = handlerMethod.getParameterTypes();// 参数类型
            int length = parameterTypes.length;
            if (length > 0 && null == extractor) {
                Throw.throwRuntimeException("the param extractor is null.");
            }
            Object[] params = new Object[length];
            for (int i = 0; i < length; i++) {
                Class<?> type = parameterTypes[i];
                String argName = getArgName(i);
                params[i] = extractor.extract(type, argName, param);
            }
            return handlerMethod.invoke(handler, params);
        } catch (Exception e) {
            throw Throw.createRuntimeException("invoke method failure:" + handlerMethod, e);
        }
    }

    public IParamExtractor getExtractor() {
        return extractor;
    }

    private Method getMethod() {
        return method;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    private String getArgName(int index) {
        if (index < 0 || null == argNames || index >= argNames.length) {
            return null;
        }
        return argNames[index];
    }
}
