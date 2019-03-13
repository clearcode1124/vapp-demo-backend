package com.wade.dingtalk.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiServiceGetCorpTokenRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiServiceGetCorpTokenResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;

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

  @PostMapping("/login")
  public OapiUserGetResponse login(@RequestParam String requestAuthCode) {
    DefaultDingTalkClient client =
        new DefaultDingTalkClient("https://oapi.dingtalk.com/service/get_corp_token");
    OapiServiceGetCorpTokenRequest req4AccessToken = new OapiServiceGetCorpTokenRequest();
    req4AccessToken.setAuthCorpid("ding06e35a04569c475d35c2f4657eb6378f");
    OapiServiceGetCorpTokenResponse execute4AccessToken;
    try {
      execute4AccessToken = client.execute(req4AccessToken, "suitezgvrkfc3usrkqvrb",
          "zYoSxt3WqkFAzOlNQAjTkSBGofX22QwkWRmweM95Y8GZqlXjdiujGNsfwPxBNAp6", "123");
      String accessToken = execute4AccessToken.getAccessToken();
      DingTalkClient client4UserId =
          new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
      OapiUserGetuserinfoRequest request4UserId = new OapiUserGetuserinfoRequest();
      request4UserId.setCode(requestAuthCode);
      request4UserId.setHttpMethod("GET");
      OapiUserGetuserinfoResponse response = client4UserId.execute(request4UserId, accessToken);
      String userId = response.getUserid();

      DingTalkClient client4UserInfo =
          new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
      OapiUserGetRequest request4UserInfo = new OapiUserGetRequest();
      request4UserInfo.setUserid(userId);
      request4UserInfo.setHttpMethod("GET");
      return client4UserInfo.execute(request4UserInfo, accessToken);
    } catch (ApiException e) {
      return null;
    }
  }
}

