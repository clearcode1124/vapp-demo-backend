package com.wade.dingtalk.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2019年3月13日 下午2:02:13
 * @Description:
 */
@Data
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  
  private String unionid;
  
  private String userid;
  
  private String isLeaderInDepts;
  
  private Boolean isBoss;
  
  private Long hiredDate;
  
  private Boolean isSenior;
  
  private Integer[] department;
  
  private String orderInDepts;
  
  private Boolean active;
  
  private String avatar;
  
  private Boolean isAdmin;
  
  private Boolean isHide;
  
  private String jobnumber;
  
  private String position;
}

