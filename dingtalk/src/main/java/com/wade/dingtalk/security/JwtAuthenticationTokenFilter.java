package com.wade.dingtalk.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wade.dingtalk.context.RequestContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2019年3月22日 上午10:58:09
 * @Description:
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  @Value("${jwt.header}")
  private String tokenHeader;

  @Value("${jwt.tokenHead}")
  private String tokenHead;

  @Value("{$jwt.secret}")
  private String secret;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    String authHeader = request.getHeader(this.tokenHeader);
    if (authHeader != null && authHeader.startsWith(tokenHead)) {
      final String authToken = authHeader.substring(tokenHead.length());
      Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
      String unionid = claims.getSubject();
      RequestContext.current().withUnionid(unionid);
      log.debug("unionid:{}", unionid);
    }
    chain.doFilter(request, response);
  }

}

