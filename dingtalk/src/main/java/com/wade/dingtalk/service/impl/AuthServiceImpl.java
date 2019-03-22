package com.wade.dingtalk.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.request.OapiServiceGetCorpTokenRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
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
  public String getSign() {
    DefaultDingTalkClient client =
        new DefaultDingTalkClient("https://oapi.dingtalk.com/service/get_corp_token");
    OapiServiceGetCorpTokenRequest req4AccessToken = new OapiServiceGetCorpTokenRequest();
    req4AccessToken.setAuthCorpid("ding06e35a04569c475d35c2f4657eb6378f");
    OapiServiceGetCorpTokenResponse execute4AccessToken;
    try {
      execute4AccessToken = client.execute(req4AccessToken, "suitezgvrkfc3usrkqvrb",
          "zYoSxt3WqkFAzOlNQAjTkSBGofX22QwkWRmweM95Y8GZqlXjdiujGNsfwPxBNAp6", "123");
      String accessToken = execute4AccessToken.getAccessToken();

      DefaultDingTalkClient client4Ticket =
          new DefaultDingTalkClient("https://oapi.dingtalk.com/get_jsapi_ticket");
      OapiGetJsapiTicketRequest req4Ticket = new OapiGetJsapiTicketRequest();
      req4Ticket.setTopHttpMethod("GET");
      OapiGetJsapiTicketResponse execute4Ticket = client4Ticket.execute(req4Ticket, accessToken);
      String ticket = execute4Ticket.getTicket();
      long timeStame = System.currentTimeMillis();
      String sign = sign(ticket, "123456", timeStame, "http://vapp.clearcode.top/");
      return timeStame + "," + sign;
    } catch (ApiException e) {
      return "";
    }
  }

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

  public String sign(String ticket, String nonceStr, long timeStamp, String url) {
    String plainFormat = "jsapi_ticket=%s&noncestr=%s&timestamp=%d&url=%s";
    String plain = String.format(plainFormat, ticket, nonceStr, timeStamp, url);
    try {
      MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
      sha1.reset();
      sha1.update(plain.getBytes("UTF-8"));
      return bytesToHex(sha1.digest());
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      return "";
    }
  }

  public String bytesToHex(byte[] src) {
    StringBuilder stringBuilder = new StringBuilder("");
    if (src == null || src.length <= 0) {
      return null;
    }
    for (int i = 0; i < src.length; i++) {
      int v = src[i] & 0xFF;
      String hv = Integer.toHexString(v);
      if (hv.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hv);
    }
    return stringBuilder.toString();
  }

}

