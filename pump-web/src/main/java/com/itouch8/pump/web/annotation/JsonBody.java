package com.itouch8.pump.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.ResponseBody;

import com.itouch8.pump.util.json.serial.wrapper.IJsonWrapper;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseBody
public @interface JsonBody {

    
    String value() default "";

    
    String[] fields() default {};

    
    JsonField[] jsonFields() default {};

    
    Class<? extends IJsonWrapper> wrapperClass() default DefaultJsonWrapper.class;

    public class DefaultJsonWrapper implements IJsonWrapper {
        @Override
        public Object wrap(Object original) {
            throw new UnsupportedOperationException("DefaultJsonWrapper only used to marked the default value");
        }
    }
}
