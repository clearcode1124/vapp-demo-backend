package com.wade.dingtalk.context;

public class RequestContext {

  private String unionid;

  private static ThreadLocal<RequestContext> requestContextHolder = new ThreadLocal<>();

  public static RequestContext current() {
    return requestContextHolder.get();
  }

  public void withUnionid(String unionid) {
    this.unionid = unionid;
  }

  public String getCurrentUnionid() {
    RequestContext requestContext = RequestContext.current();
    if (requestContext == null) {
      return null;
    }
    return this.unionid;
  }

}
