package com.wade.dingtalk.web;

import static com.wade.dingtalk.web.ResultResponseEntityUtils.*;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wade.dingtalk.context.RequestContext;
import com.wade.dingtalk.domain.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2019年3月22日 下午2:44:39
 * @Description:
 */
@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

  @PostMapping("")
  public HttpEntity<Result> save() {
    log.debug("currentUnionid: {}", RequestContext.current().getCurrentUnionid());
    return toResponseEntity(Result.ok());
  }
}

