package com.itouch8.pump.web.annotation;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.MethodParameter;

import com.itouch8.pump.core.service.spring.SpringHelp;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.util.Tool;
import com.itouch8.pump.util.json.serial.ISerialConfig;
import com.itouch8.pump.util.json.serial.SerialConfigBuilder;
import com.itouch8.pump.util.json.serial.converter.IJsonConverter;
import com.itouch8.pump.util.json.serial.wrapper.IJsonWrapper;
import com.itouch8.pump.util.json.serial.wrapper.impl.NoWrapJsonWrapper;
import com.itouch8.pump.util.json.serial.wrapper.impl.PageJsonWrapper;
import com.itouch8.pump.util.json.serial.wrapper.impl.TreeJsonWrapper;
import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.annotation.JsonBody.DefaultJsonWrapper;


public class JsonBodySupport {

    private static final IJsonWrapper nowrap = new NoWrapJsonWrapper();

    private static final ThreadLocal<JsonBodyInfo> jsonBodyInfo = new ThreadLocal<JsonBodyInfo>();

    public interface JsonBodyInfo {
        public IJsonWrapper getJsonWrapper();

        public String[] getFields();

        public JsonField[] getJsonFields();

        public Object getReturnValue();

        public MethodParameter getReturnType();

        public Throwable getThrowable();
    }

    public static void setJsonBodyInfoToContext(JsonBodyInfo jsonBodyInfo) {
        JsonBodySupport.jsonBodyInfo.set(jsonBodyInfo);
    }

    public static JsonBodyInfo getJsonBodyInfoFormContext() {
        return jsonBodyInfo.get();
    }

    public static void removeJsonBodyInfoFormContext() {
        jsonBodyInfo.remove();
    }

    public static boolean hasJsonBodyAnnotation(MethodParameter returnType) {
        return returnType.getMethodAnnotation(JsonBody.class) != null || returnType.getMethodAnnotation(ListJsonBody.class) != null || returnType.getMethodAnnotation(NoWrapJsonBody.class) != null || returnType.getMethodAnnotation(PageJsonBody.class) != null || returnType.getMethodAnnotation(TreeJsonBody.class) != null;
    }

    public static JsonBodyInfo getJsonBodyInfo(Object returnValue, MethodParameter returnType, Throwable throwable) {
        IJsonWrapper wrapper = null;
        String[] fields = null;
        JsonField[] jsonFields = null;
        if (null != throwable) {
            wrapper = WebPumpConfig.getExceptionWrapper();
            fields = null;
            jsonFields = null;
            return returnJsonBodyInfo(wrapper, fields, jsonFields, returnValue, returnType, throwable);
        }

        PageJsonBody pageBody = returnType.getMethodAnnotation(PageJsonBody.class);
        if (null != pageBody) {
            wrapper = getPageWrapper();
            fields = pageBody.value();
            jsonFields = pageBody.jsonFields();
            return returnJsonBodyInfo(wrapper, fields, jsonFields, returnValue, returnType, throwable);
        }

        ListJsonBody listBody = returnType.getMethodAnnotation(ListJsonBody.class);
        if (null != listBody) {// 同nowrap
            wrapper = nowrap;
            fields = listBody.value();
            jsonFields = listBody.jsonFields();
            return returnJsonBodyInfo(wrapper, fields, jsonFields, returnValue, returnType, throwable);
        }

        NoWrapJsonBody noBody = returnType.getMethodAnnotation(NoWrapJsonBody.class);
        if (null != noBody) {
            wrapper = nowrap;
            fields = noBody.value();
            jsonFields = noBody.jsonFields();
            return returnJsonBodyInfo(wrapper, fields, jsonFields, returnValue, returnType, throwable);
        }

        TreeJsonBody treeBody = returnType.getMethodAnnotation(TreeJsonBody.class);
        if (null != treeBody) {
            TreeJsonWrapper treeJsonWrapper = new TreeJsonWrapper();
            treeJsonWrapper.setReturnArrayWhenOnlyOneNode(treeBody.returnArrayWhenOnlyOneNode());
            fields = treeBody.value();
            jsonFields = treeBody.jsonFields();
            return returnJsonBodyInfo(treeJsonWrapper, fields, jsonFields, returnValue, returnType, throwable);
        }

        JsonBody body = returnType.getMethodAnnotation(JsonBody.class);
        wrapper = getJsonWrapper(body);
        fields = body.fields();
        jsonFields = body.jsonFields();
        return returnJsonBodyInfo(wrapper, fields, jsonFields, returnValue, returnType, throwable);
    }

