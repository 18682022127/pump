package com.itouch8.pump.core.util.exception;

import java.lang.reflect.InvocationTargetException;


public class ThrowUtils extends Throw {

    
    public static void throwException(String code, Object... args) throws PumpException {
        throw createException(code, null, args);
    }

    
    public static void throwException(Throwable e) throws PumpException {
        throw createException(null, e);
    }

    
    public static void throwException(String code, Throwable e, Object... args) throws PumpException {
        throw createException(code, e, args);
    }

    
    public static PumpException createException(String code, Object... args) {
        return createException(code, null, args);
    }

    
    public static PumpException createException(Throwable e) {
        return createException(null, e);
    }

    
    public static PumpException createException(String code, Throwable e, Object... args) {
        if (e instanceof PumpException) {
            return (PumpException) e;
        } else if (e instanceof PumpRuntimeException) {
            return new PumpException((PumpRuntimeException) e);
        } else if (e instanceof InvocationTargetException) {
            return createException(code, ((InvocationTargetException) e).getTargetException(), args);
        } else {
            PumpExceptionInnerProxy proxy = new PumpExceptionInnerProxy(code, e, args);
            return new PumpException(proxy);
        }
    }
}
