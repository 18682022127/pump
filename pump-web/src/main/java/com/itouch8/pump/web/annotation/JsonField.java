package com.itouch8.pump.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.ResponseBody;

import com.itouch8.pump.util.json.serial.converter.IJsonConverter;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseBody
public @interface JsonField {

    
    String value();

    
    String alias() default "";

    
    boolean ignore() default false;

    
    String convertBean() default "";

    
    Class<? extends IJsonConverter> convertCls() default DefaultJsonConverter.class;

    public class DefaultJsonConverter implements IJsonConverter {
        @Override
        public Object convert(Object container, Object property, Object value) {
            throw new UnsupportedOperationException("DefaultJsonConverter only used to marked the default value");
        }
    }
}
