package com.wade.dingtalk.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

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

  @JsonProperty("name")
  private String name;
  
  @JsonProperty("unionid")
  private String unionid;
  
  @JsonProperty("userid")
  private String userid;
  
  @JsonProperty("isLeaderInDepts")
  private String isLeaderInDepts;

  @JsonProperty("isBoss")
  private Boolean isBoss;

  @JsonProperty("hiredDate")
  private Long hiredDate;

  @JsonProperty("isSenior")
  private Boolean isSenior;

  @JsonProperty("department")
  private Integer[] department;

  @JsonProperty("orderInDepts")
  private String orderInDepts;

  @JsonProperty("active")
  private Boolean active;

  @JsonProperty("avatar")
  private String avatar;

  @JsonProperty("isAdmin")
  private Boolean isAdmin;

  @JsonProperty("isHide")
  private Boolean isHide;

  @JsonProperty("jobnumber")
  private String jobnumber;

  @JsonProperty("position")
  private String position;
}

