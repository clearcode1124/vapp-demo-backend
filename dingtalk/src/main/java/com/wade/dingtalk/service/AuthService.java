package com.wade.dingtalk.service;
/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2019年3月21日 上午9:50:39
 * @Description:
 */

import com.wade.dingtalk.domain.Result;

public interface AuthService {

  String getSign();
  
  Result login(String requestAuthCode);
}

