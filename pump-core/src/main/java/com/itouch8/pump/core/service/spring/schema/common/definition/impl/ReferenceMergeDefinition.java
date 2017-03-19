package com.itouch8.pump.core.service.spring.schema.common.definition.impl;

import org.springframework.context.ApplicationContext;

import com.itouch8.pump.core.service.spring.SpringHelp;
import com.itouch8.pump.core.service.spring.schema.common.definition.IMergeDefinition;


public abstract class ReferenceMergeDefinition implements IMergeDefinition {

    private String ref;

    @Override
    public Object getMergeDefinitionOrBean() {
        ApplicationContext applicationContext = SpringHelp.getApplicationContext();
        if (null != applicationContext && null != ref && !"".equals(ref.trim())) {
            Object bean = applicationContext.getBean(ref.trim());
            return doMerge(bean);
        } else {
            return this;
        }
    }

    protected Object doMerge(Object refBean) {
        return refBean;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
