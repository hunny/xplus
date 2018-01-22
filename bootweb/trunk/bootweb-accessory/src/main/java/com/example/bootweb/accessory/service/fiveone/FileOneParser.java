package com.example.bootweb.accessory.service.fiveone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.example.bootweb.accessory.api.Parser;

public class FileOneParser implements Parser<String, List<String>> {

  public static final String BEAN_ID = FileOneParser.class.getName();

  protected final Pattern PAT = Pattern.compile(".*pageno=(\\d+)$");

  private String next = null;

  public String getNext() {
    return next;
  }

  @Override
  public List<String> parse(String html) {
    if (null == html) {
      next = null;
      return Collections.emptyList();
    }
    Document doc = Jsoup.parse(html);
    Elements elements = doc.select("div.items aside");
    List<String> list = new ArrayList<>();
    for (Element elem : elements) {
      String name = elem.text();
      if (StringUtils.isEmpty(name)) {
        continue;
      }
      list.add(name);
    }
    if (list.isEmpty()) {
      next = null;
    } else {
      Element nextElement = doc.selectFirst("form#turnpage a.next");
      if (null != nextElement) {
        System.out.println(nextElement.attr("href"));
        Matcher mat = PAT.matcher(nextElement.attr("href"));
        if (mat.find()) {
          next = mat.group(1);
        }
      }
    }
    return list;
  }

}
