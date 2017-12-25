package com.example.bootweb.aop;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.rule.OutputCapture;

import com.example.springboot.aop.Application;

public class ApplicationTest {

  @Rule
  public OutputCapture outputCapture = new OutputCapture();

  private String profiles;

  @Before
  public void init() {
    this.profiles = System.getProperty("spring.profiles.active");
  }

  @After
  public void after() {
    if (this.profiles != null) {
      System.setProperty("spring.profiles.active", this.profiles);
    } else {
      System.clearProperty("spring.profiles.active");
    }
  }

  @Test
  public void testDefaultSettings() throws Exception {
    Application.main(new String[0]);
    String output = this.outputCapture.toString();
    Assertions.assertThat(output).contains("Hello Phil");
  }

  @Test
  public void testCommandLineOverrides() throws Exception {
    Application.main(new String[] {
        "--name=Gordon" });
    String output = this.outputCapture.toString();
    Assertions.assertThat(output).contains("Hello Gordon");
  }

}
