package com.itouch8.pump.web.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.itouch8.pump.core.util.aop.AopHelp;
import com.itouch8.pump.core.util.aop.IAopInterceptor;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.core.util.track.Tracker;
import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.exception.WebExceptionCodes;

public class ControllerAspect {

    public Object doAspect(ProceedingJoinPoint point) {
        long start = System.currentTimeMillis();
        boolean hasTracking = Tracker.isTracking();
        Logger logger = AopHelp.getLogger(point); 
        Map<String, Object> context = new HashMap<String, Object>();
        Method method = AopHelp.getPointMethod(point);
        Object target = point.getTarget();
        Object[] args = point.getArgs();
        List<IAopInterceptor> aopInterceptors = WebPumpConfig.getControllerAopInterceptors();
        try {
            if (!hasTracking) {
                Tracker.start();
            }
            CommonLogger.debug("enter into the class layer: Controller, call the method: " + point.getSignature(), null, logger);
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.before(context, target, method, args)) {
                        return null;
                    }
                }
            }
            Object rs = point.proceed();
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.after(context, target, method, args, rs)) {
                        break;
                    }
                }
            }
            CommonLogger.debug("the Controller method has executed success in " + (System.currentTimeMillis() - start) + " ms, exit form method: " + point.getSignature(), null, logger);
            if (null == rs) {
                return getRs(method.getReturnType());
            }
            return rs;
        } catch (Throwable e) {
            CommonLogger.error("the Controller method has occured exception, execute failure and exit after " + (System.currentTimeMillis() - start) + " ms, method: " + point.getSignature(), e, logger);
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.afterException(context, target, method, args, e)) {
                        break;
                    }
                }
            }
            throw Throw.createRuntimeException(WebExceptionCodes.YT060000, e);
        } finally {
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.afterReturn(context, target, method, args)) {
                        break;
                    }
                }
            }
            if (!hasTracking) {
                Tracker.stop();
            }
        }
    }

    private Object getRs(Class<?> returnType) throws InstantiationException, IllegalAccessException {
        if (returnType.equals(List.class)) {
            return Collections.emptyList();
        } else if (returnType.equals(Collections.class)) {
            return Collections.emptyList();
        } else if (returnType.equals(Map.class)) {
            return new HashMap<>();
        } else if (!returnType.isInterface() && !Modifier.isAbstract(returnType.getModifiers())) {
            return returnType.newInstance();
        } else {
            return new Object();
        }

    }
}
