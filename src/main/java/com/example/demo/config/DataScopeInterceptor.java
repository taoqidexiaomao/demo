package com.example.demo.config;

import com.example.demo.annotation.DataScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * mubatis拦截器  拦截sql的执行
 * Executor -> StatementHandler->ParameterHandler->ResultSetHandler
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
@Slf4j
public class DataScopeInterceptor implements Interceptor {


    /**
     * 进行拦截的时候要执行的方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("====intercept====== ");
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        //id为执行的mapper方法的全路径名，如com.metro.dao.UserMapper.insertUser
        String methodId = mappedStatement.getId();
        // 获取Mapper的Class名称
        String clazzName = methodId.substring(0, methodId.lastIndexOf("."));
        // 获取拦截的方法名
        String methodName = methodId.substring(methodId.lastIndexOf(".") + 1);
        // 反射获取方法上的注解内容
        Method[] methods = Class.forName(clazzName).getDeclaredMethods();
        DataScope dataScope = null;
        for (Method md : methods) {
            if (methodName.equalsIgnoreCase(md.getName())) {
                dataScope = md.getAnnotation(DataScope.class);
            }
        }
        if (dataScope == null) {
            return invocation.proceed();
        }
        String type = dataScope.type();
        String column = dataScope.column();
        if (StringUtils.isAnyEmpty(type, column)) {
            return invocation.proceed();
        }
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = mappedStatement.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }
        //获取到原始sql语句
        String sql = boundSql.getSql();
        sql = sql.replaceAll("\\n", "").replaceAll("\\t", "");
        log.info("original SQL: {}", sql);
        // 9.根据注解内容修改SQL后执行
        if ("USER".equals(type)) {
            String newSql = "SELECT * FROM (" + sql + ") sss WHERE sss." + column + " = 1";
            log.info("new SQL: {}", newSql);
            //通过反射修改boundSql对象的sql语句
            Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, newSql);
            //执行修改后的sql语句
            return executor.query(mappedStatement, parameter, rowBounds, resultHandler, cacheKey, boundSql);
        }
        //执行结果
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}




