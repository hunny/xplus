package com.example.bootweb.httpclient.service;

import org.springframework.stereotype.Service;

import com.example.bootweb.httpclient.api.AboutService;

@Service
public class AboutServiceImpl implements AboutService {

  @Override
  public String about() {
    return "My About";
  }

}
