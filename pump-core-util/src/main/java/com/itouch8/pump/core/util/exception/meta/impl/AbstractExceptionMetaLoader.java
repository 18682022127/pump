package com.itouch8.pump.core.util.exception.meta.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.meta.IExceptionMeta;
import com.itouch8.pump.core.util.exception.meta.IExceptionMetaLoader;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class AbstractExceptionMetaLoader implements IExceptionMetaLoader {

    
    protected static final Pattern exceptionCodePatter = Pattern.compile("^[A-Z]{2}[0-9A-Za-z]{6}$");

    
    protected static final Map<String, IExceptionMeta> codeMetaMapping = new HashMap<String, IExceptionMeta>();

    
    protected static final Map<Class<?>, IExceptionMeta> classMetaMapping = new HashMap<Class<?>, IExceptionMeta>();

    
    @Override
    public IExceptionMeta lookup(String code, Throwable cause) {
        IExceptionMeta meta = null;
        if (null != code) {
            meta = codeMetaMapping.get(code);
        }
        if (null == meta && null != cause) {
            Class<?> oCls = cause.getClass();
            if (classMetaMapping.containsKey(oCls)) {
                return classMetaMapping.get(oCls);
            } else {
                Class<?> cls = oCls;
                while (!Object.class.equals(cls)) {
                    meta = classMetaMapping.get(cls);
                    if (null != meta) {
                        classMetaMapping.put(oCls, meta);
                        return meta;
                    }
                    cls = cls.getSuperclass();
                }
            }
        }
        return meta;
    }

    
    protected String generateExceptionCode() {
        return "##" + RandomStringUtils.randomNumeric(6);
    }

    
    protected boolean isGenerateExceptionCode(String code) {
        return !CoreUtils.isBlank(code) && !code.startsWith("##");
    }

    
    protected void validateExceptionCode(String code) {
        if (codeMetaMapping.containsKey(code)) {
            throw new IllegalArgumentException("found duplicate exception code: " + code);
        } else if (!exceptionCodePatter.matcher(code).find()) {
            throw new IllegalArgumentException("the exception code is incorrect: " + code);
        }
    }

    
    protected void logExceptionMeta(IExceptionMeta meta) {
        StringBuffer sb = new StringBuffer();
        sb.append("{exception-meta}==>{code=").append(meta.getCode());

        if (meta instanceof ExceptionMeta) {
            String desc = ((ExceptionMeta) meta).getDescription();
            if (!CoreUtils.isBlank(desc)) {
                sb.append(",description=").append(desc);
            }
        }

        String pCode = meta.getParentCode();
        if (!this.isGenerateExceptionCode(pCode)) {
            sb.append(",parentCode=").append(pCode);
        }
        if (!CoreUtils.isBlank(meta.getMessageKey())) {
            sb.append(",messageKey=").append(meta.getMessageKey());
        }
        if (null != meta.getLevel()) {
            sb.append(",level=").append(meta.getLevel());
        }
        if (!CoreUtils.isBlank(meta.getView())) {
            sb.append(",view=").append(meta.getView());
        }
        List<Class<? extends Throwable>> causes = meta.getCauses();
        if (null != causes && !causes.isEmpty()) {
            sb.append(",causes=").append(causes);
        }
        sb.append("}");
        CommonLogger.info(sb.toString());
    }
}
