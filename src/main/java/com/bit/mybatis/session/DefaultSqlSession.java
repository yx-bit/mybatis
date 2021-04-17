package com.bit.mybatis.session;

import com.bit.mybatis.binding.MapperProxy;
import com.bit.mybatis.config.Configuration;
import com.bit.mybatis.config.MappedStatement;
import com.bit.mybatis.executor.DefaultExceutor;
import com.bit.mybatis.executor.Executor;

import java.lang.reflect.Proxy;
import java.util.List;

public class DefaultSqlSession implements SqlSession {
    private final Configuration configuration;

    private Executor executor;
    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        executor = new DefaultExceutor(configuration);
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.<T>selectOne(statement,null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<T> list = this.<T>selectList(statement, parameter);
        if (list == null) {
            return null;
        }
        if (list.size() == 1) {
            return (T) list.get(0);
        }
        if (list.size() > 1) {
            throw new RuntimeException("Expected one result to be return many");
        }
        return null;
    }

    @Override
    public <T> List<T> selectList(String statement, Object parameter) {
        MappedStatement ms = configuration.getMappedStatement(statement);
        return executor.query(ms,parameter);
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {
        ///配置文件解读+动态代理的增强
        //找到session中对应的方法执行
        //找到命名空间和方法名
        //传递参数
        MapperProxy proxy = new MapperProxy(this);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},proxy);
    }
}
