# SpringBoot自定义Conditional

## 前言
SpringBoot的AutoConfig内部大量使用了`@Conditional`，会根据运行环境来动态注入Bean。这里介绍一些`@Conditional`的使用和原理，并自定义@Conditional来自定义功能。

## Conditional

`@Conditional`是SpringFramework的功能，SpringBoot在它的基础上定义了`@ConditionalOnClass`，`@ConditionalOnProperty`的一系列的注解来实现更丰富的内容。
研究`@ConditionalOnClass`会发现它注解了`@Conditional(OnClassCondition.class)`。

```java
package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * {@link Conditional} that only matches when the specified classes are on the classpath.
 *
 * @author Phillip Webb
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassCondition.class)
public @interface ConditionalOnClass {

	/**
	 * The classes that must be present. Since this annotation is parsed by loading class
	 * bytecode, it is safe to specify classes here that may ultimately not be on the
	 * classpath, only if this annotation is directly on the affected component and
	 * <b>not</b> if this annotation is used as a composed, meta-annotation. In order to
	 * use this annotation as a meta-annotation, only use the {@link #name} attribute.
	 * @return the classes that must be present
	 */
	Class<?>[] value() default {};

	/**
	 * The classes names that must be present.
	 * @return the class names that must be present.
	 */
	String[] name() default {};

}
```

OnClassCondition则继承了SpringBootCondition，实现了Condition接口。

```java
@Order(Ordered.HIGHEST_PRECEDENCE)
class OnClassCondition extends SpringBootCondition
		implements AutoConfigurationImportFilter, BeanFactoryAware, BeanClassLoaderAware {
	// ...
}

public abstract class SpringBootCondition implements Condition {
	// ...
}
```

查看SpringFramework的源码会发现加载使用这些注解的入口在`ConfigurationClassPostProcessor`中，这个实现了`BeanFactoryPostProcessor`接口，这将会嵌入到Spring的加载过程中。
这个类主要是从`ApplicationContext`中取出`Configuration`注解的类并解析其中的注解，包括 `@Conditional`，`@Import`和 `@Bean`等。
解析`@Conditional`逻辑在`ConfigurationClassParser`类中，这里面用到了`ConditionEvaluator`这个类。

```java
class ConfigurationClassParser {

	// ...

	public ConfigurationClassParser(MetadataReaderFactory metadataReaderFactory,
			ProblemReporter problemReporter, Environment environment, ResourceLoader resourceLoader,
			BeanNameGenerator componentScanBeanNameGenerator, BeanDefinitionRegistry registry) {

		this.metadataReaderFactory = metadataReaderFactory;
		this.problemReporter = problemReporter;
		this.environment = environment;
		this.resourceLoader = resourceLoader;
		this.registry = registry;
		this.componentScanParser = new ComponentScanAnnotationParser(
				resourceLoader, environment, componentScanBeanNameGenerator, registry);
		this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
	}

	// ...

	protected void processConfigurationClass(ConfigurationClass configClass) throws IOException {
		if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationPhase.PARSE_CONFIGURATION)) {
			return;
		}

		ConfigurationClass existingClass = this.configurationClasses.get(configClass);
		if (existingClass != null) {
			if (configClass.isImported()) {
				if (existingClass.isImported()) {
					existingClass.mergeImportedBy(configClass);
				}
				// Otherwise ignore new imported config class; existing non-imported class overrides it.
				return;
			}
			else {
				// Explicit bean definition found, probably replacing an import.
				// Let's remove the old one and go with the new one.
				this.configurationClasses.remove(configClass);
				for (Iterator<ConfigurationClass> it = this.knownSuperclasses.values().iterator(); it.hasNext(); ) {
					if (configClass.equals(it.next())) {
						it.remove();
					}
				}
			}
		}

		// Recursively process the configuration class and its superclass hierarchy.
		SourceClass sourceClass = asSourceClass(configClass);
		do {
			sourceClass = doProcessConfigurationClass(configClass, sourceClass);
		}
		while (sourceClass != null);

		this.configurationClasses.put(configClass, configClass);
	}

	// ...

}
```

`ConditionEvaluator`中的`shouldSkip`方法则使用了`@Conditional`中设置的`Condition`类。

```java
class ConditionEvaluator {

	// ...

	public boolean shouldSkip(AnnotatedTypeMetadata metadata, ConfigurationPhase phase) {
		if (metadata == null || !metadata.isAnnotated(Conditional.class.getName())) {
			return false;
		}

		if (phase == null) {
			if (metadata instanceof AnnotationMetadata &&
					ConfigurationClassUtils.isConfigurationCandidate((AnnotationMetadata) metadata)) {
				return shouldSkip(metadata, ConfigurationPhase.PARSE_CONFIGURATION);
			}
			return shouldSkip(metadata, ConfigurationPhase.REGISTER_BEAN);
		}

		List<Condition> conditions = new ArrayList<Condition>();
		for (String[] conditionClasses : getConditionClasses(metadata)) {
			for (String conditionClass : conditionClasses) {
				Condition condition = getCondition(conditionClass, this.context.getClassLoader());
				conditions.add(condition);
			}
		}

		AnnotationAwareOrderComparator.sort(conditions);

		for (Condition condition : conditions) {
			ConfigurationPhase requiredPhase = null;
			if (condition instanceof ConfigurationCondition) {
				requiredPhase = ((ConfigurationCondition) condition).getConfigurationPhase();
			}
			if (requiredPhase == null || requiredPhase == phase) {
				if (!condition.matches(this.context, metadata)) {
					return true;
				}
			}
		}

		return false;
	}

	// ...

}
```

## 自定义Conditional

自定义Conditional就是通过自定义注解和Condition的实现类。

### 1. 定义`@ConditionalOnMyProperty`注解

```java
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMyPropertyCondition.class)
public @interface ConditionalOnMyProperty {

  String name();
  
}
```

### 2. 定义`OnMyPropertyCondition`，

`OnMyPropertyCondition`继承了SpringBootCondition重用了部分功能，然后使用getMatchOutcome实现了自定义的功能。

```java
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
```

### 3. 定义业务服务类

```java
public class HelloWorldService {
  
  public void announce() {
    System.out.println("Hello, Conditional World!");
  }
  
}
```

### 4. 定义`@ConditionalOnMyProperty`的使用类

* 请注意，依据主运行类采用的方式，要加上`@Configuration`注解或者在运行类中使用`@Import(ConditionUsingClass.class)`导入才能生效。

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMyProperty(name = "my.name")
public class ConditionUsingClass {

  @Bean
  public HelloWorldService helloWorldService() {
    return new HelloWorldService();
  }
  
}
```

### 5. 定义`ConditionalTestApplication`来测试验证

测试验证，这里运行两次SpringApplication，传入的参数不同，第一次取Bean会抛出Bean不存在的异常，第二次就会正常输出。

```java
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@Import(ConditionUsingClass.class)
public class ConditionalTestApplication {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(ConditionalTestApplication.class);
    springApplication.setWebEnvironment(false);
    ConfigurableApplicationContext noneMessageConfigurableApplicationContext = springApplication
        .run("--logging.level.root=ERROR");
    try {
      noneMessageConfigurableApplicationContext.getBean(HelloWorldService.class).announce();
    } catch (Exception e) {
      e.printStackTrace();//此处报错，是预想中的
    }
    ConfigurableApplicationContext configurableApplicationContext = springApplication
        .run("--my.name=Normal", "--logging.level.root=ERROR");
    configurableApplicationContext.getBean(HelloWorldService.class).announce();
  }

}
```
