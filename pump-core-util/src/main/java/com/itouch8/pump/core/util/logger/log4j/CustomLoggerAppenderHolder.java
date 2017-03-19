package com.itouch8.pump.core.util.logger.log4j;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.FileAppender;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.logger.log4j.filenameresolver.ILog4jFilenameResolver;


public class CustomLoggerAppenderHolder {

    private static final Map<String, ILog4jFilenameResolver> map = new HashMap<String, ILog4jFilenameResolver>();

    
    public static String resolverCustomFileName(FileAppender fileAppender, String filenameResolver, String oldFileName) {
        if (null == filenameResolver) {
            return oldFileName;
        } else {
            ILog4jFilenameResolver resolver = getResolver(filenameResolver);
            if (null == resolver) {
                return oldFileName;
            } else {
                String newFileName = resolver.resolverFileName(fileAppender, oldFileName);
                if (CoreUtils.isBlank(newFileName)) {
                    return oldFileName;
                } else {
                    return newFileName;
                }
            }
        }
    }

    
    private static ILog4jFilenameResolver getResolver(String filenameResolver) {
        if (map.containsKey(filenameResolver)) {
            return map.get(filenameResolver);
        } else {
            ILog4jFilenameResolver rs = null;
            try {
                Object o = CoreUtils.newInstance(filenameResolver);
                if (o instanceof ILog4jFilenameResolver) {
                    rs = (ILog4jFilenameResolver) o;
                }
            } catch (Exception e) {
            }
            map.put(filenameResolver, rs);
            return rs;
        }

    }
}
