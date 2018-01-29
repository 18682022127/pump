package com.itouch8.pump.core.dao.mybatis.mapper;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.ibatis.annotations.Flush;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.itouch8.pump.core.PumpConfig;
import com.itouch8.pump.core.dao.DaoUtils;
import com.itouch8.pump.core.dao.IDaoTemplate;
import com.itouch8.pump.core.dao.annotation.BatchParam;
import com.itouch8.pump.core.dao.annotation.Execute;
import com.itouch8.pump.core.dao.annotation.Executes;
import com.itouch8.pump.core.dao.annotation.FetchSize;
import com.itouch8.pump.core.dao.annotation.OriginalUsage;
import com.itouch8.pump.core.dao.annotation.SqlRef;
import com.itouch8.pump.core.dao.annotation.SqlRefs;
import com.itouch8.pump.core.dao.call.ICallResult;
import com.itouch8.pump.core.dao.exception.DaoExceptionCodes;
import com.itouch8.pump.core.dao.mybatis.MybatisUtils;
import com.itouch8.pump.core.dao.mybatis.page.PageAdapter;
import com.itouch8.pump.core.dao.sql.SqlManager;
import com.itouch8.pump.core.dao.stream.IListStreamReader;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.core.util.page.IPage;
import com.itouch8.pump.core.util.page.PageUtils;


public class MapperMethod {

    private final SqlCommand command;
    private final MethodSignature method;
    private final Method originMethod;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
        this.command = new SqlCommand(config, mapperInterface, method);
        this.method = new MethodSignature(config, method);
        this.originMethod = method;
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        String sqlId = command.getName();
        IDaoTemplate dao = command.dao;
        Class<?> returnType = this.method.getReturnType();
        boolean isOriginalUsage = originMethod.isAnnotationPresent(OriginalUsage.class);
        boolean returnIntArray = returnType.equals(int[].class);
        List<IMapperMethodExecutor> executors = PumpConfig.getMapperMethodExecutors();

        Object result;
        if (null != executors) {
            for (IMapperMethodExecutor executor : executors) {
                if (executor.isSupport(this)) {
                    return executor.execute(this, args);
                }
            }
        }

