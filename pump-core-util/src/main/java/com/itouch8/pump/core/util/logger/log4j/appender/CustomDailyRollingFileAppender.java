package com.itouch8.pump.core.util.logger.log4j.appender;

import java.io.IOException;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;

import com.itouch8.pump.core.util.logger.log4j.CustomLoggerAppenderHolder;


public class CustomDailyRollingFileAppender extends DailyRollingFileAppender {

    private String filenameResolver;

    public CustomDailyRollingFileAppender() {
        super();
    }

    public CustomDailyRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException {
        super(layout, filename, datePattern);
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
