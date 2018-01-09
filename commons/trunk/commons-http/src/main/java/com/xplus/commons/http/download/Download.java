package com.xplus.commons.http.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download {

  public static final int CONNECT_TIMEOUT = 10 * 1000;
  public static final int READ_TIMEOUT = 5 * 1000;
  public static final String REQUEST_METHOD = "GET";
  public static final String CACHE_NAME = "\\1.cache";

  public static void mutilDownload(String path, String savePath) {
    HttpURLConnection conn = null;
    try {
      URL url = new URL(path);
      conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(CONNECT_TIMEOUT);
      conn.setRequestMethod(REQUEST_METHOD);
      conn.setReadTimeout(READ_TIMEOUT);
      int code = conn.getResponseCode();
      if (code == HttpURLConnection.HTTP_OK) {
        File file = new File(savePath //
            + "\\" + path.substring(path.lastIndexOf("/") + 1));
        long filelength = conn.getContentLength();
        RandomAccessFile randomFile = //
            new RandomAccessFile(file, "rwd");
        randomFile.setLength(filelength);
        randomFile.close();
        long endposition = filelength;
        new DownloadThread(path, savePath, endposition).start();
      }
    } catch (Exception e) {

    } finally {
      conn.disconnect();
    }
  }

}
