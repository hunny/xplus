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
import com.example.bootweb.pg.domain.AboutRepository;
import com.example.bootweb.pg.jdbcdao.AboutDao;
import com.example.bootweb.pg.jdbcdao.UuidBean;

@RestController
@RequestMapping(value = "/about", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AboutRepository aboutRepository;

  @Autowired
  private AboutDao aboutDao;

  @GetMapping("/")
  public ResponseEntity<String> getAbout() {

    logger.info("Receive about request.");
    return new ResponseEntity<String>("ABC", HttpStatus.OK);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<About> get(@PathVariable("id") Long id) {
    return new ResponseEntity<>(aboutDao.get(id), HttpStatus.OK);
  }

  @GetMapping("/get/by/{id}")
  public ResponseEntity<About> getById(@PathVariable("id") Long id) {

    About about = aboutRepository.getOne(id);
    HttpStatus status = HttpStatus.OK;
    if (null == about) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(about, status);
  }

  @GetMapping("/find/by/{name}")
  public ResponseEntity<List<About>> findByName(@PathVariable("name") String name) {

    List<About> abouts = aboutRepository.findByName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

  @GetMapping("/list/by/{name}")
  public ResponseEntity<List<About>> listByName(@PathVariable("name") String name) {

    List<About> abouts = aboutRepository.listByName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

  @GetMapping("/list/like/{name}")
  public ResponseEntity<List<About>> listLikeName(@PathVariable("name") String name) {

    List<About> abouts = aboutRepository.listLikeName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

  @GetMapping("/list/id/like/{name}")
  public ResponseEntity<List<BigInteger>> listIdLikeName(@PathVariable("name") String name) {

    List<BigInteger> abouts = aboutRepository.listIdLikeName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

  @GetMapping("/list/bean/like/{name}")
  public ResponseEntity<List<UuidBean>> listBeanLikeName(@PathVariable("name") String name) {

    List<UuidBean> abouts = aboutDao.listBeanLikeName(name);
    HttpStatus status = HttpStatus.OK;
    if (null == abouts || abouts.isEmpty()) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(abouts, status);
  }

}
