package com.itouch8.pump.core.util.logger.log4j.filenameresolver.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.FileAppender;

import com.itouch8.pump.core.util.env.EnvConsts;
import com.itouch8.pump.core.util.logger.log4j.filenameresolver.ILog4jFilenameResolver;


public class AddHostFilenameResolver implements ILog4jFilenameResolver {

    
    @Override
    public String resolverFileName(FileAppender fileAppender, String oldFileName) {
        String path = FilenameUtils.getFullPathNoEndSeparator(oldFileName);
        if (!path.endsWith("/" + EnvConsts.LOCALE_HOST)) {
            path += "/" + EnvConsts.LOCALE_HOST;
        }
        return path + "/" + FilenameUtils.getBaseName(oldFileName) + "." + FilenameUtils.getExtension(oldFileName);
    }
}
