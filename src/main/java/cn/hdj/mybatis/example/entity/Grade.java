package cn.hdj.mybatis.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hdj
 * @version 1.0
 * @date 2020/7/16 下午3:02
 * @description:
 */
@Data
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * low
     */
    private Integer low;

    /**
     * upp
     */
    private Integer upp;

    /**
     * rank
     */
    private String rank;
}