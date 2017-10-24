package com.xplus.commons.util.io.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.xplus.commons.util.io.Expr;

public class ServerReadHandler implements CompletionHandler<Integer, ByteBuffer> {
  // 用于读取半包消息和发送应答
  private AsynchronousSocketChannel channel;

  public ServerReadHandler(AsynchronousSocketChannel channel) {
    this.channel = channel;
  }

  // 读取到消息后的处理
  @Override
  public void completed(Integer result, ByteBuffer attachment) {
    // flip操作
    attachment.flip();
    // 根据
    byte[] message = new byte[attachment.remaining()];
    attachment.get(message);
    try {
      String expression = new String(message, "UTF-8");
      System.out.println("服务器收到消息: " + expression);
      String calrResult = null;
      try {
        calrResult = Expr.cal(expression).toString();
      } catch (Exception e) {
        calrResult = "计算错误：" + e.getMessage();
      }
      // 向客户端发送消息
      doWrite(calrResult);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  // 发送消息
  private void doWrite(String result) {
    byte[] bytes = result.getBytes();
    ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
    writeBuffer.put(bytes);
    writeBuffer.flip();
    // 异步写数据 参数与前面的read一样
    channel.write(writeBuffer, writeBuffer, new ServerWriteHandler(channel));
  }

  @Override
  public void failed(Throwable exc, ByteBuffer attachment) {
    try {
      this.channel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
