package com.itouch8.pump.core.dao.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;

import com.itouch8.pump.core.dao.exception.DaoExceptionCodes;
import com.itouch8.pump.core.util.aop.AopHelp;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class DaoAspect {

    
    public Object doAspect(ProceedingJoinPoint point) {
        long start = System.currentTimeMillis();
        Logger logger = AopHelp.getLogger(point);
        try {
            CommonLogger.debug("enter into the class layer: DAO, call the method: " + point.getSignature(), null, logger);
            Object rs = point.proceed();
            CommonLogger.debug("the DAO method has executed success in " + (System.currentTimeMillis() - start) + " ms, exit form method: " + point.getSignature(), null, logger);
            return rs;
        } catch (Throwable e) {
            CommonLogger.error("the DAO method has occured exception, execute failure and exit after " + (System.currentTimeMillis() - start) + " ms, method: " + point.getSignature(), e, logger);
            throw Throw.createRuntimeException(DaoExceptionCodes.YT020000, e);
        }
    }
}
