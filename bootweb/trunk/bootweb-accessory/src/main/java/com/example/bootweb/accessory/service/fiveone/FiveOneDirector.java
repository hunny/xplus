package com.example.bootweb.accessory.service.fiveone;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.springframework.util.Assert;

import com.example.bootweb.accessory.api.Param;
import com.example.bootweb.accessory.builder.CustomHeaderBuilder;
import com.example.bootweb.accessory.builder.StringHttpBuilder;

public class FiveOneDirector {

  private String keyword;
  private String pageno;
  private HttpClient httpClient;
  
  public FiveOneDirector(HttpClient httpClient) {
    this.httpClient = httpClient;
  }
  
  public FiveOneDirector keyword(String keyword) {
    this.keyword = keyword;
    return this;
  }
  
  public FiveOneDirector pageno(String pageno) {
    this.pageno = pageno;
    return this;
  }
  
  public List<String> build() {
    Assert.notNull(httpClient, "httpClient");
    Assert.notNull(keyword, "keyword");
    Assert.notNull(pageno, "pageno");
    
    List<Param> params = new ArrayList<>();
    params.add(new Param("keyword", keyword));
    params.add(new Param("jobarea", "020000"));
    params.add(new Param("keywordtype", "2"));
    params.add(new Param("pageno", pageno));
    
    List<Param> headers = CustomHeaderBuilder.newBuilder()//
        .build();

    String result = StringHttpBuilder.newBuilder()//
        .httpClient(httpClient) //
        .uri("http://m.51job.com/search/joblist.php") //
        .params(params)//
        .header(headers) //
        .build(); //
    FileOneParser fileOneParser = new FileOneParser();
    List<String> list = fileOneParser.parse(result);
    System.out.println(fileOneParser.getNext());
    return list;
  }
  
}
