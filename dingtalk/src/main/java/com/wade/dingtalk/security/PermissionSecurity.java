package com.wade.dingtalk.security;

import org.springframework.stereotype.Component;

import com.taobao.api.internal.util.StringUtils;
import com.wade.dingtalk.context.RequestContext;

/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2018年11月26日 下午5:16:44
 * @Description:
 */
@Component
public class PermissionSecurity {

  public boolean check() {
    return !StringUtils.isEmpty(RequestContext.current().getCurrentUnionid());
  }

}

