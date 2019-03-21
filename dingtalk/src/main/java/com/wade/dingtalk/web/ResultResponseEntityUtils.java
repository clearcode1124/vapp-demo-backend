package com.wade.dingtalk.web;

import org.springframework.http.ResponseEntity;

import com.wade.dingtalk.domain.Result;

public class ResultResponseEntityUtils {

  private ResultResponseEntityUtils() {

  }

  public static ResponseEntity<Result> toResponseEntity(Result result) {
    return ResponseEntity.status(result.getCode()).body(result);
  }
}
