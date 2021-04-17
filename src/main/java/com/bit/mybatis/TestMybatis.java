package com.bit.mybatis;

import com.bit.mybatis.domain.FileRecords;
import com.bit.mybatis.mapper.FileRecordsMapper;
import com.bit.mybatis.session.SqlSession;
import com.bit.mybatis.session.SqlSessionFactory;

public class TestMybatis {
    public static void main(String[] args) {
        //        1.读取mybatis-config.xml配置文件

        //        2.构建SqlSessionFactory
        SqlSessionFactory factory = new SqlSessionFactory();
        //        3.打开SqlSession
        SqlSession sqlSession = factory.openSession();
        System.out.println(sqlSession);
        //        4.获取Mapper接口对象
        FileRecordsMapper mapper = sqlSession.getMapper(FileRecordsMapper.class);
        //如何去调的sqlSession
        //        5.调用Mapper接口对象的方法操作数据库
        FileRecords fileRecords = mapper.selectOne("100");
        System.out.println(fileRecords);
        //        业务处理

        //或者  实际底层去掉的sqlsession  再去调的executor  再去调jdbc
        FileRecords one = sqlSession.selectOne("com.bit.mybatis.mapper.FileRecordsMapper.selectOne","100");
        System.out.println(one);
    }
}
