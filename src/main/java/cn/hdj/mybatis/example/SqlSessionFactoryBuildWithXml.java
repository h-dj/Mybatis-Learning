package cn.hdj.mybatis.example;

import cn.hdj.mybatis.example.dao1.UserMapper;
import cn.hdj.mybatis.example.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/13 下午10:47
 * @description: 读取XML配置，初始化 SqlSessionFactory
 */
public class SqlSessionFactoryBuildWithXml {

    public static void main(String[] args) throws IOException {
        //读取配置文件
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //构造SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建SqlSession
        try (SqlSession session = sqlSessionFactory.openSession()) {
            //获取Mapper接口
            UserMapper mapper = session.getMapper(UserMapper.class);
            //执行sql
            List<User> users = mapper.selectAll();
            System.out.println(users);

            List<User> list = mapper.selectPage(new RowBounds(1, 1));
            System.out.println(list);
        }
    }
}
