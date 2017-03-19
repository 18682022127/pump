package com.itouch8.pump.core.util.exception.meta.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.level.ExceptionLevel;
import com.itouch8.pump.core.util.exception.meta.IExceptionMeta;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class XmlExceptionMetaLoader extends AbstractExceptionMetaLoader {

    /**
     * 是否加载的标志
     */
    private static final AtomicBoolean monitor = new AtomicBoolean(false);

    /**
     * 查找元信息，覆盖父类查找方法，确保查找之前已经加载
     */
    @Override
    public IExceptionMeta lookup(String code, Throwable cause) {
        load();
        return super.lookup(code, cause);
    }

    /**
     * 加载配置
     */
    protected void load() {
        if (!monitor.get()) {
            synchronized (monitor) {
                if (!monitor.get()) {
                    try {
                        loadPumpException();
                    } finally {
                        monitor.set(true);
                    }
                }
            }
        }
    }

    /**
     * 加载pump-exception.xml中配置
     */
    private void loadPumpException() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:com/itouch8/pump/**/pump-*exception.xml");
            if (null != resources) {
                Map<String, Class<? extends Throwable>> classCache = new HashMap<String, Class<? extends Throwable>>();
                for (Resource resource : resources) {
                    InputStream input = null;
                    try {
                        input = resource.getInputStream();
                        loadInputStream(input, classCache);
                    } catch (Exception e) {
                        // ignore
                    } finally {
                        CoreUtils.closeQuietly(input);
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * 从流中加载
     * 
     * @param input
     * @param classCache
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private void loadInputStream(InputStream input, Map<String, Class<? extends Throwable>> classCache) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(input);
        Element root = document.getDocumentElement();
        if (nodeNameEquals(root, "exception-config")) {
            NodeList childNodes = root.getChildNodes();
            if (null != childNodes && childNodes.getLength() >= 1) {
                for (int i = 0, l = childNodes.getLength(); i < l; i++) {
                    Node node = childNodes.item(i);
                    if ((node instanceof Element) && nodeNameEquals(node, "exception")) {
                        this.loadExceptionElement(null, (Element) node, classCache);
                    }
                }
            }
        } else if (nodeNameEquals(root, "exception")) {
            this.loadExceptionElement(null, root, classCache);
        }
    }

    /**
     * 加载exception元素
     * 
     * @param parentCode
     * @param element
     * @param classCache
     */
    private void loadExceptionElement(String parentCode, Element element, Map<String, Class<? extends Throwable>> classCache) {
        List<Element> children = new ArrayList<Element>();
        NodeList childNodes = element.getChildNodes();
        if (null != childNodes && childNodes.getLength() >= 1) {
            for (int i = 0, l = childNodes.getLength(); i < l; i++) {
                Node node = childNodes.item(i);
                if ((node instanceof Element) && nodeNameEquals(node, "exception")) {
                    children.add((Element) node);
                }
            }
        }

        String code = element.getAttribute("code");
        if (CoreUtils.isBlank(code)) {
            if (children.isEmpty()) {// 没有code，同时没有子配置
                throw new IllegalArgumentException("exception code is blank, please check the exception config... ");
            } else {
                code = generateExceptionCode();
            }
        } else {
            validateExceptionCode(code);
        }

        ExceptionMeta meta = new ExceptionMeta();
        meta.setCode(code);
        meta.setParentCode(parentCode);

        String description = element.getAttribute("description");
        if (!CoreUtils.isBlank(description)) {
            meta.setDescription(description);
        }

        loadMetaProperties(meta, element, classCache);
        codeMetaMapping.put(code, meta);
        if (null != meta.getCauses() && !meta.getCauses().isEmpty()) {
            for (Class<?> cls : meta.getCauses()) {
                classMetaMapping.put(cls, meta);
            }
        }
        super.logExceptionMeta(meta);
        if (!children.isEmpty()) {
            for (Element e : children) {
                loadExceptionElement(code, e, classCache);
            }
        }
    }

    /**
     * 加载exception的属性
     * 
     * @param meta
     * @param element
     * @param classCache
     */
    private void loadMetaProperties(ExceptionMeta meta, Element element, Map<String, Class<? extends Throwable>> classCache) {
        String messageKey = element.getAttribute("messageKey");
        if (!CoreUtils.isBlank(messageKey)) {
            meta.setMessageKey(messageKey.trim());
        }

        String level = element.getAttribute("level");
        if (!CoreUtils.isBlank(level)) {
            meta.setLevel(ExceptionLevel.instance(level.trim()));
        }

        String causes = element.getAttribute("causes");
        if (!CoreUtils.isBlank(causes)) {
            String[] hs = causes.trim().split("\\s+");
            if (null != hs && hs.length >= 1) {
                List<Class<? extends Throwable>> ehs = new ArrayList<Class<? extends Throwable>>();
                for (String h : hs) {
                    Class<? extends Throwable> t = getCauseClass(h, classCache);
                    if (null != t) {
                        ehs.add(t);
                    }
                }
                meta.setCauses(ehs);
            }
        }
    }

    /**
     * 获取异常原因
     * 
     * @param cause
     * @param classCache
     * @return
     */
    private Class<? extends Throwable> getCauseClass(String cause, Map<String, Class<? extends Throwable>> classCache) {
        if (classCache.containsKey(cause)) {
            return classCache.get(cause);
        } else {
            try {
                Class<?> h = CoreUtils.forName(cause);
                if (Throwable.class.isAssignableFrom(h)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Throwable> rs = (Class<? extends Throwable>) h;
                    classCache.put(cause, rs);
                    return rs;
                } else {
                    CommonLogger.error(" the exception config error, the cause is not a Throwable : " + cause);
                    return null;
                }
            } catch (Exception e) {
                CommonLogger.error(" the exception config error, the cause can not instance : " + cause);
                return null;
            }
        }
    }

    /**
     * 判断元素名称是否相同
     * 
     * @param node
     * @param desiredName
     * @return
     */
    private boolean nodeNameEquals(Node node, String desiredName) {
        return desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName());
    }
}
