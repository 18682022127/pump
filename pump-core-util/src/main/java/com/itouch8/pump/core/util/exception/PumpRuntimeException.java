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

    public String getCode() {
        return this.proxy.getCode();
    }

    @Override
    public String getMessage() {
        return this.getShortMessage();
    }
    
    public String getShortMessage() {
        return this.proxy.getMessage();
    }

    
    public String getStackMessage() {
        return this.proxy.getStackMessage();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        s.print(getStackMessage());
    }
    
    @Override
    public void printStackTrace(PrintWriter s) {
        s.print(getStackMessage());
    }

     PumpExceptionInnerProxy getProxy() {
        return proxy;
    }
}
