package com.xplus.commons.http.chrome.headless;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class HelloWorld {

  public static void main(String[] args) {
    
    Launcher launcher = new Launcher();

//     C:\Users\User\AppData\Local\Google\Chrome SxS\Application>chrome --headless --disable-gpu --remote-debugging-port=9222 https://www.baidu.com
    try (SessionFactory factory = launcher.launch(); //
        Session session = factory.create()) {

      session.navigate("https://www.baidu.com");
      session.waitDocumentReady();
      String content = (String) session.getProperty("//body", "outerText");
      System.out.println(content);

    }
  }
}
