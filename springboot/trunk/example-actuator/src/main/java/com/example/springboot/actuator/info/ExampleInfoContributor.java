package com.example.springboot.actuator.info;

import java.util.Collections;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class ExampleInfoContributor implements InfoContributor {

  @Override
  public void contribute(Info.Builder builder) {
    builder.withDetail("example", Collections.singletonMap("someKey", "someValue"));
  }

}
