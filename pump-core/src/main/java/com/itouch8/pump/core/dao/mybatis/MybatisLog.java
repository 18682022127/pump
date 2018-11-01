package com.itouch8.pump.core.dao.mybatis;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;

import com.itouch8.pump.core.util.logger.CommonLogger;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : Mybatis日志实现覆盖<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public class MybatisLog extends Slf4jImpl {

    public MybatisLog(String clazz) {
        super(clazz);
    }

    
    @Override
    public boolean isDebugEnabled() {
        return super.isDebugEnabled();
    }

    
    @Override
    public boolean isTraceEnabled() {
        return super.isTraceEnabled();
        // return true;
    }

    
    @Override
    public void error(String s, Throwable e) {
         CommonLogger.error(s, e);
    }

    
    @Override
    public void error(String s) {
        CommonLogger.error(s);
    }

    
    @Override
    public void debug(String s) {
    	CommonLogger.debug(s);
    }

    
    @Override
    public void trace(String s) {
    	CommonLogger.debug(s);
    }

    
    @Override
    public void warn(String s) {
    	CommonLogger.warn(s);
    }

}
