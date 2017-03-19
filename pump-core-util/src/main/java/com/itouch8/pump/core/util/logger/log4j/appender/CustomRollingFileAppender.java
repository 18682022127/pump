package com.itouch8.pump.core.util.logger.log4j.appender;

import java.io.IOException;

import org.apache.log4j.Layout;
import org.apache.log4j.RollingFileAppender;

import com.itouch8.pump.core.util.logger.log4j.CustomLoggerAppenderHolder;


public class CustomRollingFileAppender extends RollingFileAppender {

    private String filenameResolver;

    public CustomRollingFileAppender() {
        super();
    }

    public CustomRollingFileAppender(Layout layout, String filename, boolean append) throws IOException {
        super(layout, filename, append);
    }

    public CustomRollingFileAppender(Layout layout, String filename) throws IOException {
        super(layout, filename);
    }

    @Override
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
        fileName = CustomLoggerAppenderHolder.resolverCustomFileName(this, getFilenameResolver(), fileName);
        super.setFile(fileName, append, bufferedIO, bufferSize);
    }

    public String getFilenameResolver() {
        return filenameResolver;
    }

    public void setFilenameResolver(String filenameResolver) {
        this.filenameResolver = filenameResolver;
    }
}
