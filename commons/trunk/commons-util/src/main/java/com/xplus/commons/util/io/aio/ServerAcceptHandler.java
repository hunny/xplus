package com.xplus.commons.util.io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

//作为handler接收客户端连接  
public class ServerAcceptHandler
    implements CompletionHandler<AsynchronousSocketChannel, ServerAsyncHandler> {
  @Override
  public void completed(AsynchronousSocketChannel channel, ServerAsyncHandler serverHandler) {
    // 继续接受其他客户端的请求
    Server.clientCount++;
    System.out.println("连接的客户端数：" + Server.clientCount);
    serverHandler.channel.accept(serverHandler, this);
    // 创建新的Buffer
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    // 异步读 第三个参数为接收消息回调的业务Handler
    channel.read(buffer, buffer, new ServerReadHandler(channel));
  }

  @Override
  public void failed(Throwable exc, ServerAsyncHandler serverHandler) {
    exc.printStackTrace();
    serverHandler.latch.countDown();
  }
}
