package com.example.bootweb.security.handler;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.bootweb.security.profile.IndexAngularJSDemo;

@Component
@IndexAngularJSDemo
public class IndexLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      if (authority.getAuthority().equals("ROLE_USER")) {
        response.setStatus(200);
        response.getWriter().write("{OK:123}");
        response.getWriter().flush();
        return;
      }
    }
    response.setStatus(403);
  }
}