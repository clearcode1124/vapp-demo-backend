package com.wade.dingtalk.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Result implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private boolean success;
  private int code;
  private String message;
  private transient Object detail;


  public static Result success() {
    return Result.builder().success(true).build();
  }

  public static Result ok() {
    return Result.builder().success(true).code(200).build();
  }

  public static Result ok(String message) {
    return Result.builder().success(true).code(200).message(message).build();
  }

  public static Result ok(Object data) {
    return Result.builder().success(true).code(200).detail(data).build();
  }

  public static Result okWithData(Object data) {
    return Result.builder().success(true).code(200).detail(data).build();
  }

  public static Result created(Long id) {
    return Result.builder().success(true).code(201).detail(id).build();
  }

  public static Result clientError(String message) {
    return error(400, message);
  }

  public static Result clientError(String message, Object error) {
    return error(400, message, error);
  }

  public static Result clientError(String message, List<?> errors) {
    return error(400, message, errors);
  }

  public static Result unauthorized(String message) {
    return error(401, message);
  }

  public static Result forbidden() {
    return error(403);
  }

  public static Result notFound() {
    return error(404);
  }

  public static Result confict(String message) {
    return error(409, message);
  }

  public static Result confict(String message, Object data) {
    return error(409, message, data);
  }

  public static Result preconditionFailed(String message) {
    return error(412, message);
  }

  public static Result invalid(String message, Map<String, String> errorMap) {

    return error(422, message, errorMap);
  }

  public static Result serverError() {
    return error(500);
  }

  public static Result serverError(String message) {
    return error(500, message);
  }

  public static Result serverError(String message, Object error) {
    return error(500, message, error);
  }

  public static Result serverError(String message, List<?> errors) {
    return error(500, message, errors);
  }

  public static Result error(int code) {
    Preconditions.checkArgument(code >= 400);
    return Result.builder().success(false).code(code).build();
  }

  public static Result error(int code, String message) {
    Preconditions.checkArgument(code >= 400);
    return Result.builder().success(false).code(code).message(message).build();
  }

  public static Result error(int code, String message, Object error) {
    Preconditions.checkArgument(code >= 400);
    return Result.builder().success(false).code(code).message(message).detail(error).build();
  }



}
