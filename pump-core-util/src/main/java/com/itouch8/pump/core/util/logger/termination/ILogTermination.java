package com.itouch8.pump.core.util.logger.termination;

import com.itouch8.pump.core.util.logger.level.LogLevel;
import com.itouch8.pump.core.util.stack.IStack;


public interface ILogTermination {

    
    public void write(LogLevel level, IStack stack);
}
