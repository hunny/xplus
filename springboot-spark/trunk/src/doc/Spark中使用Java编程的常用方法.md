# Spark中使用Java编程的常用方法

## 初始化SparkContext

```java
  System.setProperty("hadoop.home.dir", "D:\\spark-1.6.1-bin-hadoop2.6\\spark-1.6.1-bin-hadoop2.6");
  SparkConf conf = new SparkConf().setAppName("spark test1").setMaster("local[2]");
  JavaSparkContext context = new JavaSparkContext(conf);
```

```java
  // JavaSparkContext(master: String, appName: String, sparkHome: String, jars: Array[String], environment: Map[String, String])  
  JavaSparkContext ctx = new JavaSparkContext("yarn-standalone", //
  	"JavaWordCount", // 
    System.getenv("SPARK_HOME"), //
    JavaSparkContext.jarOfClass(mysparktest.class)); //
  //也可以使用ctx获取环境变量，例如下面的语句  
  System.out.println("spark home:" + ctx.getSparkHome());  
```

### Spark属性

* Spark属性控制大部分的应用程序设置，并且为每个应用程序分别配置它。
* 这些属性可以直接在SparkConf上配置，然后传递给SparkContext。
* SparkConf允许配置一些通用的属性（如master URL、应用程序名称等等）以及通过set()方法设置的任意键值对。例如，我们可以用如下方式创建一个拥有两个线程的应用程序。
* 注意，local[2]代表2个本地线程 – 这是最小的并发方式，可以帮助我们发现一些只有在分布式上下文才能复现的bug。

```java
SparkConf conf = new SparkConf()  //
             .setMaster("local[2]")  //
             .setAppName("CountingSheep")  //
             .set("spark.executor.memory", "1g");  //
JavaSparkContext context = new JavaSparkContext(conf);
```

* 注意，本地模式下，我们可以使用n个线程（n >= 1）。而且在像Spark Streaming这样的场景下，我们可能需要多个线程来防止类似线程饿死这样的问题。
* 配置时间段的属性应该写明时间单位，如下格式都是可接受的：

```
25ms (milliseconds)
5s (seconds)
10m or 10min (minutes)
3h (hours)
5d (days)
1y (years)
```

* 配置大小的属性也应该写明单位，如下格式都是可接受的：

```
1b (bytes)
1k or 1kb (kibibytes = 1024 bytes)
1m or 1mb (mebibytes = 1024 kibibytes)
1g or 1gb (gibibytes = 1024 mebibytes)
1t or 1tb (tebibytes = 1024 gibibytes)
1p or 1pb (pebibytes = 1024 tebibytes)
```


### 动态加载Spark属性

* Spark允许简单地创建一个空conf:

```java
JavaSparkContext context = new JavaSparkContext(new SparkConf())  
```

* 然后在运行时设置变量：

```
./bin/spark-submit --name "My app" --master local[4] --conf spark.shuffle.spill=false  
  --conf "spark.executor.extraJavaOptions=-XX:+PrintGCDetails -XX:+PrintGCTimeStamps" myApp.jar
```

* 任何标签指定的值或者在配置文件中的值将会传递给应用程序，并且通过`SparkConf`合并这些值。在S`parkConf`上设置的属性具有最高的优先级，其次是传递给`spark-submit`或者`spark-shell`的属性值，最后是`spark-defaults.conf`文件中的属性值。
优先级顺序：

```
SparkConf > CLI > spark-defaults.conf
```

### 查看Spark属性

* 在`http://<driver>:4040`上的应用程序Web UI在Environment标签中列出了所有的Spark属性。
* 注意：只有通过`spark-defaults.conf`, `SparkConf`以及命令行直接指定的值才会显示。对于其它的配置属性，可以认为程序用到了默认的值。

#### 应用程序属性

