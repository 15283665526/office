package com.zjw.oa.entity;

import lombok.Data;

@Data
public class User {

  private long userId;
  private String userName;
  private String nikeName;
  private String pwd;
  private String zw;
  private long permission;
  private long sjUserId;
  private String phone;
  private String userAccount;

}
