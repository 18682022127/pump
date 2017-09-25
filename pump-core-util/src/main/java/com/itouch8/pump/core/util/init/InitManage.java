package com.itouch8.pump.core.util.init;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.reflect.MethodUtils;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.depends.impl.DependGraph;
import com.itouch8.pump.core.util.depends.impl.DependNode;
import com.itouch8.pump.core.util.env.PumpVersion;
import com.itouch8.pump.core.util.env.EnvConsts;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class InitManage {

    
    private static List<InitClass> list = null;

    
    private static final AtomicBoolean initMonitor = new AtomicBoolean(false);
    
    private static final AtomicBoolean destoryMonitor = new AtomicBoolean(false);
    
    private static final String pumpScanPackage = "com.itouch8.pump";

    
    public static void initialize() {
        if (!initMonitor.get()) {
            synchronized (initMonitor) {
                if (!initMonitor.get()) {
                    try {
                        PumpVersion.checkJdkVersion();
                        long start = System.currentTimeMillis();
                        CommonLogger.info(PumpVersion.Version + " init start...");
                        // 初始化
                        init();
                        CommonLogger.info(PumpVersion.Version + " init success in " + (System.currentTimeMillis() - start) + " ms ...");
                    } finally {
                        initMonitor.set(true);
                    }
                }
            }
        } else {
        }
    }
  
    private static void init() {
        String scanPackage = BaseConfig.getInitScanPackage();
        if (CoreUtils.isBlank(scanPackage) || scanPackage.equals(pumpScanPackage)) {
            scanPackage = BaseConfig.getScanPackage();
        }
        if (CoreUtils.isBlank(scanPackage)) {
            scanPackage = pumpScanPackage;
        } else if (!scanPackage.equals(pumpScanPackage)) {
            scanPackage += "," + pumpScanPackage;
        }
        CommonLogger.debug("@Init scan package is " + scanPackage);
        // 扫描含初始化注解的类
        Set<Class<?>> init = CoreUtils.scanClasses(scanPackage, Init.class);
        if (null != init) {
            List<InitClass> list = new ArrayList<InitClass>();
            // 根据注解将需初始化的类包装成初始化方法类（内部类）
            for (Class<?> r : init) {
                InitClass ic = new InitClass(r, r.getAnnotation(Init.class));
                list.add(ic);
            }
            // 先按照简单顺序排序
            Collections.sort(list, new Comparator<InitClass>() {
                public int compare(InitClass o1, InitClass o2) {
                    return o1.getOrder() - o2.getOrder();
                }
            });
            // 再分析依赖并排序
            list = new DependGraph<InitClass>(list).sort();
            // 依次执行初始化
            StringBuffer sb = new StringBuffer().append("initialize method list : ");
            for (int i = 0; i < list.size(); i++) {
                InitClass ic = list.get(i);
                ic.order = i + 1;
                sb.append(EnvConsts.LINE_SEPARATOR).append(i + 1).append(". hashCode(").append(ic.bean.hashCode()).append(") ==> ").append(ic.initMethod);
            }
            CommonLogger.info(sb.toString());
            for (InitClass i : list) {
                i.init();
            }
            for (int i = 0; i < list.size(); i++) {
                InitClass ic = list.get(i);
                if (ic.destoryMethod == null) {
                    list.remove(ic);
                    i--;
                }
            }
            InitManage.list = list;
        }
    }

    
    public static void destory() {
        if (!destoryMonitor.get()) {
            synchronized (destoryMonitor) {
                if (!destoryMonitor.get()) {
                    try {
                        long start = System.currentTimeMillis();
                        CommonLogger.debug("destory start...");
                        if (null != list) {
                            StringBuffer sb = new StringBuffer().append("destory method list : ");
                            for (int i = list.size() - 1, j = 1; i >= 0; i--, j++) {
                                InitClass ic = list.get(i);
                                ic.order = j;
                                sb.append(EnvConsts.LINE_SEPARATOR).append(j).append(". hashCode(").append(ic.bean.hashCode()).append(") ==> ").append(ic.destoryMethod);
                            }
                            CommonLogger.info(sb.toString());
                            for (int i = list.size() - 1; i >= 0; i--) {
                                list.get(i).destory();
                            }
                        }
                        CommonLogger.debug("destory success in " + (System.currentTimeMillis() - start) + " ms ...");
                    } finally {
                        destoryMonitor.set(true);
                    }
                }
            }
        } else {
        }
    }

    
    private static class InitClass extends DependNode {
        private Object bean; // 类实例
        private int order;
        private Method initMethod; // 初始化方法
        private Method destoryMethod; // 销毁方法
        private boolean ignoreException;

        private InitClass(Class<?> cls, Init init) {
            super(cls.getName(), CoreUtils.convertClassesToClassNames(Arrays.asList(init.depends())));
            try {
                this.bean = CoreUtils.newInstance(cls);
                this.order = init.order();
                this.initMethod = getMethod(cls, init.init());
                this.destoryMethod = getMethod(cls, init.destory());
                this.ignoreException = init.ignoreRuntimeException();
            } catch (Exception e) {
                Throw.throwRuntimeException(ExceptionCodes.YT010101, e, cls.getName());
            }
        }

        private void init() {
            initInvoked();
        }

        private void initInvoked() {
            String id = getId();
            try {
                if (null != bean && null != initMethod) {
                    CommonLogger.info(order + " (start). execute the initialize method : " + initMethod);
                    initMethod.invoke(bean);
                    CommonLogger.info(order + " (success). the initialize method has success complete: " + initMethod);
                }
            } catch (Exception e) {
                CommonLogger.error(order + " (error). the initialize method has occured exception: " + initMethod, e);
                if (!this.ignoreException) {
                    Throw.throwRuntimeException(ExceptionCodes.YT010201, e, id, initMethod);
                }
            }
        }

        private void destory() {
            String id = getId();
            try {
                if (null != bean && null != destoryMethod) {
                    CommonLogger.info(order + " (start). execute the destroy method : " + destoryMethod);
                    destoryMethod.invoke(bean);
                    CommonLogger.info(order + " (success). the destroy method has success complete : " + destoryMethod);
                }
            } catch (Exception e) {
                CommonLogger.error(order + " (error). the destroy method has occured exception : " + destoryMethod, e);
                if (!this.ignoreException) {
                    Throw.throwRuntimeException(ExceptionCodes.YT010202, e, id, destoryMethod);
                }
            }
        }

        private int getOrder() {
            return order;
        }

        private Method getMethod(Class<?> cls, String methodName) {
            try {
                if (!CoreUtils.isBlank(methodName)) {
                    return MethodUtils.getMatchingAccessibleMethod(cls, methodName);
                }
            } catch (Exception ignore) {
            }
            return null;
        }
    }
}
