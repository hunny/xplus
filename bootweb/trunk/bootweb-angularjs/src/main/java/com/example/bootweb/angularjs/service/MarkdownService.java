package com.example.bootweb.angularjs.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

@Service
public class MarkdownService {

  public String render(InputStream inputStream) throws IOException {
    MutableDataSet options = new MutableDataSet();
    options.set(HtmlRenderer.INDENT_SIZE, 2);
    options.set(HtmlRenderer.RENDER_HEADER_ID, true);
    // uncomment to set optional extensions
    options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), //
        StrikethroughExtension.create(), //
        SimTocExtension.create(), //
        TypographicExtension.create()));

    // uncomment to convert soft-breaks to hard breaks
    options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

    Parser parser = Parser.builder(options).build();
    HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    Node document = parser.parseReader(new InputStreamReader(inputStream));
    return renderer.render(document);
  }

  public List<String> list(String path, boolean absolute) {
    List<String> result = getMarkdownFiles(path);
    if (absolute) {
      return result;
    }
    List<String> tmp = new LinkedList<>();
    for (String p : result) {
      tmp.add(p.replaceFirst(path, ""));
    }
    return tmp;
  }

  public List<String> getMarkdownFiles(String rootPath) {
    List<String> result = new LinkedList<>();
    File file = new File(rootPath);
    File[] files = file.listFiles(new FileFilter() {
      @Override
      public boolean accept(File f) {
        if (f.isDirectory()) {
          return true;
        } else {
          if (f.getName().endsWith(".md")) {
            return true;
          }
          return false;
        }
      }
    });
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        result.addAll(getMarkdownFiles(files[i].getPath()));
      } else {
        result.add(files[i].getAbsolutePath());
      }
    }
    return result;
  }

}
