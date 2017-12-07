package com.example.bootweb.security.handler;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.bootweb.security.profile.CsrfAngularJSDemo;

@Component
@CsrfAngularJSDemo
public class CsrfLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    authorities.forEach(authority -> {
      if (authority.getAuthority().equals("ROLE_USER")) {
        try {
          response.setStatus(200);
          response.getWriter().write("{OK:123}");
          response.getWriter().flush();
//          redirectStrategy.sendRedirect(arg0, response, "/app/index.html#!/home");
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
        try {
          redirectStrategy.sendRedirect(arg0, response, "/admin");
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        throw new IllegalStateException();
      }
    });

  }

}