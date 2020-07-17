package cn.hdj.mybatis;

import cn.hdj.mybatis.example.dao.StudentMapper;
import cn.hdj.mybatis.example.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/16 上午11:34
 * @description: MyBatis 特性使用测试
 */
@Slf4j
public class MyBatisUseTest {


    private static SqlSessionFactory sqlSessionFactory;
    @BeforeClass
    public static void init() throws IOException {
        //读取配置文件
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //构造SqlSessionFactory
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }
    @Test
    public void testSelectList() {
        //创建SqlSession
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            //获取Mapper接口
            StudentMapper studentMapper = session.getMapper(StudentMapper.class);
            List<Student> students = studentMapper.list();
            students.forEach(student -> log.info("{}", student));
        }
    }
    @Test
    public void testInsertStudent() {
        //创建SqlSession
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            //获取Mapper接口
            StudentMapper studentMapper = session.getMapper(StudentMapper.class);

            Student student = new Student();
            student.setSname("张三");
            student.setSsex("男");
            student.setSbirthday(new Date());
            student.setSno("111");
            student.setSclass("95033");
            studentMapper.addStudent(student);

            List<Student> students = studentMapper.list();
            students.forEach(s -> log.info("{}", s));
        }
    }


    @Test
    public void testInsertStudentBatch() {
        //创建SqlSession
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            //获取Mapper接口
            StudentMapper studentMapper = session.getMapper(StudentMapper.class);

            Student student = new Student();
            student.setSname("小张");
            student.setSsex("男");
            student.setSbirthday(new Date());
            student.setSno("112");
            student.setSclass("95033");


            Student student2 = new Student();
            student2.setSname("小李");
            student2.setSsex("男");
            student2.setSbirthday(new Date());
            student2.setSno("113");
            student2.setSclass("95033");

            studentMapper.addStudentBatch(Arrays.asList(student, student2));

            List<Student> students = studentMapper.list();
            students.forEach(s -> log.info("{}", s));
        }
    }

    @Test
    public void testUpdateStudent() {
        //创建SqlSession
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            //获取Mapper接口
            StudentMapper studentMapper = session.getMapper(StudentMapper.class);
            Student student = new Student();
            student.setSname("123");
            student.setSsex("男");
            student.setSbirthday(new Date());
            student.setSno("111");
            student.setSclass("95033");
            int count = studentMapper.updateStudentById(student);
            log.info("{}", count);
            List<Student> students = studentMapper.list();
            students.forEach(s -> log.info("{}", s));
        }
    }
}
