package com.example.bootweb.security.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnMyPropertyCondition extends SpringBootCondition {

  @Override
  public ConditionOutcome getMatchOutcome(ConditionContext context,
      AnnotatedTypeMetadata metadata) {
    Object propertyName = metadata
        .getAnnotationAttributes(ConditionalOnMyProperty.class.getName()).get("name");
    if (propertyName != null) {
      String value = context.getEnvironment().getProperty(propertyName.toString());
      if (value != null) {
        return new ConditionOutcome(true, "get property of name: " + propertyName);
      }
    }
    return new ConditionOutcome(false, "can't get property of name");
  }

}
