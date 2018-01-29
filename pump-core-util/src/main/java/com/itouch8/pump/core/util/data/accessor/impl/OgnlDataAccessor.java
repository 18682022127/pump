package com.itouch8.pump.core.util.data.accessor.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.itouch8.pump.core.util.data.accessor.IDataAccessor;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;

import ognl.DefaultMemberAccess;
import ognl.MemberAccess;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;


public class OgnlDataAccessor extends AbstractDataAccessor {

    private static final MemberAccess DEFAULT_MEMBER_ACCESS = new DefaultMemberAccess(true);

    private static final Map<String, Object> expCache = new ConcurrentHashMap<String, Object>();

    private final OgnlContext ognlContext;

    public OgnlDataAccessor() {
        this(null, null, null, null);
    }

    public OgnlDataAccessor(Object root) {
        this(null, null, root, null);
    }

    public OgnlDataAccessor(Object root, Map<String, Object> vars) {
        this(null, null, root, vars);
    }

    private OgnlDataAccessor(IDataAccessor parent, OgnlContext ognlContext, Object root, Map<String, Object> vars) {
        super(parent);
        this.ognlContext = initOnglContext(ognlContext, root, vars);
    }

    
    @Override
    public <T> T value(String property, Class<T> cls) {
        Object tree = getOgnlTree(property);
        try {
            @SuppressWarnings("unchecked")
            T value = (T) Ognl.getValue(tree, ognlContext, ognlContext.getRoot(), cls);
            return value;
        } catch (OgnlException e) {
            throw Throw.createRuntimeException(ExceptionCodes.YT010003, e, property);
        }
    }

    
    @Override
    public void set(String property, Object value) {
        Object tree = getOgnlTree(property);
        try {
            Ognl.setValue(tree, ognlContext, ognlContext.getRoot(), value);
        } catch (OgnlException e) {
            throw Throw.createRuntimeException(ExceptionCodes.YT010003, e, property);
        }
    }

    
    @Override
    public void addVar(String key, Object value) {
        ognlContext.put(key, value);
    }

    
    @Override
    protected IDataAccessor doDerive(Object value) {
        return new OgnlDataAccessor(this, ognlContext, value, null);
    }

    
    @Override
    public Object getRoot() {
        return ognlContext.getRoot();
    }

    
    private Object getOgnlTree(String property) {
        Object tree = expCache.get(property);
        if (null == tree) {
            synchronized (expCache) {
                tree = expCache.get(property);
                if (null == tree) {
                    try {
                        tree = Ognl.parseExpression(property);
                        expCache.put(property, tree);
                    } catch (OgnlException e) {
                        Throw.throwRuntimeException(ExceptionCodes.YT010003, e, property);
                    }
                }
            }
        }
        return tree;
    }

    
    private OgnlContext initOnglContext(OgnlContext ognlContext, Object root, Map<String, Object> vars) {
        OgnlContext context = ognlContext;
        if (null == context) {
            context = new OgnlContext();
            context.setMemberAccess(DEFAULT_MEMBER_ACCESS);
        }
        context.setRoot(root);
        if (null != vars) {
            context.setValues(vars);
        }
        return context;
    }
}
