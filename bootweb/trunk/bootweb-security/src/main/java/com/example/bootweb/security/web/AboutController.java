package com.example.bootweb.security.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.security.about.ApplicationAbout;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @GetMapping("/about")
  public ResponseEntity<ApplicationAbout> getAbout() {
    logger.info("Receive about request.");
    return new ResponseEntity<ApplicationAbout>(ApplicationAbout.get(getClass()), HttpStatus.OK);
  }

  @GetMapping("/about/{id}")
  public ResponseEntity<Map> getAboutId(@PathVariable("id") Integer id) {
    logger.info("Receive about request id {}.", id);
    Map<String, String> map = new HashMap<String, String>();
    map.put("id", String.valueOf(id));
    return new ResponseEntity<Map>(map, HttpStatus.valueOf(id));
  }
  
  @RequestMapping(value="/logout", method = RequestMethod.GET)
  public void logoutPage (HttpServletRequest request, HttpServletResponse response) throws IOException {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null){    
          new SecurityContextLogoutHandler().logout(request, response, auth);
      }
      response.setStatus(401);
      response.getWriter().write("{status:401}");
      response.getWriter().flush();
//      return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
  }

}
