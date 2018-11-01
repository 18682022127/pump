package com.itouch8.pump.core.util.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import com.itouch8.pump.core.util.exception.Throw.PumpExceptionInnerProxy;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 平台异常类<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
final public class PumpException extends Exception {
    
    private static final long serialVersionUID = -2159369710297472601L;

    private final PumpExceptionInnerProxy proxy;

     PumpException(PumpRuntimeException exception) {
        this(exception.getProxy());
    }

     PumpException(PumpExceptionInnerProxy proxy) {
        super(proxy.getCause());
        this.proxy = proxy;
    }

    public String getCode() {
        return this.proxy.getCode();
    }

    public String getShortMessage() {
        return this.proxy.getShortMessage();
    }

    
    @Override
    public String getMessage() {
        return this.proxy.getMessage();
    }

    
    public String getStackMessage() {
        return this.proxy.getStackMessage();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        s.print(this.proxy.getStackMessage());
    }

    
    @Override
    public void printStackTrace(PrintWriter s) {
        s.print(this.proxy.getStackMessage());
    }

     PumpExceptionInnerProxy getProxy() {
        return proxy;
    }
}
