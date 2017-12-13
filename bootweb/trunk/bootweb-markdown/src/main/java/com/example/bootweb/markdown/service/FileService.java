package com.example.bootweb.markdown.service;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FileService {

  public List<String> list(String path, boolean absolute, final String suffix) {
    List<String> result = getSuffixFiles(path, suffix);
    if (absolute) {
      return result;
    }
    List<String> tmp = new LinkedList<>();
    for (String p : result) {
      tmp.add(p.replaceFirst(path, ""));
    }
    return tmp;
  }

  public List<String> getSuffixFiles(String rootPath, final String suffix) {
    List<String> result = new LinkedList<>();
    File file = new File(rootPath);
    File[] files = file.listFiles(new FileFilter() {
      @Override
      public boolean accept(File f) {
        if (f.isDirectory()) {
          return true;
        } else {
          if (f.getName().endsWith(suffix)) {
            return true;
          }
          return false;
        }
      }
    });
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        result.addAll(getSuffixFiles(files[i].getPath(), suffix));
      } else {
        result.add(files[i].getAbsolutePath());
      }
    }
    return result;
  }

}
