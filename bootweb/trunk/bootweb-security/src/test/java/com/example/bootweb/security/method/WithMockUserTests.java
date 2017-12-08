package com.example.bootweb.security.method;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.bootweb.security.config.RedirectAfterLoginSpringSecurityConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    TestConfig.class, RedirectAfterLoginSpringSecurityConfig.class })
@WithMockUser(username = "admin", roles = {
    "USER", "ADMIN" })
public class WithMockUserTests {

  @Autowired
  private MessageService messageService;

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  @Ignore // Not work, tobe.
  public void getMessageUnauthenticated() {
    messageService.getMessage();
  }

  @Test
  public void getMessage() {
    System.out.println("getMessage: " + messageService.getMessage());
  }

  @Test
  @WithMockUser
  public void getMessageWithMockUser() {
    String message = messageService.getMessage();
    System.out.println("getMessageWithMockUser: " + message);
  }

  @Test
  @WithMockUser("customUsername")
  public void getMessageWithMockUserCustomUsername() {
    String message = messageService.getMessage();
    System.out.println("getMessageWithMockUserCustomUsername: " + message);
  }

  @Test
  @WithMockUser(username = "admin", roles = {
      "USER", "ADMIN" })
  public void getMessageWithMockUserCustomUser() {
    String message = messageService.getMessage();
    System.out.println("getMessageWithMockUserCustomUser: " + message);
  }

  @Test
  @WithAnonymousUser
  public void anonymous() throws Exception {
    // override default to run as anonymous user
  }

  @Test
  @WithMockUser(username = "admin", authorities = {
      "ADMIN", "USER" })
  public void getMessageWithMockUserCustomAuthorities() {
    String message = messageService.getMessage();
    System.out.println("getMessageWithMockUserCustomAuthorities: " + message);
  }

  @Test
  @WithUserDetails("user")
  @Ignore
  public void getMessageWithUserDetails() {
    String message = messageService.getMessage();
    System.out.println("getMessageWithUserDetails: " + message);
  }

  @Test
  @WithUserDetails(value = "customUsername", userDetailsServiceBeanName = "myUserDetailsService")
  @Ignore
  public void getMessageWithUserDetailsServiceBeanName() {
    String message = messageService.getMessage();
    System.out.println("getMessageWithUserDetailsServiceBeanName: " + message);
  }

}
