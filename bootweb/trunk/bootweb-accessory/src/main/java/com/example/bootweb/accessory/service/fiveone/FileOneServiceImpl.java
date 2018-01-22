package com.example.bootweb.accessory.service.fiveone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.bootweb.accessory.api.Param;
import com.example.bootweb.accessory.api.fiveone.FiveOneService;
import com.example.bootweb.accessory.builder.CustomHeaderBuilder;
import com.example.bootweb.accessory.builder.StringHttpBuilder;

@Service
public class FileOneServiceImpl implements FiveOneService {

  @Autowired
  private Optional<CloseableHttpClient> httpClientService;

  @Override
  public List<String> listBy(String keyword) {
    Assert.notNull(keyword, "keyword");
    
    if (!httpClientService.isPresent()) {
      throw new IllegalArgumentException("httpClient is null");
    }
    CloseableHttpClient httpClient = httpClientService.get();
    List<Param> params = new ArrayList<>();
    params.add(new Param("key", keyword));
    
    List<Param> headers = CustomHeaderBuilder.newBuilder()//
        .build();

    String result = StringHttpBuilder.newBuilder()//
        .httpClient(httpClient) //
        .uri("https://m.tianyancha.com/search") //
        .params(params)//
        .header(headers) //
        .build(); //
    
    String [] arr = new FileOneParser().parse(result);
    
    return Arrays.asList(arr);
  }

}
