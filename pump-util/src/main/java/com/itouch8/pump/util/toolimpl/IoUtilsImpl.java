package com.itouch8.pump.util.toolimpl;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.exception.Throw;


public abstract class IoUtilsImpl {

    private static final IoUtilsImpl instance = new IoUtilsImpl() {};

    private IoUtilsImpl() {}

    
    public static IoUtilsImpl getInstance() {
        return instance;
    }

    
    public void closeQuietly(Closeable... closeable) {
        CoreUtils.closeQuietly(closeable);
    }

    
    public Resource getResource(String location) {
        return getResourcePatternResolver().getResource(location);
    }

    
    public Resource[] getResources(String locationPattern) {
        try {
            return getResourcePatternResolver().getResources(locationPattern);
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public Properties loadProperties(String location) {
        Properties properties = new Properties();
        fillProperties(properties, location);
        return properties;
    }

    
    public void fillProperties(Properties props, String location) {
        InputStream is = null;
        try {
            Resource resource = getResource(location);
            is = resource.getInputStream();
            String filename = resource.getFilename();
            if (filename != null && filename.toLowerCase().endsWith(".xml")) {
                props.loadFromXML(is);
            } else {
                props.load(is);
            }
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        } finally {
            this.closeQuietly(is);;
        }
    }

    
    public Properties loadProperties(String location, String prefix) {
        Properties properties = loadProperties(location);
        if (null == properties || properties.isEmpty()) {
            return properties;
        }
        Properties rs = new Properties();
        int index = prefix.length();
        for (Iterator<Object> i = properties.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            if (key.startsWith(prefix) && key.length() > index) {
                rs.put(key.substring(index + 1), properties.get(key));
            }
        }
        return rs;
    }

    
    public InputStream getInputStream(String location) {
        try {
            return getResource(location).getInputStream();
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public InputStream getInputStream(String path, Class<?> cls) {
        try {
            return new ClassPathResource(path, cls).getInputStream();
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public File getFile(String resourceLocation) {
        try {
            return ResourceUtils.getFile(resourceLocation);
        } catch (FileNotFoundException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public URL getURL(String resourceLocation) {
        try {
            return ResourceUtils.getURL(resourceLocation);
        } catch (FileNotFoundException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public LineIterator lineIterator(InputStream input) {
        return lineIterator(input, BaseConfig.getEncoding());
    }

    
    public LineIterator lineIterator(InputStream input, String encoding) {
        try {
            return IOUtils.lineIterator(input, encoding);
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public List<String> readLines(InputStream input) {
        return readLines(input, BaseConfig.getEncoding());
    }

    
    public List<String> readLines(InputStream input, String encoding) {
        try {
            return IOUtils.readLines(input, encoding);
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void serialize(Serializable obj, OutputStream outputStream) {
        SerializationUtils.serialize(obj, outputStream);
    }

    
    public byte[] serialize(Serializable obj) {
        return SerializationUtils.serialize(obj);
    }

    
    public Object deserialize(InputStream inputStream) {
        return SerializationUtils.deserialize(inputStream);
    }

    
    public Object deserialize(byte[] objectData) {
        return SerializationUtils.deserialize(objectData);
    }

    
    public void copy(InputStream input, OutputStream output) {
        try {
            IOUtils.copy(input, output);
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public void copy(Reader input, Writer output) {
        try {
            IOUtils.copy(input, output);
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        }
    }

    private ResourcePatternResolver getResourcePatternResolver() {
        return BaseConfig.getResourcePatternResolver();
    }
}
