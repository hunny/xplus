package com.example.bootweb.netty.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class TraditionalSocketTest {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public void socket(int portNumber) throws Exception {
    ServerSocket serverSocket = new ServerSocket( //
        portNumber);// ← -- 创建一个新的ServerSocket，用以监听指定端口上的连接请求
    try {
      Socket socket = serverSocket //
          .accept();// ← -- ❶ 对accept()方法的调用将被阻塞，直到一个连接建立
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(//
          socket.getOutputStream(), true);// ← -- ❷ 这些流对象都派生于该套接字的流对象
      String request, response;
      while ((request = in.readLine()) != null) {// ← -- ❸ 处理循环开始
        logger.info("Server accept :{}", request);
        if ("Done".equals(request)) {
          logger.info("Client Request Done.");
          break; // ← -- 如果客户端发送了“Done”，则退出处理循环
        }
        response = processRequest(request);// ← -- ❹ 请求被传递给服务器的处理方法
        out.println(response);// ← -- 服务器的响应被发送给了客户端
      } // ← -- 继续执行处理循环
    } finally {
      try {
        serverSocket.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @SuppressWarnings("static-method")
  private String processRequest(String request) {
    if (StringUtils.hasText(request)) {
      return MessageFormat.format("{0} After handle: {1}", request, "OK");
    }
    return null;
  }

  public void client(String address, int portNumber) throws Exception {
    Socket socket = new Socket(address, portNumber);// ← -- 建立连接
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);// ← -- 获取客户端输出流
    
    // Send a welcome message to the client.
    out.println("Hello, you are client #" + portNumber + ".");// ← -- 向服务端输出请求
    BufferedReader in = new BufferedReader(//
        new InputStreamReader(socket.getInputStream()));// ← -- 获取客户端输入
    String input = in.readLine();// ← -- 读取服务端的输出
    logger.info("Server Response :{}", input);
    
    out.println("Enter a line with 'Done' to quit");
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    input = in.readLine();
    logger.info("Server Response :{}", input);
    
    out.println("Done");
    socket.close();
  }

  public static void main(String[] args) throws Exception {
    TraditionalSocketTest traditional = new TraditionalSocketTest();
    new Thread(new Runnable() {
      public void run() {
        try {
          traditional.socket(9090);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
    
    Thread.sleep(1000);
    
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          traditional.client("127.0.0.1", 9090);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

}
