package cn.hdj.mybatis.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/16 下午2:59
 * @description:
 */
@Data
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * cno
     */
    private String cno;

    /**
     * cname
     */
    private String cname;

    /**
     * tno
     */
    private String tno;
}
