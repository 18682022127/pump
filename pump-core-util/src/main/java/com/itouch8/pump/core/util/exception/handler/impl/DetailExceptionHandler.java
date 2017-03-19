package com.itouch8.pump.core.util.exception.handler.impl;

import com.itouch8.pump.core.util.exception.PumpRuntimeException;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class DetailExceptionHandler extends AbstractExceptionHandler {

    private boolean writeErrorLogger = true;

    
    @Override
    protected Object handlerPumpRuntimeException(PumpRuntimeException exception) {
        String msg = Throw.getMessage(exception);
        if (isWriteErrorLogger()) {
            CommonLogger.error(exception);
        }
        return msg;
    }

    public boolean isWriteErrorLogger() {
        return writeErrorLogger;
    }

    public void setWriteErrorLogger(boolean writeErrorLogger) {
        this.writeErrorLogger = writeErrorLogger;
    }

}
