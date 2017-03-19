package com.itouch8.pump.core.util.cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;


public class RoleBeanDefinitionConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String[] beanNames;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        if (beanNames == null)
            return;

        for (String name : beanNames) {
            if (registry.containsBeanDefinition(name))
                ((AbstractBeanDefinition) registry.getBeanDefinition(name)).setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }
    }

    public String[] getBeanNames() {
        return beanNames;
    }

    public void setBeanNames(String[] beanNames) {
        this.beanNames = beanNames;
    }
}
