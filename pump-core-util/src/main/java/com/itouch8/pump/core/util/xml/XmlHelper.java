package com.itouch8.pump.core.util.xml;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.springframework.core.io.Resource;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.core.util.xml.context.IXmlParserContext;
import com.itouch8.pump.core.util.xml.parser.INamespaceParser;


public class XmlHelper {

    private static final String[] defaultNamespaceMappingsLocation = {"classpath*:**/pump-*-namespaces.ini"};

    @SuppressWarnings("rawtypes")
    private static final Map<String, INamespaceParser> parsers = new HashMap<String, INamespaceParser>();

    private static INIConfiguration config;

    
    public static String getNamespaceSchema(String namespaceUri) {
        return getConfig(namespaceUri, "schema");
    }

    
    public static String getNamespaceParser(String namespaceUri) {
        return getConfig(namespaceUri, "parser");
    }

    
    public static Properties getNamespaceConfigs(String namespaceUri) {
        if (namespaceUri.endsWith(".xsd")) {
            namespaceUri = namespaceUri.substring(0, namespaceUri.length() - 4);
        }
        initConfig();
        SubnodeConfiguration section = null == config ? null : config.getSection(namespaceUri);
        if (null == section) {
            return null;
        }
        Properties properties = new Properties();
        for (Iterator<String> iterator = section.getKeys(); iterator.hasNext();) {
            String key = iterator.next();
            properties.setProperty(key, section.getString(key));
        }
        return properties;
    }

    
    @SuppressWarnings({"unchecked"})
    public static <E extends IXmlParserContext> INamespaceParser<E> getNamespaceParser(Node node) {
        String uri = XmlHelper.getNamespaceURI(node);
        if (null == uri) {
            return null;
        } else {
            INamespaceParser<E> parser = parsers.get(uri);
            if (null == parser) {
                String parserCls = getNamespaceParser(uri);
                if (null == parserCls) {
                    CommonLogger.warn("not found the namespace parser [" + uri + "]");
                    return null;
                }
                parser = (INamespaceParser<E>) CoreUtils.newInstance(parserCls);
                CommonLogger.info("parse the namespace [" + uri + "] using parser [" + parserCls + "]");
                parsers.put(uri, parser);
            }
            return parser;
        }
    }

    
    public static Map<String, List<Element>> getChildElementsMapByTagName(Element ele, String... childEleNames) {
        Set<String> names = new HashSet<String>(Arrays.asList(childEleNames));
        Map<String, List<Element>> map = new HashMap<String, List<Element>>(names.size());
        for (String name : names) {
            map.put(name, new ArrayList<Element>());
        }
        NodeList nl = ele.getChildNodes();
        for (int i = 0, l = nl.getLength(); i < l; i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                String name = getLocalName(node);
                if (names.contains(name)) {
                    map.get(name).add((Element) node);
                }
            }
        }
        return map;
    }

    
    public static List<Element> getChildElementsByTagName(Element ele, String... childEleNames) {
        List<String> childEleNameList = Arrays.asList(childEleNames);
        NodeList nl = ele.getChildNodes();
        List<Element> childEles = new ArrayList<Element>();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element && nodeNameMatch(node, childEleNameList)) {
                childEles.add((Element) node);
            }
        }
        return childEles;
    }

    
    public static List<Element> getChildElementsByTagName(Element ele, String childEleName) {
        return getChildElementsByTagName(ele, new String[] {childEleName});
    }

    
    public static Element getChildElementByTagName(Element ele, String childEleName) {
        NodeList nl = ele.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element && nodeNameEquals(node, childEleName)) {
                return (Element) node;
            }
        }
        return null;
    }

    
    public static String getChildElementValueByTagName(Element ele, String childEleName) {
        Element child = getChildElementByTagName(ele, childEleName);
        return (child != null ? getTextValue(child) : null);
    }

    
    public static List<Element> getChildElements(Element ele) {
        NodeList nl = ele.getChildNodes();
        List<Element> childEles = new ArrayList<Element>();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                childEles.add((Element) node);
            }
        }
        return childEles;
    }

    
    public static String getTextValue(Element valueEle) {
        StringBuilder sb = new StringBuilder();
        NodeList nl = valueEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node item = nl.item(i);
            if ((item instanceof CharacterData && !(item instanceof Comment)) || item instanceof EntityReference) {
                sb.append(item.getNodeValue());
            }
        }
        return sb.toString();
    }

    
    public static String getNamespaceURI(Node node) {
        return node.getNamespaceURI();
    }

    
    public static String getLocalName(Node node) {
        String name = node.getLocalName();
        if (null == name) {
            name = node.getNodeName();
        }
        return name;
    }

    
    public static boolean nodeNameEquals(Node node, String desiredName) {
        return desiredName.equals(node.getNodeName()) || desiredName.equals(getLocalName(node));
    }

    
    public static boolean nodeNameMatch(Node node, Collection<String> desiredNames) {
        return (desiredNames.contains(node.getNodeName()) || desiredNames.contains(node.getLocalName()));
    }

    
    public static Element getChildElement(Element ele) {
        NodeList nl = ele.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                return (Element) node;
            }
        }
        return null;
    }

    
    public static String getAttribute(Element ele, String attr, String defaultValue) {
        String value = ele.getAttribute(attr);
        if (CoreUtils.isBlank(value)) {
            value = defaultValue;
        } else {
            value = value.trim();
        }
        return value;
    }

    private static String getConfig(String namespaceUri, String key) {
        if (namespaceUri.endsWith(".xsd")) {
            namespaceUri = namespaceUri.substring(0, namespaceUri.length() - 4);
        }
        initConfig();
        SubnodeConfiguration section = null == config ? null : config.getSection(namespaceUri);
        String value = section == null ? null : section.getString(key);
        return value;
    }

    private static void initConfig() {
        if (config == null) {
            synchronized (XmlHelper.class) {
                if (config == null) {
                    String[] namespaceMappingsLocation = defaultNamespaceMappingsLocation;
                    try {
                        Set<Resource> resources = CoreUtils.getResources(null, namespaceMappingsLocation);
                        INIConfiguration config = new INIConfiguration();
                        for (Resource resource : resources) {
                            config.read(new InputStreamReader(resource.getInputStream()));
                        }
                        XmlHelper.config = config;
                    } catch (Exception ex) {
                        throw new IllegalStateException("Unable to load ini config from location [" + CoreUtils.join(namespaceMappingsLocation, ",") + "]", ex);
                    }
                }
            }
        }
    }
}
