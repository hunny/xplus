package com.example.bootweb.accessory.runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.bootweb.accessory.api.Param;
import com.example.bootweb.accessory.builder.CustomHeaderBuilder;
import com.example.bootweb.accessory.builder.MobileHeaderBuilder;
import com.example.bootweb.accessory.builder.StringHttpBuilder;
import com.example.bootweb.accessory.profile.ZhaopinProfile;

@ZhaopinProfile
@Component
public class ZhaopinRunner implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Optional<CloseableHttpClient> httpClientService;

  @Value("${zhaopin.url:http://sou.zhaopin.com/jobs/searchresult.ashx?jl=上海&kw=外贸经理&sm=0&sg=34e3a36494fe46f893721c16d141df95&p={0}}")
  private String url;

  @Value("${tianyancha.url:https://m.tianyancha.com/}")
  private String tianyanchaurl;

  @Value("${zhaopin.excel:/Users/hunnyhu/Downloads/topease/{0}.xlsx}")
  private String excelPath;

  @Value("${zhaopin.timeout:2000}")
  private int random;

  @Override
  public void run(String... args) throws Exception {
    if (StringUtils.isEmpty(excelPath)) {
      throw new RuntimeException("请配置Excel的地址：-Dzhaopin.excel");
    }
    if (StringUtils.isEmpty(url)) {
      throw new RuntimeException("请配置zhaopin的地址：-Dzhaopin.url");
    }
    logger.info("招聘。");
    if (!httpClientService.isPresent()) {
      throw new IllegalArgumentException("httpClient is null");
    }
    CloseableHttpClient httpClient = httpClientService.get();
    for (int i = 1; i <= 100; i++) {
      List<ZhaopinBean> list = zhaopin(httpClient, i);
      for (ZhaopinBean bean : list) {
        String url = tianyancha(httpClient, bean.getName());
        logger.info("招聘: {}", url);
        if (null != url) {
          bean.setUrl(url);
          Thread.sleep(new Random().nextInt(random));
          bean.setInfo(tianyanchaDetail(httpClient, url));
        }
      }
      if (!list.isEmpty()) {
        writer(excelPath, list, i);
      }
    }
    logger.info(MessageFormat.format("信息已经保存到文件{0}中。", excelPath));
    System.exit(0);
  }

  protected String tianyanchaDetail(CloseableHttpClient httpClient, String url) {
    StringBuilder myurl = new StringBuilder();
    myurl.append(tianyanchaurl);
    myurl.append(url);
    List<Param> headers = MobileHeaderBuilder.newBuilder()//
        .build();

    String result = StringHttpBuilder.newBuilder()//
        .httpClient(httpClient) //
        .uri(myurl.toString()) //
        .params(Collections.emptyList())//
        .header(headers) //
        .build(); //
    Document doc = Jsoup.parse(result);
    Elements elements = doc.select(".company-mobile-subcontent .title-right417");
    StringBuffer buffer = new StringBuffer();
    if (null != elements) {
      for (Element elem : elements) {
        String text = elem.text();
        if (!StringUtils.isEmpty(text)) {
          buffer.append(text);
          buffer.append(", ");
        }
      }
    }
    if (buffer.length() != 0) {
      return buffer.toString();
    }
    return null;
  }

  protected String tianyancha(CloseableHttpClient httpClient, String name) {
    StringBuilder myurl = new StringBuilder();
    myurl.append(tianyanchaurl);
    myurl.append("/search?key=");
    myurl.append(name);
    myurl.append("&checkFrom=searchBox");
    String[] urlArr = myurl.toString().split("\\?");
    String[] arrs = urlArr[1].split("&");
    List<Param> params = new ArrayList<>();
    for (String arr : arrs) {
      String[] tmp = arr.split("=");
      if (tmp.length == 2) {
        params.add(new Param(tmp[0], tmp[1]));
      }
    }
    List<Param> headers = MobileHeaderBuilder.newBuilder()//
        .build();

    String result = StringHttpBuilder.newBuilder()//
        .httpClient(httpClient) //
        .uri(urlArr[0]) //
        .params(params)//
        .header(headers) //
        .build(); //
    Document doc = Jsoup.parse(result);
    Element element = doc.select(".row.ml0.mr0 a").first();
    if (null != element) {
      return element.attr("href");
    }
    return null;
  }

  protected List<ZhaopinBean> zhaopin(CloseableHttpClient httpClient, int index) {
    String[] urlArr = MessageFormat.format(url, index).split("\\?");
    String[] arrs = urlArr[1].split("&");
    List<Param> params = new ArrayList<>();
    for (String arr : arrs) {
      String[] tmp = arr.split("=");
      if (tmp.length == 2) {
        params.add(new Param(tmp[0], tmp[1]));
      }
    }
    List<Param> headers = CustomHeaderBuilder.newBuilder()//
        .build();

    String result = StringHttpBuilder.newBuilder()//
        .httpClient(httpClient) //
        .uri(urlArr[0]) //
        .params(params)//
        .header(headers) //
        .build(); //
    Document doc = Jsoup.parse(result);
    Elements elements = doc.select(".gsmc > a");
    List<ZhaopinBean> list = new ArrayList<>();
    for (Element elem : elements) {
      String name = elem.text();
      if (StringUtils.isEmpty(name)) {
        continue;
      }
      logger.info(name);
      ZhaopinBean zhapin = new ZhaopinBean(name);
      list.add(zhapin);
    }
    return list;
  }

  @SuppressWarnings("resource")
  public static void writer(String excelPath, //
      List<ZhaopinBean> list, int index) throws IOException {
    Workbook wb = null;
    File file = new File(MessageFormat.format(excelPath, index));
    Sheet sheet = null;
    if (excelPath.endsWith("xls")) {
      wb = new HSSFWorkbook();
    } else if (excelPath.endsWith("xlsx")) {
      wb = new XSSFWorkbook();
    } else {
      throw new RuntimeException("文件格式不正确");
    }
    // 创建工作文档对象
    if (!file.exists()) {
      // 创建sheet对象
      sheet = (Sheet) wb.createSheet("sheet1");
      OutputStream outputStream = new FileOutputStream(excelPath);
      wb.write(outputStream);
      outputStream.flush();
      outputStream.close();
    } else {
      throw new RuntimeException(//
          MessageFormat.format("文件{0}已经存在。", excelPath));
    }
    // 创建sheet对象
    if (sheet == null) {
      sheet = (Sheet) wb.createSheet("sheet1");
    }
    // 循环写入行数据
    for (int i = 0; i < list.size(); i++) {
      Row row = (Row) sheet.createRow(i);
      row.createCell(0).setCellValue((list.get(i)).getName());
      row.createCell(1).setCellValue((list.get(i)).getUrl());
      row.createCell(2).setCellValue((list.get(i)).getInfo());
    }

    // 创建文件流
    OutputStream stream = new FileOutputStream(excelPath);
    // 写入数据
    wb.write(stream);
    // 关闭文件流
    stream.close();
  }

}
