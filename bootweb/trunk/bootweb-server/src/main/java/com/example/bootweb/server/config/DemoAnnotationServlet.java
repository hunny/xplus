package com.example.bootweb.server.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bootweb.server.profile.AnnotationServletProfile;
import com.fasterxml.jackson.databind.ObjectMapper;

@AnnotationServletProfile
//不指定name的情况下，name默认值为类全路径
@WebServlet(urlPatterns="/demo/myservlet", description="Servlet的说明") 
public class DemoAnnotationServlet extends HttpServlet {

  private static final long serialVersionUID = 8582439831825090991L;

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @SuppressWarnings("unused")
  private ObjectMapper objectMapper;
  
  public DemoAnnotationServlet(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    logger.info(">>>>>>>>>>doGet()<<<<<<<<<<<");
    doPost(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    logger.info(">>>>>>>>>>doPost()<<<<<<<<<<<");
    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Hello World</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>Hello, my Name is WebServlet.</h1>");
    out.println("<p>ContextPath:" + req.getContextPath() + "</p>");
    out.println("<p>PathInfo:" + req.getPathInfo() + "</p>");
    out.println("<p>RequestURI:" + req.getRequestURI() + "</p>");
    out.println("<p>ServletPath:" + req.getServletPath() + "</p>");
    out.println("</body>");
    out.println("</html>");
  }

}
