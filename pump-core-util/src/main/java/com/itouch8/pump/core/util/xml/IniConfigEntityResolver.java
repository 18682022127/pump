package com.itouch8.pump.core.util.xml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;


public class IniConfigEntityResolver implements EntityResolver {

    private final Map<String, Resource> resources = new HashMap<String, Resource>();

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (CoreUtils.isBlank(systemId)) {
            return null;
        }

        Resource resource = resources.get(systemId);
        if (null != resource) {
            return resolveInputSource(publicId, systemId, resource);
        } else {
            String resourceLocation = XmlHelper.getNamespaceSchema(systemId);
            if (resourceLocation != null) {
                return resolveInputSource(publicId, systemId, new ClassPathResource(resourceLocation));
            }
        }
        return null;
    }

    private InputSource resolveInputSource(String publicId, String systemId, Resource resource) throws IOException {
        InputSource source = new InputSource(resource.getInputStream());
        source.setPublicId(publicId);
        source.setSystemId(systemId);
        return source;
    }

    
    public void addResource(String systemId, String location) {
        if (null != location && null != systemId) {
            try {
                Resource resource = CoreUtils.getResource(location);
                if (null != resource && resource.exists()) {
                    this.resources.put(systemId, resource);
                }
            } catch (Exception e) {
                Throw.throwRuntimeException(e);
            }
        }
    }

    public void setResources(Map<String, Resource> resources) {
        if (null != resources) {
            this.resources.putAll(resources);
        }
    }
}
