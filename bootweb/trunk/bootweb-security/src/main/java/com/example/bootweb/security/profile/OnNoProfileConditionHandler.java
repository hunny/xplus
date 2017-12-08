package com.example.bootweb.security.profile;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnNoProfileConditionHandler extends SpringBootCondition {

  @Override
  public ConditionOutcome getMatchOutcome(ConditionContext context,
      AnnotatedTypeMetadata metadata) {
    String value = context.getEnvironment().getProperty("spring.profiles.active");
    if (StringUtils.isBlank(value)) {
      return new ConditionOutcome(true, "No profile variable found.");
    }
    return new ConditionOutcome(false, "Profile is not blank.");
  }

}
