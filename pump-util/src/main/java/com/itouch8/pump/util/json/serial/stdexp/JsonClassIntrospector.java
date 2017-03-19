package com.itouch8.pump.util.json.serial.stdexp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.itouch8.pump.util.json.serial.ISerialConfig;
import com.itouch8.pump.util.json.serial.SerialConfigContext;


public class JsonClassIntrospector extends BasicClassIntrospector {

    
    private static final long serialVersionUID = 4095454657615388622L;

    @Override
    public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, MixInResolver r) {
        BasicBeanDescription bbd = SerialConfigContext.getBasicBeanDescription(type);
        if (null == bbd) {
            bbd = super.forSerialization(cfg, type, r);
            ISerialConfig serialConfig = SerialConfigContext.getSerialConfig(type.getRawClass());
            if (null != serialConfig) {
                Map<String, String> aliases = serialConfig.getAliases();
                Set<String> includes = serialConfig.getIncludeProperties();
                Set<String> excludes = serialConfig.getExcludeProperties();

                List<BeanPropertyDefinition> bpds = bbd.findProperties();
                for (int i = 0; i < bpds.size(); i++) {
                    BeanPropertyDefinition bpd = bpds.get(i);
                    String name = bpd.getName();
                    if (null != excludes && excludes.contains(name)) {
                        bpds.remove(bpd);
                        i--;
                    } else if (null != aliases && aliases.containsKey(name)) {
                        bpds.remove(i);
                        bpds.add(i, bpd.withSimpleName(aliases.get(name)));
                    } else if (null != includes && !includes.isEmpty() && !includes.contains(name)) {
                        bpds.remove(bpd);
                        i--;
                    }
                }
            }
        }
        return bbd;
    }
}
