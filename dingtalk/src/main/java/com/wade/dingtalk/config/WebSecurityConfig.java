package com.wade.dingtalk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wade.dingtalk.security.JwtAuthenticationTokenFilter;

/**
 * 
 * @company: hua9group
 * @author wdChen
 * @date:2019年3月22日 上午10:51:55
 * @Description:
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
    return new JwtAuthenticationTokenFilter();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        // 由于使用的是JWT，我们这里不需要csrf
        .csrf().disable()

        // 基于token，所以不需要session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        .authorizeRequests()
        .anyRequest().permitAll();
        // 对于获取token的rest api要允许匿名访问
//        .antMatchers("/api/v1/auth/**").permitAll()
//        // 除上面外的所有请求全部需要鉴权认证
//        .anyRequest().authenticated();
    httpSecurity.addFilterBefore(authenticationTokenFilterBean(),
        UsernamePasswordAuthenticationFilter.class);

    // 禁用缓存
    httpSecurity.headers().cacheControl();
  }
}

