package com.example.bootweb.qzdynh2.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.bootweb.quartz.Application;
import com.example.bootweb.quartz.service.AboutService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class) // 指定spring-boot的启动类
@WebAppConfiguration
public class AboutServiceTest {

  @Autowired
  private AboutService aboutService;

  @Test
  public void testAbout() {

    String expected = "bootweb about.";
    String actual = aboutService.about();
    Assert.assertTrue("数据一致", expected.equals(actual));
    Assert.assertFalse("数据不一致", !expected.equals(actual));

  }
}