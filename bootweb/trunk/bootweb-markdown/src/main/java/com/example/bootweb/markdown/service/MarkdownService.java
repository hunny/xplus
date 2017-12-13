package com.example.bootweb.markdown.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class MarkdownService {

  @Autowired
  private Configuration cfg;

  public void makeMarkdownPage(Map<String, Object> reqMap, Writer out) throws Exception {
    Map<String, Object> root = new HashMap<>();
    root.put("request", reqMap);
    Template template = cfg.getTemplate("markdown.html");
    template.process(root, out);
  }

  public String markdownToHtml(InputStream inputStream) throws IOException {
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
  
}
