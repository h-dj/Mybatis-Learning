package cn.hdj.mybatis.example.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/16 下午1:56
 * @description:
 */
@Data
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sno
     */
    private String sno;

    /**
     * sname
     */
    private String sname;

    /**
     * ssex
     */
    private String ssex;

    /**
     * sbirthday
     */
    private Date sbirthday;

    /**
     * sclass
     */
    private String sclass;
}