| 属性名称 | 默认值 | 含义 |
| --- | --- | --- |
| spark.app.name | (none) | 应用程序的名字。这将在UI和日志数据中出现 |
| spark.driver.cores | 1 | driver程序运行需要的cpu内核数 |
| spark.driver.maxResultSize | 1g | 每个Spark action(如collect)所有分区的序列化结果的总大小限制。设置的值应该不小于1m，0代表没有限制。如果总大小超过这个限制，程序将会终止。大的限制值可能导致driver出现内存溢出错误（依赖于spark.driver.memory和JVM中对象的内存消耗）。 |
| spark.driver.memory | 512m | driver进程使用的内存数 |
| spark.executor.memory | 512m | 每个executor进程使用的内存数。和JVM内存串拥有相同的格式（如512m,2g） |
| spark.extraListeners | (none) | 注册监听器，需要实现SparkListener |
| spark.local.dir | /tmp | Spark中暂存空间的使用目录。在Spark1.0以及更高的版本中，这个属性被SPARK_LOCAL_DIRS(Standalone, Mesos)和LOCAL_DIRS(YARN)环境变量覆盖。 |
| spark.logConf | false | 当SparkContext启动时，将有效的SparkConf记录为INFO。 |
| spark.master | (none) | 集群管理器连接的地方 |

#### Spark UI

| 属性名 | 默认值 | 含义 |
| --- | --- | --- |
| spark.eventLog.compress | false | 是否压缩事件日志（当然spark.eventLog.enabled必须开启） |
| spark.eventLog.dir | file:///tmp/spark-events | Spark events日志的基础目录（当然spark.eventLog.enabled必须开启）。在这个目录中，spark会给每个应用创建一个单独的子目录，然后把应用的events log打到子目录里。用户可以设置一个统一的位置（比如一个HDFS目录），这样history server就可以从这里读取历史文件。 |
| spark.eventLog.enabled | false | 是否启用Spark事件日志。如果Spark应用结束后，仍需要在SparkUI上查看其状态，必须启用这个。 |
| spark.ui.killEnabled | true | 允许从SparkUI上杀掉stage以及对应的作业（job） |
| spark.ui.port | 4040 | SparkUI端口，展示应用程序运行状态。 |
| spark.ui.retainedJobs | 1000 | SparkUI和status API最多保留多少个spark作业的数据（当然是在垃圾回收之前） |
| spark.ui.retainedStages | 1000 | SparkUI和status API最多保留多少个spark步骤（stage）的数据（当然是在垃圾回收之前） |
| spark.worker.ui.retainedExecutors | 1000 | SparkUI和status API最多保留多少个已结束的执行器（executor）的数据（当然是在垃圾回收之前） |
| spark.worker.ui.retainedDrivers | 1000 | SparkUI和status API最多保留多少个已结束的驱动器（driver）的数据（当然是在垃圾回收之前） |
| spark.sql.ui.retainedExecutions | 1000 | SparkUI和status API最多保留多少个已结束的执行计划（execution）的数据（当然是在垃圾回收之前） |
| spark.streaming.ui.retainedBatches | 1000 | SparkUI和status API最多保留多少个已结束的批量（batch）的数据（当然是在垃圾回收之前） |

### 环境变量

* 有些Spark设置需要通过环境变量来设定，这些环境变量可以在`${SPARK_HOME}/conf/spark-env.sh`脚本（Windows下是`conf/spark-env.cmd`）中设置。如果是独立部署或者Mesos模式，这个文件可以指定机器相关信息（如hostname）。运行本地Spark应用或者submit脚本时，也会引用这个文件。
* 注意，`conf/spark-env.sh`默认是不存在的。需要复制`conf/spark-env.sh.template`这个模板来创建，还有注意给这个文件附上可执行权限。
* 以下变量可以在spark-env.sh中设置：

