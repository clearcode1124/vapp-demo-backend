package com.wade.dingtalk;
/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2019年3月13日 上午8:47:00
 * @Description:
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.request.OapiServiceGetCorpTokenRequest;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.dingtalk.api.response.OapiServiceGetCorpTokenResponse;

public class SignTester {

  @Test
  public void test() throws Exception {
    DefaultDingTalkClient client =
        new DefaultDingTalkClient("https://oapi.dingtalk.com/service/get_corp_token");
    OapiServiceGetCorpTokenRequest req4AccessToken = new OapiServiceGetCorpTokenRequest();
    req4AccessToken.setAuthCorpid("ding06e35a04569c475d35c2f4657eb6378f");
    OapiServiceGetCorpTokenResponse execute4AccessToken =
        client.execute(req4AccessToken, "suitezgvrkfc3usrkqvrb",
            "zYoSxt3WqkFAzOlNQAjTkSBGofX22QwkWRmweM95Y8GZqlXjdiujGNsfwPxBNAp6", "123");
    String accessToken = execute4AccessToken.getAccessToken();
    System.out.println(accessToken);

    DefaultDingTalkClient client4Ticket =
        new DefaultDingTalkClient("https://oapi.dingtalk.com/get_jsapi_ticket");
    OapiGetJsapiTicketRequest req4Ticket = new OapiGetJsapiTicketRequest();
    req4Ticket.setTopHttpMethod("GET");
    OapiGetJsapiTicketResponse execute4Ticket = client4Ticket.execute(req4Ticket, accessToken);
    String ticket = execute4Ticket.getTicket();
    System.out.println(ticket);
    long timeStame = System.currentTimeMillis();
    System.out.println(timeStame);
    System.out.println(sign(ticket, "123456", timeStame, "http://vapp.clearcode.top/"));
  }

  public String sign(String ticket, String nonceStr, long timeStamp, String url) {
    String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp="
        + String.valueOf(timeStamp) + "&url=" + url;
    try {
      MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
      sha1.reset();
      sha1.update(plain.getBytes("UTF-8"));
      return bytesToHex(sha1.digest());
    } catch (NoSuchAlgorithmException e) {
    } catch (UnsupportedEncodingException e) {
    }
    return "";
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

