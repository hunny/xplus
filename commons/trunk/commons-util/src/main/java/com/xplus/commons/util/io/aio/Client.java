package com.xplus.commons.util.io.aio;

import java.util.Scanner;

public class Client {
  private static String DEFAULT_HOST = "127.0.0.1";
  private static int DEFAULT_PORT = 12345;
  private static ClientAsyncHandler clientHandle;

  public static void start() {
    start(DEFAULT_HOST, DEFAULT_PORT);
  }

  public static synchronized void start(String ip, int port) {
    if (clientHandle != null) {
      return;
    }
    clientHandle = new ClientAsyncHandler(ip, port);
    new Thread(clientHandle, "Client").start();
  }

  // 向服务器发送消息
  public static boolean sendMsg(String msg) throws Exception {
    if (msg.equals("q")) {
      return false;
    }
    clientHandle.sendMsg(msg);
    return true;
  }

  @SuppressWarnings("resource")
  public static void main(String[] args) throws Exception {
    Client.start();
    System.out.println("请输入请求消息：");
    Scanner scanner = new Scanner(System.in);
    while (Client.sendMsg(scanner.nextLine()))
      ;
  }
}
