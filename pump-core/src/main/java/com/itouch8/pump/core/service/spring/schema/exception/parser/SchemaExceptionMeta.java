package com.itouch8.pump.core.service.spring.schema.exception.parser;

import java.util.Set;

import com.itouch8.pump.core.util.exception.meta.impl.ExceptionMeta;


public class SchemaExceptionMeta extends ExceptionMeta {

    private Set<String> schemaHandlers;

    private Set<SchemaExceptionMeta> metas;

    public Set<String> getSchemaHandlers() {
        return schemaHandlers;
    }

    public void setSchemaHandlers(Set<String> schemaHandlers) {
        this.schemaHandlers = schemaHandlers;
    }

    public Set<SchemaExceptionMeta> getMetas() {
        return metas;
    }

    public void setMetas(Set<SchemaExceptionMeta> metas) {
        this.metas = metas;
    }
}