| 环境变量 | 含义 |
| --- | --- |
| JAVA_HOME | Java安装目录（如果没有在PATH变量中指定） |
| PYSPARK_PYTHON | 驱动器和worker上使用的Python二进制可执行文件（默认是python） |
| PYSPARK_DRIVER_PYTHON | 仅在驱动上使用的Python二进制可执行文件（默认同PYSPARK_PYTHON） |
| SPARKR_DRIVER_R | SparkR shell使用的R二进制可执行文件（默认是R） |
| SPARK_LOCAL_IP | 本地绑定的IP |
| SPARK_PUBLIC_DNS | Spark程序公布给其他机器的hostname |

## 使用parallelize方法

* 创建RDD最简单的方式就是把程序中一个已有的集合传给SparkContext的parallelize()方法

```java
  JavaRDD lines = context.parallelize(Arrays.asList("pandas", "i like pandas"));
  System.out.println(lines.collect());
```

输出：[pandas, i like pandas]

## RDD操作（filter方法）

* RDD支持两种操作：转化操作和行动操作。
* RDD的转化操作是返回一个新的RDD的操作，比如map()和filter()，而行动操作则是想驱动器程序返回结果或把结果写入外部系统的操作，会触发实际的计算，比如count()和first()。

```java
  JavaRDD inputRDD = context.textFile("D:\\log\\521.txt");
  JavaRDD errorsRDD = inputRDD.filter(
    new Function(){
      @Override
      public Boolean call(String x) throws Exception {
        return x.contains("error");
      }
    });
  System.out.println("errors显示为：" + errorsRDD.collect());
  System.out.println("errors个数为：" + errorsRDD.count());
```

其中521.log为android的logcat文件，里面包含很多错误信息。

## 使用lambda表达式

* Java8 开始支持lambda表达式，可以简洁地实现函数接口。

```java
  JavaRDD inputRDD = context.textFile("D:\\log\\521.txt");
  JavaRDD errors  = inputRDD.filter(s -> s.contains("error"));
  System.out.println(errors.count());
```

输出：23

## 使用map方法

* 将函数应用于RDD中的每个元素，将返回值构成新的RDD

```java
  JavaRDD rdd = context.parallelize(Arrays.asList(1, 3, 5, 7));
  JavaRDD result = rdd.map(
    new Function(){
      @Override
      public Integer call(Integer x) throws Exception {
        return x * x;
      }
    });
  System.out.println(StringUtils.join(result.collect(), ","));
```

输出：1,9,25,49

## 使用flatMap方法

* 将函数应用于RDD中的每个元素，将返回的迭代器的所有内容构成新的RDD，通常用来切分单词。与map的区别是：这个函数返回的值是list的一个，去除原有的格式

```java
  JavaRDD lines = context.parallelize(Arrays.asList("hello world", "hi"));
  JavaRDD words = lines.flatMap(
    new FlatMapFunction(){
      @Override
      public Iterable call(String lines) throws Exception {
        return Arrays.asList(lines.split(" "));
      }
    });
  System.out.println(words.collect());
  System.out.println(words.first());
```

* 输出：

```
[hello, world, hi]
hello
```

## 使用PairRDD方法

* Spark为包含键值对类型的RDD提供了一些专有的操作，这些RDD称为pair RDD。当需要把一个普通的RDD转为pair RDD时，可以调用map()函数来实现。

```java
  JavaRDD lines = context.parallelize(Arrays.asList("hello world", "hangtian is from hangzhou", "hi", "hi"));
  PairFunction keyData = new PairFunction() {
    @Override
    public Tuple2 call(String x) throws Exception {
      return new Tuple2(x.split(" ")[0], x);
    }
  };
  JavaPairRDD pairs = (JavaPairRDD) lines.mapToPair(keyData);
  System.out.println(pairs.collect());
```

输出：

```
[(hello,hello world), (hangtian,hangtian is from hangzhou), (hi,hi), (hi,hi)]
```

## 计算单词个数

