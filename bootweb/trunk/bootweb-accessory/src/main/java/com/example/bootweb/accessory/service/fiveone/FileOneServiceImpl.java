package com.example.bootweb.accessory.service.fiveone;

import java.util.List;
import java.util.Optional;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.bootweb.accessory.api.fiveone.FiveOneService;

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

    return new FiveOneDirector(httpClient) //
        .keyword(keyword) //
        .pageno("1") //
        .build();
  }

}
