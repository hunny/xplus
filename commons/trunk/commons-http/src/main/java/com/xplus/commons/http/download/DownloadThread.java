package com.xplus.commons.http.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {
  private String urlPath;
  private String savePath;
  private long lastPostion;
  private long endposition;

  public DownloadThread(String urlPath, String savePath, long endposition) {
    this.urlPath = urlPath;
    this.endposition = endposition;
    this.savePath = savePath;
  }

  @Override
  public void run() {
    HttpURLConnection conn = null;
    try {
      URL url = new URL(urlPath);
      conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(Download.CONNECT_TIMEOUT);
      conn.setRequestMethod(Download.REQUEST_METHOD);
      conn.setReadTimeout(Download.READ_TIMEOUT);
      long startposition = 0;
      File tempfile = new File(savePath + Download.CACHE_NAME);// 创建记录缓存文件
      if (tempfile.exists()) {
        InputStreamReader isr = new InputStreamReader(//
            new FileInputStream(tempfile));
        BufferedReader br = new BufferedReader(isr);
        String lastStr = br.readLine();
        lastPostion = Integer.parseInt(lastStr);
        conn.setRequestProperty("Range", //
            "bytes=" + lastPostion + "-" + endposition);
        br.close();
      } else {
        lastPostion = startposition;
        conn.setRequestProperty("Range", //
            "bytes=" + lastPostion + "-" + endposition);
      }

      if (HttpURLConnection.HTTP_PARTIAL == conn.getResponseCode()) {
        InputStream is = conn.getInputStream();
        RandomAccessFile accessFile = new RandomAccessFile(new File(savePath + //
            "\\" + urlPath.substring(urlPath.lastIndexOf("/") + 1)), "rwd");
        accessFile.seek(lastPostion); // "开始位置"
        byte[] bt = new byte[1024 * 200];
        int len = 0;
        long total = 0;
        while ((len = is.read(bt)) != -1) {
          total += len;
          accessFile.write(bt, 0, len);
          long currentposition = startposition + total;
          File cachefile = new File(savePath + Download.CACHE_NAME);
          RandomAccessFile rf = new RandomAccessFile(cachefile, "rwd");
          rf.write(String.valueOf(currentposition).getBytes());
          rf.close();
        }
        is.close();// "下载完毕"
        accessFile.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
