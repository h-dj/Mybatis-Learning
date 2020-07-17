package cn.hdj.mybatis.example;

import cn.hdj.mybatis.example.dao.UserMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;

import java.io.IOException;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/13 下午10:47
 * @description: Java配置，初始化 SqlSessionFactory
 */
public class SqlSessionFactoryBuildWithoutXml {

    public static void main(String[] args) throws IOException {
        //构建数据源
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:./test");
        dataSource.setUser("admin");
        dataSource.setPassword("admin");
        //事务工厂
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        //环境容器
        Environment environment = new Environment("development", transactionFactory, dataSource);
        //配置
        Configuration configuration = new Configuration(environment);
        //添加Mapper接口
        configuration.addMapper(UserMapper.class);
        //初始化SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
}
