package cn.hdj.mybatis.example.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/16 下午3:00
 * @description:
 */
@Data
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * tno
     */
    private String tno;

    /**
     * tname
     */
    private String tname;

    /**
     * tsex
     */
    private String tsex;

    /**
     * tbirthday
     */
    private Date tbirthday;

    /**
     * prof
     */
    private String prof;

    /**
     * depart
     */
    private String depart;
}
