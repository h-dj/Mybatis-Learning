package cn.hdj.mybatis.demo.dao;

import cn.hdj.mybatis.demo.entity.User;

import java.util.List;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/13 下午10:53
 * @description:
 */
public interface UserMapper {

   List<User> selectAll();
}
