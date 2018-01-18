package com.example.bootweb.accessory.api;

/**
 * 
 * @param <S> 处理源
 * @param <R> 处理结果
 */
public interface Parser<S, R> {

  R parse(S source);
  
}
