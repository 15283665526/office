package com.zjw.oa.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
/**
 * 
 * @TableName operate_log
 */
public class OperateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msg;
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private String userName;

    /**
     *
     */
    private String userAccount;

    /**
     * 操作
     */
    private String operate;

    /**
     * 
     */
    private Date createDate;


}