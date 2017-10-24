package com.xplus.commons.util.io.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

  // 用于读取半包消息和发送应答
  private AsynchronousSocketChannel channel;

  public ServerWriteHandler(AsynchronousSocketChannel channel) {
    this.channel = channel;
  }

  @Override
  public void completed(Integer result, ByteBuffer buffer) {
    // 如果没有发送完，就继续发送直到完成
    if (buffer.hasRemaining())
      channel.write(buffer, buffer, this);
    else {
      // 创建新的Buffer
      ByteBuffer readBuffer = ByteBuffer.allocate(1024);
      // 异步读 第三个参数为接收消息回调的业务Handler
      channel.read(readBuffer, readBuffer, new ServerReadHandler(channel));
    }
  }

  @Override
  public void failed(Throwable exc, ByteBuffer attachment) {
    try {
      channel.close();
    } catch (IOException e) {
    }
  }

}
