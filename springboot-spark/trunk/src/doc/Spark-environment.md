[TOC]

# Spark

## 参考

[https://stackoverflow.com/questions/30053449/use-spring-together-with-spark](https://stackoverflow.com/questions/30053449/use-spring-together-with-spark)

## 概述

* 每个Spark应用都包括一个在集群上运行用户的main函数和各种并行操作的驱动程序。Spark提供的主要抽象是一个弹性分布式数据集（RDD），RDD就是一个可以并行操作的分布在集群上各个节点的元素集合。RDDs可以通过位于Hadoop文件系统上的文件（或者任何其他Hadoop支持的文件系统），或者已经存在于驱动程序中的的Scala集合来创建并且变换。用户可能还会要求Spark将RDD持久化在内存中，这样就可以在并行操作之间高效地重复利用这个RDD。最后，RDDs可以从节点失败中自动恢复。

* Spark中的第二个抽象是可以在并行操作中使用的共享变量。默认得，当Spark把一个函数当作一个任务集合在不同的节点上并行运行时，它会为函数中用到的每个变量发送一份拷贝到各个任务。有时，一个变量需要在任务之间共享，或者在任务和驱动程序之间共享。

* Spark支持两种类型的共享变量：
	- 广播变量（broadcast variables），它可以用来在所有节点的内存中缓存一个值；
	- 累加器（accumulators），它是只能做加法的变量，例如计数器和算术器。

## 搭建Spark单节点本地运行环境

### 环境搭建

* 确保JDK 1.8+的环境，并且JDK路径中不要有空格

```
java version "1.8.0_92"
Java(TM) SE Runtime Environment (build 1.8.0_92-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.92-b14, mixed mode)
```

* 下载spark版本并设置环境变量

地址：[http://spark.apache.org/downloads.html](http://spark.apache.org/downloads.html)页面中选择合适的版本，解压之后配置环境变量：

```
SPARK_HOME=....
Path=....;%SPARK_HOME%\bin;
```

* 下载上一步中对应的spark的预编译对应hadoop版本

地址：[https://archive.apache.org/dist/hadoop/common/](https://archive.apache.org/dist/hadoop/common/)，解压之后配置环境变量：

```
HADOOP_HOME=...
Path=....;%HADOOP_HOME%\bin;
```

* 下载并安装scala（非必须）

地址：[http://www.scala-lang.org/download/all.html](http://www.scala-lang.org/download/all.html)

* Windows环境下需要下载winutils.exe，用来设置hadoop权限

地址：[https://github.com/steveloughran/winutils/](https://github.com/steveloughran/winutils/)，选择对应版本的winutils.exe（有些版本的windows下执行winutils.exe时会报系统不兼容之类的错误，降低或升高winutils.exe版本进行尝试）

### 安装成功

运行spark-shell，输出以下：

```
Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
Setting default log level to "WARN".
To adjust logging level use sc.setLogLevel(newLevel). For SparkR, use setLogLevel(newLevel).
17/10/30 15:49:58 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
17/10/30 15:50:01 WARN General: Plugin (Bundle) "org.datanucleus.api.jdo" is already registered. Ensure you dont have multiple JAR versions of the same plugin i
n the classpath. The URL "file:/C:/spark-2.2.0-bin-hadoop2.7/jars/datanucleus-api-jdo-3.2.6.jar" is already registered, and you are trying to register an identi
cal plugin located at URL "file:/C:/spark-2.2.0-bin-hadoop2.7/bin/../jars/datanucleus-api-jdo-3.2.6.jar."
17/10/30 15:50:01 WARN General: Plugin (Bundle) "org.datanucleus.store.rdbms" is already registered. Ensure you dont have multiple JAR versions of the same plug
in in the classpath. The URL "file:/C:/spark-2.2.0-bin-hadoop2.7/bin/../jars/datanucleus-rdbms-3.2.9.jar" is already registered, and you are trying to register
an identical plugin located at URL "file:/C:/spark-2.2.0-bin-hadoop2.7/jars/datanucleus-rdbms-3.2.9.jar."
17/10/30 15:50:01 WARN General: Plugin (Bundle) "org.datanucleus" is already registered. Ensure you dont have multiple JAR versions of the same plugin in the cl
asspath. The URL "file:/C:/spark-2.2.0-bin-hadoop2.7/jars/datanucleus-core-3.2.10.jar" is already registered, and you are trying to register an identical plugin
 located at URL "file:/C:/spark-2.2.0-bin-hadoop2.7/bin/../jars/datanucleus-core-3.2.10.jar."
17/10/30 15:50:14 WARN ObjectStore: Version information not found in metastore. hive.metastore.schema.verification is not enabled so recording the schema versio
n 1.2.0
17/10/30 15:50:15 WARN ObjectStore: Failed to get database default, returning NoSuchObjectException
17/10/30 15:50:17 WARN ObjectStore: Failed to get database global_temp, returning NoSuchObjectException
Spark context Web UI available at http://172.17.2.172:4040
Spark context available as 'sc' (master = local[*], app id = local-1509349799804).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.2.0
      /_/

Using Scala version 2.11.8 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_92)
Type in expressions to have them evaluated.
Type :help for more information.

scala>
```

	- 注意有以下输出，意味着安装成功：

```
Spark context available as 'sc' (master = local[*], app id = local-1509349799804).
Spark session available as 'spark'.
```

### 搭建环境参考

  - [Spark在Windows下的环境搭建](http://blog.csdn.net/u011513853/article/details/52865076)
  - [How to start Spark applications on Windows (aka Why Spark fails with NullPointerException)?](https://stackoverflow.com/questions/32721647/how-to-start-spark-applications-on-windows-aka-why-spark-fails-with-nullpointer)
  - [windows 运行spark或者hadoop程序报winutils.exe错误](http://blog.csdn.net/zhouyan8603/article/details/51873345)


