package com.itouch8.pump.util.json.serial.stdexp;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.itouch8.pump.util.json.serial.ISerialConfig;
import com.itouch8.pump.util.json.serial.SerialConfigContext;
import com.itouch8.pump.util.json.serial.converter.IJsonConverter;


public class JsonMapSerializer extends MapSerializer {

    
    private static final long serialVersionUID = 746297418507458604L;

    public JsonMapSerializer(HashSet<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer) {
        super(ignoredEntries, keyType, valueType, valueTypeIsStatic, vts, keySerializer, valueSerializer);
    }

    public JsonMapSerializer(MapSerializer src, BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, HashSet<String> ignored) {
        super(src, property, keySerializer, valueSerializer, ignored);
    }

    public JsonMapSerializer(MapSerializer src, Object filterId, boolean sortKeys) {
        super(src, filterId, sortKeys);
    }

    public JsonMapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue) {
        super(src, vts, suppressableValue);
    }

    @Override
    protected void _ensureOverride() {}

    @Override
    public MapSerializer _withValueTypeSerializer(TypeSerializer vts) {
        if (_valueTypeSerializer == vts) {
            return this;
        }
        return new JsonMapSerializer(this, vts, null);
    }

    public MapSerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, HashSet<String> ignored, boolean sortKeys) {
        MapSerializer ser = new JsonMapSerializer(this, property, keySerializer, valueSerializer, ignored);
        // if (sortKeys != ser._sortKeys) {
        ser = new JsonMapSerializer(ser, _filterId, sortKeys);
        // }
        return ser;
    }

    @Override
    public MapSerializer withFilterId(Object filterId) {
        if (_filterId == filterId) {
            return this;
        }
        return new JsonMapSerializer(this, filterId, _sortKeys);
    }

    public MapSerializer withContentInclusion(Object suppressableValue) {
        if (suppressableValue == _suppressableValue) {
            return this;
        }
        _ensureOverride();
        return new JsonMapSerializer(this, _valueTypeSerializer, suppressableValue);
    }

    @Override
    public void serializeFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (null == value) {
            super.serializeFields(value, gen, provider);
            return;
        }
        ISerialConfig serialConfig = SerialConfigContext.getSerialConfig(value.getClass());
        if (null == serialConfig) {
            super.serializeFields(value, gen, provider);
            return;
        }

        // If value type needs polymorphic type handling, some more work needed:
        if (_valueTypeSerializer != null) {
            serializeTypedFields(value, gen, provider, null);
            return;
        }
        final JsonSerializer<Object> keySerializer = _keySerializer;
        final HashSet<String> ignored = _ignoredEntries;

        PropertySerializerMap serializers = _dynamicValueSerializers;

        Map<String, String> aliases = serialConfig.getAliases();
        Set<String> includes = serialConfig.getIncludeProperties();
        Set<String> excludes = serialConfig.getExcludeProperties();

        for (Map.Entry<?, ?> entry : value.entrySet()) {
            Object valueElem = entry.getValue();
            // First, serialize key
            Object keyElem = entry.getKey();
            String name = null == keyElem ? null : keyElem.toString();
            IJsonConverter converter = SerialConfigContext.getJsonConverter(value, name);
            if (null != converter) {
                valueElem = converter.convert(value, name, valueElem);
            }

            if (null != excludes && excludes.contains(keyElem)) {
                continue;
            } else if (null != aliases && aliases.containsKey(keyElem)) {
                keyElem = aliases.get(keyElem);
            } else if (null != includes && !includes.isEmpty() && !includes.contains(keyElem)) {
                continue;
            }

            if (keyElem == null) {
                provider.findNullKeySerializer(_keyType, _property).serialize(null, gen, provider);
            } else {
                // One twist: is entry ignorable? If so, skip
                if (ignored != null && ignored.contains(keyElem))
                    continue;
                keySerializer.serialize(keyElem, gen, provider);
            }

            // And then value
            if (valueElem == null) {
                provider.defaultSerializeNull(gen);
            } else {
                Class<?> cc = valueElem.getClass();
                JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                if (serializer == null) {
                    if (_valueType.hasGenericTypes()) {
                        serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(_valueType, cc), provider);
                    } else {
                        serializer = _findAndAddDynamic(serializers, cc, provider);
                    }
                    serializers = _dynamicValueSerializers;
                }
                try {
                    serializer.serialize(valueElem, gen, provider);
                } catch (Exception e) {
                    // Add reference information
                    String keyDesc = "" + keyElem;
                    wrapAndThrow(provider, e, value, keyDesc);
                }
            }
        }
    }

}
