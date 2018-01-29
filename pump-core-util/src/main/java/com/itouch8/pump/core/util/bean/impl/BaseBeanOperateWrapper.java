package com.itouch8.pump.core.util.bean.impl;

import java.util.Map;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.bean.IBeanOperateWrapper;
import com.itouch8.pump.core.util.bean.IContextBeanOperateWrapper;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;

import ognl.DefaultMemberAccess;
import ognl.MemberAccess;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;


public class BaseBeanOperateWrapper implements IContextBeanOperateWrapper {

    private static final IContextBeanOperateWrapper ognlWrapper = new OgnlBeanOperateWrapper();
    private static final IBeanOperateWrapper mapWrapper = new MapBeanOperateWrapper();

    
    @Override
    public Object getProperty(Object bean, String property) {
        if (isComplex(property)) {
            return ognlWrapper.getProperty(bean, property);
        } else if (bean instanceof Map) {
            return mapWrapper.getProperty(bean, property);
        } else {
            // try {
            // return FieldUtils.readField(bean, property, true);
            // } catch (IllegalAccessException e) {
            // throw Throw.createRuntimeException(e);
            // }
            return ognlWrapper.getProperty(bean, property);
        }
    }

    
    @Override
    public <E> E getProperty(Object bean, String property, Class<E> resultType) {
        if (isComplex(property)) {
            return ognlWrapper.getProperty(bean, property, resultType);
        } else if (bean instanceof Map) {
            return mapWrapper.getProperty(bean, property, resultType);
        } else {
            // try {
            // Object rs = FieldUtils.readField(bean, property, true);
            // return CoreUtils.convert(rs, resultType);
            // } catch (IllegalAccessException e) {
            // throw Throw.createRuntimeException(e);
            // }
            return ognlWrapper.getProperty(bean, property, resultType);
        }
    }

    
    @Override
    public void setProperty(Object bean, String property, Object value) {
        if (isComplex(property)) {
            ognlWrapper.setProperty(bean, property, value);
        } else if (bean instanceof Map) {
            mapWrapper.setProperty(bean, property, value);
        } else {
            // try {
            // FieldUtils.writeField(bean, property, value, true);
            // } catch (IllegalAccessException e) {
            // throw Throw.createRuntimeException(e);
            // }
            ognlWrapper.setProperty(bean, property, value);
        }
    }

    
    @Override
    public void removeProperty(Object bean, String property) {
        if (isComplex(property)) {
            ognlWrapper.removeProperty(bean, property);
        } else if (bean instanceof Map) {
            mapWrapper.removeProperty(bean, property);
        } else {
            ognlWrapper.removeProperty(bean, property);
        }
    }

    
    @Override
    public Object getProperty(Object bean, String expression, Map<String, Object> context) {
        return ognlWrapper.getProperty(bean, expression, context);
    }

    
    @Override
    public <E> E getProperty(Object bean, String expression, Map<String, Object> context, Class<E> resultType) {
        return ognlWrapper.getProperty(bean, expression, context, resultType);
    }

    
    @Override
    public void setProperty(Object bean, String expression, Object value, Map<String, Object> context) {
        ognlWrapper.setProperty(bean, expression, value, context);
    }

    private boolean isComplex(String property) {
        return -1 != property.indexOf('.') || -1 != property.indexOf('[');
    }

    private static class OgnlBeanOperateWrapper implements IContextBeanOperateWrapper {

        private static final MemberAccess DEFAULT_MEMBER_ACCESS = new DefaultMemberAccess(true);
        private static final OgnlContext EMPTY_OGNLCONTEXT = new OgnlContext(OgnlContext.DEFAULT_CLASS_RESOLVER, OgnlContext.DEFAULT_TYPE_CONVERTER, DEFAULT_MEMBER_ACCESS);

        
        private Map<?, ?> getDefaultOgnlContext(Map<?, ?> context) {
            if (null == context) {
                return EMPTY_OGNLCONTEXT;
            } else if (!(context instanceof OgnlContext)) {
                return new OgnlContext(OgnlContext.DEFAULT_CLASS_RESOLVER, OgnlContext.DEFAULT_TYPE_CONVERTER, DEFAULT_MEMBER_ACCESS, context);
            } else {
                return context;
            }
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public Object getProperty(Object bean, String property) {
            return this.getProperty(bean, property, (Map) null);
        }

        @Override
        public <E> E getProperty(Object bean, String property, Class<E> resultType) {
            return this.getProperty(bean, property, null, resultType);
        }

        @Override
        public void setProperty(Object bean, String property, Object value) {
            this.setProperty(bean, property, value, null);
        }

        @Override
        public void removeProperty(Object bean, String property) {
            this.setProperty(bean, property, null, null);
        }

        @Override
        public Object getProperty(Object bean, String expression, Map<String, Object> context) {
            try {
                return Ognl.getValue(expression, getDefaultOgnlContext(context), bean);
            } catch (OgnlException e) {
                throw Throw.createRuntimeException(ExceptionCodes.YT010003, e, expression);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public <E> E getProperty(Object bean, String expression, Map<String, Object> context, Class<E> resultType) {
            try {
                return (E) Ognl.getValue(expression, getDefaultOgnlContext(context), bean, resultType);
            } catch (OgnlException e) {
                throw Throw.createRuntimeException(ExceptionCodes.YT010003, e, expression);
            }
        }

        @Override
        public void setProperty(Object bean, String expression, Object value, Map<String, Object> context) {
            try {
                Ognl.setValue(expression, getDefaultOgnlContext(context), bean, value);
            } catch (OgnlException e) {
                Throw.throwRuntimeException(ExceptionCodes.YT010003, e, expression);
            }
        }
    }

    private static class MapBeanOperateWrapper implements IBeanOperateWrapper {

        @Override
        public Object getProperty(Object bean, String property) {
            return ((Map<?, ?>) bean).get(property);
        }

        public <E> E getProperty(Object bean, String property, Class<E> resultType) {
            Object rs = getProperty(bean, property);
            if (null != rs) {
                return CoreUtils.convert(rs, resultType);
            }
            return null;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void setProperty(Object bean, String property, Object value) {
            ((Map) bean).put(property, value);
        }

        @Override
        public void removeProperty(Object bean, String property) {
            ((Map<?, ?>) bean).remove(property);
        }
    }
}
