package cn.hdj.mybatis.demo;

import cn.hdj.mybatis.demo.dao.UserMapper;
import cn.hdj.mybatis.demo.entity.User;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;

import java.util.List;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/13 下午10:57
 * @description:
 */
public class GetSqlSession {
    public static void main(String[] args) {
        //构建数据源
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:/home/hdj/db/h2db;AUTO_SERVER=TRUE");
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

        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> users = mapper.selectAll();
            System.out.println(users);
        }
    }
}
