package com.itouch8.pump.core.service.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.expression.spel.support.StandardTypeLocator;
import org.springframework.stereotype.Component;

import com.itouch8.pump.ReturnCodes;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;


@Component
public class SpringHelp implements ApplicationContextAware {

    
    private static ApplicationContext context = null;

    
    private static Properties placeholderPropertis;

    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    
    public static boolean hasInit() {
        return context != null;
    }

    
    public static ApplicationContext getApplicationContext() {
        if (null == context) {
            Throw.throwRuntimeException("pump.core.service.spring_not_init");
        }
        return context;
    }

    
    public static Object getBean(String name) {
        try {
            return getApplicationContext().getBean(name);
        } catch (BeansException e) {
            throw Throw.createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, "pump.core.service.not_found_bean_from_spring", name);
        }
    }

    
    public static boolean containsBean(String name) {
        return getApplicationContext().containsBean(name);
    }

    
    public static <E> E getBean(Class<E> cls) {
        try {
            return getApplicationContext().getBean(cls);
        } catch (BeansException e) {
            throw Throw.createRuntimeException(ReturnCodes.SYSTEM_ERROR.code,"pump.core.service.not_found_bean_from_spring", cls);
        }
    }

    
    public static <E> E getBean(String name, Class<E> cls) {
        try {
            return getApplicationContext().getBean(name, cls);
        } catch (BeansException e) {
            throw Throw.createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, "pump.core.service.not_found_bean_from_spring","name=" + name + ";class=" + cls);
        }
    }

    
    public static <E> Map<String, E> getBeansOfType(Class<E> cls) {
        try {
            return getApplicationContext().getBeansOfType(cls);
        } catch (BeansException e) {
            throw Throw.createRuntimeException(ReturnCodes.SYSTEM_ERROR.code, "pump.core.service.not_found_bean_from_spring",cls);
        }
    }

    
    public static <E> List<E> getBeanslistOfType(Class<E> cls) {
        Map<String, E> map = getBeansOfType(cls);
        if (null != map && !map.isEmpty()) {
            List<E> list = new ArrayList<E>(map.size());
            for (E bean : map.values()) {
                list.add(bean);
            }
            return list;
        }
        return null;
    }
    
    public static Object evaluate(String expression) {
        return evaluate(null, expression, (Map<String, Object>) null);
    }

    
    public static Object evaluate(Object param, String expression) {
        return evaluate(param, expression, (Map<String, Object>) null);
    }

    
    public static Object evaluate(Object param, String expression, Map<String, Object> vars) {
        return SpelHelp.evaluate(param, expression, vars);
    }

    
    public static <T> T evaluate(String expression, Class<T> type) {
        return evaluate(null, expression, null, type);
    }

    
    public static <T> T evaluate(Object param, String expression, Class<T> type) {
        return evaluate(param, expression, null, type);
    }

    
    public static <T> T evaluate(Object param, String expression, Map<String, Object> vars, Class<T> type) {
        return SpelHelp.evaluate(param, expression, vars, type);
    }

    
    public static void setValue(String expression, Object value) {
        setValue(null, expression, null, value);
    }

    
    public static void setValue(Object param, String expression, Object value) {
        setValue(param, expression, null, value);
    }

    
    public static void setValue(Object param, String expression, Map<String, Object> vars, Object value) {
        SpelHelp.setValue(param, expression, vars, value);
    }

    
    public static String getPlaceholderProperty(String key) {
        return placeholderPropertis == null ? null : placeholderPropertis.getProperty(key);
    }

    
    public static void setPlaceholderPropertis(Properties placeholderPropertis) {
        SpringHelp.placeholderPropertis = placeholderPropertis;
    }

    
    public static void addSpelVariable(String name, Object variable) {
        SpelHelp.customVariables.put(name, variable);
    }

    
    public static void removeSpelVariable(String name) {
        SpelHelp.customVariables.remove(name);
    }

    
    private static class SpelHelp {
        private static final ThreadLocal<StandardEvaluationContext> context = new ThreadLocal<StandardEvaluationContext>();
        private static final ThreadLocal<Map<String, Object>> variables = new ThreadLocal<Map<String, Object>>();
        private static final ExpressionParser expressionParser = new SpelExpressionParser();
        private static final Map<String, Object> customVariables = new HashMap<String, Object>();
        private static final Map<String, Expression> expressionCache = new ConcurrentHashMap<String, Expression>();

        private static void initialStandardEvaluationContext(StandardEvaluationContext evaluationContext) throws BeansException {
            if (SpringHelp.context.getAutowireCapableBeanFactory() instanceof ConfigurableBeanFactory) {
                ConfigurableBeanFactory factory = (ConfigurableBeanFactory) SpringHelp.context.getAutowireCapableBeanFactory();
                evaluationContext.addPropertyAccessor(new BeanExpressionContextAccessor());
                evaluationContext.addPropertyAccessor(new BeanFactoryAccessor());
                evaluationContext.addPropertyAccessor(new MapAccessor() {
                    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
                        return true;
                    }

                    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
                        @SuppressWarnings("rawtypes")
                        Map map = (Map) target;
                        Object value = map.get(name);
                        if (value == null) {
                            return TypedValue.NULL;
                        }
                        return new TypedValue(value);
                    }
                });
                evaluationContext.addPropertyAccessor(new EnvironmentAccessor());
                evaluationContext.setBeanResolver(new BeanFactoryResolver(factory));
                evaluationContext.setTypeLocator(new StandardTypeLocator(factory.getBeanClassLoader()));
                ConversionService conversionService = factory.getConversionService();
                if (conversionService != null) {
                    evaluationContext.setTypeConverter(new StandardTypeConverter(conversionService));
                }
            }
        }

        private static Object evaluate(Object param, String expression, Map<String, Object> vars) {
            try {
                Expression expr = getExpression(expression);
                EvaluationContext evaluationContext = getEvaluationContext(vars);
                if (null == param) {
                    return expr.getValue(evaluationContext);
                } else {
                    return expr.getValue(evaluationContext, param);
                }
            } finally {
                resetContext();
            }
        }

        private static <T> T evaluate(Object param, String expression, Map<String, Object> vars, Class<T> type) {
            try {
                Expression expr = getExpression(expression);
                EvaluationContext evaluationContext = getEvaluationContext(vars);
                if (null == param) {
                    return expr.getValue(evaluationContext, type);
                } else {
                    return expr.getValue(evaluationContext, param, type);
                }
            } finally {
                resetContext();
            }
        }

        private static void setValue(Object param, String expression, Map<String, Object> vars, Object value) {
            try {
                Expression expr = getExpression(expression);
                EvaluationContext evaluationContext = getEvaluationContext(vars);
                if (null == param) {
                    expr.setValue(evaluationContext, value);
                } else {
                    expr.setValue(evaluationContext, param, value);
                }
            } finally {
                resetContext();
            }
        }

        private static void resetContext() {
            Map<String, Object> varis = variables.get();
            if (null != varis) {
                varis.clear();
            }
        }

        private static EvaluationContext getEvaluationContext(Map<String, Object> vars) {
            StandardEvaluationContext evaluationContext = context.get();
            if (null == evaluationContext) {
                synchronized (SpelHelp.class) {
                    evaluationContext = context.get();
                    if (null == evaluationContext) {
                        evaluationContext = new StandardEvaluationContext();
                        initialStandardEvaluationContext(evaluationContext);
                        @SuppressWarnings("unchecked")
                        Map<String, Object> varis = (Map<String, Object>) CoreUtils.getProperty(evaluationContext, "variables");
                        context.set(evaluationContext);
                        variables.set(varis);
                    }
                }
            }
            evaluationContext.setVariables(customVariables);
            if (null != vars) {
                evaluationContext.setVariables(vars);
            }
            return evaluationContext;
        }

        private static Expression getExpression(String expression) {
            Expression expr = expressionCache.get(expression);
            if (expr == null) {
                synchronized (SpelHelp.class) {
                    expr = expressionCache.get(expression);
                    if (expr == null) {
                        expr = expressionParser.parseExpression(expression);
                        expressionCache.put(expression, expr);
                    }
                }
            }
            return expr;
        }
    }
}
