package cn.hdj.mybatis;

import cn.hdj.mybatis.example.entity.User;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.junit.Test;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/20 上午9:30
 * @description: MetaObject 工具类使用
 */
public class MetaObjectTest {


    /**
     * 如何使用MetaObject工具类获取User对象的属性信息
     */
    @Test
    public void testObjectProperties() {
        User user=new User();
        user.setAge(25);
        user.setName("java");
        MetaObject metaObject = SystemMetaObject.forObject(user);
        //获取用户名称
        Object name = metaObject.getValue("name");
        System.out.println(name);
        //判断是否有年龄设置
        boolean age = metaObject.hasGetter("age");
        System.out.println(age);
    }
}
