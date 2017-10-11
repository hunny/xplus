# Spring Boot自动重启和Debug断点

## 自动重启

* 添加依赖
```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
</dependency>
```

* 当修改classpath下的文件时，就会自动重启
	- 默认排除如下目录
		+ /META-INF/resources、/resources、/static、/public和
/templates；
	- 可设置```spring.devtools.restart.exclude```属性覆盖默认的重启排除目录：
		+ 例如，只排除/static和/templates目录：```spring.devtools.restart.exclude=/static/**,/templates/**```

## Debug断点

* 在eclipse中debug启动时，项目启动结束时自动断点停到了如下位置
```
public static void exitCurrentThread() {
  throw new SilentExitException();
}
```
不影响项目运行，自动启动也是可以的，在stackoverflow上找到的解决方案是：
```
Eclipse -> Preferences ->Java ->Debug
```
去掉"Suspend execution on uncaught exceptions"前面的勾；

* 参考资料：[Breakpoint at “throw new SilentExitException()” in Eclipse + Spring Boot](https://stackoverflow.com/questions/32770884/breakpoint-at-throw-new-silentexitexception-in-eclipse-spring-boot)
This is unfortunately a know issue with the new spring-boot-devtools module (see https://github.com/spring-projects/spring-boot/issues/3100). We use this trick to kill the main thread so that we can replace it with a re-loadable version. So far I've not found a way to prevent the debug breakpoint from triggering.
For now, you can toggle the "suspend execution on uncaught exceptions" checkbox in Java -> Debug preferences to prevent it from happening.

* 网上还有博文说要在maven插件里加fork配置，然而我没加也好用，甚至把maven插件去掉了也没影响
```
<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <!--fork : 如果没有该项配置，这个devtools不会起作用，即应用不会restart -->
      <configuration>
        <fork>true</fork>
      </configuration>
    </plugin>
  </plugins>
</build>
```
