package com.itouch8.pump.core.util.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.aopalliance.aop.Advice;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.config.CacheManagementConfigUtils;
import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.annotation.Configable;
import com.itouch8.pump.core.util.annotation.Warning;
import com.itouch8.pump.core.util.bean.IContextBeanOperateWrapper;
import com.itouch8.pump.core.util.data.accessor.IDataAccessorFactory;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.exception.handler.IExceptionHandler;
import com.itouch8.pump.core.util.exception.level.ExceptionLevel;
import com.itouch8.pump.core.util.exception.meta.ExceptionCodes;
import com.itouch8.pump.core.util.exception.meta.IExceptionMetaLoader;
import com.itouch8.pump.core.util.locale.ILocaleMessageResolver;
import com.itouch8.pump.core.util.locale.ILocaleResolver;
import com.itouch8.pump.core.util.monitor.IMonitor;
import com.itouch8.pump.core.util.page.IPageFactory;
import com.itouch8.pump.core.util.reflect.object.IObjectFactory;
import com.itouch8.pump.core.util.reflect.object.impl.SpringObjectFactory;
import com.itouch8.pump.core.util.scan.IScan;
import com.itouch8.pump.core.util.stack.IStackFactory;
import com.itouch8.pump.core.util.track.ITracker;


public class BaseConfig extends ConfigHelper implements InitializingBean, ApplicationContextAware {

    
    private static ApplicationContext applicationContext;

    
    @Configable
    private static String scanPackage;

    
    @Configable
    private static String initScanPackage;

    
    @Configable
    private static String encoding;

    
    @Configable
    private static String defaultLocale;

    
    @Configable
    private static String dateFormat;

    
    @Configable
    private static String timeFormat;

    
    @Configable
    private static String datetimeFormat;

    
    @Configable
    private static String appName;

    
    @Configable
    private static ILocaleResolver localeResolver;

    
    @Configable
    @Warning
    private static ILocaleMessageResolver localeMessageResolver;

    
    private static IContextBeanOperateWrapper beanOperateWrapper;

    
    @Configable
    @Warning
    private static IPageFactory pageFactory;

    
    @Configable
    private static IStackFactory stackFactory;
    