         if (returnIntArray && !isOriginalUsage) {// 批量
            if (null == this.method.executes) {
                SqlRefs refs = this.originMethod.getAnnotation(SqlRefs.class);
                if (null == refs || refs.value() == null || refs.value().length == 0) {
                    List<Object> param = method.convertArgsToBatchParam(args, 1);
                    result = dao.executeBatch(sqlId, param);
                } else {
                    result = executeBatch(args, method, refs, originMethod);
                }
            } else {
                Object param = method.convertArgsToSqlCommandParam(args);
                List<String> sqlIds = new ArrayList<String>();
                List<Object> params = new ArrayList<Object>();
                int i = 0;
                for (Execute execute : method.executes.value()) {
                    String condition = execute.condition();
                    if (CoreUtils.isBlank(condition) || CoreUtils.getProperty(param, condition, boolean.class)) {
                        method.convertArgsToBatchExecute(param, sqlIds, params, execute);
                    } else {
                        CommonLogger.info("the execute condition is not match, so ignore the execute [index : " + i + ", method : " + this.originMethod + "]");
                    }
                    i++;
                }
                if (null == params || params.isEmpty()) {
                    CommonLogger.warn("the batch params is null or empty, so ignore the method [" + this.originMethod + "]");
                    return null;
                }
                result = DaoUtils.executeBatch(sqlIds, params);
            }
        } else if (ICallResult.class.isAssignableFrom(returnType)) {
            Object param = method.convertArgsToSqlCommandParam(args);
            result = dao.call(sqlId, param);
        } else if (IListStreamReader.class.isAssignableFrom(returnType)) {// 流式查询
            Object param = method.convertArgsToSqlCommandParam(args);
            int fetchSize = getFetchSize(originMethod, args);
            if (-1 != fetchSize) {
                result = dao.selectListStream(sqlId, param, fetchSize);
            } else {
                result = dao.selectListStream(sqlId, param);
            }
        } else if (SqlCommandType.INSERT == command.getType()) {
            Object param = method.convertArgsToSqlCommandParam(args);
            result = rowCountResult(dao.insert(sqlId, param));
        } else if (SqlCommandType.UPDATE == command.getType()) {
            Object param = method.convertArgsToSqlCommandParam(args);
            result = rowCountResult(dao.update(sqlId, param));
        } else if (SqlCommandType.DELETE == command.getType()) {
            Object param = method.convertArgsToSqlCommandParam(args);
            result = rowCountResult(dao.delete(sqlId, param));
        } else if (SqlCommandType.SELECT == command.getType()) {
            if (method.returnsVoid() && method.hasResultHandler()) {
                executeWithResultHandler(sqlSession, args);
                result = null;
            } else if (method.returnsMany()) {
                result = executeForMany(sqlSession, args);
            } else if (method.returnsMap()) {
                result = executeForMap(sqlSession, args);
            } else {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = dao.selectOne(command.getName(), param);
            }
        } else if (SqlCommandType.FLUSH == command.getType()) {
            result = sqlSession.flushStatements();
        } else {
            throw new BindingException("Unknown execution method for: " + command.getName());
        }
        if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
            throw new BindingException("Mapper method '" + command.getName() + " attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
        }
        return result;
    }

    private int getFetchSize(Method method, Object[] args) {
        FetchSize fs = null;
        Object[][] annos = method.getParameterAnnotations();
        for (int i = 0, l = annos.length; i < l; i++) {
            for (Object aAnno : annos[i]) {
                if (aAnno instanceof FetchSize) {
                    fs = (FetchSize) aAnno;
                    Object arg = args[i];
                    if (arg instanceof Integer) {
                        return ((Integer) arg).intValue();
                    } else {
                        return fs.value();
                    }
                }
            }
        }
        fs = this.originMethod.getAnnotation(FetchSize.class);
        if (null != fs) {
            return fs.value();
        }
        return -1;
    }

    private Object executeBatch(Object[] args, MethodSignature method, SqlRefs refs, Method originMethod) {
        List<String> sqlIds = new ArrayList<String>();
        for (SqlRef ref : refs.value()) {
            sqlIds.add(SqlManager.resolverSqlId(ref, originMethod));
        }
        List<Object> param = method.convertArgsToBatchParam(args, sqlIds.size());
        Object result = DaoUtils.executeBatch(sqlIds, param);
        return result;
    }

    private Object rowCountResult(int rowCount) {
        final Object result;
        if (method.returnsVoid()) {
            result = null;
        } else if (Integer.class.equals(method.getReturnType()) || Integer.TYPE.equals(method.getReturnType())) {
            result = Integer.valueOf(rowCount);
        } else if (Long.class.equals(method.getReturnType()) || Long.TYPE.equals(method.getReturnType())) {
            result = Long.valueOf(rowCount);
        } else if (Boolean.class.equals(method.getReturnType()) || Boolean.TYPE.equals(method.getReturnType())) {
            result = Boolean.valueOf(rowCount > 0);
        } else {
            throw new BindingException("Mapper method '" + command.getName() + "' has an unsupported return type: " + method.getReturnType());
        }
        return result;
    }

    private void executeWithResultHandler(SqlSession sqlSession, Object[] args) {
        MappedStatement ms = sqlSession.getConfiguration().getMappedStatement(command.getName());
        if (void.class.equals(ms.getResultMaps().get(0).getType())) {
            throw new BindingException("method " + command.getName() + " needs either a @ResultMap annotation, a @ResultType annotation," + " or a resultType attribute in XML so a ResultHandler can be used as a parameter.");
        }
        Object param = method.convertArgsToSqlCommandParam(args);
        if (method.hasRowBounds()) {
            RowBounds rowBounds = method.extractRowBounds(args);
            sqlSession.select(command.getName(), param, rowBounds, method.extractResultHandler(args));
        } else {
            sqlSession.select(command.getName(), param, method.extractResultHandler(args));
        }
    }

    private <E> Object executeForMany(SqlSession sqlSession, Object[] args) {
        List<E> result = null;
        Object param = method.convertArgsToSqlCommandParam(args);
        if (method.hasRowBounds()) {
            IPage page = method.extractPageParam(args);
            result = command.dao.selectList(command.getName(), param, page);
            // sqlSession.select(command.getName(), param, rowBounds, new ResultHandler<Object>(){
            // public void handleResult(ResultContext<? extends Object> context) {
            // E rs = MybatisUtils.dealMapResult(context.getResultObject());
            // result.add(rs);
            // }
            // });
        } else {
            result = command.dao.selectList(command.getName(), param);
            // sqlSession.select(command.getName(), param, new ResultHandler<Object>(){
            // public void handleResult(ResultContext<? extends Object> context) {
            // E rs = MybatisUtils.dealMapResult(context.getResultObject());
            // result.add(rs);
            // }
            // });
        }

        // issue #510 Collections & arrays support
        if (!method.getReturnType().isAssignableFrom(result.getClass())) {
            if (method.getReturnType().isArray()) {
                return convertToArray(result);
            } else {
                return convertToDeclaredCollection(sqlSession.getConfiguration(), result);
            }
        }
        return result;
    }

    private <E> Object convertToDeclaredCollection(Configuration config, List<E> list) {
        Object collection = config.getObjectFactory().create(method.getReturnType());
        MetaObject metaObject = config.newMetaObject(collection);
        metaObject.addAll(list);
        return collection;
    }

    @SuppressWarnings("unchecked")
    private <E> Object convertToArray(List<E> list) {
        Class<?> componentType = method.getReturnType().getComponentType();
        int size = list.size();
        if (componentType.equals(int.class)) {
            int[] rs = new int[size];
            int i = 0;
            for (E o : list) {
                rs[i++] = CoreUtils.convert(o, int.class);
            }
            return rs;
        } else if (componentType.equals(short.class)) {
            short[] rs = new short[size];
            int i = 0;
            for (E o : list) {
                rs[i++] = CoreUtils.convert(o, short.class);
            }
            return rs;
        } else if (componentType.equals(byte.class)) {
            byte[] rs = new byte[size];
            int i = 0;
            for (E o : list) {
                rs[i++] = CoreUtils.convert(o, byte.class);
            }
            return rs;
        } else if (componentType.equals(char.class)) {
            char[] rs = new char[size];
            int i = 0;
            for (E o : list) {
                rs[i++] = CoreUtils.convert(o, char.class);
            }
            return rs;
        } else if (componentType.equals(long.class)) {
            long[] rs = new long[size];
            int i = 0;
            for (E o : list) {
                rs[i++] = CoreUtils.convert(o, long.class);
            }
            return rs;
        } else if (componentType.equals(float.class)) {
            float[] rs = new float[size];
            int i = 0;
            for (E o : list) {
                rs[i++] = CoreUtils.convert(o, float.class);
            }
            return rs;
        } else if (componentType.equals(double.class)) {
            double[] rs = new double[size];
            int i = 0;
            for (E o : list) {
                rs[i++] = CoreUtils.convert(o, double.class);
            }
            return rs;
        } else if (componentType.equals(boolean.class)) {
            boolean[] rs = new boolean[size];
            int i = 0;
            for (E o : list) {
                rs[i++] = o == null ? false : CoreUtils.string2Boolean(o.toString());
            }
            return rs;
        } else {
            E[] array = (E[]) Array.newInstance(componentType, size);
            array = list.toArray(array);
            return array;
        }
    }

    private <K, V> Map<K, V> executeForMap(SqlSession sqlSession, Object[] args) {
        Map<K, V> result;
        Object param = method.convertArgsToSqlCommandParam(args);
        if (method.hasRowBounds()) {
            RowBounds rowBounds = method.extractRowBounds(args);
            result = sqlSession.<K, V>selectMap(command.getName(), param, method.getMapKey(), rowBounds);
        } else {
            result = sqlSession.<K, V>selectMap(command.getName(), param, method.getMapKey());
        }
        return result;
    }

    public static class ParamMap<V> extends HashMap<String, V> {

        private static final long serialVersionUID = -2212268410512043556L;

        @Override
        public V get(Object key) {
            return super.get(key);
        }
    }

    public static class SqlCommand {

        private final String name;
        private final SqlCommandType type;
        private final IDaoTemplate dao;

         SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName = SqlManager.getSqlId(method);
            MappedStatement ms = null;
            if (null != statementName) {
                if (configuration.hasStatement(statementName)) {
                    ms = configuration.getMappedStatement(statementName);
                }
            }
            SqlRef ref = method.getAnnotation(SqlRef.class);
            if (null == ms && null == ref) {
                statementName = mapperInterface.getName() + "." + method.getName();
                if (configuration.hasStatement(statementName)) {
                    ms = configuration.getMappedStatement(statementName);
                } else if (!mapperInterface.equals(method.getDeclaringClass())) { // issue  #35
                    String parentStatementName = method.getDeclaringClass().getName() + "." + method.getName();
                    if (configuration.hasStatement(parentStatementName)) {
                        ms = configuration.getMappedStatement(parentStatementName);
                    }
                }
            } else if (null == ms) {
                Throw.throwRuntimeException("not found the sqlId : " + statementName);
            }
            if (ms == null) {
                if (method.getAnnotation(Flush.class) != null) {
                    name = null;
                    dao = null;
                    type = SqlCommandType.FLUSH;
                } else if (method.isAnnotationPresent(SqlRefs.class) || method.isAnnotationPresent(Executes.class)) {
                    name = null;
                    dao = null;
                    type = null;
                } else {
                    // throw new BindingException("Invalid bound statement (not found): " + statementName);
                    throw Throw.createRuntimeException(DaoExceptionCodes.YT020016, statementName);
                }
            } else {
                name = ms.getId();
                type = ms.getSqlCommandType();
                if (type == SqlCommandType.UNKNOWN) {
                    throw new BindingException("Unknown execution method for: " + name);
                }
                dao = MybatisUtils.getDaoTemplate(name);
            }
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }

        public IDaoTemplate getDao() {
            return dao;
        }
    }

    public static class MethodSignature {
        private final boolean returnsMany;
        private final boolean returnsMap;
        private final boolean returnsVoid;
        private final Class<?> returnType;
        private final String mapKey;
        private final Integer resultHandlerIndex;
        private final Integer rowBoundsIndex;
        private final SortedMap<Integer, String> params;
        private final boolean hasNamedParameters;
        private final Executes executes;
        private final int batchItemIndex;
        private final BatchParam batchParam;
        private final Method method;

         MethodSignature(Configuration configuration, Method method) {

            SqlRefs refs = method.getAnnotation(SqlRefs.class);
            if (null != refs && (null == refs.value() || refs.value().length <= 1)) {
                Throw.throwRuntimeException(DaoExceptionCodes.YT020014, method);
            }

            this.returnType = method.getReturnType();
            this.returnsVoid = void.class.equals(this.returnType);
            this.returnsMany = (configuration.getObjectFactory().isCollection(this.returnType) || this.returnType.isArray());
            this.mapKey = getMapKey(method);
            this.returnsMap = (this.mapKey != null);
            this.hasNamedParameters = hasNamedParams(method);
            this.rowBoundsIndex = getUniquePageParamIndex(method);
            this.resultHandlerIndex = getUniqueParamIndex(method, ResultHandler.class);
            this.params = Collections.unmodifiableSortedMap(getParams(method, this.hasNamedParameters));
            this.method = method;

            Executes executes = null;
            int batchItemIndex = -1;
            BatchParam batchParam = null;
            if (this.returnType.equals(int[].class)) {
                executes = method.getAnnotation(Executes.class);
                if (null == executes || null == executes.value() || 0 == executes.value().length) {
                    Class<?>[] clss = method.getParameterTypes();
                    if (null != clss) {
                        final Object[][] paramAnnos = method.getParameterAnnotations();
                        for (int i = 0, l = clss.length; i < l; i++) {
                            batchParam = getBatchParamAnnoation(paramAnnos[i]);
                            if (null != batchParam) {// 存在批量参数注解
                                batchItemIndex = i;
                                break;
                            }
                        }
                    }
                    executes = null;
                }
            } else {
                if (null != method.getAnnotation(Executes.class)) {
                    Throw.throwRuntimeException(DaoExceptionCodes.YT020018, Executes.class.getSimpleName(), method);
                } else if (null != method.getAnnotation(SqlRefs.class)) {
                    Throw.throwRuntimeException(DaoExceptionCodes.YT020018, SqlRefs.class.getSimpleName(), method);
                } else {
                    Class<?>[] clss = method.getParameterTypes();
                    if (null != clss) {
                        final Object[][] paramAnnos = method.getParameterAnnotations();
                        for (int i = 0, l = clss.length; i < l; i++) {
                            if (null != getBatchParamAnnoation(paramAnnos[i])) {// 存在批量参数注解
                                Throw.throwRuntimeException(DaoExceptionCodes.YT020018, BatchParam.class.getSimpleName(), method);
                            }
                        }
                    }
                }
            }
            this.executes = executes;
            this.batchItemIndex = batchItemIndex;
            this.batchParam = batchParam;
        }

        public Object convertArgsToSqlCommandParam(Object[] args) {
            final int paramCount = params.size();
            if (args == null || paramCount == 0) {
                return null;
            } else if (!hasNamedParameters && paramCount == 1 && !(args[params.keySet().iterator().next().intValue()] instanceof IPage)) {
                return args[params.keySet().iterator().next().intValue()];
            } else if (!hasNamedParameters && paramCount == 2 && hasRowBounds()) {
                for (Map.Entry<Integer, String> entry : params.entrySet()) {
                    Object value = args[entry.getKey().intValue()];
                    if (!(value instanceof IPage)) {
                        return value;
                    }
                }
                return null;
            } else {
                final Map<String, Object> param = new ParamMap<Object>();
                int i = 0;
                for (Map.Entry<Integer, String> entry : params.entrySet()) {
                    Object value = args[entry.getKey().intValue()];
                    if (!(value instanceof IPage)) {
                        param.put(entry.getValue(), args[entry.getKey().intValue()]);
                        // issue #71, add param names as param1, param2...but ensure backward compatibility
                        final String genericParamName = "param" + String.valueOf(i + 1);
                        if (!param.containsKey(genericParamName)) {
                            param.put(genericParamName, args[entry.getKey()]);
                        }
                    }
                    i++;
                }
                return param;
            }
        }

        @SuppressWarnings("unchecked")
        public List<Object> convertArgsToBatchParam(Object[] args, int batchCount) {
            final int paramCount = params.size();
            if (args == null || paramCount == 0) {
                return null;
            } else {
                List<Object> list = new ArrayList<Object>();
                if (this.batchItemIndex == -1) {// 没有@BatchParam注解
                    Object param = convertArgsToSqlCommandParam(args);
                    for (int i = 0; i < batchCount; i++) {
                        list.add(param);
                    }
                } else {
                    Object batchArg = this.resolverBatchParam(args[batchItemIndex], batchParam);
                    if (null == batchArg || !isBatchParamType(batchArg.getClass())) {
                        Throw.throwRuntimeException(DaoExceptionCodes.YT020013, method);
                    }
                    List<Object> batchArgs = CoreUtils.convertToList(batchArg, Object.class);
                    if (batchCount != 1 && (null == batchArgs || batchArgs.size() != batchCount)) {
                        Throw.throwRuntimeException(DaoExceptionCodes.YT020015, batchCount, null == batchArgs ? 0 : batchArgs.size());
                    }

                    Object commParam = convertArgsToSqlCommandParam(args);
                    String batchParamName = this.batchParam.item();
                    String batchIndexName = this.batchParam.index();
                    for (int index = 0, l = batchArgs.size(); index < l; index++) {
                        Object arg = batchArgs.get(index);
                        final Map<String, Object> param = new ParamMap<Object>();
                        if (commParam instanceof ParamMap) {
                            param.putAll((ParamMap<Object>) commParam);
                        } else {
                            param.put("param1", commParam);
                        }
                        if (!CoreUtils.isBlank(batchIndexName)) {
                            param.put(batchIndexName, index);
                        }
                        if (!CoreUtils.isBlank(batchParamName)) {
                            param.put(batchParamName, arg);
                        }
                        list.add(param);
                    }
                }
                return list;
            }
        }

        @SuppressWarnings("unchecked")
        public void convertArgsToBatchExecute(Object context, List<String> sqlIds, List<Object> params, Execute execute) {
            BatchParam batchParam = execute.param();
            String sqlId = SqlManager.resolverSqlId(execute.sqlRef(), method);
            Object batchArg = this.resolverBatchParam(context, batchParam);
            if (null == batchArg) {
                CommonLogger.info("the batch params is null, so ignore the sqlId [" + sqlId + "]");
                return;
            } else if (batchParam.value()) {
                if (!isBatchParamType(batchArg.getClass())) {
                    Throw.throwRuntimeException(DaoExceptionCodes.YT020013, method);
                }
                List<Object> batchArgs = CoreUtils.convertToList(batchArg, Object.class);
                String batchParamName = batchParam.item();
                String batchIndexName = batchParam.index();
                for (int index = 0, l = batchArgs.size(); index < l; index++) {
                    Object arg = batchArgs.get(index);
                    final Map<String, Object> param = new ParamMap<Object>();
                    if (context instanceof ParamMap) {
                        param.putAll((ParamMap<Object>) context);
                    } else {
                        param.put("param1", context);
                    }
                    if (!CoreUtils.isBlank(batchIndexName)) {
                        param.put(batchIndexName, index);
                    }
                    if (!CoreUtils.isBlank(batchParamName)) {
                        param.put(batchParamName, arg);
                    }
                    sqlIds.add(sqlId);
                    params.add(param);
                }
            } else {
                sqlIds.add(sqlId);
                params.add(batchArg);
            }
        }

        public boolean hasRowBounds() {
            return rowBoundsIndex != null;
        }

        public RowBounds extractRowBounds(Object[] args) {
            PageAdapter rb = null;
            if (hasRowBounds()) {
                Object o = args[rowBoundsIndex];
                if (null == o) {
                    rb = new PageAdapter(PageUtils.createPage());
                } else if (o instanceof IPage) {
                    rb = new PageAdapter((IPage) o);
                } else {
                    return (RowBounds) o;
                }
            }
            return rb;
        }

        public IPage extractPageParam(Object[] args) {
            IPage rb = null;
            if (hasRowBounds()) {
                Object o = args[rowBoundsIndex];
                if (null == o) {
                    rb = PageUtils.createPage();
                } else {
                    return (IPage) o;
                }
            }
            return rb;
        }

        public boolean hasResultHandler() {
            return resultHandlerIndex != null;
        }

        @SuppressWarnings("rawtypes")
        public ResultHandler extractResultHandler(Object[] args) {
            return hasResultHandler() ? (ResultHandler) args[resultHandlerIndex] : null;
        }

        public String getMapKey() {
            return mapKey;
        }

        public Class<?> getReturnType() {
            return returnType;
        }

        public boolean returnsMany() {
            return returnsMany;
        }

        public boolean returnsMap() {
            return returnsMap;
        }

        public boolean returnsVoid() {
            return returnsVoid;
        }

        private Object resolverBatchParam(Object param, BatchParam batchParam) {
            String property = batchParam.property();
            if (!"this".equals(property) && !CoreUtils.isBlank(property)) {
                Object rs = CoreUtils.getProperty(param, property);
                return rs;
            }
            return param;
        }

        private Integer getUniquePageParamIndex(Method method) {
            Integer index = null;
            final Class<?>[] argTypes = method.getParameterTypes();
            // Class<?> rcls = RowBounds.class;
            Class<?> pcls = IPage.class;
            for (int i = 0; i < argTypes.length; i++) {
                if ( pcls.isAssignableFrom(argTypes[i])) {
                    if (index == null) {
                        index = i;
                    } else {
                        throw new BindingException(method.getName() + " cannot have multiple " + pcls.getSimpleName() + " parameters");
                    }
                }
            }
            return index;
        }

        private Integer getUniqueParamIndex(Method method, Class<?> paramType) {
            Integer index = null;
            final Class<?>[] argTypes = method.getParameterTypes();
            for (int i = 0; i < argTypes.length; i++) {
                if (paramType.isAssignableFrom(argTypes[i])) {
                    if (index == null) {
                        index = i;
                    } else {
                        throw new BindingException(method.getName() + " cannot have multiple " + paramType.getSimpleName() + " parameters");
                    }
                }
            }
            return index;
        }

        private String getMapKey(Method method) {
            String mapKey = null;
            if (Map.class.isAssignableFrom(method.getReturnType())) {
                final MapKey mapKeyAnnotation = method.getAnnotation(MapKey.class);
                if (mapKeyAnnotation != null) {
                    mapKey = mapKeyAnnotation.value();
                }
            }
            return mapKey;
        }

        private SortedMap<Integer, String> getParams(Method method, boolean hasNamedParameters) {
            final SortedMap<Integer, String> params = new TreeMap<Integer, String>();
            final Class<?>[] argTypes = method.getParameterTypes();
            for (int i = 0; i < argTypes.length; i++) {
                if (!RowBounds.class.isAssignableFrom(argTypes[i]) && !ResultHandler.class.isAssignableFrom(argTypes[i])) {
                    String paramName = String.valueOf(params.size());
                    if (hasNamedParameters) {
                        paramName = getParamNameFromAnnotation(method, i, paramName);
                    }
                    params.put(i, paramName);
                }
            }
            return params;
        }

        private String getParamNameFromAnnotation(Method method, int i, String paramName) {
            final Object[] paramAnnos = method.getParameterAnnotations()[i];
            for (Object paramAnno : paramAnnos) {
                if (paramAnno instanceof Param) {
                    paramName = ((Param) paramAnno).value();
                    break;
                }
            }
            return paramName;
        }

        private boolean hasNamedParams(Method method) {
            final Object[][] paramAnnos = method.getParameterAnnotations();
            for (Object[] paramAnno : paramAnnos) {
                for (Object aParamAnno : paramAnno) {
                    if (aParamAnno instanceof Param) {
                        return true;
                    }
                }
            }
            return false;
        }

        private BatchParam getBatchParamAnnoation(Object[] paramAnno) {
            for (Object aParamAnno : paramAnno) {
                if (aParamAnno instanceof BatchParam) {
                    return (BatchParam) aParamAnno;
                }
            }
            return null;
        }

        private boolean isBatchParamType(Class<?> cls) {
            return cls.isArray() || Iterator.class.isAssignableFrom(cls) || Enumeration.class.isAssignableFrom(cls) || Iterable.class.isAssignableFrom(cls); // 因此包含Collection，从而也包含List、Set、Queue等常见集合类型
        }
    }

    public SqlCommand getCommand() {
        return command;
    }

    public MethodSignature getMethodSignature() {
        return method;
    }

    public Method getMethod() {
        return originMethod;
    }

}
