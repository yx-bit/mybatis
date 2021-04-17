package com.bit.mybatis.session;

import java.util.List;

//对外提供主要服务的api
//底层对内通过Executor去执行
public interface SqlSession {

     <T> T selectOne(String statement);
     <T> T selectOne(String statement,Object parameter);
     <T> List<T> selectList(String statement,Object parameter);

    <T> T getMapper(Class<T> clazz);
}
