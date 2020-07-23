package cn.hdj.mybatis.example.dao1;

import cn.hdj.mybatis.example.entity.User;
import cn.hdj.mybatis.example.entity.User2;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/13 下午10:53
 * @description:
 */
public interface UserMapper {
    List<User> selectAll();

    User selectById(Integer id);

    List<User> selectPage(RowBounds rowBounds);

    Integer count();

    List<User2> selectAll2();

    int insert(User user);

    int batchInsert(List<User> list);

}
