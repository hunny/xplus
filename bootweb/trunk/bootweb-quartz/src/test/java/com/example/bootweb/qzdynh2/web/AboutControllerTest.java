package com.example.bootweb.qzdynh2.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.bootweb.quartz.Application;
import com.example.bootweb.quartz.service.AboutService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class AboutControllerTest {

  private MockMvc mvc;

  @Autowired
  private WebApplicationContext webApplicationConnect;

  @Autowired
  private AboutService aboutService;

  @Before
  public void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
  }

  @Test
  public void testAbout() throws Exception {

    String expectedResult = aboutService.about();
    String uri = "/about";
    MvcResult mvcResult = mvc //
        .perform(MockMvcRequestBuilders //
            .get(uri) //
            .accept(MediaType.APPLICATION_JSON)) //
        .andReturn();
    int status = mvcResult.getResponse().getStatus();
    String content = mvcResult.getResponse().getContentAsString();

    Assert.assertTrue("正确的返回值为200", status == 200);
    Assert.assertFalse("错误，正确的返回值为200", status != 200);
    Assert.assertTrue("数据一致", expectedResult.equals(content));
    Assert.assertFalse("数据不一致", !expectedResult.equals(content));

  }

}
