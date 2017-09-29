package com.xplus.commons.junit.mockito.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

public class MockServletContextListener {
  private ServletContextListenerFake app;
  private ServletContext sc;

  @Before
  public void setUp() throws Exception {
    sc = mock(ServletContext.class);
    // 指定类似 Tomcat 的虚拟目录，若设置为 "" 表示 Root 根目录
    when(sc.getContextPath()).thenReturn("/zjtv");
    // 设置项目真实的目录，当前是 返回 一个特定的 目录，你可以不执行该步
    when(sc.getRealPath(anyString()))
        .thenReturn("C:\\project\\test\\WebContent" + "\\js");
    // 设置 /META-INF 目录，当前使用该目录来保存 配置
    when(sc.getRealPath("/META-INF")).thenReturn("C:\\project\\test\\WebContent\\META-INF");

    app = new ServletContextListenerFake();
  }

  @Test
  public void testContextInitialized() throws IOException, ServletException {
    ServletContextEvent sce = mock(ServletContextEvent.class);
    when(sce.getServletContext()).thenReturn(sc);
    app.contextInitialized(sce);
    assertNotNull(sce);
    assertTrue("App started OK!", true);
  }
}
