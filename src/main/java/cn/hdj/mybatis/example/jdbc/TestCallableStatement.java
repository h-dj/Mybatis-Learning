package cn.hdj.mybatis.example.jdbc;

import java.sql.*;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/8/14 下午12:24
 * @description:
 */
public class TestCallableStatement {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //获取连接对象
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8", "root", "123456");
        String sql = "CALL findById(?)";
        CallableStatement stmt = connection.prepareCall(sql);
        stmt.setString(1, "108");
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString("SNO");
            String name = resultSet.getString("SNAME");
            String sex = resultSet.getString("SSEX");
            System.out.println("id=" + id + " name=" + name + " age=" + sex);
        }
    }
}
