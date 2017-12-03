package com.example.bootweb.markdown.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bootweb.markdown.profile.MarkdownServletProfile;
import com.example.bootweb.markdown.service.MarkdownService;

@MarkdownServletProfile
public class MarkdownServlet extends HttpServlet {

  private static final long serialVersionUID = 778579109868163148L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private MarkdownService markdownService;

  public MarkdownServlet(MarkdownService markdownService) {
    this.markdownService = markdownService;
  }

  @Override
  protected void doGet(HttpServletRequest req, //
      HttpServletResponse resp) //
      throws ServletException, IOException {
    doPost(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, //
      HttpServletResponse resp) //
      throws ServletException, IOException {
    resp.setContentType("text/html;charset=UTF-8");
    resp.setCharacterEncoding("UTF-8");
    PrintWriter out = resp.getWriter();
    Map<String, Object> reqMap = new HashMap<>();
    reqMap.put("contextPath", req.getContextPath());
    reqMap.put("pathInfo", req.getPathInfo());
    reqMap.put("requestURI", req.getRequestURI());
    reqMap.put("servletPath", req.getServletPath());
    for (Map.Entry<String, Object> map : reqMap.entrySet()) {
      if (null == map.getValue()) {
        reqMap.put(map.getKey(), "");
      }
    }
    try {
      markdownService.makeMarkdownPage(reqMap, out);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      throw new IOException(e);
    }
  }
}
