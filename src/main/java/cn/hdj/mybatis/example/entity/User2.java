package cn.hdj.mybatis.example.entity;

import lombok.ToString;
import org.apache.ibatis.annotations.Param;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/13 下午11:22
 * @description: 测试有参构造函数 MyBatis 对象映射
 */
@ToString
public class User2 {
    private Integer id;
    private String name;
    private Integer age;

    public User2(@Param("id") Integer id, @Param("name") String name, @Param("age") Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