```java
  JavaRDD input = context.textFile("D:\\test.txt");
  JavaRDD words = input.flatMap(new FlatMapFunction() {
    @Override
    public Iterable call(String x) throws Exception {
      return Arrays.asList(x.split(" "));
    }
  });
  JavaPairRDD wordspair = words.mapToPair(new PairFunction() {
    @Override
    public Tuple2 call(String x) throws Exception {
      return new Tuple2(x, 1);
    }
  });
  JavaPairRDD result = wordspair.reduceByKey(new Function2() {
    @Override
    public Integer call(Integer x, Integer y) throws Exception {
      return x + y;
    }
  });
  System.out.println(result.sortByKey().collect());
```

输出：

```
[(,2), (are,1), (can,1), (go,1), (i,2), (love,1), (me,1), (much,1), (ok?,1), (should,1), (so,2), (with,1), (you,3)]
```

## 使用Accumulator方法

* Spark有两种共享变量：累加器和广播变量。
* 累加器用来对信息进行聚合，而广播变量用来高效分发较大的对象。
* 累加器提供了将工作节点中的值聚合到驱动器程序中的简单语法。

```java
  JavaRDD rdd = context.textFile("D:\\test.txt");
  final Accumulator blankLines = context.accumulator(0);
  JavaRDD callSigns = rdd.flatMap(new FlatMapFunction() {
    @Override
    public Iterable call(String line) throws Exception {
      if (line.equals("")) {
        blankLines.add(1);
      }
      return Arrays.asList(line.split(" "));
    }
  });
  System.out.println(callSigns.collect());
  System.out.println("Blank lines: " + blankLines.value()); 
```

输出：

```
[i, love, you, so, much, , so, i, should, you, can, go, with, me, , are, you, ok?]
Blank lines: 2
```

## Spark SQL使用

* Spark提供Spark SQL来操作结构化和半结构化数据。就是说，可以使用sql语句操作json和txt文件进行数据查询等操作。

```java
  JavaRDD rdd = context.textFile("D:\\test.json");
  SQLContext sqlContext = SQLContext.getOrCreate(rdd.context());
  DataFrame dataFrame = sqlContext.read().json(rdd);
  dataFrame.registerTempTable("person");
  DataFrame resultDataFrame = sqlContext.sql("select * from person where lovesPandas=true");
  resultDataFrame.show(false);
```

输出：

```
+-----------+---------+
|lovesPandas|name     |
+-----------+---------+
|true       |nanchang |
|true       |qier     |
|true       |kongshuai|
+-----------+---------+
```

## Spark Stream使用

* 用来实时计算数据，其构造函数接口用来指定多久时间处理一次新数据的批次间隔作为输入。
* 以下代码在本地未能执行通过。设想是把netcat工具作为输入源，在程序中打印输入信息并进行处理

```java
 JavaStreamingContext jssc = new JavaStreamingContext(conf, new Duration(2000));
 JavaDStream lines = jssc.socketTextStream("localhost", 7778);
 lines.print();
 jssc.start();
 jssc.awaitTermination();
```

以上代码运行还需要删除最上面的context初始化的代码。

## 一个备注说明示例

