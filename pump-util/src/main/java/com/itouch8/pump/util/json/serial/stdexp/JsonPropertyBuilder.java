package com.itouch8.pump.util.json.serial.stdexp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.itouch8.pump.util.json.serial.SerialConfigContext;
import com.itouch8.pump.util.json.serial.converter.IJsonConverter;


public class JsonPropertyBuilder extends PropertyBuilder {

    public JsonPropertyBuilder(SerializationConfig config, BeanDescription beanDesc) {
        super(config, beanDesc);
    }

    protected BeanPropertyWriter createBeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember am, Annotations annotations, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serializationType, boolean suppressNulls, Object valueToSuppress) {
        BeanPropertyWriter bpw = new JsonBeanPropertyWriter(propDef, am, _beanDesc.getClassAnnotations(), declaredType, ser, typeSer, serializationType, suppressNulls, valueToSuppress);
        return bpw;
    }

    protected BeanPropertyWriter buildWriter(SerializerProvider prov, BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping) throws JsonMappingException {
        // do we have annotation that forces type to use (to declared type or its super type)?
        JavaType serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);

        // Container types can have separate type serializers for content (value / element) type
        if (contentTypeSer != null) {
            
            if (serializationType == null) {
                // serializationType = TypeFactory.type(am.getGenericType(), _beanDesc.getType());
                serializationType = declaredType;
            }
            JavaType ct = serializationType.getContentType();
            // Not exactly sure why, but this used to occur; better check explicitly:
            if (ct == null) {
                throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '" + propDef.getName() + "' (of type " + _beanDesc.getType() + "); serialization type " + serializationType + " has no content");
            }
            serializationType = serializationType.withContentTypeHandler(contentTypeSer);
            ct = serializationType.getContentType();
        }

        Object valueToSuppress = null;
        boolean suppressNulls = false;

        JsonInclude.Value inclV = _defaultInclusion.withOverrides(propDef.findInclusion());
        JsonInclude.Include inclusion = inclV.getValueInclusion();
        if (inclusion == JsonInclude.Include.USE_DEFAULTS) { // should not occur but...
            inclusion = JsonInclude.Include.ALWAYS;
        }

        

        switch (inclusion) {
            case NON_DEFAULT:
                // 11-Nov-2015, tatu: This is tricky because semantics differ between cases,
                // so that if enclosing class has this, we may need to values of property,
                // whereas for global defaults OR per-property overrides, we have more
                // static definition. Sigh.
                // First: case of class specifying it; try to find POJO property defaults
                JavaType t = (serializationType == null) ? declaredType : serializationType;
                if (_defaultInclusion.getValueInclusion() == JsonInclude.Include.NON_DEFAULT) {
                    valueToSuppress = getPropertyDefaultValue(propDef.getName(), am, t);
                } else {
                    valueToSuppress = getDefaultValue(t);
                }
                if (valueToSuppress == null) {
                    suppressNulls = true;
                } else {
                    if (valueToSuppress.getClass().isArray()) {
                        valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
                    }
                }

                break;
            case NON_ABSENT: // new with 2.6, to support Guava/JDK8 Optionals
                // always suppress nulls
                suppressNulls = true;
                // and for referential types, also "empty", which in their case means "absent"
                if (declaredType.isReferenceType()) {
                    valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
                }
                break;
            case NON_EMPTY:
                // always suppress nulls
                suppressNulls = true;
                // but possibly also 'empty' values:
                valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
                break;
            case NON_NULL:
                suppressNulls = true;
                // fall through
            case ALWAYS: // default
            default:
                // we may still want to suppress empty collections, as per [JACKSON-254]:
                if (declaredType.isContainerType() && !_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)) {
                    valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
                }
                break;
        }
        BeanPropertyWriter bpw = this.createBeanPropertyWriter(propDef, am, _beanDesc.getClassAnnotations(), declaredType, ser, typeSer, serializationType, suppressNulls, valueToSuppress);

        // How about custom null serializer?
        Object serDef = _annotationIntrospector.findNullSerializer(am);
        if (serDef != null) {
            bpw.assignNullSerializer(prov.serializerInstance(am, serDef));
        }
        // And then, handling of unwrapping
        NameTransformer unwrapper = _annotationIntrospector.findUnwrappingNameTransformer(am);
        if (unwrapper != null) {
            bpw = bpw.unwrappingWriter(unwrapper);
        }
        return bpw;
    }

    public class JsonBeanPropertyWriter extends BeanPropertyWriter {

        
        private static final long serialVersionUID = -8747217602937660412L;

        private BeanPropertyDefinition propDef;

        public JsonBeanPropertyWriter() {
            super();
        }

        public JsonBeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember member, Annotations contextAnnotations, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType, boolean suppressNulls, Object suppressableValue) {
            super(propDef, member, contextAnnotations, declaredType, ser, typeSer, serType, suppressNulls, suppressableValue);
            this.propDef = propDef;
        }

        public JsonBeanPropertyWriter(BeanPropertyWriter base, PropertyName name) {
            super(base, name);
        }

        public JsonBeanPropertyWriter(BeanPropertyWriter base, SerializedString name) {
            super(base, name);
        }

        public JsonBeanPropertyWriter(BeanPropertyWriter base) {
            super(base);
        }

        protected BeanPropertyWriter _new(PropertyName newName) {
            return new JsonBeanPropertyWriter(this, newName);
        }

        @Override
        public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
            String name = null != propDef ? propDef.getInternalName() : getName();
            IJsonConverter converter = SerialConfigContext.getJsonConverter(bean, name);
            if (null == converter) {
                super.serializeAsField(bean, gen, prov);
                return;
            }

            // inlined 'get()'
            Object oldValue = (_accessorMethod == null) ? _field.get(bean) : _accessorMethod.invoke(bean);
            Object value = converter.convert(bean, name, oldValue);

            // Null handling is bit different, check that first
            if (value == null) {
                if (_nullSerializer != null) {
                    gen.writeFieldName(_name);
                    _nullSerializer.serialize(null, gen, prov);
                }
                return;
            }
            // then find serializer to use
            JsonSerializer<Object> ser = null;// _serializer;
            if (null != oldValue && value.getClass().equals(oldValue.getClass())) {
                ser = _serializer;
            }

            if (ser == null) {
                Class<?> cls = value.getClass();
                PropertySerializerMap m = _dynamicSerializers;
                ser = m.serializerFor(cls);
                if (ser == null) {
                    ser = _findAndAddDynamic(m, cls, prov);
                }
            }
            // and then see if we must suppress certain values (default, empty)
            if (_suppressableValue != null) {
                if (MARKER_FOR_EMPTY == _suppressableValue) {
                    if (ser.isEmpty(prov, value)) {
                        return;
                    }
                } else if (_suppressableValue.equals(value)) {
                    return;
                }
            }
            // For non-nulls: simple check for direct cycles
            if (value == bean) {
                // three choices: exception; handled by call; or pass-through
                if (_handleSelfReference(bean, gen, prov, ser)) {
                    return;
                }
            }
            gen.writeFieldName(_name);
            if (_typeSerializer == null) {
                ser.serialize(value, gen, prov);
            } else {
                ser.serializeWithType(value, gen, prov, _typeSerializer);
            }
        }
    }
}
