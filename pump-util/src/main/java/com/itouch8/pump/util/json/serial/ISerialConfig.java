package com.itouch8.pump.util.json.serial;

import java.util.Map;
import java.util.Set;

import com.itouch8.pump.util.json.serial.converter.IJsonConverter;


public interface ISerialConfig {

    
    public Class<?> getSerialType();

    
    public Map<String, String> getAliases();

    
    public Set<String> getExcludeProperties();

    
    public Set<String> getIncludeProperties();

    
    public Map<String, IJsonConverter> getConverters();

    
    public boolean isValid();
}
