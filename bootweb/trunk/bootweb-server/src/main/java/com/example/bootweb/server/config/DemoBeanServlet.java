package com.example.bootweb.server.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bootweb.server.profile.BeanServletProfile;
import com.fasterxml.jackson.databind.ObjectMapper;

@BeanServletProfile
public class DemoBeanServlet extends HttpServlet {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final long serialVersionUID = -8685285401859800066L;

  @SuppressWarnings("unused")
  private ObjectMapper objectMapper;
  
  public DemoBeanServlet(ObjectMapper objectMapper) {
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
    out.println("<h1>Hello, my Name is Servlet.</h1>");
    out.println("<p>ContextPath:" + req.getContextPath() + "</p>");
    out.println("<p>PathInfo:" + req.getPathInfo() + "</p>");
    out.println("<p>RequestURI:" + req.getRequestURI() + "</p>");
    out.println("<p>ServletPath:" + req.getServletPath() + "</p>");
    out.println("</body>");
    out.println("</html>");
  }

}
