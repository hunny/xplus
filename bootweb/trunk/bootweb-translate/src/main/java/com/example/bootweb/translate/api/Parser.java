package com.example.bootweb.translate.api;

public interface Parser<S, R> {

  R parse(S s);
  
}
