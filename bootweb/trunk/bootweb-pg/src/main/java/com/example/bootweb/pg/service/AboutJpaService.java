package com.example.bootweb.pg.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bootweb.pg.domain.About;
import com.example.bootweb.pg.domain.AboutRepository;

@Service
public class AboutJpaService {

  @Autowired
  private AboutRepository aboutRepository;

  public About getById(Long id) {

    About abt = aboutRepository.getOne(id);

    if (null == abt.getId()) {
      return null;
    }

    About about = new About();

    BeanUtils.copyProperties(abt, about);

    return about;
  }

  public List<About> findByName(String name) {

    List<About> abouts = aboutRepository.findByName(name);
    return abouts;
  }

  public List<About> listByName(String name) {

    List<About> abouts = aboutRepository.listByName(name);
    return abouts;
  }

  public List<About> listLikeName(String name) {

    List<About> abouts = aboutRepository.listLikeName(name);
    return abouts;
  }

  public List<BigInteger> listIdLikeName(String name) {

    List<BigInteger> abouts = aboutRepository.listIdLikeName(name);
    return abouts;
  }

}
