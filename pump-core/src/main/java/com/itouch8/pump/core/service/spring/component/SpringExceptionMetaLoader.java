package com.itouch8.pump.core.service.spring.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itouch8.pump.core.service.exception.ServiceExceptionCodes;
import com.itouch8.pump.core.service.spring.SpringHelp;
import com.itouch8.pump.core.service.spring.schema.exception.parser.SchemaExceptionMeta;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.handler.IExceptionHandler;
import com.itouch8.pump.core.util.exception.meta.impl.XmlExceptionMetaLoader;
import com.itouch8.pump.core.util.init.Init;


@Init(order = Integer.MIN_VALUE / 2)
public class SpringExceptionMetaLoader extends XmlExceptionMetaLoader {

    
    public void init() {
        super.load();
        Map<String, SchemaExceptionMeta> metas = SpringHelp.getBeansOfType(SchemaExceptionMeta.class);
        if (null != metas && !metas.isEmpty()) {
            for (SchemaExceptionMeta meta : metas.values()) {
                resolverExceptionMeta(meta);
            }
        }
    }

    
    private void resolverExceptionMeta(SchemaExceptionMeta meta) {
        if (null != meta) {
            String code = meta.getCode();
            Set<SchemaExceptionMeta> metas = meta.getMetas();
            if (CoreUtils.isBlank(code)) {
                if (null == metas || metas.isEmpty()) {// 没有code，同时没有子配置
                    Throw.throwRuntimeException(ServiceExceptionCodes.BF030003);
                } else {
                    code = generateExceptionCode();
                }
            } else if (codeMetaMapping.containsKey(code)) {
                Throw.throwRuntimeException(ServiceExceptionCodes.BF030004, code);
            } else if (!exceptionCodePatter.matcher(code).find()) {
                Throw.throwRuntimeException(ServiceExceptionCodes.BF030005, code);
            }
            meta.setHandlers(resolverExceptionHandlers(meta.getSchemaHandlers()));
            codeMetaMapping.put(code, meta);
            if (null != meta.getCauses() && !meta.getCauses().isEmpty()) {
                for (Class<?> cls : meta.getCauses()) {
                    classMetaMapping.put(cls, meta);
                }
            }
            logExceptionMeta(meta);
            if (null != metas) {
                for (SchemaExceptionMeta m : metas) {
                    m.setParentCode(code);
                    resolverExceptionMeta(m);
                }
            }
        }
    }

    
    private List<IExceptionHandler> resolverExceptionHandlers(Set<String> handlers) {
        List<IExceptionHandler> exceptionHandlers = null;
        if (null != handlers && handlers.size() > 0) {
            exceptionHandlers = new ArrayList<IExceptionHandler>();
            for (String handler : handlers) {
                IExceptionHandler h = SpringHelp.getBean(handler, IExceptionHandler.class);
                exceptionHandlers.add(h);
            }
            return exceptionHandlers;
        }
        return null;
    }
}
