package com.itouch8.pump.core.util.logger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.logger.level.LogLevel;
import com.itouch8.pump.core.util.logger.termination.ILogTermination;
import com.itouch8.pump.core.util.stack.IStack;
import com.itouch8.pump.core.util.stack.IStackFactory;


public class CommonLogger {

    
    public static boolean isDebugEnabled() {
        StackTraceElement stack = getStack();
        return isEnabled(stack, LogLevel.DEBUG);
    }

    
    public static boolean isInfoEnabled() {
        StackTraceElement stack = getStack();
        return isEnabled(stack, LogLevel.INFO);
    }

    
    public static boolean isWarnEnabled() {
        StackTraceElement stack = getStack();
        return isEnabled(stack, LogLevel.WARN);
    }

    
    public static boolean isErrorEnabled() {
        StackTraceElement stack = getStack();
        return isEnabled(stack, LogLevel.ERROR);
    }

    
    public static void debug(String msg, String... args) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.DEBUG), stack, format(msg, args), null);
    }

    
    public static void debug(Throwable e) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.DEBUG), stack, null, e);
    }

    
    public static void debug(String msg, Throwable e, String... args) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.DEBUG), stack, format(msg, args), e);
    }

    
    public static void debug(String msg, Throwable e, ILogTermination termination, String... args) {
        StackTraceElement stack = getStack();
        IStack ss = getStack(null, stack, format(msg, args), e);
        termination.write(getLogLevel(LogLevel.DEBUG), ss);
    }

    
    public static void debug(String msg, Throwable e, Logger logger, String... args) {
        StackTraceElement stack = getStack();
        write(logger, getLogLevel(LogLevel.DEBUG), stack, format(msg, args), e);
    }

    
    public static void info(String msg, String... args) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.INFO), stack, format(msg, args), null);
    }

    
    public static void info(Throwable e) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.INFO), stack, null, e);
    }

    
    public static void info(String msg, Throwable e, String... args) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.INFO), stack, format(msg, args), e);
    }

    
    public static void info(String msg, Throwable e, ILogTermination termination, String... args) {
        StackTraceElement stack = getStack();
        IStack ss = getStack(null, stack, format(msg, args), e);
        termination.write(getLogLevel(LogLevel.INFO), ss);
    }

    
    public static void info(String msg, Throwable e, Logger logger, String... args) {
        StackTraceElement stack = getStack();
        write(logger, getLogLevel(LogLevel.INFO), stack, format(msg, args), e);
    }

    
    public static void warn(String msg, String... args) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.WARN), stack, format(msg, args), null);
    }

    
    public static void warn(Throwable e) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.WARN), stack, null, e);
    }

    
    public static void warn(String msg, Throwable e, String... args) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.WARN), stack, format(msg, args), e);
    }

    
    public static void warn(String msg, Throwable e, ILogTermination termination, String... args) {
        StackTraceElement stack = getStack();
        IStack ss = getStack(null, stack, format(msg, args), e);
        termination.write(getLogLevel(LogLevel.WARN), ss);
    }

    
    public static void warn(String msg, Throwable e, Logger logger, String... args) {
        StackTraceElement stack = getStack();
        write(logger, getLogLevel(LogLevel.WARN), stack, format(msg, args), e);
    }

    
    public static void error(String msg, String... args) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.ERROR), stack, format(msg, args), null);
    }

    
    public static void error(Throwable e) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.ERROR), stack, null, e);
    }

    
    public static void error(String msg, Throwable e, String... args) {
        StackTraceElement stack = getStack();
        write(null, getLogLevel(LogLevel.ERROR), stack, format(msg, args), e);
    }

    
    public static void error(String msg, Throwable e, ILogTermination termination, String... args) {
        StackTraceElement stack = getStack();
        IStack ss = getStack(null, stack, format(msg, args), e);
        termination.write(getLogLevel(LogLevel.ERROR), ss);
    }

    
    public static void error(String msg, Throwable e, Logger logger, String... args) {
        StackTraceElement stack = getStack();
        write(logger, getLogLevel(LogLevel.ERROR), stack, format(msg, args), e);
    }

    
    private static String format(String pattern, String... arguments) {
        if (null == pattern) {
            return null;
        }
        if (null == arguments) {
            return pattern;
        }
        Object[] args = new Object[arguments.length];
        for (int i = arguments.length - 1; i >= 0; i--) {
            args[i] = arguments[i];
        }
        return CoreUtils.format(pattern, args);
    }

    
    private static boolean isEnabled(StackTraceElement stack, LogLevel logLevel) {
        Logger logger = LoggerFactory.getLogger(stack.getClassName());
        LogLevel level = getLogLevel(logLevel);
        boolean enabled = false;
        switch (level) {
            case DEBUG:
                enabled = logger.isDebugEnabled();
                break;
            case INFO:
                enabled = logger.isInfoEnabled();
                break;
            case WARN:
                enabled = logger.isWarnEnabled();
                break;
            case ERROR:
                enabled = logger.isErrorEnabled();
                break;
        }
        return enabled;
    }

    
    private static void write(Logger logger, LogLevel level, StackTraceElement stack, String message, Throwable e) {
        IStackFactory stackFactory = BaseConfig.getStackFactory();
        List<ILogTermination> logTerminationList = BaseConfig.getLogTerminations();
        if (null != stackFactory && null != logTerminationList && !logTerminationList.isEmpty()) {
            IStack ss = getStack(logger, stack, message, e);
            for (ILogTermination logTermination : logTerminationList) {
                logTermination.write(level, ss);
            }
        }
    }

    
    private static LogLevel getLogLevel(LogLevel logLevel) {
        return LogMonitor.isMonitoring() ? LogLevel.DEBUG : logLevel;
    }

    
    private static IStack getStack(Logger logger, StackTraceElement stack, String message, Throwable e) {
        IStackFactory stackFactory = BaseConfig.getStackFactory();
        IStack ss = stackFactory.getStack(logger, stack, message, e);
        return ss;
    }

    
    private static StackTraceElement getStack() {
        return new Exception().getStackTrace()[2];
    }
}
