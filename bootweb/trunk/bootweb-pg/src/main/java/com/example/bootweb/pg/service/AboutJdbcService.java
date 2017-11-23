package com.example.bootweb.pg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bootweb.pg.domain.About;
import com.example.bootweb.pg.jdbcdao.AboutDao;
import com.example.bootweb.pg.jdbcdao.UuidBean;

@Service
public class AboutJdbcService {

  @Autowired
  private AboutDao aboutDao;

  public About get(Long id) {
    return aboutDao.get(id);
  }

  public List<UuidBean> listBeanLikeName(String name) {

    List<UuidBean> abouts = aboutDao.listBeanLikeName(name);
    return abouts;
  }
  
  public List<About> listLikeName(String name) {

    List<About> abouts = aboutDao.listLikeName(name);
    return abouts;
  }

}
