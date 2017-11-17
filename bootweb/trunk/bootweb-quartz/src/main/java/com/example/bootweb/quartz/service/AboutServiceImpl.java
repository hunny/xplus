package com.example.bootweb.quartz.service;

import org.springframework.stereotype.Service;

@Service
public class AboutServiceImpl implements AboutService {

  @Override
  public String about() {
    return "bootweb about.";
  }

}
