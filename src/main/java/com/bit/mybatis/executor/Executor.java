package com.bit.mybatis.executor;

import com.bit.mybatis.config.MappedStatement;

import java.util.List;

//mybatis 核心接口之一 定义了数据库操作最基本的方法 sqlSession的功能是基于executor实现的
public interface Executor {
    <E> List<E> query(MappedStatement ms, Object parameter);
}
