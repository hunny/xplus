package com.xplus.server.service.auth.custom;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplus.server.service.auth.jwt.TokenAuthenticationUtils;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

  public JWTLoginFilter(String url, AuthenticationManager authManager) {
    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) //
      throws AuthenticationException, IOException, ServletException {
    // JSON反序列化成 AccountCredentials
    UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(),
        UserCredentials.class);

    // 返回一个验证令牌
    return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword()));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
      FilterChain chain, Authentication auth) throws IOException, ServletException {
    TokenAuthenticationUtils.addAuthentication(res, auth.getName());
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getOutputStream().print("Authentication Failed.");
    ;
  }

}
