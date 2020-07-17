package cn.hdj.mybatis.example.dao;

import cn.hdj.mybatis.example.entity.Student;

import java.util.List;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/13 下午10:53
 * @description:
 */
public interface StudentMapper {

    //返回学生列表
    List<Student> list();

    //返回学生列表
    List<Student> list(String a);


    //添加学生
    int addStudent(Student student);

    //批量添加学生
    int addStudentBatch(List<Student> students);

    //更新学生
    int updateStudentById(Student student);
}
