package com.itouch8.pump.core.util.init;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Init {

    
    String init() default "init";

    
    String destory() default "destory";

    
    boolean ignoreRuntimeException() default false;

    
    int order() default 0;

    
    Class<?>[] depends() default {};
}
