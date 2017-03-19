package com.itouch8.pump.core.util.logger.log4j.filenameresolver;

import org.apache.log4j.FileAppender;


public interface ILog4jFilenameResolver {

    
    public String resolverFileName(FileAppender fileAppender, String oldFileName);
}
