package com.itouch8.pump.core.service.spring.schema.logger.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itouch8.pump.core.service.spring.schema.common.parser.AbstractParser;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.logger.termination.impl.LogTerminationProxy;
import com.itouch8.pump.core.util.stack.impl.SimpleStackFactory;


public class LoggerParser extends AbstractParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return BaseConfig.class;
    }

    @Override
    protected void parseOneSimpleProperty(Element element, ParserContext parserContext, BeanDefinitionBuilder builder, Attr attribute, String name, String value) {
        if ("stackFactory-ref".equalsIgnoreCase(name)) {// 堆栈工厂属性的特殊处理
            String p = "stackFactory";
            if ("simple".equals(value)) {
                builder.addPropertyValue(p, getStackFactoryBeanDefinition(value));
            } else {
                builder.addPropertyReference(p, value);
            }
        } else {
            super.parseOneSimpleProperty(element, parserContext, builder, attribute, name, value);
        }
    }

    @Override
    protected void postProcess(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
        BeanDefinition containingBean = builder.getBeanDefinition();
        NodeList childNodes = element.getChildNodes();

        for (int i = 0, l = childNodes.getLength(); i < l; i++) {
            Node node = childNodes.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            if (nodeNameEquals(node, "stackFactory")) {
                String ref = ((Element) node).getAttribute("ref");
                if (StringUtils.hasLength(ref)) {
                    if ("simple".equals(ref)) {
                        builder.addPropertyValue("stackFactory", getStackFactoryBeanDefinition(ref));
                    } else {
                        builder.addPropertyReference("stackFactory", ref);
                    }
                    continue;
                }
                builder.addPropertyValue("stackFactory", parseStackFactory((Element) node, delegate, containingBean));
            } else if (nodeNameEquals(node, "logTermination")) {
                ManagedList<Object> logTerminations = parseLogTermination((Element) node, delegate, containingBean);
                if (!logTerminations.isEmpty()) {
                    builder.addPropertyValue("logTerminations", logTerminations);
                }
            }
        }
    }

    private Object getStackFactoryBeanDefinition(String name) {
        if ("simple".equals(name)) {
            BeanDefinitionBuilder b = BeanDefinitionBuilder.genericBeanDefinition();
            b.getRawBeanDefinition().setBeanClass(SimpleStackFactory.class);
            return b.getBeanDefinition();
        }
        return null;
    }

    private Object parseStackFactory(Element ele, BeanDefinitionParserDelegate delegate, BeanDefinition containingBean) {
        NodeList childNodes = ele.getChildNodes();
        for (int i = 0, l = childNodes.getLength(); i < l; i++) {
            Node node = childNodes.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            if (nodeNameEquals(node, "simple")) {
                return getStackFactoryBeanDefinition("simple");
            } else {
                return parseSingleObject((Element) node, delegate, containingBean);
            }
        }
        return null;
    }

    private ManagedList<Object> parseLogTermination(Element ele, BeanDefinitionParserDelegate delegate, BeanDefinition containingBean) {
        ManagedList<Object> logTerminations = new ManagedList<Object>();
        NodeList childNodes = ele.getChildNodes();
        for (int i = 0, l = childNodes.getLength(); i < l; i++) {
            Node node = childNodes.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            if (nodeNameEquals(node, "proxy")) {
                BeanDefinitionBuilder b = BeanDefinitionBuilder.genericBeanDefinition();
                b.getRawBeanDefinition().setBeanClass(LogTerminationProxy.class);
                logTerminations.add(b.getBeanDefinition());
            } else {
                logTerminations.add(parseSingleObject((Element) node, delegate, containingBean));
            }
        }
        return logTerminations;
    }
}
