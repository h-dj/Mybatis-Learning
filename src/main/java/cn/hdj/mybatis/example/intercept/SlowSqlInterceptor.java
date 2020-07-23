package cn.hdj.mybatis.example.intercept;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;
import java.util.Properties;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/23 下午4:43
 * @description: 慢sql 插件拦截器
 * <p>
 * 使用注解@Intercepts 用于指定拦截的方法
 */
@Slf4j
@Intercepts({
        //拦截StatementHandler 类中的query方法， args指定方法中的形参类型
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public class SlowSqlInterceptor implements Interceptor {

    private Integer limitSecond;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        StatementHandler target = (StatementHandler) invocation.getTarget();
        try {
            return invocation.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long cost = end - start;
            if (cost > limitSecond * 1000) {
                BoundSql boundSql = target.getBoundSql();
                log.warn("慢SQL >>{}", boundSql.getSql());
            }
        }
    }

    //返回代理类
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    //设置属性
    @Override
    public void setProperties(Properties properties) {
        String limitSecond = (String) properties.get("limitSecond");
        this.limitSecond = Integer.parseInt(limitSecond);
    }
}
