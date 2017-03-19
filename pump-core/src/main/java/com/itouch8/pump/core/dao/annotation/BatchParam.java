package com.itouch8.pump.core.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BatchParam {

    
    boolean value() default true;

    
    String item() default "item";

    
    String property() default "this";

    
    String index() default "index";
}
