package cn.hdj.mybatis.example.jdbc;

import java.sql.*;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/14 下午4:26
 * @description: 使用原生JDBC操纵数据库
 */
public class GetDataWithJDBC {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        findUser();

        updateUser();
    }


    public static void findUser() throws ClassNotFoundException, SQLException {
        //加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //获取连接对象
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test", "root", "123456");

        //创建statement
        Statement statement = connection.createStatement();
        //获取结果集
        ResultSet resultSet = statement.executeQuery("select * from user");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.println("id=" + id + " name=" + name + " age=" + age);
        }
    }

    public static void updateUser() throws ClassNotFoundException, SQLException {
        //加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //获取连接对象
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test", "root", "123456");
        //关闭自动提交
        connection.setAutoCommit(false);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE test.`user` SET name= ? , age= ? WHERE id= ?");
            preparedStatement.setString(1, "Java--555");
            preparedStatement.setInt(2, 25);
            preparedStatement.setInt(3, 1);
            preparedStatement.execute();

            //在提交前发生错误
            int a = 1 / 0;
            //手动提交
            connection.commit();
        } catch (Exception e) {
            //失败回滚
            connection.rollback();
            e.printStackTrace();
        }
    }
}