    public static void writeWithJsonBody(JsonBodyInfo jsonBody, OutputStream out) {
        IJsonWrapper wrapper = jsonBody.getJsonWrapper();
        String[] fields = jsonBody.getFields();
        JsonField[] jsonFields = jsonBody.getJsonFields();
        Object returnValue = jsonBody.getReturnValue();
        // MethodParameter returnType = jsonBody.getReturnType();

        Object target = returnValue;
        if (null != wrapper) {
            target = wrapper.wrap(target);
        }
        if (null == target) {
            return;
        } else if (null == returnValue) {
            Tool.JSON.serialize(out, target);
        } else if ((null == fields || fields.length == 0) && (null == jsonFields || jsonFields.length == 0)) {
            Tool.JSON.serialize(out, target);
        } else {
            Class<?> serialType = returnValue.getClass();
            if (List.class.isAssignableFrom(serialType)) {// 如果是集合类型
                List<?> c = (List<?>) returnValue;
                if (!c.isEmpty()) {
                    serialType = c.get(0).getClass();// ignore check null == c.get(0)
                } else {
                    Tool.JSON.serialize(out, target);
                    return;
                }
            }

            SerialConfigBuilder builder = SerialConfigBuilder.start().serialType(serialType);
            Set<String> fieldSet = new HashSet<String>();
            if (null != jsonFields && jsonFields.length > 0) {
                for (JsonField field : jsonFields) {
                    resolverJsonField(fieldSet, field, builder);
                }
            }
            if (null != fields && fields.length > 0) {
                for (String field : fields) {
                    resolverShortField(fieldSet, field, builder);
                }
            }
            ISerialConfig config = builder.build();
            Tool.JSON.serialize(out, target, config);
        }
    }

    private static IJsonWrapper getPageWrapper() {
        return WebPumpConfig.getPageJsonWrapper();
    }

    private static IJsonWrapper getDefaultJsonWrapper() {
        return WebPumpConfig.getDefaultJsonWrapper();
    }

    private static void resolverShortField(Set<String> fieldSet, String field, SerialConfigBuilder builder) {
        String[] cfgs = field.split("\\s*#\\s*");
        String name = cfgs[0];
        if (fieldSet.contains(name)) {// 已经有配置，忽略不计
            return;
        }
        if (cfgs.length == 1) {
            builder.addIncludeProperty(name);
        } else {
            String cfg1 = cfgs[1].toLowerCase();
            if ("false".equals(cfg1) || "0".equals(cfg1)) {
                builder.addExcludeProperty(name);
            } else if ("true".equals(cfg1) || "1".equals(cfg1)) {
                builder.addIncludeProperty(name);
            } else {
                builder.addAlias(name, cfgs[1]);
            }
        }
    }

    private static void resolverJsonField(Set<String> fieldSet, JsonField field, SerialConfigBuilder builder) {
        String name = field.value();
        if (fieldSet.contains(name)) {// 已经有配置，忽略不计
            return;
        }
        if (field.ignore()) {
            builder.addExcludeProperty(name);
        } else {
            boolean hasConvert = false;
            String convertBean = field.convertBean();
            if (!CoreUtils.isBlank(convertBean)) {
                hasConvert = true;
                builder.addConverter(name, SpringHelp.getBean(convertBean, IJsonConverter.class));
            } else {
                Class<? extends IJsonConverter> cls = field.convertCls();
                if (!JsonField.DefaultJsonConverter.class.equals(cls)) {
                    hasConvert = true;
                    builder.addConverter(name, CoreUtils.newInstance(cls));
                }
            }

            String alias = field.alias();
            if (!CoreUtils.isBlank(alias)) {
                builder.addAlias(name, alias);
            } else if (hasConvert) {// 如果有转换器，这里使用别名，从而阻止开启肯定筛选模式
                builder.addAlias(name, name);
            } else {
                builder.addIncludeProperty(name);
            }
        }
    }

    private static JsonBodyInfo returnJsonBodyInfo(final IJsonWrapper wrapper, final String[] fields, final JsonField[] jsonFields, final Object returnValue, final MethodParameter returnType, final Throwable throwable) {
        return new JsonBodyInfo() {
            @Override
            public IJsonWrapper getJsonWrapper() {
                return wrapper;
            }

            @Override
            public String[] getFields() {
                return fields;
            }

            @Override
            public JsonField[] getJsonFields() {
                return jsonFields;
            }

            public Object getReturnValue() {
                return returnValue;
            }

            public MethodParameter getReturnType() {
                return returnType;
            }

            public Throwable getThrowable() {
                return throwable;
            }
        };
    }

    private static IJsonWrapper getJsonWrapper(JsonBody body) {
        IJsonWrapper wrapper = null;
        if (!CoreUtils.isBlank(body.value())) {
            wrapper = SpringHelp.getBean(body.value(), IJsonWrapper.class);
        } else {
            Class<? extends IJsonWrapper> wc = body.wrapperClass();
            if (PageJsonWrapper.class.equals(wc)) {
                return getPageWrapper();
            } else if (DefaultJsonWrapper.class.equals(wc)) {
                return getDefaultJsonWrapper();
            } else if (NoWrapJsonWrapper.class.equals(wc)) {
                return nowrap;
            } else {
                wrapper = CoreUtils.newInstance(wc);
            }
        }
        return wrapper;
    }
}
