package cn.hdj.mybatis.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/16 下午3:00
 * @description:
 */
@Data
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sno
     */
    private String sno;

    /**
     * cno
     */
    private String cno;

    /**
     * degree
     */
    private String degree;

}
