package com.example.bootweb.activemq;

import static org.assertj.core.api.Assertions.assertThat;

import javax.jms.JMSException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.springboot.activemq.Application;
import com.example.springboot.activemq.Producer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

  @Rule
  public OutputCapture outputCapture = new OutputCapture();

  @Autowired
  private Producer producer;

  @Test
  public void sendSimpleMessage() throws InterruptedException, JMSException {
    this.producer.send("Test message");
    Thread.sleep(1000L);
    assertThat(this.outputCapture.toString().contains("Test message")).isTrue();
  }

}
