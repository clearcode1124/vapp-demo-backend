package com.wade.dingtalk.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiServiceGetCorpTokenRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiServiceGetCorpTokenResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.api.ApiException;
import com.wade.dingtalk.domain.Result;
import com.wade.dingtalk.domain.TokenResult;
import com.wade.dingtalk.domain.User;
import com.wade.dingtalk.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2019年3月21日 上午9:51:12
 * @Description:
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  @Value("{$jwt.secret}")
  private String secret;

  private static final String CLAIM_KEY_USERNAME = "sub";
  private static final String CLAIM_KEY_CREATED = "created";

  @Override
  public Result login(String requestAuthCode) {
    try {
      DefaultDingTalkClient client =
          new DefaultDingTalkClient("https://oapi.dingtalk.com/service/get_corp_token");
      OapiServiceGetCorpTokenRequest req4AccessToken = new OapiServiceGetCorpTokenRequest();
      req4AccessToken.setAuthCorpid("ding06e35a04569c475d35c2f4657eb6378f");
      OapiServiceGetCorpTokenResponse execute4AccessToken;
      execute4AccessToken = client.execute(req4AccessToken, "suitezgvrkfc3usrkqvrb",
          "zYoSxt3WqkFAzOlNQAjTkSBGofX22QwkWRmweM95Y8GZqlXjdiujGNsfwPxBNAp6", "123");
      String accessToken = execute4AccessToken.getAccessToken();

      client.resetServerUrl("https://oapi.dingtalk.com/user/getuserinfo");
      OapiUserGetuserinfoRequest request4UserId = new OapiUserGetuserinfoRequest();
      request4UserId.setCode(requestAuthCode);
      request4UserId.setHttpMethod("GET");
      OapiUserGetuserinfoResponse response = client.execute(request4UserId, accessToken);
      String userId = response.getUserid();

      client.resetServerUrl("https://oapi.dingtalk.com/user/get");
      OapiUserGetRequest request4UserInfo = new OapiUserGetRequest();
      request4UserInfo.setUserid(userId);
      request4UserInfo.setHttpMethod("GET");
      OapiUserGetResponse response4Userinfo = client.execute(request4UserInfo, accessToken);
      Map<String, Object> claims = new HashMap<>();
      claims.put(CLAIM_KEY_USERNAME, response4Userinfo.getUnionid());
      claims.put(CLAIM_KEY_CREATED, new Date());
      String accessToken4Local = Jwts.builder().setClaims(claims)
          .setExpiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000))
          .signWith(SignatureAlgorithm.HS512, secret).compact();
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      String json = mapper.writeValueAsString(response4Userinfo);
      User user = mapper.readValue(json, User.class);
      log.debug("accessToken4Local：{}", user.getUnionid());
      return new TokenResult(accessToken4Local, accessToken4Local, user);
    } catch (ApiException | IOException e) {
      log.error("登录失败：{}", e);
      return Result.clientError(String.format("登录失败：%s", e));
    }
  }

}

