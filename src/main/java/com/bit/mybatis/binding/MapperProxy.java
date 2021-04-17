package com.bit.mybatis.binding;

import com.bit.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

public class MapperProxy implements InvocationHandler {
    private SqlSession sqlSession;

    public MapperProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //核心点  解析mapper
        Class<?> returnType = method.getReturnType();
        String statement = method.getDeclaringClass().getName() + "." + method.getName();
        if (Collection.class.isAssignableFrom(returnType)) {
            return sqlSession.selectList(statement, args==null?null:args[0]);
        }
        return sqlSession.selectOne(statement, args == null ? null : args[0]);
    }
}
