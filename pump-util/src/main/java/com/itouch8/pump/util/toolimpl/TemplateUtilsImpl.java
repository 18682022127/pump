package com.itouch8.pump.util.toolimpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.util.Tool;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateModelException;


public abstract class TemplateUtilsImpl {

    private static final TemplateUtilsImpl instance = new TemplateUtilsImpl() {};

    private TemplateUtilsImpl() {}

    
    public static TemplateUtilsImpl getInstance() {
        return instance;
    }

    
    private static final Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    static {
        config.setLocalizedLookup(false);// 关闭国际化模板查找
    }

    
    public Configuration getTemplateConfiguration() {
        return config;
    }

    
    public void setSharedVariable(Map<String, Object> sharedVariables) {
        if (null != sharedVariables && !sharedVariables.isEmpty()) {
            for (Map.Entry<String, Object> entry : sharedVariables.entrySet()) {
                try {
                    config.setSharedVariable(entry.getKey(), entry.getValue());
                } catch (TemplateModelException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    public String fillStringFtl2String(String ftl, Object data) {
        try {
            String ftlName = "tmp";
            StringTemplateLoader tl = new StringTemplateLoader();
            tl.putTemplate(ftlName, ftl);
            config.setTemplateLoader(tl);
            Template template = config.getTemplate(ftlName);
            StringWriter result = new StringWriter();
            template.process(data, result);
            return result.toString();
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    
    public String fillFileFtl2String(String ftlPath, Object data) {
        return fillFileFtl2String(ftlPath, data, getTemplateEncoding());
    }

    
    public String fillFileFtl2String(String ftlPath, Object data, String encoding) {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            fillFileFtl2OutputStream(ftlPath, data, out, encoding, encoding);
            return out.toString(encoding);
        } catch (UnsupportedEncodingException e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(out);
        }
    }

    
    public void fillFileFtl2File(String ftlPath, Object data, String targetPath) {
        fillFileFtl2File(ftlPath, data, targetPath, getTemplateEncoding());
    }

    
    public void fillFileFtl2File(String ftlPath, Object data, String targetPath, String encoding) {
        OutputStream out = null;
        try {
            File file = new File(targetPath);
            Tool.FILE.forceMkdir(file.getParentFile());
            out = new FileOutputStream(file);
            fillFileFtl2OutputStream(ftlPath, data, out, encoding, encoding);
        } catch (FileNotFoundException e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(out);
        }
    }

    
    public void fillFileFtl2OutputStream(String ftlPath, Object data, OutputStream out, String ftlEncoding, String outputEncoding) {
        Writer writer = null;
        try {
            File ftl = new File(ftlPath);
            config.setDirectoryForTemplateLoading(ftl.getParentFile());
            Template template = config.getTemplate(ftl.getName(), ftlEncoding);
            writer = new OutputStreamWriter(out, outputEncoding);
            template.process(data, writer);
            out.flush();
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(writer);
        }
    }

    
    public String fillClasspathFtl2String(Class<?> cls, String name, Object data) {
        return fillClasspathFtl2String(cls, name, data, getTemplateEncoding());
    }

    
    public String fillClasspathFtl2String(Class<?> cls, String name, Object data, String encoding) {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            fillClasspathFtl2OutputStream(cls, name, data, out, encoding, encoding);
            return out.toString(encoding);
        } catch (UnsupportedEncodingException e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(out);
        }
    }

    
    public void fillClasspathFtl2OutputStream(Class<?> cls, String name, Object data, OutputStream os, String ftlEncoding, String outputEncoding) {
        Writer writer = null;
        try {
            config.setClassForTemplateLoading(cls, "");
            Template template = config.getTemplate(name, ftlEncoding);
            writer = new OutputStreamWriter(os, outputEncoding);
            template.process(data, writer);
            os.flush();
        } catch (Exception e) {
            Throw.throwRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(writer);
        }
    }

    
    public String processTemplateIntoString(Template template, Object model) {
        try {
            StringWriter result = new StringWriter();
            template.process(model, result);
            return result.toString();
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        }
    }

    private String getTemplateEncoding() {
        String encoding = config.getDefaultEncoding();
        if (Tool.CHECK.isBlank(encoding)) {
            encoding = BaseConfig.getEncoding();
        }
        return encoding;
    }
}
