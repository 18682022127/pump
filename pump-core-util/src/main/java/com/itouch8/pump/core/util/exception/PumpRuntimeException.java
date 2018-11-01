package com.itouch8.pump.core.util.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.itouch8.pump.core.util.exception.Throw.PumpExceptionInnerProxy;


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

   
    public String getCode() {
        return this.proxy.getCode();
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
    
    @Override
    public void printStackTrace(PrintStream s) {
        s.print(Throw.getStackMessage(this));
    }

    
    @Override
    public void printStackTrace(PrintWriter s) {
        s.print(Throw.getStackMessage(this));
    }

    
     PumpExceptionInnerProxy getProxy() {
        return proxy;
    }
}
