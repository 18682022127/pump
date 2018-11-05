package com.itouch8.pump.core.dao.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class DaoAspect {

    
    public Object doAspect(ProceedingJoinPoint point) {
        long start = System.currentTimeMillis();
        try {
            CommonLogger.debug("enter into the class layer: DAO, call the method: " + point.getSignature());
            Object rs = point.proceed();
            CommonLogger.debug("the DAO method has executed success in " + (System.currentTimeMillis() - start) + " ms, exit form method: " + point.getSignature());
            return rs;
        } catch (Throwable e) {
            CommonLogger.error("the DAO method has occured exception, execute failure and exit after " + (System.currentTimeMillis() - start) + " ms, method: " + point.getSignature(), e);
            throw Throw.createRuntimeException("pump.core.dao.batch_param_size_not_equal", e);
        }
    }
}
