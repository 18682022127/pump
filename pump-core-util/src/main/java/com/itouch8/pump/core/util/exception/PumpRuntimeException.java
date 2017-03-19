package com.itouch8.pump.core.util.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

import com.itouch8.pump.core.util.exception.Throw.PumpExceptionInnerProxy;
import com.itouch8.pump.core.util.exception.handler.IExceptionHandler;
import com.itouch8.pump.core.util.exception.level.ExceptionLevel;


final public class PumpRuntimeException extends RuntimeException {
    
    private static final long serialVersionUID = -2159369710297472601L;

    private final PumpExceptionInnerProxy proxy;

     PumpRuntimeException(PumpException exception) {
        this(exception.getProxy());
    }

     PumpRuntimeException(PumpExceptionInnerProxy proxy) {
        super(proxy.getCause());
        this.proxy = proxy;
    }

    
    public String getTrackId() {
        return this.proxy.getTrackId();
    }

    
    public String getParentCode() {
        return this.proxy.getParentCode();
    }

    
    public String getCode() {
        return this.proxy.getCode();
    }

    
    public String getView() {
        return this.proxy.getView();
    }

    
    public ExceptionLevel getLevel() {
        return this.proxy.getLevel();
    }

    
    public String getShortMessage() {
        return this.proxy.getMessage();
    }

    
    @Override
    public String getMessage() {
        return Throw.getMessage(this);
    }

    
    public String getStackMessage() {
        return Throw.getStackMessage(this);
    }

    
    public List<IExceptionHandler> getHandlers() {
        return this.proxy.getHandlers();
    }

    
    @Override
    public void printStackTrace(PrintStream s) {
        s.print(Throw.getStackMessage(this));
        // super.printStackTrace(s);
    }

    
    @Override
    public void printStackTrace(PrintWriter s) {
        s.print(Throw.getStackMessage(this));
        // super.printStackTrace(s);
    }

    
     PumpExceptionInnerProxy getProxy() {
        return proxy;
    }
}
