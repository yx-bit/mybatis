package com.bit.mybatis.executor;

import com.bit.mybatis.config.Configuration;
import com.bit.mybatis.config.MappedStatement;
import com.bit.mybatis.reflection.ReflectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultExceutor implements Executor {
    private Configuration configuration;

    public DefaultExceutor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter) {
        String sql = ms.getSql();
        System.out.println(sql);
        List<E> list = new ArrayList<>();
        //jdbc规范
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        //1.加载驱动
        try {
             Class.forName(configuration.getJdbcDriver());
            //2创建链接
            connection = DriverManager.getConnection(configuration.getJdbcUrl(), configuration.getJdbcUsername(), configuration.getJdbcPassword());
            //3.创建一个查询
            stmt = connection.prepareStatement(sql);
            //4.设置查询参数
            dealParameter(stmt, parameter);
            //5.从数据库中加载数据resultset
            resultSet= stmt.executeQuery();
            handlerResultSet(resultSet, list, ms.getResultType());
            //6.释放链接
            resultSet.close();
            stmt.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


        return list;
    }

    private <E> void handlerResultSet(ResultSet resultSet, List<E> list, String resultType) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        while (resultSet.next()) {
            Class<?> aClass = Class.forName(resultType);
            E object = (E)aClass.newInstance();
            ReflectionUtil.setPropToBeanFromResultSet(object,resultSet);
            list.add(object);
        }
    }

    private void dealParameter(PreparedStatement stmt, Object parameter) throws SQLException {
        stmt.setString(1,(String) parameter);
    }
}
