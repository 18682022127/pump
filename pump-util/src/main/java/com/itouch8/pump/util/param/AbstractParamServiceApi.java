package com.itouch8.pump.util.param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itouch8.pump.util.param.IParam;
import com.itouch8.pump.util.param.IParamServiceApi;
import com.itouch8.pump.util.param.common.IParamLoader;
import com.itouch8.pump.util.param.common.IParamStore;
import com.itouch8.pump.util.param.common.impl.CacheParamStore;


public abstract class AbstractParamServiceApi<P extends IParam> implements IParamServiceApi<P> {

    private IParamLoader<P> loader;

    private IParamStore<P> store;

    public AbstractParamServiceApi() {
        this.store = new CacheParamStore<P>(getStoreElementName());
    }

    
    @Override
    public P getParam(String name) {
        Map<String, P> params = getParams(Arrays.asList(name));
        if (null != params && !params.isEmpty()) {
            return params.values().iterator().next();
        }
        return null;
    }

    
    @Override
    public Map<String, P> getParams(List<String> names) {
        IParamLoader<P> loader = getLoader();
        if (null == loader || null == names || names.isEmpty()) {
            return null;
        }
        IParamStore<P> store = getStore();
        if (null == store) {
            return doLoad(store, names);
        } else {
            List<String> loadnames = new ArrayList<String>();
            Map<String, P> caches = new HashMap<String, P>();
            for (String name : names) {
                P treeParam = store.get(name);
                if (null != treeParam) {
                    caches.put(name, treeParam);
                } else {
                    loadnames.add(name);
                }
            }
            if (!loadnames.isEmpty()) {
                Map<String, P> loads = this.doLoad(store, loadnames);
                if (null != loads) {
                    caches.putAll(loads);
                }
            }
            return caches;
        }
    }

    @Override
    public void removeParams(List<String> names) {
        IParamStore<P> store = getStore();
        if (null == store || null == names || names.isEmpty()) {
            return;
        } else {
            for (String name : names) {
                store.remove(name);
            }
        }
    }

    
    public boolean isLoaded(String name) {
        IParamStore<P> store = getStore();
        if (null == store) {
            return false;
        } else {
            return store.contains(name);
        }
    }

    @Override
    public void clear() {
        IParamStore<P> store = getStore();
        if (null != store) {
            store.clear();
        }
    }

    private Map<String, P> doLoad(IParamStore<P> store, List<String> names) {
        IParamLoader<P> loader = getLoader();
        Map<String, P> o = loader.loadParams(names);
        if (null != o && !o.isEmpty()) {
            this.afterLoad(o);
        }
        return o;
    }

    
    protected abstract String getStoreElementName();

    
    protected abstract void afterLoad(Map<String, P> params);

    public IParamLoader<P> getLoader() {
        return loader;
    }

    public void setLoader(IParamLoader<P> loader) {
        this.loader = loader;
    }

    public IParamStore<P> getStore() {
        return store;
    }

    public void setStore(IParamStore<P> store) {
        this.store = store;
    }
}
