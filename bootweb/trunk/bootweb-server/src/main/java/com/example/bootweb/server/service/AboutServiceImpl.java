package com.example.bootweb.server.service;

import org.springframework.stereotype.Service;

@Service
public class AboutServiceImpl implements AboutService {

  @Override
  public String about() {
    return "bootweb about.";
  }

}
