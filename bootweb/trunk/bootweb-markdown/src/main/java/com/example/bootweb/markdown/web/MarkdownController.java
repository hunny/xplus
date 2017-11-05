package com.example.bootweb.markdown.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

@Controller
public class MarkdownController {

  @RequestMapping(value = "render")
  public String render(@RequestParam("name") String name, Model model) {
    model.addAttribute("title", name);
    try {
      model.addAttribute("body", renderFile(name));
    } catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("body", e.getMessage());
    }
    return "markdown";
  }

  private String renderFile(String name) throws IOException, FileNotFoundException {
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

    Node document = parser.parseReader(new FileReader(new File(name)));
    return renderer.render(document);
  }

}
