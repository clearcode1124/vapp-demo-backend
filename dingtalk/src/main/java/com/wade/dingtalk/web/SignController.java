package com.wade.dingtalk.web;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.request.OapiServiceGetCorpTokenRequest;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.dingtalk.api.response.OapiServiceGetCorpTokenResponse;
import com.taobao.api.ApiException;

/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2019年3月13日 上午10:19:20
 * @Description:
 */
@RestController
@RequestMapping("/api/v1/signs")
public class SignController {

  @GetMapping("/get-sign")
  public String getSigns() {
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

