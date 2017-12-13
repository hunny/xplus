package com.example.bootweb.markdown.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.markdown.service.FileService;

@RestController
@RequestMapping(value = { "/video" })
public class VideoController {

  @Autowired
  private FileService fileService;

  @RequestMapping(value = "/file/list", method = RequestMethod.GET)
  public List<FileBean> list(@RequestParam(name = "path", required = false) String path) {
    if (StringUtils.isBlank(path)) {
      return Collections.emptyList();
    }
    List<FileBean> result = new ArrayList<>();
    List<String> list = fileService.list(path, true, ".mp4");
    for (String str : list) {
      File file = new File(str);
      String p = file.getAbsolutePath().replaceAll("\\\\", "/");
      if (!p.startsWith("/")) {
        p = "/" + p;
      }
      result.add(new FileBean(file.getName(), p));
    }
    return result;
  }

  /**
   * 视频流读取
   * 
   * @param id
   * @param response
   * @throws Exception
   */
  @RequestMapping("/play")
  public void playVideo(@RequestParam("path") String path, HttpServletResponse response) throws Exception {
    File file = new File(path);
    if (!file.exists()) {
      return;
    }
    FileInputStream in = new FileInputStream(file);
    ServletOutputStream out = response.getOutputStream();
    byte[] b = null;
    while (in.available() > 0) {
      if (in.available() > 10240) {
        b = new byte[10240];
      } else {
        b = new byte[in.available()];
      }
      in.read(b, 0, b.length);
      out.write(b, 0, b.length);
    }
    in.close();
    out.flush();
    out.close();
  }

}
