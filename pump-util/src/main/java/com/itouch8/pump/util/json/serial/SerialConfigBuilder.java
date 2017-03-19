package com.itouch8.pump.util.json.serial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itouch8.pump.util.json.serial.converter.IJsonConverter;


public class SerialConfigBuilder {

    private final List<SerialConfigBuilder> builders;
    private Class<?> serialType;
    private final Map<String, String> aliases = new HashMap<String, String>();
    private final Set<String> excludeProperties = new HashSet<String>();
    private final Set<String> includeProperties = new HashSet<String>();
    private final Map<String, IJsonConverter> converters = new HashMap<String, IJsonConverter>();

    
    public static SerialConfigBuilder start() {
        return new SerialConfigBuilder(null);
    }

    
    public SerialConfigBuilder newInstance() {
        return new SerialConfigBuilder(this.builders);
    }

    
    private SerialConfigBuilder(List<SerialConfigBuilder> builders) {
        this.builders = builders == null ? new ArrayList<SerialConfigBuilder>() : builders;
        this.builders.add(this);
    }

    
    public ISerialConfig build() {
        return new ISerialConfig() {
            @Override
            public Class<?> getSerialType() {
                return serialType;
            }

            @Override
            public Map<String, String> getAliases() {
                return aliases;
            }

            @Override
            public Set<String> getExcludeProperties() {
                return excludeProperties;
            }

            @Override
            public Set<String> getIncludeProperties() {
                return includeProperties;
            }

            @Override
            public Map<String, IJsonConverter> getConverters() {
                return converters;
            }

            @Override
            public boolean isValid() {
                return (null != aliases && !aliases.isEmpty()) || (null != excludeProperties && !excludeProperties.isEmpty()) || (null != includeProperties && !includeProperties.isEmpty()) || (null != converters && !converters.isEmpty());
            }
        };
    }

    
    public ISerialConfig[] builds() {
        int size = null == builders ? 0 : builders.size();
        if (size > 0) {
            ISerialConfig[] configs = new ISerialConfig[builders.size()];
            for (int i = 0; i < size; i++) {
                configs[i] = builders.get(i).build();
            }
            return configs;
        }
        return null;
    }

    
    public SerialConfigBuilder serialType(Class<?> serialType) {
        this.serialType = serialType;
        return this;
    }

    
    public SerialConfigBuilder addAlias(String property, String alias) {
        this.aliases.put(property, alias);
        return this;
    }

    
    public SerialConfigBuilder addAliases(Map<String, String> aliases) {
        if (null != aliases) {
            this.aliases.putAll(aliases);
        }
        return this;
    }

    
    public SerialConfigBuilder addIncludeProperty(String... property) {
        if (null != property) {
            for (String p : property) {
                this.includeProperties.add(p);
            }
        }
        return this;
    }

    
    public SerialConfigBuilder addIncludeProperties(Collection<String> includeProperties) {
        if (null != includeProperties) {
            this.includeProperties.addAll(includeProperties);
        }
        return this;
    }

    
    public SerialConfigBuilder addExcludeProperty(String... property) {
        if (null != property) {
            for (String p : property) {
                this.excludeProperties.add(p);
            }
        }
        return this;
    }

    
    public SerialConfigBuilder addExcludeProperties(Collection<String> excludeProperties) {
        if (null != excludeProperties) {
            this.excludeProperties.addAll(excludeProperties);
        }
        return this;
    }

    
    public SerialConfigBuilder addConverter(String property, IJsonConverter converter) {
        this.converters.put(property, converter);
        return this;
    }

    
    public SerialConfigBuilder addConverters(Map<String, IJsonConverter> converters) {
        if (null != converters) {
            this.converters.putAll(converters);
        }
        return this;
    }
}
