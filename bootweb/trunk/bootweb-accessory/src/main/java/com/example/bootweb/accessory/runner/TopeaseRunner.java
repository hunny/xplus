package com.example.bootweb.accessory.runner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.bootweb.accessory.dao.InsertBuilder;
import com.example.bootweb.accessory.dao.SqlResult;
import com.example.bootweb.accessory.profile.TopeaseProfile;

@TopeaseProfile
@Component
public class TopeaseRunner implements CommandLineRunner {

  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  @Value("${session.id:}")
  private String sessionId;
  
  @Value("${oa.domain:}")
  private String domain;
  
  @Value("${oa.list.url:}")
  private String listurl;
  
  @Value("${oa.detail.url:}")
  private String detailurl;

  @Override
  public void run(String... args) throws Exception {
    BasicCookieStore cookieStore = new BasicCookieStore();
    BasicClientCookie cookie = new BasicClientCookie("ASP.NET_SessionId", //
        sessionId);
    cookie.setDomain(domain);
    cookie.setPath("/");
    cookieStore.addCookie(cookie);
    CloseableHttpClient httpclient = HttpClients //
        .custom() //
        .setDefaultCookieStore(cookieStore) //
        .build();
    for (int i = 1; i <= (100 / 20 + 1); i++) {
      customs(i, httpclient);
    }
    System.exit(0);
  }

  public void parseList(String html, CloseableHttpClient httpclient) throws Exception {
    Document doc = Jsoup.parse(html, "UTF-8");
    Elements elems = doc.select("tr[onclick]");
    if (elems.isEmpty()) {
      System.out.println("没有列表数据。");
      return;
    }
    for (Element elem : elems) {
      Elements tds = elem.select("td");
      String id = tds.get(0).select("input").first().attr("value").trim();
      String name = tds.get(1).text().trim();
      String refname = tds.get(2).text().trim();
      String times = tds.get(6).text().trim();
      System.out.print(id);
      System.out.print(" | ");
      System.out.print(name);
      System.out.print(" | ");
      System.out.print(refname);
      System.out.print(" | ");
      System.out.print(times);
      System.out.println();

      List<String> companys = jdbcTemplate.query( //
          "select name from company where name = ?", //
          new Object[] { //
              name //
          }, new SingleColumnRowMapper<String>(String.class));
      if (companys.isEmpty()) {
        InsertBuilder builder = InsertBuilder.newBuilder(); //
        builder.table("company");//
        builder.addValue("name", name); //
        if (!StringUtils.isEmpty(refname)) {
          builder.addValue("refname", refname); //
        }
        if (!StringUtils.isEmpty(times)) {
          builder.addValue("times", times); //
        }
        SqlResult result = builder.build();
        jdbcTemplate.update(result.getSql(), //
            result.getParams().toArray(new Object[] {}));
        details(id, name, httpclient);
      }

    }
  }

  public void parseDetail(String id, String company, String html) throws Exception {
    Document doc = Jsoup.parse(html, "UTF-8");
    Elements elems = doc.select("tbody > tr");
    if (elems.isEmpty()) {
      System.out.println("没有详细列表数据。");
      return;
    }
    for (Element elem : elems) {
      Elements tds = elem.select("td");
      String type = tds.get(1).text().trim();
      String name = tds.get(2).text().trim();
      String caller = tds.get(3).text().trim();
      String title = tds.get(4).text().trim();
      String phone = tds.get(5).text().trim();
      String mobile = tds.get(6).text().trim();
      String email = tds.get(7).text().trim();
      String fax = tds.get(8).text().trim();
      String address = tds.get(9).text().trim();
      String remark = tds.get(10).text().trim();
      System.out.print(type);
      System.out.print(" | ");
      System.out.print(name);
      System.out.print(" | ");
      System.out.print(caller);
      System.out.print(" | ");
      System.out.print(title);
      System.out.print(" | ");
      System.out.print(phone);
      System.out.print(" | ");
      System.out.print(mobile);
      System.out.print(" | ");
      System.out.print(email);
      System.out.print(" | ");
      System.out.print(fax);
      System.out.print(" | ");
      System.out.print(address);
      System.out.print(" | ");
      System.out.print(remark);
      System.out.println();

      List<String> companys = jdbcTemplate.query( //
          "select name from company where name = ?", //
          new Object[] { //
              name //
          }, new SingleColumnRowMapper<String>(String.class));
      if (companys.isEmpty()) {
        InsertBuilder builder = InsertBuilder.newBuilder(); //
        builder.table("contactor");//
        if (!StringUtils.isEmpty(type)) {
          builder.addValue("type", type); //
        }
        if (!StringUtils.isEmpty(company)) {
          builder.addValue("company", company); //
        }
        if (!StringUtils.isEmpty(name)) {
          builder.addValue("name", name); //
        }
        if (!StringUtils.isEmpty(caller)) {
          builder.addValue("caller", caller); //
        }
        if (!StringUtils.isEmpty(title)) {
          builder.addValue("title", title); //
        }
        if (!StringUtils.isEmpty(phone)) {
          builder.addValue("phone", phone); //
        }
        if (!StringUtils.isEmpty(mobile)) {
          builder.addValue("mobile", mobile); //
        }
        if (!StringUtils.isEmpty(email)) {
          builder.addValue("email", email); //
        }
        if (!StringUtils.isEmpty(fax)) {
          builder.addValue("fax", fax); //
        }
        if (!StringUtils.isEmpty(address)) {
          builder.addValue("address", address); //
        }
        if (!StringUtils.isEmpty(remark)) {
          builder.addValue("remark", remark); //
        }
        SqlResult result = builder.build();
        jdbcTemplate.update(result.getSql(), //
            result.getParams().toArray(new Object[] {}));
      }

    }
  }

