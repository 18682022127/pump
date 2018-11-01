package com.itouch8.pump.core.service.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.util.aop.AopHelp;
import com.itouch8.pump.core.util.aop.IAopInterceptor;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.core.util.track.Tracker;

/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 服务层拦截器<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public class ServiceAspect {

    
    public Object doAspect(ProceedingJoinPoint point) {
        long start = System.currentTimeMillis();
        boolean hasTracking = Tracker.isTracking();
        Map<String, Object> context = new HashMap<String, Object>();
        Object target = point.getTarget();
        Method method = AopHelp.getPointMethod(point);
        Object[] args = point.getArgs();
        List<IAopInterceptor> aopInterceptors = PumpConfig.getServiceAopInterceptors();
        try {
            if (!hasTracking) {
                Tracker.start();
            }
            CommonLogger.debug("enter into the class layer: Service, call the method: {}",point.getSignature());
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.before(context, target, method, args)) {
                        return null;
                    }
                }
            }
            Object rs = point.proceed();
            CommonLogger.debug("the Service method has executed success in {} ms, exit form method: {}",(System.currentTimeMillis() - start),point.getSignature());
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.after(context, target, method, args, rs)) {
                        break;
                    }
                }
            }
            return rs;
        } catch (Throwable e) {
            CommonLogger.error("the Service method has occured exception, execute failure and exit after {} ms, method: {}" , (System.currentTimeMillis() - start), point.getSignature());
            if (null != aopInterceptors && !aopInterceptors.isEmpty()) {
                for (IAopInterceptor sai : aopInterceptors) {
                    if (!sai.afterException(context, target, method, args, e)) {
                        break;
                    }
                }
            }
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
