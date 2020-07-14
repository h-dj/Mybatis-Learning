package cn.hdj.mybatis.demo;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/14 下午4:12
 * @description: 使用MyBatis 实现获取数据源
 */
public class GetDataSource {

    public static void main(String[] args) throws IOException, SQLException {
        DataSourceFactory dsf = new UnpooledDataSourceFactory();
        Properties properties = new Properties();
        InputStream configStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("database.properties");

        properties.load(configStream);

        dsf.setProperties(properties);
        DataSource dataSource = dsf.getDataSource();
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from user");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.println("id=" + id + " name=" + name + " age=" + age);
        }
    }
}
