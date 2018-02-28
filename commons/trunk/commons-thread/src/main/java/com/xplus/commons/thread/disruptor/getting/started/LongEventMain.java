package com.xplus.commons.thread.disruptor.getting.started;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class LongEventMain {

  public static void main(String[] args) throws InterruptedException {

    // The factory for the event
    LongEventFactory factory = new LongEventFactory();

    // Specify the size of the ring buffer, must be power of 2.
    int bufferSize = 1024;

    // Construct the Disruptor
    Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, //
        bufferSize, //
        Executors.defaultThreadFactory(), //
        ProducerType.SINGLE, new YieldingWaitStrategy());

    // Connect the handler
    disruptor.handleEventsWith(new LongEventHandler[] { //
        new LongEventHandler() //
    });

    // Start the Disruptor, starts all threads running
    disruptor.start();

    // Get the ring buffer from the Disruptor to be used for publishing.
    RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

    LongEventProducer producer = new LongEventProducer(ringBuffer);

    ByteBuffer bb = ByteBuffer.allocate(8);
    for (long l = 0; true; l++) {
      bb.putLong(0, l);
      producer.onData(bb);
      Thread.sleep(1000);
    }

  }

}
