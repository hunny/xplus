package com.xplus.commons.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat; 

public class ReadFileCreateTime {
  
  public static void main(String[] args) throws IOException {
    
    if (args.length == 0) {
      System.out.println("输入一个文件的目录，查看创建时间和修改时间。");
      return;
    }
    
    String basePath = args[0];
    File file = new File(basePath);
    if (!file.isDirectory()) {
      System.out.println(MessageFormat.format("[{0}]不是一个文件目录。", basePath));
      return;
    }
    
    check(file);
  }
  
  public static void check(File file) throws IOException {
    if (file.isDirectory()) {
      File [] mFile = file.listFiles();
      for (File f : mFile) {
        check(f);
      }
    } else {
      String fileName = file.getAbsolutePath();
      Path path = Paths.get(fileName);
      BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
      System.out.println("=====> File:" + fileName);
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      System.out.println("DateCreated: " + df.format(attr.creationTime().toMillis()));
      System.out.println("DateCreated: " + attr.creationTime());
      System.out.println("LastUpdated: " + attr.lastModifiedTime());
    }
  }

}
