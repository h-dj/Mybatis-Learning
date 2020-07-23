package cn.hdj.mybatis.example;

import cn.hdj.mybatis.example.dao1.UserMapper;
import cn.hdj.mybatis.example.entity.User;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/13 下午10:57
 * @description:
 */
public class GetSqlSession {
    public static void main(String[] args) throws IOException {
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
        //解析Mapper.xml文件
        //自定义xml文件位置
        String resource = "mapper/UserMapper.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        mapperParser.parse();
        //初始化SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(configuration);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> users = mapper.selectAll();
            System.out.println(users);
        }
    }
}
