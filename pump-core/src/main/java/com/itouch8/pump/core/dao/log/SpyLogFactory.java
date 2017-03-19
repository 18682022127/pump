
package com.itouch8.pump.core.dao.log;


public class SpyLogFactory {
    
    private SpyLogFactory() {}

    
    private static final SpyLogDelegator logger = new Slf4jSpyLogDelegator();
    // new Log4jSpyLogDelegator();

    
    public static SpyLogDelegator getSpyLogDelegator() {
        return logger;
    }
}