```java
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class MySpark {
  
  public static void main(String[] args) throws Exception {

    // context ，用于读文件 ，类似于scala的sc
    // 格式为：
    // JavaSparkContext(master: String, appName: String, sparkHome: String,
    // jars: Array[String], environment: Map[String, String])
    JavaSparkContext ctx = new JavaSparkContext("local[2]", "JavaWordCount",
        System.getenv("SPARK_HOME"), JavaSparkContext.jarOfClass(MySpark.class));

    // 也可以使用ctx获取环境变量，例如下面的语句
    System.out.println("spark home:" + ctx.getSparkHome());

    // 一次一行，String类型 ,还有hadoopfile，sequenceFile什么的 ，可以直接用sc.textFile("path")
    JavaRDD<String> lines = ctx.textFile(args[1], 1); // java.lang.String path,
                                                      // int minSplits
    lines.cache(); // cache，暂时放在缓存中，一般用于哪些可能需要多次使用的RDD，据说这样会减少运行时间

    // collect方法，用于将RDD类型转化为java基本类型，如下
    List<String> line = lines.collect();
    for (String val : line)
      System.out.println(val);

    // 下面这些也是RDD的常用函数
    // lines.collect(); List<String>
    // lines.union(); javaRDD<String>
    // lines.top(1); List<String>
    // lines.count(); long
    // lines.countByValue();

    /**
     * filter test 定义一个返回bool类型的函数，spark运行filter的时候会过滤掉那些返回只为false的数据 String
     * s，中的变量s可以认为就是变量lines（lines可以理解为一系列的String类型数据）的每一条数据
     */
    JavaRDD<String> contaninsE = lines.filter(new Function<String, Boolean>() {
      @Override
      public Boolean call(String s) throws Exception {

        return (s.contains("they"));
      }
    });
    System.out.println("--------------next filter's  result------------------");
    line = contaninsE.collect();
    for (String val : line)
      System.out.println(val);

    /**
     * sample test sample函数使用很简单，用于对数据进行抽样 参数为：withReplacement: Boolean,
     * fraction: Double, seed: Int
     * 
     */

    JavaRDD<String> sampletest = lines.sample(false, 0.1, 5);
    System.out.println("-------------next sample-------------------");
    line = sampletest.collect();
    for (String val : line)
      System.out.println(val);

    /**
     * 
     * new FlatMapFunction<String, String>两个string分别代表输入和输出类型
     * Override的call方法需要自己实现一个转换的方法，并返回一个Iterable的结构
     * 
     * flatmap属于一类非常常用的spark函数，简单的说作用就是将一条rdd数据使用你定义的函数给分解成多条rdd数据
     * 例如，当前状态下，lines这个rdd类型的变量中，每一条数据都是一行String，我们现在想把他拆分成1个个的词的话， 可以这样写 ：
     */

    JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public Iterator<String> call(String s) {
        String[] words = s.split(" ");
        return Arrays.asList(words).iterator();
      }
    });

    /**
     * map 键值对 ，类似于MR的map方法 pairFunction<T,K,V>: T:输入类型；K,V：输出键值对 需要重写call方法实现转换
     */
    JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
      @Override
      public Tuple2<String, Integer> call(String s) {
        return new Tuple2<String, Integer>(s, 1);
      }
    });

    // A two-argument function that takes arguments
    // of type T1 and T2 and returns an R.
    /**
     * reduceByKey方法，类似于MR的reduce
     * 要求被操作的数据（即下面实例中的ones）是KV键值对形式，该方法会按照key相同的进行聚合，在两两运算
     */
    JavaPairRDD<String, Integer> counts = ones
        .reduceByKey(new Function2<Integer, Integer, Integer>() {
          @Override
          public Integer call(Integer i1, Integer i2) { // reduce阶段，key相同的value怎么处理的问题
            return i1 + i2;
          }
        });

    // 备注：spark也有reduce方法，输入数据是RDD类型就可以，不需要键值对，
    // reduce方法会对输入进来的所有数据进行两两运算

    /**
     * sort，顾名思义，排序
     */
    JavaPairRDD<String, Integer> sort = counts.sortByKey();
    System.out.println("----------next sort----------------------");

    /**
     * collect方法其实之前已经出现了多次，该方法用于将spark的RDD类型转化为我们熟知的java常见类型
     */
    List<Tuple2<String, Integer>> output = sort.collect();
    for (Tuple2<?, ?> tuple : output) {
      System.out.println(tuple._1 + ": " + tuple._2());
    }

    /**
     * 保存函数，数据输出，spark为结果输出提供了很多接口
     */
    sort.saveAsTextFile("/tmp/spark-tmp/test");

    // sort.saveAsNewAPIHadoopFile();
    // sort.saveAsHadoopFile();
    System.exit(0);
  }
}
```

## 访问Spark监控服务

* [Spark Jobs](http://localhost:7077)
* [Spark UI](http://localhost:4040)

## 参考

* 并发编程网-[《Spark 官方文档》Spark配置](http://ifeve.com/spark-config/)

