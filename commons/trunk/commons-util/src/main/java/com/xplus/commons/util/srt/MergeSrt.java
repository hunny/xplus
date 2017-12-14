package com.xplus.commons.util.srt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

public class MergeSrt {

  public static void main(String[] args) throws Exception {

    File file = new File("C:/youtube-dl/srt");
    File[] files = file.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        String name = pathname.getName();
        return name.endsWith(".srt") && name.startsWith("(English)");
      }
    });

    String targetFloder = "c:/target/";

    new File(targetFloder).mkdirs();
    
    for (File f : files) {
      String src = f.getAbsolutePath();
      String dest = src.replaceAll("\\(English\\)", "");
      System.out.println(src);
      System.out.println(dest);

      Map<Integer, Subtitle> source = readSubtitle(src);
      Map<Integer, Subtitle> target = readSubtitle(dest);

      for (Map.Entry<Integer, Subtitle> t : target.entrySet()) {
        Subtitle value = t.getValue();
        Subtitle sub = source.get(t.getKey());
        if (null == sub) {
          continue;
        }
        if (sub.getTitle().size() == 1) {
          sub.getTitle().add(value.getTitle().get(0));
        }
      }

      List<Integer> keys = new ArrayList<>(source.keySet());

      Collections.sort(keys);

      File targetFile = new File(targetFloder + f.getName().replaceAll("\\(English\\)", ""));
      BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
      for (Integer key : keys) {
        Subtitle sub = source.get(key);
        writer.write(sub.getNum());
        writer.write("\r\n");
        writer.write(sub.getTime());
        writer.write("\r\n");
        for (String str : sub.getTitle()) {
          writer.write(str);
          writer.write("\r\n");
        }
        writer.write("\r\n");
      }
      writer.flush();
      writer.close();
    }

  }

  public static Map<Integer, Subtitle> readSubtitle(String target) {
    List<String> list = fileToList(target);
    List<Subtitle> titles = new LinkedList<>();
    int i = 0;
    while (i < list.size()) {
      Subtitle subtitle = new Subtitle();
      String str = null;
      do {
        str = list.get(i);
        subtitle.getTitle().add(str);
        i++;
      } while (i < list.size() //
          && !StringUtils.isEmpty(str = list.get(i)));
      i++;
      if (!subtitle.getTitle().isEmpty()) {
        titles.add(subtitle);
      }
    }
    System.out.println("list size:" + list.size() + ", subtitle size:" + titles.size());
    Map<Integer, Subtitle> result = new HashMap<Integer, Subtitle>();
    int index = 0;
    for (Subtitle subtitle : titles) {
      Subtitle tmp = new Subtitle();
      tmp.setIndex(index);
      tmp.setNum(subtitle.getTitle().get(0));
      tmp.setTime(subtitle.getTitle().get(1));
      tmp.setTitle(subtitle.getTitle().subList(2, subtitle.getTitle().size()));
      result.put(tmp.getIndex(), tmp);
      index++;
    }
    System.out.println(result.size());
    return result;
  }

  public static class Subtitle {
    private Integer index;
    private String num;
    private String time;
    private final List<String> title = new ArrayList<>();

    public String getNum() {
      return num;
    }

    public void setNum(String num) {
      this.num = num;
    }

    public Integer getIndex() {
      return index;
    }

    public void setIndex(Integer index) {
      this.index = index;
    }

    public String getTime() {
      return time;
    }

    public void setTime(String time) {
      this.time = time;
    }

    public List<String> getTitle() {
      return title;
    }

    public void setTitle(List<String> title) {
      this.title.clear();
      if (null != title) {
        this.title.addAll(title);
      }
    }

    @Override
    public String toString() {
      return "Subtitle [index=" + index + ", time=" + time + ", title=" + title + "]";
    }

  }

  public static List<String> fileToList(String args) {
    List<String> list = new LinkedList<>();
    File file = new File(args);
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String tempString = null;
      // 一次读入一行，直到读入null为文件结束
      while ((tempString = reader.readLine()) != null) {
        list.add(tempString);
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
    return list;
  }

}
