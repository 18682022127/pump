package com.itouch8.pump.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.ResponseBody;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseBody
public @interface ListJsonBody {

    
    String[] value() default {};

    
    JsonField[] jsonFields() default {};
}
