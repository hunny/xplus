package com.example.bootweb.pg.web;

import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.pg.domain.About;
import com.example.bootweb.pg.jdbcdao.UuidBean;
import com.example.bootweb.pg.service.AboutJdbcService;
import com.example.bootweb.pg.service.AboutJpaService;

@RestController
@RequestMapping(value = "/about", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AboutJpaService aboutJpaService;

  @Autowired
  private AboutJdbcService aboutJdbcService;

  @GetMapping("/")
  public ResponseEntity<String> getAbout() {
    logger.info("Receive about request.");
    return new ResponseEntity<String>("ABC", HttpStatus.OK);
  }

  @GetMapping("/jpa/get/by/{id}")
  public ResponseEntity<About> getByIdUsingJpa(@PathVariable("id") Long id) {

    About about = aboutJpaService.getById(id);
    HttpStatus status = HttpStatus.OK;
    if (null == about) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(about, status);
  }

  @GetMapping("/jpa/find/by/{name}")
  public ResponseEntity<List<About>> findByNameUsingJpa(@PathVariable("name") String name) {

    List<About> abouts = aboutJpaService.findByName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

  @GetMapping("/jpa/list/by/{name}")
  public ResponseEntity<List<About>> listByNameUsingJpa(@PathVariable("name") String name) {

    List<About> abouts = aboutJpaService.listByName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

  @GetMapping("/jpa/like/{name}")
  public ResponseEntity<List<About>> listLikeNameUsingJpa(@PathVariable("name") String name) {

    List<About> abouts = aboutJpaService.listLikeName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

  @GetMapping("/jpa/id/like/{name}")
  public ResponseEntity<List<BigInteger>> listIdLikeNameUsingJpa(
      @PathVariable("name") String name) {

    List<BigInteger> abouts = aboutJpaService.listIdLikeName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

  @GetMapping("/jdbc/get/{id}")
  public ResponseEntity<About> getByIdUsingJdbc(@PathVariable("id") Long id) {
    return new ResponseEntity<>(aboutJdbcService.get(id), HttpStatus.OK);
  }

  @GetMapping("/jdbc/like/{name}")
  public ResponseEntity<List<About>> listLikeNameUsingJdbc(@PathVariable("name") String name) {

    List<About> abouts = aboutJdbcService.listLikeName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

  @GetMapping("/jdbc/bean/like/{name}")
  public ResponseEntity<List<UuidBean>> listBeanLikeNameUsingJdbc(
      @PathVariable("name") String name) {

    List<UuidBean> abouts = aboutJdbcService.listBeanLikeName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

}
