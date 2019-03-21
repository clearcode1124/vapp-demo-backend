package com.wade.dingtalk.web;

import static com.wade.dingtalk.web.ResultResponseEntityUtils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wade.dingtalk.domain.Result;
import com.wade.dingtalk.service.AuthService;

/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2019年3月13日 下午2:00:36
 * @Description:
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public HttpEntity<Result> login(@RequestParam String requestAuthCode) {
    return toResponseEntity(authService.login(requestAuthCode));
  }
}

