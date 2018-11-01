package com.itouch8.pump.web.springmvc.messageconverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.itouch8.pump.web.annotation.JsonBodySupport;

/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 添加JsonBody支持<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-11-01<br>
 */
public class JsonBodyHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {

    private MediaType jsonContentType = MediaType.APPLICATION_JSON_UTF8;

    public JsonBodyHttpMessageConverter() {
        super(MediaType.APPLICATION_JSON_UTF8, new MediaType("application", "*+json", Charset.forName("UTF-8")));
    }

    @Override
    protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().setContentType(getJsonContentType());
        JsonBodySupport.writeWithJsonBody(JsonBodySupport.getJsonBodyInfoFormContext(), outputMessage.getBody());
        JsonBodySupport.removeJsonBodyInfoFormContext();
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return JsonBodySupport.getJsonBodyInfoFormContext() != null;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return true;// 直接覆盖根据媒体类型选择转换器
    }

    protected boolean canRead(MediaType mediaType) {
        return false;// 不做读操作
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException("not support read JsonBodyInfo.");
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException("not support read JsonBodyInfo.");
    }

    public MediaType getJsonContentType() {
        return jsonContentType;
    }

    public void setJsonContentType(MediaType jsonContentType) {
        this.jsonContentType = jsonContentType;
    }
}
