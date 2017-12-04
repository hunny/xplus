# Spark性能优化

* 序列化、内存、并行度、数据存储格式、Shuffle

## 序列化

### 背景：

在以下过程中，需要对数据进行序列化：
* shuffling data时需要通过网络传输数据
* RDD序列化到磁盘时
* 
### 性能优化点：

Spark默认的序列化类型是Java序列化。Java序列化的优势是兼容性好，不需要自已注册类。劣势是性能差。
为提升性能，建议使用Kryo序列化替代默认的Java序列化。
Kryo序列化的优势是速度快，体积小，劣势是兼容性差，需要自已注册类。
序列化的配置项：spark.serializer
使用方法1
```
val conf = new SparkConf().setMaster(...).setAppName(...)
conf.registerKryoClasses(Array(classOf[MyClass1], classOf[MyClass2]))
val sc = new SparkContext(conf)
```
使用方法2

参考[Spark序列化与压缩](http://blog.cheyo.net/58.html)
注意：还外还有一个叫闭包序列化的配置项，此配置项只支持Java序列化：spark.closure.serializer

## 内存：容量规划

### 背景

Spark中负责具体计算任务的是executor。每个executor上的内存大小是可以配置的。从executor的内存中划出一定的比例用于RDD的缓存，其他内存用于Task的任务计算，比如保存新创建的对象等。executor的内存大小通过spark.executor.memory参数配置，默认是512M。上述比例通过spark.storage.memoryFraction参数配置，默认是0.6。即默认每个executor的内存是512M，其中512M*0.6=307.2M用于RDD缓存，其余 512M*0.4=204.8用于Task任务计算。

### 性能优化点：
* 如果executor报OOM内存不足，需要考虑增大spark.executor.memory。
* 如果频繁Full GC，可能是executor中用于Task任务计算的内存不足:
  - 需要考虑降低spark.storage.memoryFraction的比例，即减小用于缓存的内存大小，增大用于Task任务计算的内存大小。
  - 需要考虑优化RDD中的数据结构，减小数据占用的内存大小。
* 如果频繁Minor GC, 需要考虑增大年轻代内存的大小。

### 相关点：

* 如何查看内存使用情况？
  - 调用cache()进行缓存时，可以在日志中查看到RDD内存大小。此处应该还有其他办法可以查看，不可能要触发cache才能查看。
* 如何查看GC情况？
  - spark-env.sh 中设置 JAVA_OPTS 参数以打印 GC 的相关信息。这样如果有GC发生，就可以在master和work的日志上看到。
JAVA_OPTS=" -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"

## 内存：优化数据结构

### 背景

* 通过优化RDD中存储的数据的数据结构，减小数据占用的内存空间大小。

### 性能优化点：

* 使用Set/Map等集合类型替代Iterator迭代器
  - Set/Map的查询速度接近O(1),而Iterator是O(n)
* 数据结构使用原始数据结构替代集合类
  - RDD中的数据结构使用Scala的原始数据结构替代List、Set等集合类，这样可以得到更好的性能。而fastutil库已经过优化，可以使用。
* 数据结构避免嵌套结构
* 数据结构避免使用String作为Key
  - Java中的String是常量，每个String需要额外占用几十个字节的空间，使用String作为Key效率不高。而且在shuffle过程中，需要比较Key。比较String效率不高。因此需要避免使用String作为Key。

## 内存：调整存储级别

默认的存储级别是MEMORY_ONLY，即以对象的形式只存储在内存中。如果内存不够用，一种优化方式是将存储级别改为MEMORYONLYSER。这种级别会在对象进行序列化后再存入内存，可以将占用的内存空间减小。

### 并行度：

#### 背景1：Map Task的数量

RDD的map task的数量与Partition的数量相同。Partition的数量由创建Partition的方法中指定。
确定Partition数量的原则(优先级)：
* 参数中的numPartitions参数(比如parallelize方法的第二个参数,reduceByKey、groupByKey等方法中的第二个参数)
* spark.default.parallelism参数
* 父RDD切片数
比如使用parallelize创建RDD，其Partition数量依如下顺序确定：
1. 方法的第二个参数 > 2. spark.default.parallelism参数 > 3. 按照“2-4 partitions for each CPU core”的 原则自动设置partition的数量。
比如使用textfile方法创建RDD：其Partition数量依如下顺序确定：1. 方法的第二个参数(大于实际的block数量) > 2. block数量。如果是HDFS，block大小默认是64MB或128MB。
比如使用reduceByKey方法创建RDD：其Partition数量依如下顺序确定：1. 方法的第二个参数 > 2. spark.default.parallelism参数 > 3. 所有依赖的RDD中，Partition最多的RDD的Partition的数量。

#### 背景2：

Spark进行并行计算时，同时进行计算的Task数量并不是并行度设置的值，而是整个集群的CPU核数。因为每个CPU核，每次只能处理一个任务。

#### 性能优化点：

假设有360G的数据需要处理。当前有三台服务器，每台服务器32个CPU核心，每台服务器256G内存。spark.executor.memory为128G，cache比例为0.6。则每台服务器可用于Task计算的内存为：128G * 0.4 = 76.8G。
此时，如果并行度设置为120。则每台服务器上同时执行的Task数量为：32个（CPU核数）。同时执行的Task占用的内存为：(360G/120)*32核=96GB——仅输入数据的大小，不含中间对象等其他内存占用96GB > 76.8。所以，此时必然会内存不足。
解决办法，提高并行度。比如调整到360。则：则每台服务器上同时执行的Task数量为：32个（CPU核数）。同时执行的Task占用的内存为：(360G/360)*32核=32GB——仅输入数据的大小，不含中间对象等其他内存占用32GB > 76.8。

## 数据存储格式
当Spark只读取文件中的部分列时，此时可以将文件的存储格式设计为采用列存储格式。这有助于提升数据读取性能。

## Shuffle过程优化

### 背景

一般情况下，Shuffle过程中，需要N*M个文件（N是Map任务数，M是Shuffle任务数）。过多的中间文件，可能会导致性能下降。

### 性能优化点
通过如下配置，可以合并部分Shuffle中间文件，减少中间文件数量：
spark.shuffle.consolidateFiles=true