package com.example.bootweb.qzdynh2.service;

import org.springframework.stereotype.Service;

@Service
public class AboutServiceImpl implements AboutService {

  @Override
  public String about() {
    return "bootweb about.";
  }

}
