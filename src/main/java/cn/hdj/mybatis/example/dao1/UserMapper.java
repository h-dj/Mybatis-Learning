package cn.hdj.mybatis.example.dao1;

import cn.hdj.mybatis.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/13 下午10:53
 * @description:
 */
@Mapper
public interface UserMapper {
   List<User> selectAll();
   User selectById(Integer id);
}