    @Configable
    private static IMonitor logMonitor;

    
    @Configable
    private static IMonitor exceptionMonitor;

    
    @Configable
    @Warning
    private static IExceptionMetaLoader exceptionMetaLoader;

    
    @Configable
    private static List<IExceptionHandler> exceptionHandlers;

    
    @Configable
    private static String exceptionCode;

    
    @Configable
    private static ExceptionLevel exceptionLevel;

    
    @Configable
    private static String exceptionView;

    
    private static IScan scan;

    
    private static ResourcePatternResolver resourcePatternResolver;

    
    private static ConversionService conversionService;

    
    @Configable
    private static ITracker tracker;

    
    @Configable
    private static List<String> ignoreStacks;

    
    @Configable
    private static String encryptKey;

    
    private static String[] pumpLocaleBasenames;

    
    @Configable
    private static String defaultCacheName;

    
    @Configable
    private static CacheManager cacheManager;

    
    @Configable
    private static IObjectFactory objectFactory;

    
    @Configable
    private static IDataAccessorFactory dataAccessorFactory;

    
    @Override
    public void afterPropertiesSet() throws Exception {
        // 配置值有效性校验
        this.validate();
        // Spring相关的组件初始化
        if (null != applicationContext) {
            try {
                initSpringComponents();
            } catch (Exception ignore) {
            }

            if (null == objectFactory) {
                SpringObjectFactory sof = new SpringObjectFactory();
                sof.setApplicationContext(applicationContext);
                objectFactory = sof;
            }
        }

        // 默认国际化对象，一般用于操作系统的默认语言和期望的语言不一致的情况
        String defaultLocale = getDefaultLocale();
        if (!CoreUtils.isBlank(defaultLocale)) {
            try {
                Locale.setDefault(LocaleUtils.toLocale(defaultLocale));
            } catch (Exception e) {
                Throw.throwRuntimeException(ExceptionCodes.YT010010, e, defaultLocale);
            }
        }
    }

    
    protected void initSpringComponents() {
        // 资源加载器
        if (null == resourcePatternResolver) {
            resourcePatternResolver = applicationContext;
        }
        // 类型转换服务
        if (null == conversionService) {
            try {
                AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
                if (beanFactory instanceof ConfigurableBeanFactory) {
                    conversionService = ((ConfigurableBeanFactory) beanFactory).getConversionService();
                }
                if (null == conversionService) {
                    conversionService = applicationContext.getBean(ConversionService.class);
                }
            } catch (Exception e) {
            }
        }
        // 缓存管理器
        if (null == cacheManager) {
            // 缓存切面
            BeanFactoryCacheOperationSourceAdvisor bean = applicationContext.getBean(CacheManagementConfigUtils.CACHE_ADVISOR_BEAN_NAME, BeanFactoryCacheOperationSourceAdvisor.class);
            // 缓存通知
            Advice advice = bean.getAdvice();
            if (advice instanceof CacheInterceptor) {
                CacheResolver cacheResolver = ((CacheInterceptor) advice).getCacheResolver();
                if (cacheResolver instanceof SimpleCacheResolver) {
                    cacheManager = ((SimpleCacheResolver) cacheResolver).getCacheManager();
                }
            }
        }
    }

    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BaseConfig.applicationContext = applicationContext;
    }

    
    public void validate() {
        // 校验字符集是否支持
        boolean isSupported = false;
        try {
            isSupported = Charset.isSupported(encoding);
        } catch (Exception e) {
        }
        if (!isSupported) {
            Throw.createRuntimeException(ExceptionCodes.YT010002, encoding);
        }

        // 校验日期、时间、日期时间格式
    }

    
    public static String getScanPackage() {
        return getValue(scanPackage, "scan_package");
    }

    
    public void setScanPackage(String scanPackage) {
        BaseConfig.scanPackage = scanPackage;
    }

    
    public static String getInitScanPackage() {
        return getValue(initScanPackage, "scan_package");
    }

    
    public void setInitScanPackage(String initScanPackage) {
        BaseConfig.initScanPackage = initScanPackage;
    }

    
    public static String getEncoding() {
        return getValue(encoding, "encoding");
    }

    
    public void setEncoding(String encoding) {
        BaseConfig.encoding = encoding;
    }

    
    public static String getDefaultLocale() {
        return getValue(defaultLocale, "defaultLocale");
    }

    
    public void setDefaultLocale(String defaultLocale) {
        BaseConfig.defaultLocale = defaultLocale;
    }

    
    public static String getDateFormat() {
        return getValue(dateFormat, "date_format");
    }

    
    public void setDateFormat(String dateFormat) {
        BaseConfig.dateFormat = dateFormat;
    }

    
    public static String getTimeFormat() {
        return getValue(timeFormat, "time_format");
    }

    
    public void setTimeFormat(String timeFormat) {
        BaseConfig.timeFormat = timeFormat;
    }

    
    public static String getDatetimeFormat() {
        return getValue(datetimeFormat, "datetime_format");
    }

    
    public void setDatetimeFormat(String datetimeFormat) {
        BaseConfig.datetimeFormat = datetimeFormat;
    }

    
    public static String getAppName() {
        return getValue(appName, "app_name");
    }

    
    public void setAppName(String appName) {
        BaseConfig.appName = appName;
    }

    
    public static ILocaleResolver getLocaleResolver() {
        return getComponent(localeResolver, ILocaleResolver.class);
    }

    
    public void setLocaleResolver(ILocaleResolver localeResolver) {
        BaseConfig.localeResolver = localeResolver;
    }

    
    public static ILocaleMessageResolver getLocaleMessageResolver() {
        return getComponent(localeMessageResolver, ILocaleMessageResolver.class);
    }

    
    public void setLocaleMessageResolver(ILocaleMessageResolver localeMessageResolver) {
        BaseConfig.localeMessageResolver = localeMessageResolver;
    }

    
    public static IScan getScan() {
        return getComponent(scan, IScan.class);
    }

    // public void setScan(IScan scan) {
    // BaseConfig.scan = scan;
    // }

    
    public static ResourcePatternResolver getResourcePatternResolver() {
        return getComponent(resourcePatternResolver, ResourcePatternResolver.class);
    }

    // public void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
    // BaseConfig.resourcePatternResolver = resourcePatternResolver;
    // }

    
    public static IContextBeanOperateWrapper getBeanOperateWrapper() {
        return getComponent(beanOperateWrapper, IContextBeanOperateWrapper.class);
    }

    // public void setBeanOperateWrapper(IContextBeanOperateWrapper beanOperateWrapper) {
    // BaseConfig.beanOperateWrapper = beanOperateWrapper;
    // }

    
    public static IPageFactory getPageFactory() {
        return getComponent(pageFactory, IPageFactory.class);
    }

    
    public void setPageFactory(IPageFactory pageFactory) {
        BaseConfig.pageFactory = pageFactory;
    }

    
    public static IStackFactory getStackFactory() {
        return getComponent(stackFactory, IStackFactory.class);
    }

    
    public void setStackFactory(IStackFactory stackFactory) {
        BaseConfig.stackFactory = stackFactory;
    }
    
    public static IMonitor getLogMonitor() {
        return getComponent(logMonitor, IMonitor.class, "log");
    }

    
    public void setLogMonitor(IMonitor logMonitor) {
        BaseConfig.logMonitor = logMonitor;
    }

    
    public static IExceptionMetaLoader getExceptionMetaLoader() {
        return getComponent(exceptionMetaLoader, IExceptionMetaLoader.class);
    }

    
    public void setExceptionMetaLoader(IExceptionMetaLoader exceptionMetaLoader) {
        BaseConfig.exceptionMetaLoader = exceptionMetaLoader;
    }

    
    public static IMonitor getExceptionMonitor() {
        return getComponent(exceptionMonitor, IMonitor.class, "exception");
    }

    
    public void setExceptionMonitor(IMonitor exceptionMonitor) {
        BaseConfig.exceptionMonitor = exceptionMonitor;
    }

    
    public static List<IExceptionHandler> getExceptionHandlers() {
        return getComponents(exceptionHandlers, IExceptionHandler.class);
    }

    
    public void setExceptionHandlers(List<IExceptionHandler> exceptionHandlers) {
        BaseConfig.exceptionHandlers = exceptionHandlers;
    }

    
    public static String getExceptionCode() {
        return getValue(exceptionCode, "exception_code");
    }

    
    public void setExceptionCode(String exceptionCode) {
        BaseConfig.exceptionCode = exceptionCode;
    }

    
    public static ExceptionLevel getExceptionLevel() {
        return getComponent(exceptionLevel, ExceptionLevel.class);
    }

    
    public void setExceptionLevel(ExceptionLevel exceptionLevel) {
        BaseConfig.exceptionLevel = exceptionLevel;
    }

    
    public static String getExceptionView() {
        return getValue(exceptionView, "exception_view");
    }

    
    public void setExceptionView(String exceptionView) {
        BaseConfig.exceptionView = exceptionView;
    }

    
    public static ConversionService getConversionService() {
        return getComponent(conversionService, ConversionService.class);
    }

    // public void setConversionService(ConversionService conversionService) {
    // BaseConfig.conversionService = conversionService;
    // }

    
    public static ITracker getTracker() {
        return getComponent(tracker, ITracker.class);
    }

    
    public void setTracker(ITracker tracker) {
        BaseConfig.tracker = tracker;
    }

    
    public static List<String> getIgnoreStacks() {
        return combineValues(ignoreStacks, "ignore_stacks");
    }

    
    public void setIgnoreStacks(List<String> ignoreStacks) {
        if (null != ignoreStacks && !ignoreStacks.isEmpty()) {
            if (null == BaseConfig.ignoreStacks) {
                BaseConfig.ignoreStacks = ignoreStacks;
            } else {
                BaseConfig.ignoreStacks.addAll(ignoreStacks);
            }
        }
    }

    
    public static String getEncryptKey() {
        return getValue(encryptKey, "encryptKey");
    }

    
    public static void setEncryptKey(String encryptKey) {
        BaseConfig.encryptKey = encryptKey;
    }

    
    public static String[] getPumpLocaleBasenames() {
        List<String> basenames = null;
        if (null != pumpLocaleBasenames && pumpLocaleBasenames.length >= 1) {
            basenames = Arrays.asList(pumpLocaleBasenames);
        }
        List<String> ss = getValues(basenames, "pump_locale_basenames");
        if (null != ss && !ss.isEmpty()) {
            String[] bs = new String[ss.size()];
            ss.toArray(bs);
            return bs;
        }
        return null;
    }

    
    public static String getDefaultCacheName() {
        return defaultCacheName;
    }

    
    public void setDefaultCacheName(String defaultCacheName) {
        BaseConfig.defaultCacheName = defaultCacheName;
    }

    
    public static CacheManager getCacheManager() {
        return getComponent(cacheManager, CacheManager.class);
    }

    
    public void setCacheManager(CacheManager cacheManager) {
        BaseConfig.cacheManager = cacheManager;
    }

    
    public static IObjectFactory getObjectFactory() {
        return getComponent(objectFactory, IObjectFactory.class);
    }

    
    public void setObjectFactory(IObjectFactory objectFactory) {
        BaseConfig.objectFactory = objectFactory;
    }

    
    public static IDataAccessorFactory getDataAccessorFactory() {
        return ConfigHelper.getComponent(dataAccessorFactory, IDataAccessorFactory.class);
    }

    
    public void setDataAccessorFactory(IDataAccessorFactory dataAccessorFactory) {
        BaseConfig.dataAccessorFactory = dataAccessorFactory;
    }
}
