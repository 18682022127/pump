package com.itouch8.pump.core.service.spring.component;

import java.util.Map;

import com.itouch8.pump.core.service.spring.SpringHelp;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.bean.impl.BaseBeanOperateWrapper;


public class SpringContextBeanOperateWrapper extends BaseBeanOperateWrapper {

    private String prefix = "spel:";

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    
    protected String getSpelExpression(Object bean, String property) {
        if (CoreUtils.isBlank(property) || CoreUtils.isBlank(getPrefix())) {
            return null;
        } else if (property.startsWith(getPrefix())) {
            return property.substring(getPrefix().length());
        } else if (bean instanceof String && SpringHelp.containsBean((String) bean)) {
            return property;
        } else {
            return null;
        }
    }

    
    @Override
    public Object getProperty(Object bean, String property) {
        String spel = getSpelExpression(bean, property);
        if (!CoreUtils.isBlank(spel)) {
            return SpringHelp.evaluate(bean, spel);
        }
        return super.getProperty(bean, property);
    }

    
    @Override
    public <E> E getProperty(Object bean, String property, Class<E> resultType) {
        String spel = getSpelExpression(bean, property);
        if (!CoreUtils.isBlank(spel)) {
            return SpringHelp.evaluate(bean, spel, null, resultType);
        }
        return super.getProperty(bean, property, resultType);
    }

    
    @Override
    public void setProperty(Object bean, String property, Object value) {
        String spel = getSpelExpression(bean, property);
        if (!CoreUtils.isBlank(spel)) {
            SpringHelp.setValue(bean, spel, null, value);
        }
        super.setProperty(bean, property, value);
    }

    
    @Override
    public void removeProperty(Object bean, String property) {
        String spel = getSpelExpression(bean, property);
        if (!CoreUtils.isBlank(spel)) {
            SpringHelp.setValue(bean, spel, null, null);
        }
        super.removeProperty(bean, property);
    }

    
    @Override
    public Object getProperty(Object bean, String expression, Map<String, Object> context) {
        String spel = getSpelExpression(bean, expression);
        if (!CoreUtils.isBlank(spel)) {
            return SpringHelp.evaluate(bean, spel, context);
        }
        return super.getProperty(bean, expression, context);
    }

    
    @Override
    public <E> E getProperty(Object bean, String expression, Map<String, Object> context, Class<E> resultType) {
        String spel = getSpelExpression(bean, expression);
        if (!CoreUtils.isBlank(spel)) {
            return SpringHelp.evaluate(bean, spel, context, resultType);
        }
        return super.getProperty(bean, expression, context, resultType);
    }

    
    @Override
    public void setProperty(Object bean, String expression, Object value, Map<String, Object> context) {
        String spel = getSpelExpression(bean, expression);
        if (!CoreUtils.isBlank(spel)) {
            SpringHelp.setValue(bean, spel, context, value);
        }
        super.setProperty(bean, expression, value, context);
    }
}
