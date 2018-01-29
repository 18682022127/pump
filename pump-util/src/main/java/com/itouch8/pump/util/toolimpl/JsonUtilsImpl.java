package com.itouch8.pump.util.toolimpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BasicSerializerFactory;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.util.json.serial.ISerialConfig;
import com.itouch8.pump.util.json.serial.SerialConfigContext;
import com.itouch8.pump.util.json.serial.stdexp.JsonBeanSerializerFactory;
import com.itouch8.pump.util.json.serial.stdexp.JsonClassIntrospector;
import com.itouch8.pump.util.json.serial.wrapper.IJsonWrapper;

public abstract class JsonUtilsImpl {

    private static final JsonUtilsImpl instance = new JsonUtilsImpl() {};

    private JsonUtilsImpl() {}

    public static JsonUtilsImpl getInstance() {
        return instance;
    }

    private static ObjectMapper mapper = null;

    public ObjectMapper getSingleonObjectMapper() {
        if (null == mapper) {
            synchronized (JsonUtilsImpl.class) {
                if (null == mapper) {
                    ObjectMapper mapper = getObjectMapper();
                    JsonUtilsImpl.mapper = mapper;
                }
            }
        }
        return mapper;
    }

    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) // 允许使用单引号
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true) // 允许字段名不用引号
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) // 允许字段名不用引号
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)// 忽略字符串有java中没有的属性
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)//
                .configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false)// 自引用时是否失败
                .configure(SerializationFeature.INDENT_OUTPUT, true)// 格式化输出
                .configure(MapperFeature.AUTO_DETECT_FIELDS, false).configure(MapperFeature.AUTO_DETECT_GETTERS, true).configure(MapperFeature.AUTO_DETECT_IS_GETTERS, true).setSerializationInclusion(Include.NON_NULL);// 是否包括null值属性

        // 使用自定义的类侦测器
        JsonClassIntrospector jci = new JsonClassIntrospector();
        mapper.setConfig(mapper.getSerializationConfig().with(jci));
        // 使用自定义的序列化工厂类（目前只替换了Map的序列化）
        BasicSerializerFactory sf = (BasicSerializerFactory) mapper.getSerializerFactory();
        mapper.setSerializerFactory(new JsonBeanSerializerFactory(sf.getFactoryConfig()));
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return mapper;
    }

    public Map<String, Object> deserial2Map(String jsonString) {
        try {
            return getSingleonObjectMapper().readValue(jsonString, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    public List<Map<String, Object>> deserial2ListMap(String jsonString) {
        try {
            return getSingleonObjectMapper().readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    public String serialize(Object original, ISerialConfig... configs) {
        return serialize(original, null, configs);
    }

    public String serialize(Object original, IJsonWrapper wrapper, ISerialConfig... configs) {
        try {
            SerialConfigContext.addSerialConfigs(configs);
            Object target = original;
            if (null != wrapper) {
                target = wrapper.wrap(original);
            }
            return getContextObjectMapper().writeValueAsString(target);
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        } finally {
            SerialConfigContext.remove();
        }
    }

    public void serialize(OutputStream out, Object original, ISerialConfig... configs) {
        serialize(out, original, null, configs);
    }

    public void serialize(OutputStream out, Object original, IJsonWrapper wrapper, ISerialConfig... configs) {
        try {
            SerialConfigContext.addSerialConfigs(configs);
            Object target = original;
            if (null != wrapper) {
                target = wrapper.wrap(original);
            }
            getContextObjectMapper().writeValue(out, target);
        } catch (IOException e) {
            Throw.throwRuntimeException(e);
        } finally {
            SerialConfigContext.remove();
        }
    }

    public <T> List<T> deserialize2ListBean(String content, Class<T> cls) {

        try {
            return getSingleonObjectMapper().readValue(content, getType(getSingleonObjectMapper(), List.class, cls));
        } catch (IOException e) {
            Throw.throwRuntimeException(e);
        }
        return null;
    }

    private JavaType getType(ObjectMapper mapper, Class<?> cls, Class<?>... cs) {
        return mapper.getTypeFactory().constructParametricType(cls, cs);
    }

    public <T> T deserialize2Bean(String content, Class<T> cls) {

        try {
            return getSingleonObjectMapper().readValue(content, cls);
        } catch (IOException e) {
            Throw.throwRuntimeException(e);
        }
        return null;
    }

    public <T> List<T> deserialize2ListBean(InputStream stream, Class<T> cls) {

        try {
            return getSingleonObjectMapper().readValue(stream, new TypeReference<List<T>>() {});
        } catch (IOException e) {
            Throw.throwRuntimeException(e);
        }
        return null;
    }

    public <T> T deserialize2Bean(InputStream stream, Class<T> cls) {

        try {
            return getSingleonObjectMapper().readValue(stream, cls);
        } catch (IOException e) {
            Throw.throwRuntimeException(e);
        }
        return null;
    }

    private ObjectMapper getContextObjectMapper() {
        if (SerialConfigContext.isValid()) {
            return getObjectMapper();
        } else {
            return getSingleonObjectMapper();
        }
    }
}