  protected void customs(int num, CloseableHttpClient httpclient)
      throws URISyntaxException, IOException, ClientProtocolException {
    System.err.println("num=" + num);
    HttpUriRequest login = getBuilder() //
        .setUri(new URI(listurl)) //
        .addParameter("pageNum", String.valueOf(num)) //
        .addParameter("pageSize", String.valueOf(20)) //
        .addParameter("orderField", "Id") //
        .addParameter("orderDirection", "desc") //
        .addParameter("recordCount", String.valueOf(2117)) //
        .addParameter("customerid", "") //
        .addParameter("options", "Contactor") //
        .addParameter("statusid", "3") //
        .addParameter("optionName", "companyname") //
        .addParameter("optionValue", "") //
        .addParameter("ServiceEndDate", "") //
        .addParameter("_", String.valueOf(new Date().getTime())) //
        .build();
    CloseableHttpResponse checkresponse = httpclient.execute(login);
    try {
      HttpEntity entity = checkresponse.getEntity();
      String str = EntityUtils.toString(entity);
//      System.out.println(str);
      parseList(str, httpclient);
      EntityUtils.consume(entity);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void details(String id, String company, CloseableHttpClient httpclient)
      throws URISyntaxException, IOException, ClientProtocolException {
    HttpUriRequest login = postBuilder() //
        .setUri(new URI(detailurl)) //
        .addParameter("addFlag", "%201") //
        .addParameter("customerid", id) //
        .addParameter("options", "Contactor") //
        .addParameter("statusid", "3") //
        .addParameter("optionName", "companyname") //
        .addParameter("_", String.valueOf(new Date().getTime())) //
        .build();
    CloseableHttpResponse checkresponse = httpclient.execute(login);
    try {
      HttpEntity entity = checkresponse.getEntity();
      parseDetail(id, company, EntityUtils.toString(entity));
      EntityUtils.consume(entity);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void personalRecord(CloseableHttpClient httpclient)
      throws URISyntaxException, IOException, ClientProtocolException {
    HttpUriRequest login = postBuilder() //
        .setUri(new URI("http://oa.topease.cn/Finance/PersonalPer.bee")) //
        .addParameter("_", String.valueOf(new Date().getTime())) //
        .build();
    CloseableHttpResponse checkresponse = httpclient.execute(login);
    try {
      HttpEntity entity = checkresponse.getEntity();
      Document doc = Jsoup.parse(EntityUtils.toString(entity), "UTF-8");
      doc.select("");
      SqlResult result = InsertBuilder.newBuilder() //
          .addValue("", "") //
          .build();
      jdbcTemplate.update(result.getSql(), //
          result.getParams().toArray(new Object[] {}));
      EntityUtils.consume(entity);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void userCheck(CloseableHttpClient httpclient)
      throws URISyntaxException, IOException, ClientProtocolException {
    HttpUriRequest login = postBuilder() //
        .addHeader(new BasicHeader("Content-Length", //
            "0")) //
        .setUri(new URI("http://oa.topease.cn/Users/CheckUsersInfo.bee")) //
        .build();
    CloseableHttpResponse checkresponse = httpclient.execute(login);
    try {
      HttpEntity entity = checkresponse.getEntity();
      System.out.println("Login form get: " + checkresponse.getStatusLine());
      System.out.println(EntityUtils.toString(entity));
      EntityUtils.consume(entity);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected RequestBuilder postBuilder() {
    RequestBuilder builder = RequestBuilder //
        .post(); //
    List<Header> headers = headers();
    for (Header header : headers) {
      builder.addHeader(header);//
    }
    return builder;
  }

  protected RequestBuilder getBuilder() {
    RequestBuilder builder = RequestBuilder //
        .get(); //
    List<Header> headers = headers();
    for (Header header : headers) {
      builder.addHeader(header);//
    }
    return builder;
  }

  protected List<Header> headers() {
    Header userAgent = new BasicHeader("User-Agent", //
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
    Header xPoweredBy = new BasicHeader("X-Powered-By", //
        "ASP.NET");
    List<Header> headers = new ArrayList<>();
    headers.add(userAgent);
    headers.add(xPoweredBy);
    headers.add(new BasicHeader("Accept", //
        "application/json, text/javascript, */*; q=0.01"));
    headers.add(new BasicHeader("Accept-Encoding", //
        "gzip, deflate"));
    headers.add(new BasicHeader("Accept-Language", //
        "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6"));
    headers.add(new BasicHeader("Origin", //
        "http://oa.topease.cn"));
    headers.add(new BasicHeader("Referer", //
        "http://oa.topease.cn/Index.bee"));
    headers.add(new BasicHeader("Proxy-Connection", //
        "keep-alive"));
    headers.add(new BasicHeader("X-Requested-With", //
        "XMLHttpRequest"));
    return headers;
  }

}
