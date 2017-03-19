package com.itouch8.pump.util.toolimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import com.itouch8.pump.util.Tool;


public abstract class ScriptUtilsImpl {

    private static final ScriptUtilsImpl instance = new ScriptUtilsImpl() {};

    private ScriptUtilsImpl() {}

    
    public static ScriptUtilsImpl getInstance() {
        return instance;
    }

    
    public Object js2Java(String jsString) {
        try {
            Context cx = ContextFactory.getGlobal().enterContext();
            Scriptable scope = cx.initStandardObjects();
            // js.eval();
            Object value = cx.evaluateString(scope, "$aa=" + jsString, null, 1, null);
            return parseNativeValue(value);
        } catch (Exception e) {
            return jsString;
        } finally {
            Context.exit();
        }
    }

    // 
    // public Object js2Java(String jsString){
    // ScriptEngineManager manager = new ScriptEngineManager();
    // ScriptEngine js = manager.getEngineByName("javascript");
    // try {
    // js.eval("$aa="+jsString);
    // return parseNativeValue(js.get("$aa"));
    // } catch (ScriptException e) {
    // return jsString;
    // }
    // }

    
    private Object parseNativeValue(Object value) {
        if (null == value || value instanceof Undefined) {// Null、Undefined
            return null;
        } else if (value instanceof String) {// 字符串
            String v = (String) value;
            if (!Tool.CHECK.isEmpty(v)) {
                return v;
            }
        } else if (value instanceof NativeArray) {// 数组
            NativeArray array = (NativeArray) value;
            List<Object> list = new ArrayList<Object>();
            for (int i = 0, l = (int) array.getLength(); i < l; i++) {
                Object v = parseNativeValue(array.get(i, array));
                // 这里即使v为null也需要添加到list中
                list.add(v);
            }
            if (null != list && !list.isEmpty()) {
                return list;
            }
        } else if (value instanceof NativeObject) {// 对象
            NativeObject jsObj = (NativeObject) value;
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            for (Object c : jsObj.getAllIds()) {
                String key = c.toString();
                Object v = parseNativeValue(jsObj.get(key, jsObj));
                if (null != v) {
                    map.put(key, v);
                }
            }
            if (null != map && !map.isEmpty()) {
                return map;
            }
        } else {
            return value;
        }
        return null;
    }
}
