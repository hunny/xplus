package com.example.bootweb.markdown.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;

@Controller
public class MarkdownController {
  
  private final Logger logger = LoggerFactory.getLogger(MarkdownController.class);

  // The Environment object will be used to read parameters from the
  // application.properties configuration file
  @Autowired
  private Environment env;

  @RequestMapping(value = "render")
  public String render(@RequestParam("name") String name, Model model) {
    model.addAttribute("title", name);
    try {
      logger.info("Markdown file: {}", name);
      model.addAttribute("body", renderFile(new File(name)));
    } catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("body", e.getMessage());
    }
    return "markdown";
  }

  /**
   * 文件上传具体实现方法;
   * 
   * @param file
   * @return
   */
  @RequestMapping("/upload")
  public String upload(@RequestParam("file") MultipartFile uploadfile, Model model) {
    if (!uploadfile.isEmpty()) {
      try {
        // Get the filename and build the local file path
        String filename = uploadfile.getOriginalFilename();
        logger.info("filename: {}", filename);
        String directory = env.getProperty("java.io.tmpdir");
        logger.info("directory: {}", directory);
        String filepath = Paths.get(directory, filename).toString();
        logger.info("filepath: {}", filepath);

        // Save the file locally
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
        stream.write(uploadfile.getBytes());
        stream.close();
        
        File uploadFile = new File(filepath);
        model.addAttribute("title", uploadFile.getName());
        model.addAttribute("body", renderFile(uploadFile));
        logger.info("Delete upload file: {}", uploadFile.delete());
      } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("body", "上传失败," + e.getMessage());
      }
    } else {
      model.addAttribute("body", "上传失败，因为文件是空的.");
    }
    return "markdown";
  }

  private String renderFile(File file) throws IOException, FileNotFoundException {
    MutableDataSet options = new MutableDataSet();

    options.setFrom(ParserEmulationProfile.MARKDOWN);
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

    Node document = parser.parseReader(new FileReader(file));
    return renderer.render(document);
  }

}
