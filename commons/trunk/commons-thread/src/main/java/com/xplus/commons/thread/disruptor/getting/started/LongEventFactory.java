package com.xplus.commons.thread.disruptor.getting.started;

import com.lmax.disruptor.EventFactory;

public class LongEventFactory implements EventFactory<LongEvent> {

  @Override
  public LongEvent newInstance() {
    return new LongEvent();
  }
}
