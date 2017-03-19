package com.itouch8.pump.core.service.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.util.aop.AopHelp;
import com.itouch8.pump.core.util.aop.IAopInterceptor;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.core.util.track.Tracker;


public class ServiceAspect {

    
    public Object doAspect(ProceedingJoinPoint point) {
        long start = System.currentTimeMillis();
        boolean hasTracking = Tracker.isTracking();
        Map<String, Object> context = new HashMap<String, Object>();
        Logger logger = AopHelp.getLogger(point);
        Object target = point.getTarget();
        Method method = AopHelp.getPointMethod(point);
        Object[] args = point.getArgs();
        List<IAopInterceptor> aopInterceptors = PumpConfig.getServiceAopInterceptors();
        try {
            if (!hasTracking) {
                Tracker.start();
            }
            CommonLogger.debug("enter into the class layer: Service, call the method: " + point.getSignature(), null, logger);
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.before(context, target, method, args)) {
                        return null;
                    }
                }
            }
            Object rs = point.proceed();
            CommonLogger.debug("the Service method has executed success in " + (System.currentTimeMillis() - start) + " ms, exit form method: " + point.getSignature(), null, logger);
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.after(context, target, method, args, rs)) {
                        break;
                    }
                }
            }
            return rs;
        } catch (Throwable e) {
            CommonLogger.error("the Service method has occured exception, execute failure and exit after " + (System.currentTimeMillis() - start) + " ms, method: " + point.getSignature(), e, logger);
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.afterException(context, target, method, args, e)) {
                        break;
                    }
                }
            }
            // IExceptionMeta meta = Throw.lookupExceptionMeta(null, e);
            throw Throw.createRuntimeException(e);
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
}
