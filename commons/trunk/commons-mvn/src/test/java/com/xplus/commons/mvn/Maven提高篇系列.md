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

## 二、多模块VS继承

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

