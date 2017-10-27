[TOC]

# Maven提高篇系列

* 包括如下知识点：
	- Maven简介
	- Maven单工程
	- 多模块VS继承
	- 配置Plugin到某个Phase（以Selenium集成测试为例）
	- 使用自己的Repository(Nexus)
	- 使用Profile
	- 处理依赖冲突
	- 编写自己的Plugin

* 参考
	- [Maven提高篇系列](http://www.cnblogs.com/davenkin/p/advanced-maven-multi-module-vs-inheritance.html)
	- [Jetty开发指导：Maven和Jetty](http://blog.csdn.net/tomato__/article/details/37927813)
	- [selenium之 chromedriver与chrome版本映射表](http://blog.csdn.net/huilan_same/article/details/51896672)

## 一、Maven简介

Maven是一个用于项目构建的工具，通过它便捷的管理项目的生命周期。即项目的jar包依赖，开发，测试，发布打包。

### 1 项目坐标

Maven通过特定的标识来定义项目名称，这样既可以唯一的匹配其他的jar包，也可以通过发布，使别人能使用自己的发布产品。这个标识就被叫做坐标，长的其实很普通，就是简单的xml而已：

```
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example.parent</groupId>
  <artifactId>demo-parent</artifactId>
  <packaging>pom</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>demo-parent</name>
  <url>http://maven.apache.org</url>  
```

* modelVersion： 这个XML文件所使用的POM格式的版本
* groupId： 相当于这个project的所有者或者机构的一个标识，一般是com.company.xxx这种格式
* artifactId：  这个project最后所生成的文档(jar、war)的名字，比如对于junit这个开源的project，它的artifactId就是junit
* packaging： 这个project的打包的类型，一般是war、jar等值
* version： project的版本
* name： project的名字，生成文档等内容的时候会用的这个名字

### 2 maven一个标准的目录结构

| 文件夹 | 说明 |
| --- | --- |
| src/main/java | Application/Library sources | |
| src/main/resources | Application/Library resources |
| src/main/filters | Resource filter files |
| src/main/assembly | Assembly descriptors |
| src/main/config | Configuration files |
| src/main/scripts | Application/Library scripts |
| src/main/webapp | Web application sources |
| src/test/java | Test sources |
| src/test/resources | Test resources |
| src/test/filters | Test resource filter files |
| src/site | Site |

src/main/java 和 src/test/java 是不能改动，不然maven会无法找到源文件。

### 3 Build lifecycle & Build phase & Goal

maven有一套build的生命周期，是按照一套顺序走下来的，这一套顺序就叫一个生命周期(lifecycle)。
maven内置三种生命周期：default, clean 和 site。
一个生命周期分为多个build phase，下面是default生命周期全部的build phase：

| 阶段 | 说明 |
| --- | --- |
| validate | validate the project is correct and all necessary information is available. |
| initialize | initialize build state, e.g. set properties or create directories. |
| generate-sources | generate any source code for inclusion in compilation. |
| process-sources | process the source code, for example to filter any values. |
| generate-resources | generate resources for inclusion in the package. |
| process-resources | copy and process the resources into the destination directory, ready for packaging. |
| compile | compile the source code of the project. |
| process-classes | post-process the generated files from compilation, for example to do bytecode enhancement on Java classes. |
| generate-test-sources | generate any test source code for inclusion in compilation. |
| process-test-sources | process the test source code, for example to filter any values. |
| generate-test-resources | create resources for testing. |
| process-test-resources | copy and process the resources into the test destination directory. |
| test-compile | compile the test source code into the test destination directory |
| process-test-classes | post-process the generated files from test compilation, for example to do bytecode enhancement on Java classes. For Maven 2.0.5 and above. |
| test | run tests using a suitable unit testing framework. These tests should not require the code be packaged or deployed. |
| prepare-package | perform any operations necessary to prepare a package before the actual packaging. This often results in an unpacked, processed version of the package. (Maven 2.1 and above) |
| package | take the compiled code and package it in its distributable format, such as a JAR. |
| pre-integration-test | perform actions required before integration tests are executed. This may involve things such as setting up the required environment. |
| integration-test | process and deploy the package if necessary into an environment where integration tests can be run. |
| post-integration-test | perform actions required after integration tests have been executed. This may including cleaning up the environment. |
| verify | run any checks to verify the package is valid and meets quality criteria. |
| install | install the package into the local repository, for use as a dependency in other projects locally. |
| deploy | done in an integration or release environment, copies the final package to the remote repository for sharing with other developers and projects. |

这些build phase是按照顺序执行的，如果执行后面的build phase，前面的build phase 也会被执行。

### 4 常用命令操作

* `mvn clean`，可清理工程模块。
* `mvn dependency:analyze`，可分析工程的依赖。
* `mvn clean compile`，可编译工程。
* `mvn clean package`，可打包工程，根据pom.xml里面的packaging选项打包成相应的文件。
* `mvn clean test`，可测试工程。
* `mvn site`，为project创建一个站点。
* `mvn clean install`命令，完成整个工程的编译、打包和安装到本地Maven Repository的过程。
* `mvn -v` 显示版本 
* `mvn help:describe -Dplugin=help` 使用 help 插件的  describe 目标来输出 Maven Help 插件的信息。 
* `mvn help:describe -Dplugin=help -Dfull` 使用Help 插件输出完整的带有参数的目标列 
* `mvn help:describe -Dplugin=compiler -Dmojo=compile -Dfull` 获取单个目标的信息,设置  mojo 参数和  plugin 参数。此命令列出了Compiler 插件的compile 目标的所有信息 
* `mvn help:describe -Dplugin=exec -Dfull` 列出所有 Maven Exec 插件可用的目标 
* `mvn help:effective-pom` 看这个“有效的 (effective)”POM，它暴露了 Maven的默认设置
* `mvn archetype:generate -DgroupId=org.sonatype.mavenbook.ch03 -DartifactId=simple -DpackageName=org.sonatype.mavenbook` 创建Maven的普通java项目，在命令行使用Maven Archetype 插件 
* `mvn exec:java -Dexec.mainClass=org.sonatype.mavenbook.weather.Main` Exec 插件让我们能够在不往 classpath 载入适当的依赖的情况下，运行这个程序 
* `mvn dependency:analyze` 打印出依赖的分析列表 
* `mvn dependency:resolve` 打印出已解决依赖的列表 
* `mvn dependency:tree` 打印整个依赖树
* `mvn install -X` 想要查看完整的依赖踪迹，包含那些因为冲突或者其它原因而被拒绝引入的构件，打开 Maven 的调试标记运行 
* `mvn install -Dmaven.test.skip=true` 给任何目标添加maven.test.skip 属性就能跳过测试 
* `mvn install assembly:assembly` 构建装配Maven Assembly 插件是一个用来创建你应用程序特有分发包的插件
* `mvn jetty:run`     调用 Jetty 插件的 Run 目标在 Jetty Servlet 容器中启动 web 应用 
* `mvn compile`       编译你的项目 
* `mvn clean install` 删除再编译
* `mvn hibernate3:hbm2ddl` 使用 Hibernate3 插件构造数据库
* 发布第三方Jar到本地库中：

```
mvn install:install-file -DgroupId=com -DartifactId=client -Dversion=0.1.0 -Dpackaging=jar -Dfile=d:\client-0.1.0.jar -DdownloadSources=true -DdownloadJavadocs=true
```

### 5 执行命令时的控制变量

* `-DskipTests`，不执行测试用例，但编译测试用例类生成相应的class文件至target/test-classes下。
* `-Dmaven.test.skip=true`，不执行测试用例，也不编译测试用例类。
* `-Dmaven.javadoc.skip=true`，忽略javadoc的生成。

## 二、Maven单工程

### 1 使用命令创建Maven单工程：

```
mvn archetype:generate -DgroupId=com.example -DartifactId=demo -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

创建日志如下：

```
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building Maven Stub Project (No POM) 1
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] >>> maven-archetype-plugin:3.0.1:generate (default-cli) > generate-sources @ standalone-pom >>>
[INFO]
[INFO] <<< maven-archetype-plugin:3.0.1:generate (default-cli) < generate-sources @ standalone-pom <<<
[INFO]
[INFO] --- maven-archetype-plugin:3.0.1:generate (default-cli) @ standalone-pom ---
[INFO] Generating project in Batch mode
[INFO] ----------------------------------------------------------------------------
[INFO] Using following parameters for creating project from Old (1.x) Archetype: maven-archetype-quickstart:1.0
[INFO] ----------------------------------------------------------------------------
[INFO] Parameter: basedir, Value: D:\learn\maven
[INFO] Parameter: package, Value: com.example
[INFO] Parameter: groupId, Value: com.example
[INFO] Parameter: artifactId, Value: demo
[INFO] Parameter: packageName, Value: com.example
[INFO] Parameter: version, Value: 1.0-SNAPSHOT
[INFO] project created from Old (1.x) Archetype in dir: D:\learn\maven\demo
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 43.956 s
[INFO] Finished at: 2017-10-26T12:35:54+08:00
[INFO] Final Memory: 14M/179M
[INFO] ------------------------------------------------------------------------
```

生成的pom.xml如下：

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>demo</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>demo</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

从日志中可以看出，命令行中还可以指定其它一些参数：

```
-Dversion=0.0.1-SHAPSHOT -Dpackage=com.example.service
```

### 2 配置常用属性

在pom.xml文件中，追加如下配置：

```
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
  </properties>
```

`project.build.sourceEncoding`表明，工程的文件使用的编码，本例中使用`UTF-8`编码。
`project.reporting.outputEncoding`表明，工程的报表输出文件编码，本例中使用`UTF-8`编码。
`java.version`表明，工程使用Java JDK版本，本例中使用的是1.8。

### 3 如何添加依赖

maven可以让我们方便地管理jar包依赖，具体做法如下：

```
<dependencies>
     <dependency>   <!--添加一个jar包依赖-->
         <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <version>2.5</version>
    </dependency>
</dependencies>
```

如果不通过继承，则需要在每个pom中加入这样的依赖，这样子pom对应的模块可以引用到这个jar包。上面提到的重复引用jar包，可以通过下面的方式解决：
主pom中把依赖通过<dependecyManagement>引起来，表示子pom可能会用到的jar包依赖：

```
<dependencyManagement>
   <dependencies>
      <dependency>
           <groupId>javax.servlet</groupId>
          <artifactId>servlet-api</artifactId>
          <version>2.5</version>
      </dependency>
   </dependencies>
</dependencyManagement>
```

子pom如果需要引用该jar包，则直接引用即可！不需要加入<version>，便于统一管理。此外也可以加入仅在子pom中用到的jar包，比如：

```
<dependencies>
   <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>   <!--此处不再需要verison了！-->
   </dependency>
   <dependency>
       <groupId>org.codehaus.jackson</groupId>
       <artifactId>jackson-core-lgpl</artifactId>
       <version>1.9.4</version>    <!--当然也可以加入只在这个子模块中用到的jar包-->
   </dependency>
</dependencies>
```

除了jar包依赖，插件也可以通过这样的方式进行管理:

主pom.xml配置：

```
<build>
   <pluginManagement>
      <plugins>
          <plugin>
               <groupId>org.apache.maven.plugins</groupId>
               <artifactId>maven-source-plugin</artifactId>
               <version>2.1.1</version>
          </plugin>
      </plugins>
   </pluginManagement>
</build>
```

子pom.xml配置：

```
<build>   
   <plugins>
      <plugin>
           <groupId>org.apache.maven.plugins</groupId>
           <artifactId>maven-source-plugin</artifactId>
      </plugin>
   </plugins>
</build>
```

子pom间存在引用关系，比如childB引用到了childA的jar包

```
<dependency>
   <groupId>com.module</groupId>
   <artifactId>childA</artifactId>       <!--加上childA的依赖-->
   <version>1.0.0</version>
</dependency>
```

如果要依赖另外一个parent，使用如下方式：

```
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
```

### 4 Maven如何查找依赖？

Maven会先在本地Repository中查找依赖，如果依赖存在，则使用该依赖，如果不存在，则通过pom.xml中的Repository配置从远程下载依赖到本地Repository中。默认情况下，Maven将使用[Maven Central Repository](http://search.maven.org/)作为远端Repository。在pom.xml中为什么没有看到这样的配置信息? 原因在于，任何一个Maven工程都默认地继承自一个[Super POM](http://books.sonatype.com/mvnref-book/reference/pom-relationships-sect-pom.html#pom-relationships-sect-super-pom)，Repository的配置信息便包含在其中。

## 三、多模块VS继承

通常来说，在Maven的多模块工程中，都存在一个pom类型的工程作为根模块，该工程只包含一个pom.xml文件，在该文件中以模块（module）的形式声明它所包含的子模块，即多模块工程。在子模块的pom.xml文件中，又以parent的形式声明其所属的父模块，即继承。然而，这两种声明并不必同时存在，我们将在下文中讲到这其中的区别。

### 1 创建Maven多模块工程

多模块的好处是你只需在根模块中执行Maven命令，Maven会分别在各个子模块中执行该命令，执行顺序通过Maven的Reactor机制决定。先来看创建Maven多模块工程的常规方法。在我们的示例工程中，存在一个父工程，它包含了两个子工程（模块），一个core模块，一个webapp模块，webapp模块依赖于core模块。这是一种很常见的工程划分方式，即core模块中包含了某个领域的核心业务逻辑，webapp模块通过调用core模块中服务类来创建前端网站。这样将核心业务逻辑和前端展现分离开来，如果之后决定开发另一套桌面应用程序，那么core模块是可以重用在桌面程序中。

#### 1.1 创建demo-parent父工程

首先通过Maven的Archetype插件创建一个父工程，即一个pom类型的Maven工程，其中只包含一个pom.xml文件：

```
mvn archetype:generate -DgroupId=com.example.parent -DartifactId=demo-parent -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

以上命令在当前目录下创建了一个名为demo-parent的目录，在该目录只有一个pom.xml文件：

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example.parent</groupId>
  <artifactId>demo-parent</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>demo-parent</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

修改`packaging`为`pom`类型，表示该工程为`pom`类型。其他的Maven工程类型还有`jar`、`war`、`ear`等。


#### 1.2 创建demo-core模块工程

创建core模块，由于core模块属于demo-parent模块，将工作目录切换到demo-parent目录下，创建core模块命令如下：

```
cd demo-parent
mvn archetype:generate -DgroupId=com.example.parent.core -DartifactId=demo-core -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

创建core模块，查看core模块中的pom.xml文件：

```
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.parent</groupId>
    <artifactId>demo-parent</artifactId>
    <version>1.0-SNAPSHOT</version>
  </parent>
  <groupId>com.example.parent.core</groupId>
  <artifactId>demo-core</artifactId>
  <version>1.0-SNAPSHOT</version>
  <name>demo-core</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

请注意里面的`<parent>...  </parent>`，它将demo-parent模块做为了自己的父模块。当创建demo-core模块时，Maven将自动识别出已经存在的demo-parent父模块，然后分别创建两个方向的指引关系，即在demo-parent模块中将core作为自己的子模块，在demo-core模块中将demo-parent作为自己的父模块。
要使Maven有这样的自动识别功能，需要使demo-parent的pom.xml文件的`packaging`为pom并且在demo-parent目录下创建demo-core模块，也可以通过手动修改两个模块中的pom.xml文件来创建他们之间的父子关系，从而达到同样的目的。

此时，查看demo-parent目录下的pom.xml文件：

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example.parent</groupId>
  <artifactId>demo-parent</artifactId>
  <packaging>pom</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>demo-parent</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <modules>
    <module>demo-core</module>
  </modules>
</project>
```

多了一段代码：

```
  <modules>
    <module>demo-core</module>
  </modules>
```

#### 1.3 创建demo-web模块

在demo-parent目录下，使用如下命令创建demo-web模块：

```
mvn archetype:generate -DgroupId=com.example.parent.web -DartifactId=demo-web -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false
```

这里的maven-archetype-webapp表明Maven创建的是一个war类型的工程模块，其pom.xml如下:

```
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.parent</groupId>
    <artifactId>demo-parent</artifactId>
    <version>1.0-SNAPSHOT</version>
  </parent>
  <groupId>com.example.parent.web</groupId>
  <artifactId>demo-web</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>war</packaging>
  <name>demo-web Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <build>
    <finalName>demo-web</finalName>
  </build>
</project>
```
在webapp模块的pom.xml文件中，也以`<parent>...  </parent>`的方式将demo-parent模块声明为自己的父模块，与之前的命令行创建相比，该pom的类型为`<packaging>war</packaging>`并且多了一段代码：

```
  <build>
    <finalName>demo-web</finalName>
  </build>
```

此时再看demo-parent模块的pom.xml文件，其中的<modules>中多了一个webapp模块：

```
  <modules>
    <module>demo-core</module>
    <module>demo-web</module>
  </modules>
```

### 2 手动添加子模块之间的依赖关系

此时虽然创建了一个多模块的Maven工程，但是有两个问题我们依然没有解决：
* A. 没有发挥Maven父模块的真正作用（配置共享）
* B. webapp模块对core模块的依赖关系尚未建立

对于A问题，Maven父模块的作用本来是使子模块可以继承并覆盖父模块中的配置，比如dependency等，但是如果我们看看webapp和core模块中pom.xml文件，他们都声明了对Junit的依赖，而如果多个子模块都依赖于相同的类库，我们应该将这些依赖配置在父模块中，继承自父模块的子模块将自动获得这些依赖。所以接下来我们要做的便是：将webapp和core模块对junit的依赖删除，并将其迁移到父模块中。

对于B问题，Maven在创建webapp模块时并不知道webapp依赖于core，所以这种依赖关系需要我们手动加入，在webapp模块的pom.xml中加入对core模块的依赖：

```
    <dependency>
      <groupId>com.example.parent.core</groupId>
      <artifactId>demo-core</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
```

此时再在demo-parent目录下执行`mvn clean install`，Maven将根据自己的Reactor机制决定哪个模块应该先执行，哪个模块应该后执行。比如，这里的webapp模块依赖于core模块，那么Maven会先在core模块上执行`mvn clean install`，再在webapp模块上执行相同的命令。在webapp上执行`mvn clean install`时，由于core模块已经被安装到了本地的Repository中，webapp便可以顺利地找到所依赖的core模块。

总的来看，此时命令的执行顺序为demo-parent -> core -> webapp，先在demo-parent上执行是因为其他两个模块都将它作为父模块，即对它存在依赖关系，又由于core被webapp依赖，所以接下来在core上执行命令，最后在webapp上执行。

### 3 多模块vs继承

在Maven中，由多模块（Project Aggregation）和继承（Project Inheritance）关系并不必同时存在。

#### 3.1 多模块

如果保留demo-parent中的子模块声明，而删除webapp和core中对demo-parent的父关系声明，此时整个工程只是一个多模块工程，而没有父子关系。
Maven会正确处理模块之间的依赖关系，即在webapp模块上执行Maven命令之前，会先在core模块上执行该命令，但是由于core和webapp模块不再继承自demo-parent，对于每一个依赖，他们都需要自己声明，比如我们需要分别在webapp和core的pom.xml文件中声明对Junit依赖。

#### 3.2 继承

如果保留webapp和core中对demo-parent的父关系声明，即保留`<parent>...  </parent>`，而删除demo-parent中的子模块声明，即`<modules>...<modules>`，此时，整个工程已经不是一个多模块工程，而只是具有父子关系的多个工程集合。
如果我们在demo-parent目录下执行`mvn clean install`，Maven只会在demo-parent本身上执行该命令，继而只会将demo-parent安装到本地Repository中，而不会在webapp和core模块上执行该命令，因为Maven根本就不知道这两个子模块的存在。另外，如果我们在webapp目录下执行相同的命令，由于由子到父的关系还存在，Maven会在本地的Repository中找到demo-parent的pom.xml文件和对core的依赖（当然前提是他们存在于本地的Repository中），然后顺利执行该命令。
这时，如果我们要发布webapp，那么我们需要先在demo-parent目录下执行`mvn clean install`将最新的父pom安装在本地Repository中，再在core目录下执行相同的命令将最新的core模块安装在本地Repository中，最后在webapp目录下执行相同的命令完成最终war包的安装。

#### 3.3 总结

多模块和父子关系是不同的。如果core和webapp只是在逻辑上属于同一个总工程，那么完全可以只声明模块关系，而不用声明父子关系。
如果core和webapp分别处理两个不同的领域，但是它们又共享了很多，比如依赖等，那么我们可以将core和webapp分别继承自同一个父pom工程，而不必属于同一个工程下的子模块。
更多解析请参考[这里](http://maven.apache.org/guides/introduction/introduction-to-the-pom.html)。

## 四、配置Plugin到某个phase

持续交付要“自动化所有东西”，对于集成测试也是一样。集成测试和单元测试相比需要更多的环境准备工作，包括测试数据的准备和启动服务器等。在本篇中我们设想以下一种场景：

* 开发了一个web应用，集成测试使用了Selenium，你希望通过一个Maven命令跑完所有的测试，包括集成测试。

Maven的plugin包含了一个或多个goal，每一个goal表示plugin的一个操作单位，在plugin开发中，一个goal通常对应于Java类的一个方法(Mojo的execute方法)。一个goal可以默认绑定到Mavan的某个phase，比如Jar plugin的jar这个goal便默认绑定在了package这个phase上。当Maven执行到package时，Jar的jar goal将自动执行。当然，在默认情况下plugin的goal也可以不绑定在任何一个phase上，此时Maven将不做任何操作。但是，我们可以显式地手动将某个plugin的某个goal绑定在一个phase中。

对于上面的场景，我们的解决方案是：在集成测试之前（对应Maven的phase为pre-integration-test），我们使用jetty-maven-plugin启动web应用，在集成测试时通过Selenium访问网站进行验证，集成测试完毕之后（对应Maven的phase为post-integration-test），同样使用jetty-maven-plugin关闭web应用。

### 1 修改demo-parent中的文件

* 修改pom.xml文件：

修改demo-parent依赖，发挥parent作用，把`<dependencies>`放到`<dependencyManagement>`中，并且为了使用Selenium，我们需要将Selenium依赖加入pom.xml文件中：

```
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
      </dependency>
      <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>2.52.0</version>
      </dependency>
    </dependencies>
  </dependencyManagement>
```

### 2 修改demo-web中的文件

* 修改pom.xml中`<dependencies>`依赖，追加Selenium依赖：

```
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.example.parent.core</groupId>
      <artifactId>demo-core</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-java</artifactId>
    </dependency>
  </dependencies>
```

* 修改pom.xml中`<build>`内容：

```
  <build>
    <plugins>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>jetty-maven-plugin</artifactId>
        <version>8.1.12.v20130726</version>
        <configuration>
          <scanintervalseconds>0</scanintervalseconds>
          <stopKey>stop</stopKey>
          <stopPort>9999</stopPort>
        </configuration>
        <executions>
          <execution>
            <id>start-jetty</id>
            <phase>pre-integration-test</phase>
            <goals>
              <goal>run</goal>
            </goals>
            <configuration>
              <scanintervalseconds>0</scanintervalseconds>
              <daemon>true</daemon>
            </configuration>
          </execution>
          <execution>
            <id>stop-jetty</id>
            <phase>post-integration-test</phase>
            <goals>
              <goal>stop</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <includes>
            <include>**/unit/*Test.java</include>
          </includes>
        </configuration>
        <executions>
          <execution>
            <id>surefire-it</id>
            <phase>integration-test</phase>
            <goals>
              <goal>test</goal>
            </goals>
            <configuration>
              <includes>
                <include>**/*IntegrationTest.java</include>
              </includes>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
    <finalName>demo-web</finalName>
  </build>
```

* 增加helloworld.html文件

在`src/main/webapp/`目录下新建`helloworld.html`文件，内容如下：

```
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>Hello World!</h2>
</body>
</html>
```

* 使用`mvn jetty:run`在浏览器上查看结果：

```
Hello World!
```

* 添加Selelium Webdriver集成测试如下：

```
package com.example.parent.web;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HelloWorldIntegrationTest {
  @Test
  public void testHelloWorldIndexPage() {

    WebDriver driver = new HtmlUnitDriver();
    
    // WebDriver driver = new ChromeDriver(chromeOptions);// use Chrome
    // WebDriver driver = new SafariDriver();//use safari
    // WebDriver driver = new InternetExplorerDriver();//use IE
    // WebDriver driver = new FirefoxDriver();//use fireforx

    driver.get("http://localhost:8080/helloworld.html");
    System.out.println(driver.getPageSource());
    WebElement element = driver.findElement(By.tagName("h2"));
    MatcherAssert.assertThat("", element.getText(),
        CoreMatchers.is(CoreMatchers.equalTo("Hello World!")));
  }
}
```

* 配置jetty-maven-plugin：

```
  <build>
    <plugins>
      <plugin>
        <groupId>org.mortbay.jetty</groupId>
        <artifactId>jetty-maven-plugin</artifactId>
        <version>8.1.12.v20130726</version>
        <configuration>
          <scanintervalseconds>0</scanintervalseconds>
          <stopKey>stop</stopKey>
          <stopPort>9999</stopPort>
        </configuration>
        <executions>
          <execution>
            <id>start-jetty</id>
            <phase>pre-integration-test</phase>
            <goals>
              <goal>run</goal>
            </goals>
            <configuration>
              <scanintervalseconds>0</scanintervalseconds>
              <daemon>true</daemon>
            </configuration>
          </execution>
          <execution>
            <id>stop-jetty</id>
            <phase>post-integration-test</phase>
            <goals>
              <goal>stop</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
    <finalName>demo-web</finalName>
  </build>
```

在pre-integration-test阶段，我们调用了jetty-maven-plugin的run，此时web服务器启动，在post-integration-test阶段，我们调用了jetty-maven-plugin的stop来关闭web服务器。

但是这里有个问题，在运行mvn clean install时，Maven会先运行单元测试，再运行集成测试，并且在默认情况下这两种测试都会运行以*Test.java结尾的测试类，结果在单元测试阶段也会运行上面的HelloWorldIntegrationTest，结果还没有执行到集成测试阶段就挂了。

此时，我们需要将单元测试和集成测试分开，Maven使用maven-surefire-plugin执行测试，我们可以先将HelloWorldIntegrationTest排除在测试之外，这样单元测试将不会运行该测试，然后在集成测试中，再将HelloWorldIntegrationTest包含进来，此时我们需要修改maven-surefire-plugin的配置。

* 在单元测试排除集成测试配置

```
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <includes>
            <include>**/unit/*Test.java</include>
          </includes>
        </configuration>
        <executions>
          <execution>
            <id>surefire-it</id>
            <phase>integration-test</phase>
            <goals>
              <goal>test</goal>
            </goals>
            <configuration>
              <includes>
                <include>**/*IntegrationTest.java</include>
              </includes>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

现在运行mvn clean install，成功。在上面的例子中，先将只有在unit目录下的以`*Test.java`结尾的类看做测试类，此时不包含HelloWorldIntegrationTest，而在itegration-test阶段，我们将运行以*IntegrationTest.java结尾的测试类。

请注意，此时运行`mvn clean test`不会执行集成测试。

如果要看到了实际的页面，可以将HelloWorldIntegrationTest中的：

```
WebDriver driver = new HtmlUnitDriver();//使用HtmlUnit
```

修改为：

```
 WebDriver driver = new SafariDriver();//打开Safari浏览器
```

或者：

```
WebDriver driver = new InternetExplorerDriver();//打开IE浏览器
```

此时再运行mvn clean install，在浏览器窗口打开访问即可。

## 五、使用自己的repository—Nexus

创建一个专属的Repository（Internal Repository），所有项目都只使用这个专属的Repository下载依赖，部署等。

- 专属Repository有以下优点：
  + 代理外部Repository（比如Maven Central Repository），可以对外部Repository做各种各样的过滤操作，例如可以限制只使用Spring的某个版本。
  + 通过代理，专属Repository还可以起到缓存的作用，这样每个开发者只需要从局域网的专属Repository下载依赖，而不用消耗对外网络资源。
  + 发布自己的项目，如果开发的项目需要被其他团队使用，而又不能发布到外部的Repository中，那么专属Repository是最好的选择。
  + 发布一些购买的第三方软件产品以供公司所有人使用，比如Oracle的数据库Driver。

有多种专属Repository，比如[Nexus](http://www.sonatype.org/nexus/)和[Artifactory](http://www.jfrog.com/home/v_artifactory_opensource_overview)等，你甚至可以用一个[FTP服务器作为一个专属Repository](http://stuartsierra.com/2009/09/08/run-your-own-maven-repository)。本文将以开源的Nexus为例，演示如何将开发的项目部署到Nexus Repository中。

### 1 下载并安装Nexus

[下载](https://www.sonatype.com/download-oss-sonatype)Nexus的war包（本文使用的是nexus-2.5.war），将该war包放在tomcat服务器的webapps目录下，启动tomcat。打开http://localhost:8080/nexus-2.5/，你将看到nexus页面：

点击右上角的“Log In”，输入用户名admin，密码admin123（这是Nexus默认的），此后点击右侧的“Repositories”，显示当前Nexus所管理的Repository，默认情况下Nexus为我们创建了以下主要的Repository：
	* Public Repositories，这是一个Repository Group，它所对应的URL为http://localhost:8080/nexus-2.5/content/groups/public/，该Repository  Group包含了多个Repository，其中包含了Releases、Snapshots、Third Party和Central。Repository Group的作用是我们只需要在自己的项目中配置该Repository Group就行了，它将自动从其所包含的Repository中下载依赖，比如如果我们声明对Spring的依赖，那么根据Repository Group中各个Repository的顺序（可以配置），Nexus将首先从Releases中下载Spring，发现没有，再从Snapshots中下载（极大可能也没有，因为它是个Snapshots的Repository），依次查找，最后可能在Central Repository中找到。在配置项目的Repository时，我们应该首先考虑Public Repositories。
	* 3rd party，该Repository即是存放你公司所购买的第三方软件库的地方，它是一个由Nexus自己维护的一个Repository。
	* Apache Snapshots，这是一个代理Repository，即最终的依赖还是得在Apache官网上去下载，然后缓存在Nexus中。
	* Central，这就是代理Maven Central Repository的Repository。
	* Releases，自己的项目要发布时，就应该发布在这个Repository，它也是Nexus自己维护的Repository，而不是代理。
	* Snapshots，自己项目Snapshot的Repository。

### 2 用Nexus Repository取代Maven Central Repository

在上一步，只是创建了一个专属的Nexus Repository，项目默认还是使用Maven Central Repository，所以这时需要将Maven Central Repository换成自己创建的Nexus Repository，可以通过修改`~/.m2/settings.xml`。在该settings.xml文件中（没有的话可以创建一个），加入以下配置：

```
 <mirrors>
   <mirror>
     <!--This sends everything else to /public -->
     <id>nexus</id>
     <mirrorOf>*</mirrorOf>
     <url>http://localhost:8080/nexus-2.5/content/groups/public</url>
   </mirror>
 </mirrors>
 <profiles>
   <profile>
     <id>nexus</id>
     <!--Enable snapshots for the built in central repo to direct -->
     <!--all requests to nexus via the mirror -->
     <repositories>
       <repository>
         <id>central</id>
         <url>http://central</url>
         <releases><enabled>true</enabled></releases>
         <snapshots><enabled>true</enabled></snapshots>
       </repository>
     </repositories>
    <pluginRepositories>
       <pluginRepository>
         <id>central</id>
         <url>http://central</url>
         <releases><enabled>true</enabled></releases>
         <snapshots><enabled>true</enabled></snapshots>
       </pluginRepository>
     </pluginRepositories>
   </profile>
 </profiles>
 <activeProfiles>
   <!--make the profile active all the time -->
   <activeProfile>nexus</activeProfile>
 </activeProfiles>
```

以上配置通过mirror和profile的方式将central Repository全部转向到我们自己的Public Repositories，包括release版本和snapshot版本（Maven默认允许从Maven Central Repository下载release版本，这是合理的。这样一来，项目中的所有依赖都从Nexus的Public Repositories下载，由于其中包含了对Maven Central Repository的代理，所以此时Maven Central Repository中的类库也是可以间接下载到的。此时可以将`~/.m2/repository/`中所有的内容删除掉，再在项目中执行“mvn clean install”，Maven开始从Nexus 的Public Repositories中下载依赖了。

请注意，此时项目本身不需要做任何修改。只是创建了另一个Repository和修改了Maven的默认配置（学习完本文后，应该需要将`~/.m2/settings.xml`还原，不然如果下次在构建之前的Nexus服务器没有启动，构建将失败。）。

### 3 在项目中配置Nexus Repository的信息

分两步：
* 在项目中指明部署目的Repository的URL。
* 提供用户名和密码。

一个Maven项目而言，如果项目版本号中有“SNAPSHOT”字样，则表示此时的项目是snapshot版本，即处于开发中。否则，Maven则认为这是一个release版本。所以在部署时，需要分别配置这两种发布版本所对应的Repository。在项目的pom.xml文件中配置需要发布的目标Repository：

```
<distributionManagement>
   <repository>
	   <id>releases</id>
	   <name>Nexus Release Repository</name>
	   <url>http://localhost:8080/nexus-2.5/content/repositories/releases/</url>
   </repository>
   <snapshotRepository>
       <id>snapshots</id>
       <name>Nexus Snapshot Repository</name>
       <url>http://localhost:8080/nexus-2.5/content/repositories/snapshots/</url>
   </snapshotRepository>
</distributionManagement>
```

用户名和密码，这些信息当然不能放在项目中，Maven将这些信息的存放在`~/.m2/settings.xml`文件中，每台机器都可以有不同的`settings.xml`文件。在该文件中加入以下配置：

```
<servers>  
  <server>  
    <id>releases</id>  
    <username>admin</username>  
    <password>admin123</password>  
  </server>  
  <server>  
    <id>snapshots</id>  
    <username>admin</username>  
    <password>admin123</password>  
  </server>  
</servers>
```

admin和admin123都是Nexus默认的，特别需要注意的是，这里的<id>需要和上面项目pom.xml文件中配置Repostory的<id>对应起来。

### 4 发布到Nexus Repository

执行命令：

```
mvn clean deploy
```

## 六、使用Profile

* 在开发项目时，设想有以下场景：
	- Maven项目存放在一个远程代码库中，该项目需要访问数据库，两台电脑，一台是Linux，一台是Mac OS X，希望在两台电脑上都能做项目开发。但是，安装Linux的电脑上安装的是MySQL数据库，而Mac OS X的电脑安装的是PostgreSQL数据库。此时需要一种简单的方法在两种数据库连接中进行切换。
	- 项目需要部署。为了调试，在开发时在Java编译结果中加入了调试信息（Java默认）。而在部署时希望Java编译结果中不出现调试信息。

Maven的Profile用于在不同的环境下应用不同的配置。一套配置即称为一个Profile。这里的“环境”可以是操作系统版本，JDK版本或某些文件是否存在这样的物理环境，也可以是自己定义的一套逻辑环境。比如上面所说的Linux和Mac OS X便是一种物理环境，而开发环境和部署环境则为逻辑环境。Maven提供了Activation机制来激活某个Profile，它既允许自动激活（即在某些条件满足时自动使某个Profile生效），也可以手动激活。

一个Profile几乎可以包含所有能够出现在pom.xml中的配置项，比如`<artifactId>`，`<outputDirectory>`等。相当于在Profile中定义的配置信息会覆盖原有pom.xml中的相应配置项。

### 1 定义Profile

通常情况将Profile的定义放在pom.xml文件的最后：

```
<profiles>
   <profile>
       <id>apple</id>
       <activation>
           <activeByDefault>true</activeByDefault>
       </activation>
       <properties>
           <fruit>APPLE</fruit>
       </properties>
   </profile>
   <profile>
       <id>banana</id>
       <properties>
           <fruit>BANANA</fruit>
       </properties>
   </profile>
</profiles>
```

在上面的配置中，定义了两个Profile，一个id为apple，该Profile将fruit属性设置为APPLE，另一个id为banana，它将fruit属性设置为BANANA。此外，第一个Profile还配置了`<activeByDefault>true</activeByDefault>`，表明该Profile默认即是生效的。

为了打印出fruit这个属性，再向pom.xml中添加一个maven-antrun-plugin插件，可以通过该插件的echo任务来打印属性信息。将该打印配置在Maven的initialize阶段（任何阶段都可以）：
```
<plugin>
   <artifactId>maven-antrun-plugin</artifactId>
   <executions>
       <execution>
           <phase>initialize</phase>
           <goals>
               <goal>run</goal>
           </goals>
           <configuration>
               <tasks>
                   <echo>Fruit:${fruit}</echo>
               </tasks>
           </configuration>
       </execution>
   </executions>
</plugin>
```

配置完成之后，执行：`mvn initialize`，由于id为apple的Profile默认生效，此时将在终端输出“APPLE”字样：

```
......
[INFO] Executing tasks
    [echo] Fruit:APPLE
[INFO] Executed tasks
......
```

如果要使用id为banana的Profile，我们可以显式地指定使用该Profile：`mvn initialize -Pbanana`：

```
......
[INFO] Executing tasks
    [echo] Fruit:BANANA
[INFO] Executed tasks
......
```

### 2 手动激活Profile

在上面的例子中，在显示“BANANA”时便使用了手动激活Profile的方式。手动激活Profile要求在运行mvn命令时通过`-PprofileId`的方式指定使用某个Profile。对于上文提到的第二点，可以通过一下配置完成：

```
<profile>
	<id>production</id>
	<build>
	   <plugins>
	       <plugin>
	           <groupId>org.apache.maven.plugins</groupId>
	           <artifactId>maven-compiler-plugin</artifactId>
	           <configuration>
	               <debug>false</debug>
	           </configuration>
	       </plugin>
	   </plugins>
	</build>
</profile>
```

在开发时，我们使用`mvn clean install`，此时名为production的Profile并没有被激活，所以还是采用Java编译默认的配置（即在结果中包含了调试信息）。当需要为生产部署环境编译时，便可以使用`mvn clean install -Pproduction`。

Maven的Profile机制最大的好处在于它的自动激活性，因为如果手动激活，在运行mvn命令时依然需要告诉Maven一些信息（即这里的`-PprofileId`）来完成配置，完全可以通过另外的方法来达到相同的目的。比如，可以定义一个父pom和两个子pom（比如pom.xml和pomB.xml），在父pom中我们存放两个子pom共享的配置（比如上面的maven-antrun-plugin），而在两个子pom中分别配置不同的信息以代表不同的环境，比如在pom.xml中（默认执行的pom），我们将fruit属性设置成APPLE，而在pomB.xml中，将fruit属性设置成BANANA。此时，pom.xml和pomB.xml都继承自父pom。虽然在默认情况下Maven会将名为“pom.xml”的文件作为输入文件，但是我们通过“-f”参数来指定其他pom文件。比如，如果要显示“APPLE”，我们可以直接执行`mvn initialize`，如果要显示“BANANA”，则可以执行`mvn initialize -f pomB.xml`。

### 3 自动激活Profile

在自动激活Profile中，需要为某个Profile预先定义一些前提条件（比如操作系统版本），当这些前提条件满足时，该Profile将被自动激活。比如，对于上文中的第一点，可以为Mac OS X和Linux(Unix)分别定义一套数据库连接：

```
<profile>
   <id>mac</id>
   <activation>
       <activeByDefault>false</activeByDefault>
       <os>
           <family>mac</family>
       </os>
   </activation>
   <properties>
       <database.driverClassName>org.postgresql.Driver</database.driverClassName>
       <database.url>jdbc:postgresql://localhost/database</database.url>
       <database.user>username</database.user>
       <database.password>password</database.password>
   </properties>
</profile>
<profile>
   <id>unix</id>
   <activation>
       <activeByDefault>false</activeByDefault>
       <os>
           <family>unix</family>
       </os>
   </activation>
   <properties>
       <database.driverClassName>com.mysql.jdbc.Driver</database.driverClassName>
       <database.url>jdbc:mysql://localhost:3306/database</database.url>
       <database.user>username</database.user>
       <database.password>password</database.password>
   </properties>
</profile>
```

请注意，以上两个Profile在默认情况下都没有被激活，Maven在运行时会检查操作系统，如果操作系统为Mac OS X，那么Maven将自动激活id为mac的Profile，此时将使用PostgreSQL的数据库链接，如果操作系统为Linux或Unix，那么将使用MySQL数据库连接。更多的Profile自动激活条件，请参考[此文档](http://docs.codehaus.org/display/MAVENUSER/Profiles)。

## 七、处理依赖冲突

在使用Maven时是否遇到过诸如`NoSuchMethodError`或`ClassNotFoundException`之类的问题，甚至发生这些问题的Java类没都没有听说过。要搞清楚这里面的缘由，我们得学习Maven对依赖冲突的处理机制。

Maven采用“最近获胜策略（nearest wins strategy）”的方式处理依赖冲突，即如果一个项目最终依赖于相同artifact的多个版本，在依赖树中离项目最近的那个版本将被使用。让我们来看看一个实际的例子。

有一个web应用resolve-web，该工程依赖于project-A和project-B，project-A依赖于project-common的1.0版本并调用其中的sayHello()方法。project-B依赖于project-C，而project-C又进一步依赖于project-common的2.0版本并调用其中的sayGoodBye()方法。project-common的1.0和2.0版本是不同的，1.0中之包含sayHello()方法，而2.0中包含了sayHello()和sayGoodBye()两个方法。
根据Maven的transitive依赖机制，resolve-web将同时依赖于project-common的1.0和2.0版本，这就造成了依赖冲突。而根据最近获胜策略，Maven将选择project-common的1.0版本作为最终的依赖。
由于proejct-common的1.0版本比2.0版本在依赖树中离resolve-web更近，故1.0版本获胜。在resolve-web中执行"mvn dependency:tree -Dverbose"可以看到resolve-web的依赖关系：

```
[INFO] resolve-web:resolve-web:war:1.0-SNAPSHOT
[INFO] +- junit:junit:jar:3.8.1:test
[INFO] +- project-B:project-B:jar:1.0:compile
[INFO] |  \- project-C:project-C:jar:1.0:compile
[INFO] |     \- (project-common:project-commmon:jar:2.0:compile - omitted for conflict with 1.0)
[INFO] +- project-A:project-A:jar:1.0:compile
[INFO] |  \- project-common:project-commmon:jar:1.0:compile
[INFO] \- javax.servlet:servlet-api:jar:2.4:provided
```

由上可知，project-common:project-commmon:jar:2.0被忽略掉了。此时在resolve-web的war包中将只包含project-common的1.0版本，于是问题来了。由于project-common的1.0版本中不包含sayGoodBye()方法，而该方法正是project-C所需要的，所以运行时将出现“NoSuchMethodError”。

对于这种有依赖冲突所导致的问题，有两种解决方法：

* 方法1：显式加入对project-common 2.0版本的依赖。
	先前的2.0版本不是离resolve-web远了点吗，那我们就直接将它作为resolve-web的依赖，这不就比1.0版本离resolve-web还近吗？在resove-web的pom.xml文件中直接加上对project-common 2.0 的依赖：

	```
	<dependency>       
	   <groupId>project-common</groupId>      
	   <artifactId>project-commmon</artifactId>  
	   <version>2.0</version>   
	</dependency>  
	```

* 方法2：resolve-web对project-A的dependency声明中，将project-common排除掉。
	在resolve-web的pom.xml文件中修改对project-A的dependency声明：
	```
	<dependency>  
      <groupId>project-A</groupId>  
      <artifactId>project-A</artifactId>  
      <version>1.0</version>  
      <exclusions>  
	      <exclusion>  
	          <groupId>project-common</groupId>  
	          <artifactId>project-commmon</artifactId>  
	      </exclusion>  
      </exclusions>  
	</dependency>  
	```
此时再在resolve-web中执行"mvn dependency:tree -Dverbose"，结果如下：

```
......
[INFO] resolve-web:resolve-web:war:1.0-SNAPSHOT
[INFO] +- junit:junit:jar:3.8.1:test
[INFO] +- project-B:project-B:jar:1.0:compile
[INFO] |  \- project-C:project-C:jar:1.0:compile
[INFO] |     \- project-common:project-commmon:jar:2.0:compile
[INFO] +- project-A:project-A:jar:1.0:compile
[INFO] \- javax.servlet:servlet-api:jar:2.4:provided
......
```

此时的依赖树中已经不包含project-common的1.0版本了。

另外，我们还可以在project-A中将对project-common的依赖声明为optional，optional即表示非transitive，此时当在resolve-web中引用project-A时，Maven并不会将project-common作为transitive依赖自动加入，除非有别的项目（比如project-B）声明了对project-common的transitive依赖或者我们在resolve-web中显式声明对project-common的依赖（方法一）。

## 八、编写自己的Plugin

Maven就其本身来说只是提供一个执行环境，它并不知道需要在项目上完成什么操作，真正操作项目的是插件（plugin），比如编译Java有Compiler插件，打包有Jar插件等。所以要让Maven完成各种各样的任务，需要配置不同的插件，甚至自己编写插件。

Maven在默认情况下已经配置了一些常用的插件，上面的Compiler和Jar便在这些插件之列。要查看Maven的默认插件，需要找到Super Pom，Super Pom位于`M2_HOME/lib/maven-2.2.1-uber.jar`文件中，文件名为`pom-4.0.0.xml`，里面包含了Maven所有的默认插件：

```
<pluginManagement>       
   <plugins>       
     <plugin>       
       <artifactId>maven-antrun-plugin</artifactId>       
       <version>1.3</version>       
     </plugin>              
     <plugin>       
       <artifactId>maven-assembly-plugin</artifactId>       
       <version>2.2-beta-2</version>       
     </plugin>                
     <plugin>       
       <artifactId>maven-clean-plugin</artifactId>       
       <version>2.2</version>       
     </plugin>       
     <plugin>       
       <artifactId>maven-compiler-plugin</artifactId>       
       <version>2.0.2</version>       
     </plugin>  
      ......  
   </plugins>  
</pluginManagement>  
```

任何Maven工程默认都继承自这个Super Pom，也可以在自己的项目中执行: 

```
mvn help:effective-pom
```

来查看包括继承内容的整个pom文件，其中便包含了从Super Pom继承下来的内容。

和其他Maven项目一样，Maven的插件也是一种packaging类型（类型为maven-plugin），也拥有groupId，artifactId和version。简单地讲，一个Maven插件包含了一些列的goal，每一个goal对应于一个Mojo类（Maven Old Java Object，命名来自于Pojo），每个Mojo都需要实现org.apache.maven.plugin.Mojo接口，该接口定义了一个execute方法，在开发插件时，你的任务就是实现这个execute方法。

### 1 创建自己的插件

先通过archetype创建一个Maven插件工程:

```
mvn archetype:generate -DgroupId=com.example.plugin -DartifactId=demo-maven-plugin -DarchetypeArtifactId=maven-archetype-mojo -DarchetypeGroupId=org.apache.maven.archetype -DinteractiveMode=false
```

此时打开工程中的pom.xml文件，你可以看到该工程的packaging类型：

```
<packaging>maven-plugin</packaging>
```

向工程中添加一个Mojo类：

```
/** 
 * @goal buildinfo 
 * @phase  pre-integration-test 
 */  
public class BuildInfoMojo extends AbstractMojo {  
   
   /** 
    * @parameter expression="${project}" 
    * @readonly 
    */  
   private MavenProject project;  
   
   /** 
    * @parameter expression="${buildinfo.prefix}" 
    * default-value="+++" 
    */  
   private String prefix;  
   
   public void execute() throws MojoExecutionException {  
       Build build = project.getBuild();  
       String outputDirectory = build.getOutputDirectory();  
       String sourceDirectory = build.getSourceDirectory();  
       String testOutputDirectory = build.getTestOutputDirectory();  
       String testSourceDirectory = build.getTestSourceDirectory();  
       getLog().info("\n==========================\nProject build info:");  
       String[] info = {outputDirectory, sourceDirectory, testOutputDirectory, testSourceDirectory};  
       for (String item : info) {  
           getLog().info("\t" + prefix + "   " + item);  
       }  
       getLog().info("=======================");  
   }  
} 
```

在上面的代码中，`@goal buildinfo`表示该Mojo对应的goal的名字为buildinfo（在调用该goal时需要给出它的名字），“@phase   pre-integration-test”表示该Mojo默认被绑定在了pre-integration-test阶段。之后的：

```
/**  
 * @parameter expression="${project}"  
 * @readonly  
 */  
 private MavenProject project;  
```

表示该插件持有一个到MavenProject的引用，当客户方在执行该插件时，这里的project字段便表示客户工程。这里我们并没有对project进行初始化，但是“@parameter expression="${project}"”中的${project}即表示当前的客户工程，Maven在运行时会通过依赖注入自动将客户工程对象赋给project字段（请参考Maven自己的IoC容器[Plexus](http://plexus.codehaus.org/)）。此外，我们还声明了一个prefix字段，该字段表示对输出的各行加上prefix前缀字符串，默认为“+++”（加入prefix字段主要用于演示对插件参数的配置，这里的project和prefix都表示插件参数，我们可以在客户方重新配置这些参数）。

由于上面的代码用到了MavenProject类，我们还需要在该插件工程的pom.xml中加入以下依赖：

```
<dependency>  
   <groupId>org.apache.maven</groupId>  
   <artifactId>maven-project</artifactId>  
   <version>2.2.1</version>  
 </dependency> 
```

在执行了“mvn clean install“之后，我们便可以通过一下命令调用该Mojo：

```
mvn com.example.plugin:demo-maven-plugin:1.0-SNAPSHOT:buildinfo
```

在当前的插件工程中执行该命令输出结果如下：

```
......
==========================
Project build info:
[INFO] +++   /home/user/Desktop/demo-maven-plugin/demo-maven-pugin/target/classes
[INFO] +++   /home/user/Desktop/demo-maven-plugin/demo-maven-pugin/src/main/java
[INFO] +++   /home/user/Desktop/demo-maven-plugin/demo-maven-pugin/target/test-classes
[INFO] +++   /home/user/Desktop/demo-maven-plugin/demo-maven-pugin/src/test/java
[INFO] =======================
......
```

以上的”+++“便表示prefix的默认属性值，后跟各个build目录。我们也可以通过"-D"参数为prefix重新赋值：

```
mvn com.example.plugin:demo-maven-plugin:1.0-SNAPSHOT:buildinfo -Dbuildinfo.prefix=---
```

以上我们用"---"代替了默认的"+++"。

你可能注意到，为了调用该插件的buildinfo这个goal，我们需要给出该插件的所有坐标信息，包括groupId，artifactId和version号。你可能之前已经执行过`mvn eclipase:eclipase`或`mvn idea:idea`这样简洁的命令，让我们也来将自己的插件调用变简单一点。

要通过简单别名的方式调用Maven插件，我们需要做到以下两点：
* 插件的artifactId应该遵循`***-maven-plugin`或`maven-***-plugin`命名规则，对于本文中的插件，我们已经遵循了。（当然不遵循也是可以的，此时你需要使用Maven Plugin Plugin来设置goalPrefix，此时就不见得为“demo”了）
* 需要将插件的groupId放在Maven默认的插件搜寻范围之内，默认情况下Maven只会在org.apache.maven.plugins和org.codehaus.mojo两个groupId下搜索插件，要让Maven同时搜索我们自己的groupId，我们需要在`~/.m2/settings.xml`中加入：

```
<pluginGroups>  
    <pluginGroup>com.example.plugin</pluginGroup>  
</pluginGroups>
```

在达到以上两点之后，我们便可以通过以下命令来调用自己的插件了：

```
mvn demo:buildinfo
```

### 2 在别的项目使用插件

要在别的项目中应用插件也是简单的，我们只需要在该项目的pom.xml文件中声明该插件的即可：

```
<plugin>  
  <groupId>com.example.plugin</groupId>  
  <artifactId>demo-maven-plugin</artifactId>  
  <version>1.0-SNAPSHOT</version>  
  <configuration>  
     <prefix>---</prefix>  
  </configuration>  
  <executions>  
	  <execution>  
	     <id>buildinfo</id>  
	     <phase>process-sources</phase>  
	     <goals>  
	        <goal>buildinfo</goal>  
	     </goals>  
	  </execution>  
  </executions>  
</plugin>  
```

在上面的配置中，我们将demo-maven-plugin插件的buildinfo绑定在了process-sources阶段，并将prefix参数该为了"---"，这样在执行"mvn clean install" 时，该插件的输出内容将显示在终端。另外，我们可以通过设置属性的方式为demo-maven-plugin的prefix参数赋值，在pom.xml中加入一下property：

```
<properties>  
   <buildinfo.prefix>---</buildinfo.prefix>  
</properties> 
```

此时，去掉plugin配置中的：

```
<configuration>  
   <prefix>---</prefix>  
</configuration>  
```

运行`mvn clean install`，输出效果和之前一样。
