package cn.hdj.mybatis.example;

import java.sql.*;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/14 下午4:26
 * @description: 使用原生JDBC操纵数据库
 */
public class GetDataWithJDBC {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //加载驱动
        Class.forName("org.h2.Driver");
        //获取连接对象
        Connection connection = DriverManager.getConnection("jdbc:h2:/home/hdj/db/h2db;AUTO_SERVER=TRUE", "admin", "admin");

        //如果需要操作事务
        // connection.setAutoCommit(false) ;    // 取消掉自动提交
        ////如果有多个操作，需要回滚到特定保存点
        //Savepoint savepoint = connection.setSavepoint();

        //创建statement
        Statement statement = connection.createStatement();

        try {
            //获取结果集
            ResultSet resultSet = statement.executeQuery("select * from user");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                System.out.println("id=" + id + " name=" + name + " age=" + age);
            }
        } catch (Exception e) {
            //如果报错，回滚
            //connection.rollback();
            //回滚特定保存点
            //connection.rollback(savepoint);
        }
    }
}
