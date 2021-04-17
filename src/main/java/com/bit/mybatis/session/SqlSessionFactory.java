package com.bit.mybatis.session;

import com.bit.mybatis.config.Configuration;
import com.bit.mybatis.config.MappedStatement;
import com.bit.mybatis.util.IgnoreDTDEntityResolver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

//1. c初始化配置文件到内存
//1. 作为工厂类创建sqlsession
public class SqlSessionFactory {
    private final Configuration configuration=new Configuration();
    public static final String MAPPER_CONFIG_LOCATION="mappers";
    public static final String DB_CONFIG_FILE = "db.properties";


    public SqlSessionFactory() {
        loadDbInfo();
        loadMappersInfo();
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
    private void loadMappersInfo() {
        URL resources = null;
        resources = SqlSessionFactory.class.getClassLoader().getResource(MAPPER_CONFIG_LOCATION);
        File mappers = new File(resources.getFile());
        if (mappers.isDirectory()) {
            File[] listFiles = mappers.listFiles();
            for (File file : listFiles) {
                loadMapperInfo(file);
            }
        }
    }

    private void loadMapperInfo(File file) {
        SAXReader reader = new SAXReader();
        reader.setEntityResolver(new IgnoreDTDEntityResolver());
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attribute("namespace").getData().toString();
        List<Element> elements = rootElement.elements("select");
        for (Element element : elements) {
            MappedStatement statement = new MappedStatement();
            String id = element.attribute("id").getData().toString();
            String resultType = element.attribute("resultType").getData().toString();
            String sql = element.getData().toString();
            String sourceId = namespace + "." + id;
            statement.setNamespace(namespace);
            statement.setResultType(resultType);
            statement.setSql(sql);
            statement.setSourceId(sourceId);
            configuration.getMappedStatementMap().put(sourceId, statement);
        }
    }

    //加载数据库配置信息
    private void loadDbInfo() {
        InputStream inputStream = SqlSessionFactory.class.getClassLoader().getResourceAsStream(DB_CONFIG_FILE);
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //jdbc.url=jdbc:mysql://115.28.129.208:3306/dev
        //jdbc.username=mysql
        //jdbc.password=mysql.dev
        configuration.setJdbcDriver(p.get("jdbc.driver").toString());
        configuration.setJdbcUrl(p.get("jdbc.url").toString());
        configuration.setJdbcUsername(p.get("jdbc.username").toString());
        configuration.setJdbcPassword(p.get("jdbc.password").toString());

    }
}
