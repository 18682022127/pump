package com.itouch8.pump.core.dao.sql.function;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;


public interface ISqlConfigFunction {

    
    public int getOrder();

    
    public boolean isSingleon();

    
    public String getName();

    
    public Object evaluateValue(IJndi jndi, Object parameter, String expression, String[] args);

    
    public String evaluateSql(IJndi jndi, Object parameter, String expression, String[] args);

    
    public class Manager {

        private static Map<String, ISqlConfigFunctionBuilder> sqlTextFunctionBuilders;

        public static ISqlConfigFunction getSqlTextFunction(String name) {
            if (sqlTextFunctionBuilders == null) {
                synchronized (Manager.class) {
                    if (sqlTextFunctionBuilders == null) {
                        sqlTextFunctionBuilders = new HashMap<String, ISqlConfigFunctionBuilder>();
                        Map<String, Integer> orders = new HashMap<String, Integer>();

                        Set<Class<? extends ISqlConfigFunction>> clss = CoreUtils.scanClassesByParentCls(BaseConfig.getScanPackage(), ISqlConfigFunction.class);
                        if (clss != null) {
                            for (final Class<? extends ISqlConfigFunction> cls : clss) {
                                ISqlConfigFunction fn = CoreUtils.newInstance(cls);
                                String fname = fn.getName();
                                Integer order = orders.get(fname);
                                if (null == order || fn.getOrder() < order) {// 不存在优先级更高的函数
                                    orders.put(fname, fn.getOrder());
                                    if (fn.isSingleon()) {
                                        sqlTextFunctionBuilders.put(fname, new SingleonSqlConfigFunctionBuilder(fn));
                                    } else {
                                        sqlTextFunctionBuilders.put(fname, new ClassSqlConfigFunctionBuilder(cls));
                                    }
                                }
                            }
                        }

                        Set<Class<? extends ISqlConfigFunctionFactory>> factoryClss = CoreUtils.scanClassesByParentCls(BaseConfig.getScanPackage(), ISqlConfigFunctionFactory.class);
                        if (null != factoryClss) {
                            for (Class<? extends ISqlConfigFunctionFactory> cls : factoryClss) {
                                final ISqlConfigFunctionFactory factory = CoreUtils.newInstance(cls);
                                Set<ISqlConfigFunction> fns = factory.getAllSqlConfigFunctions();
                                if (null != fns) {
                                    for (ISqlConfigFunction fn : fns) {
                                        final String fname = fn.getName();
                                        Integer order = orders.get(fname);
                                        if (null == order || fn.getOrder() < order) {// 不存在优先级更高的函数
                                            orders.put(fname, fn.getOrder());
                                            if (fn.isSingleon()) {
                                                sqlTextFunctionBuilders.put(fname, new SingleonSqlConfigFunctionBuilder(fn));
                                            } else {
                                                sqlTextFunctionBuilders.put(fname, new FactorySqlConfigFunctionBuilder(factory, fname));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (sqlTextFunctionBuilders.containsKey(name)) {
                return sqlTextFunctionBuilders.get(name).builder();
            } else {
                return null;
            }
        }

        private interface ISqlConfigFunctionBuilder {
            public ISqlConfigFunction builder();
        }

        private static class SingleonSqlConfigFunctionBuilder implements ISqlConfigFunctionBuilder {
            private ISqlConfigFunction instance;

            private SingleonSqlConfigFunctionBuilder(ISqlConfigFunction instance) {
                this.instance = instance;
            }

            public ISqlConfigFunction builder() {
                return instance;
            }
        }
        private static class ClassSqlConfigFunctionBuilder implements ISqlConfigFunctionBuilder {
            private Class<? extends ISqlConfigFunction> cls;

            private ClassSqlConfigFunctionBuilder(Class<? extends ISqlConfigFunction> cls) {
                this.cls = cls;
            }

            public ISqlConfigFunction builder() {
                return CoreUtils.newInstance(cls);
            }
        }
        private static class FactorySqlConfigFunctionBuilder implements ISqlConfigFunctionBuilder {
            private ISqlConfigFunctionFactory factory;
            private String name;

            private FactorySqlConfigFunctionBuilder(ISqlConfigFunctionFactory factory, String name) {
                this.factory = factory;
                this.name = name;
            }

            public ISqlConfigFunction builder() {
                return factory.getSqlConfigFunction(name);
            }
        }
    }
}
