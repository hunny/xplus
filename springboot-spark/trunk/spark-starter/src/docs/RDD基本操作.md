# RDD基本操作

　　本文主要是讲解spark里RDD的基础操作。RDD是spark特有的数据模型，谈到RDD就会提到什么弹性分布式数据集，什么有向无环图，本文暂时不去展开这些高深概念，在阅读本文时候，大家可以就把RDD当作一个数组，这样的理解对我们学习RDD的API是非常有帮助的。本文所有示例代码都是使用scala语言编写的。

　　Spark里的计算都是操作RDD进行，那么学习RDD的第一个问题就是如何构建RDD，构建RDD从数据来源角度分为两类：第一类是从内存里直接读取数据，第二类就是从文件系统里读取，当然这里的文件系统种类很多常见的就是HDFS以及本地文件系统了。

　　第一类方式从内存里构造RDD，使用的方法：makeRDD和parallelize方法，如下代码所示：

```
/* 使用makeRDD创建RDD */
/* List */
val rdd01 = sc.makeRDD(List(1,2,3,4,5,6))
val r01 = rdd01.map { x => x * x }
println(r01.collect().mkString(","))
/* Array */
val rdd02 = sc.makeRDD(Array(1,2,3,4,5,6))
val r02 = rdd02.filter { x => x < 5}
println(r02.collect().mkString(","))
 
val rdd03 = sc.parallelize(List(1,2,3,4,5,6), 1)
val r03 = rdd03.map { x => x + 1 }
println(r03.collect().mkString(","))
/* Array */
val rdd04 = sc.parallelize(List(1,2,3,4,5,6), 1)
val r04 = rdd04.filter { x => x > 3 }
println(r04.collect().mkString(","))
```

　　大家看到了RDD本质就是一个数组，因此构造数据时候使用的是List（链表）和Array（数组）类型。
　　第二类方式是通过文件系统构造RDD，代码如下所示：

```
val rdd:RDD[String] = sc.textFile("file:///D:/sparkdata.txt", 1)
val r:RDD[String] = rdd.flatMap { x => x.split(",") }
println(r.collect().mkString(","))
```

　　这里例子使用的是本地文件系统，所以文件路径协议前缀是file://。
　　构造了RDD对象了，接下来就是如何操作RDD对象了，RDD的操作分为转化操作（transformation）和行动操作（action），RDD之所以将操作分成这两类这是和RDD惰性运算有关，当RDD执行转化操作时候，实际计算并没有被执行，只有当RDD执行行动操作时候才会促发计算任务提交，执行相应的计算操作。区别转化操作和行动操作也非常简单，转化操作就是从一个RDD产生一个新的RDD操作，而行动操作就是进行实际的计算。
　　下面是RDD的基础操作API介绍：

* 转化操作
map() 参数是函数，函数应用于RDD每一个元素，返回值是新的RDD
flatMap() 参数是函数，函数应用于RDD每一个元素，将元素数据进行拆分，变成迭代器，返回值是新的RDD
filter() 参数是函数，函数会过滤掉不符合条件的元素，返回值是新的RDD
distinct() 没有参数，将RDD里的元素进行去重操作
union() 参数是RDD，生成包含两个RDD所有元素的新RDD
intersection() 参数是RDD，求出两个RDD的共同元素
subtract() 参数是RDD，将原RDD里和参数RDD里相同的元素去掉
cartesian() 参数是RDD，求两个RDD的笛卡儿积

* 行动操作
collect() 返回RDD所有元素
count() RDD里元素个数
countByValue() 各元素在RDD中出现次数
reduce() 并行整合所有RDD数据，例如求和操作
fold(0)(func)和reduce功能一样，不过fold带有初始值
aggregate(0)(seqOp,combop)和reduce功能一样，但是返回的RDD数据类型和原RDD不一样
foreach(func) 对RDD每个元素都是使用特定函数

　　下面是以上API操作的示例代码，如下：

　　转化操作：

```
val rddInt:RDD[Int] = sc.makeRDD(List(1,2,3,4,5,6,2,5,1))
val rddStr:RDD[String] = sc.parallelize(Array("a","b","c","d","b","a"), 1)
val rddFile:RDD[String] = sc.textFile(path, 1)
 
val rdd01:RDD[Int] = sc.makeRDD(List(1,3,5,3))
val rdd02:RDD[Int] = sc.makeRDD(List(2,4,5,1))
 
/* map操作 */
println("======map操作======")
println(rddInt.map(x => x + 1).collect().mkString(","))
println("======map操作======")
/* filter操作 */
println("======filter操作======")
println(rddInt.filter(x => x > 4).collect().mkString(","))
println("======filter操作======")
/* flatMap操作 */
println("======flatMap操作======")
println(rddFile.flatMap { x => x.split(",") }.first())
println("======flatMap操作======")
/* distinct去重操作 */
println("======distinct去重======")
println(rddInt.distinct().collect().mkString(","))
println(rddStr.distinct().collect().mkString(","))
println("======distinct去重======")
/* union操作 */
println("======union操作======")
println(rdd01.union(rdd02).collect().mkString(","))
println("======union操作======")
/* intersection操作 */
println("======intersection操作======")
println(rdd01.intersection(rdd02).collect().mkString(","))
println("======intersection操作======")
/* subtract操作 */
println("======subtract操作======")
println(rdd01.subtract(rdd02).collect().mkString(","))
println("======subtract操作======")
/* cartesian操作 */
println("======cartesian操作======")
println(rdd01.cartesian(rdd02).collect().mkString(","))
println("======cartesian操作======")
```

　　行动操作代码如下：

```
val rddInt:RDD[Int] = sc.makeRDD(List(1,2,3,4,5,6,2,5,1))
val rddStr:RDD[String] = sc.parallelize(Array("a","b","c","d","b","a"), 1)
 
/* count操作 */
println("======count操作======")
println(rddInt.count())
println("======count操作======")  
/* countByValue操作 */
println("======countByValue操作======")
println(rddInt.countByValue())
println("======countByValue操作======")
/* reduce操作 */
println("======countByValue操作======")
println(rddInt.reduce((x ,y) => x + y))
println("======countByValue操作======")
/* fold操作 */
println("======fold操作======")
println(rddInt.fold(0)((x ,y) => x + y))
println("======fold操作======")
/* aggregate操作 */
println("======aggregate操作======")
val res:(Int,Int) = rddInt.aggregate((0,0))((x,y) => (x._1 + x._2,y),(x,y) => (x._1 + x._2,y._1 + y._2))
println(res._1 + "," + res._2)
println("======aggregate操作======")
/* foeach操作 */
println("======foeach操作======")
println(rddStr.foreach { x => println(x) })
println("======foeach操作======")
```

　　RDD操作暂时先学习到这里，剩下的内容在下一篇里再谈了，下面我要说说如何开发spark，安装spark的内容我后面会使用专门的文章进行讲解，这里我们假设已经安装好了spark，那么我们就可以在已经装好的spark服务器上使用spark-shell进行与spark交互的shell，这里我们直接可以敲打代码编写spark程序。但是spark-shell毕竟使用太麻烦，而且spark-shell一次只能使用一个用户，当另外一个用户要使用spark-shell就会把前一个用户踢掉，而且shell也没有IDE那种代码补全，代码校验的功能，使用起来很是痛苦。

　　不过spark的确是一个神奇的框架，这里的神奇就是指spark本地开发调试非常简单，本地开发调试不需要任何已经装好的spark系统，我们只需要建立一个项目，这个项目可以是java的也可以是scala，然后我们将spark-assembly-1.6.1-hadoop2.6.0.jar这样的jar放入项目的环境里，这个时候我们就可以在本地开发调试spark程序了。

　　大家请看我们装有scala插件的eclipse里的完整代码：

```
package cn.com.sparktest
 
import org.apache.spark.SparkConf
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
 
object SparkTest {
  val conf:SparkConf = new SparkConf().setAppName("xtq").setMaster("local[2]")
  val sc:SparkContext = new SparkContext(conf)
   
  /**
   * 创建数据的方式--从内存里构造数据(基础)
   */
  def createDataMethod():Unit = {
    /* 使用makeRDD创建RDD */
    /* List */
    val rdd01 = sc.makeRDD(List(1,2,3,4,5,6))
    val r01 = rdd01.map { x => x * x }
    println("===================createDataMethod:makeRDD:List=====================")
    println(r01.collect().mkString(","))
    println("===================createDataMethod:makeRDD:List=====================")
    /* Array */
    val rdd02 = sc.makeRDD(Array(1,2,3,4,5,6))
    val r02 = rdd02.filter { x => x < 5}
    println("===================createDataMethod:makeRDD:Array=====================")
    println(r02.collect().mkString(","))
    println("===================createDataMethod:makeRDD:Array=====================")
     
    /* 使用parallelize创建RDD */
    /* List */
    val rdd03 = sc.parallelize(List(1,2,3,4,5,6), 1)
    val r03 = rdd03.map { x => x + 1 }
    println("===================createDataMethod:parallelize:List=====================")
    println(r03.collect().mkString(","))
    println("===================createDataMethod:parallelize:List=====================")
    /* Array */
    val rdd04 = sc.parallelize(List(1,2,3,4,5,6), 1)
    val r04 = rdd04.filter { x => x > 3 }
    println("===================createDataMethod:parallelize:Array=====================")
    println(r04.collect().mkString(","))
    println("===================createDataMethod:parallelize:Array=====================")
  }
   
  /**
   * 创建Pair Map
   */
  def createPairRDD():Unit = {
    val rdd:RDD[(String,Int)] = sc.makeRDD(List(("key01",1),("key02",2),("key03",3)))
    val r:RDD[String] = rdd.keys
    println("===========================createPairRDD=================================")
    println(r.collect().mkString(","))
    println("===========================createPairRDD=================================")
  }
   
  /**
   * 通过文件创建RDD
   * 文件数据：
   *    key01,1,2.3
          key02,5,3.7
      key03,23,4.8
      key04,12,3.9
      key05,7,1.3
   */
  def createDataFromFile(path:String):Unit = {
    val rdd:RDD[String] = sc.textFile(path, 1)
    val r:RDD[String] = rdd.flatMap { x => x.split(",") }
    println("=========================createDataFromFile==================================")
    println(r.collect().mkString(","))
    println("=========================createDataFromFile==================================")
  }
   
  /**
   * 基本的RDD操作
   */
  def basicTransformRDD(path:String):Unit = {
    val rddInt:RDD[Int] = sc.makeRDD(List(1,2,3,4,5,6,2,5,1))
    val rddStr:RDD[String] = sc.parallelize(Array("a","b","c","d","b","a"), 1)
    val rddFile:RDD[String] = sc.textFile(path, 1)
     
    val rdd01:RDD[Int] = sc.makeRDD(List(1,3,5,3))
    val rdd02:RDD[Int] = sc.makeRDD(List(2,4,5,1))
 
    /* map操作 */
    println("======map操作======")
    println(rddInt.map(x => x + 1).collect().mkString(","))
    println("======map操作======")
    /* filter操作 */
    println("======filter操作======")
    println(rddInt.filter(x => x > 4).collect().mkString(","))
    println("======filter操作======")
    /* flatMap操作 */
    println("======flatMap操作======")
    println(rddFile.flatMap { x => x.split(",") }.first())
    println("======flatMap操作======")
    /* distinct去重操作 */
    println("======distinct去重======")
    println(rddInt.distinct().collect().mkString(","))
    println(rddStr.distinct().collect().mkString(","))
    println("======distinct去重======")
    /* union操作 */
    println("======union操作======")
    println(rdd01.union(rdd02).collect().mkString(","))
    println("======union操作======")
    /* intersection操作 */
    println("======intersection操作======")
    println(rdd01.intersection(rdd02).collect().mkString(","))
    println("======intersection操作======")
    /* subtract操作 */
    println("======subtract操作======")
    println(rdd01.subtract(rdd02).collect().mkString(","))
    println("======subtract操作======")
    /* cartesian操作 */
    println("======cartesian操作======")
    println(rdd01.cartesian(rdd02).collect().mkString(","))
    println("======cartesian操作======")   
  }
   
  /**
   * 基本的RDD行动操作
   */
  def basicActionRDD():Unit = {
    val rddInt:RDD[Int] = sc.makeRDD(List(1,2,3,4,5,6,2,5,1))
    val rddStr:RDD[String] = sc.parallelize(Array("a","b","c","d","b","a"), 1)
     
    /* count操作 */
    println("======count操作======")
    println(rddInt.count())
    println("======count操作======")  
    /* countByValue操作 */
    println("======countByValue操作======")
    println(rddInt.countByValue())
    println("======countByValue操作======")
    /* reduce操作 */
    println("======countByValue操作======")
    println(rddInt.reduce((x ,y) => x + y))
    println("======countByValue操作======")
    /* fold操作 */
    println("======fold操作======")
    println(rddInt.fold(0)((x ,y) => x + y))
    println("======fold操作======")
    /* aggregate操作 */
    println("======aggregate操作======")
    val res:(Int,Int) = rddInt.aggregate((0,0))((x,y) => (x._1 + x._2,y),(x,y) => (x._1 + x._2,y._1 + y._2))
    println(res._1 + "," + res._2)
    println("======aggregate操作======")
    /* foeach操作 */
    println("======foeach操作======")
    println(rddStr.foreach { x => println(x) })
    println("======foeach操作======")   
  }
   
  def main(args: Array[String]): Unit = {
    println(System.getenv("HADOOP_HOME"))
    createDataMethod()
    createPairRDD()
    createDataFromFile("file:///D:/sparkdata.txt")
    basicTransformRDD("file:///D:/sparkdata.txt")
    basicActionRDD()
    /*打印结果*/
    /*D://hadoop
===================createDataMethod:makeRDD:List=====================
1,4,9,16,25,36
===================createDataMethod:makeRDD:List=====================
===================createDataMethod:makeRDD:Array=====================
1,2,3,4
===================createDataMethod:makeRDD:Array=====================
===================createDataMethod:parallelize:List=====================
2,3,4,5,6,7
===================createDataMethod:parallelize:List=====================
===================createDataMethod:parallelize:Array=====================
4,5,6
===================createDataMethod:parallelize:Array=====================
===========================createPairRDD=================================
key01,key02,key03
===========================createPairRDD=================================
key01,1,2.3,key02,5,3.7,key03,23,4.8,key04,12,3.9,key05,7,1.3
=========================createDataFromFile==================================
2,3,4,5,6,7,3,6,2
======map操作======
======filter操作======
5,6,5
======filter操作======
======flatMap操作======
key01
======flatMap操作======
======distinct去重======
4,6,2,1,3,5
======distinct去重======
======union操作======
1,3,5,3,2,4,5,1
======union操作======
======intersection操作======
1,5
======intersection操作======
======subtract操作======
3,3
======subtract操作======
======cartesian操作======
(1,2),(1,4),(3,2),(3,4),(1,5),(1,1),(3,5),(3,1),(5,2),(5,4),(3,2),(3,4),(5,5),(5,1),(3,5),(3,1)
======cartesian操作======
======count操作======
9
======count操作======
======countByValue操作======
Map(5 -> 2, 1 -> 2, 6 -> 1, 2 -> 2, 3 -> 1, 4 -> 1)
======countByValue操作======
======countByValue操作======
29
======countByValue操作======
======fold操作======
29
======fold操作======
======aggregate操作======
19,10
======aggregate操作======
======foeach操作======
a
b
c
d
b
a
======foeach操作======*/
  }
}
```

　　Spark执行时候我们需要构造一个SparkContenxt的环境变量，构造环境变量时候需要构造一个SparkConf对象，例如代码：setAppName("xtq").setMaster("local[2]")

　　appName就是spark任务名称，master为local[2]是指使用本地模式，启动2个线程完成spark任务。

　　在eclipse里运行spark程序时候，会报出如下错误：

```
java.io.IOException: Could not locate executable null\bin\winutils.exe in the Hadoop binaries.
    at org.apache.hadoop.util.Shell.getQualifiedBinPath(Shell.java:355)
    at org.apache.hadoop.util.Shell.getWinUtilsPath(Shell.java:370)
    at org.apache.hadoop.util.Shell.<clinit>(Shell.java:363)
    at org.apache.hadoop.util.StringUtils.<clinit>(StringUtils.java:79)
    at org.apache.hadoop.security.Groups.parseStaticMapping(Groups.java:104)
    at org.apache.hadoop.security.Groups.<init>(Groups.java:86)
    at org.apache.hadoop.security.Groups.<init>(Groups.java:66)
    at org.apache.hadoop.security.Groups.getUserToGroupsMappingService(Groups.java:280)
    at org.apache.hadoop.security.UserGroupInformation.initialize(UserGroupInformation.java:271)
    at org.apache.hadoop.security.UserGroupInformation.ensureInitialized(UserGroupInformation.java:248)
    at org.apache.hadoop.security.UserGroupInformation.loginUserFromSubject(UserGroupInformation.java:763)
    at org.apache.hadoop.security.UserGroupInformation.getLoginUser(UserGroupInformation.java:748)
    at org.apache.hadoop.security.UserGroupInformation.getCurrentUser(UserGroupInformation.java:621)
    at org.apache.spark.util.Utils$$anonfun$getCurrentUserName$1.apply(Utils.scala:2160)
    at org.apache.spark.util.Utils$$anonfun$getCurrentUserName$1.apply(Utils.scala:2160)
    at scala.Option.getOrElse(Option.scala:120)
    at org.apache.spark.util.Utils$.getCurrentUserName(Utils.scala:2160)
    at org.apache.spark.SparkContext.<init>(SparkContext.scala:322)
    at cn.com.sparktest.SparkTest$.<init>(SparkTest.scala:10)
    at cn.com.sparktest.SparkTest$.<clinit>(SparkTest.scala)
    at cn.com.sparktest.SparkTest.main(SparkTest.scala)
```

　　该错误不会影响程序的运算，但总是让人觉得不舒服，这个问题是因为spark运行依赖于hadoop，可是在window下其实是无法安装hadoop，只能使用cygwin模拟安装，而新版本的hadoop在windows下使用需要使用winutils.exe，解决这个问题很简单，就是下载一个winutils.exe，注意下自己操作系统是32位还是64位，找到对应版本，然后放置在这样的目录下：

　　D:\hadoop\bin\winutils.exe

　　然后再环境变量里定义HADOOP_HOME= D:\hadoop

　　环境变量的改变要重启eclipse，这样环境变量才会生效，这个时候程序运行就不会报出错误了。


　　Spark是一个计算框架，是对mapreduce计算框架的改进，mapreduce计算框架是基于键值对也就是map的形式，之所以使用键值对是人们发现世界上大部分计算都可以使用map这样的简单计算模型进行计算。但是Spark里的计算模型却是数组形式，RDD如何处理Map的数据格式了？本篇文章就主要讲解RDD是如何处理Map的数据格式。

　　Pair RDD及键值对RDD，Spark里创建Pair RDD也是可以通过两种途径，一种是从内存里读取，一种是从文件读取。

　　首先是从文件读取，上篇里我们看到使用textFile方法读取文件，读取的文件是按行组织成一个数组，要让其变成map格式就的进行转化，代码如下所示：

```
/*
 * 测试文件数据:
 * x01,1,4
   x02,11,1
x01,3,9
x01,2,6
   x02,18,12
   x03,7,9
 *
 * */
val rddFile:RDD[(String,String)] = sc.textFile("file:///F:/sparkdata01.txt", 1).map { x => (x.split(",")(0),x.split(",")(1) + "," + x.split(",")(2)) }
val rFile:RDD[String] = rddFile.keys
println("=========createPairMap File=========")
println(rFile.collect().mkString(","))// x01,x02,x01,x01,x02,x03
println("=========createPairMap File=========")
```

　　我们由此可以看到以读取文件方式构造RDD，我们需要使用map函数进行转化，让其变成map的形式。

　　下面是通过内存方式进行创建，代码如下：

```
val rdd:RDD[(String,Int)] = sc.makeRDD(List(("k01",3),("k02",6),("k03",2),("k01",26)))
val r:RDD[(String,Int)] = rdd.reduceByKey((x,y) => x + y)
println("=========createPairMap=========")
println(r.collect().mkString(","))// (k01,29),(k03,2),(k02,6)
println("=========createPairMap=========")
```

　　RDD任然是数组形式，只不过数组的元素是("k01",3)格式是scala里面特有的Tuple2及二元组，元组可以当作一个集合，这个集合可以是各种不同数据类型组合而成，二元组就是只包含两个元素的元组。

　　由此可见Pair RDD也是数组，只不过是一个元素为二元组的数组而已，上篇里对RDD的操作也是同样适用于Pair RDD的。

　　下面是Pair RDD的API讲解，同样我们先说转化操作的API：

```
reduceByKey：合并具有相同键的值；
groupByKey：对具有相同键的值进行分组；
keys：返回一个仅包含键值的RDD；
values：返回一个仅包含值的RDD；
sortByKey：返回一个根据键值排序的RDD；
flatMapValues：针对Pair RDD中的每个值应用一个返回迭代器的函数，然后对返回的每个元素都生成一个对应原键的键值对记录；
mapValues：对Pair RDD里每一个值应用一个函数，但是不会对键值进行操作；
combineByKey：使用不同的返回类型合并具有相同键的值；
subtractByKey：操作的RDD我们命名为RDD1，参数RDD命名为参数RDD，剔除掉RDD1里和参数RDD中键相同的元素；
join：对两个RDD进行内连接；
rightOuterJoin：对两个RDD进行连接操作，第一个RDD的键必须存在，第二个RDD的键不再第一个RDD里面有那么就会被剔除掉，相同键的值会被合并；
leftOuterJoin：对两个RDD进行连接操作，第二个RDD的键必须存在，第一个RDD的键不再第二个RDD里面有那么就会被剔除掉，相同键的值会被合并；
cogroup：将两个RDD里相同键的数据分组在一起
```

　　下面就是行动操作的API了，具体如下：

```
countByKey：对每个键的元素进行分别计数；
collectAsMap：将结果变成一个map；
lookup：在RDD里使用键值查找数据
```

　　行动操作：

```
take(num):返回RDD里num个元素，随机的；
top(num):返回RDD里最前面的num个元素，这个方法实用性还比较高；
takeSample：从RDD里返回任意一些元素；
sample：对RDD里的数据采样；
takeOrdered：从RDD里按照提供的顺序返回最前面的num个元素
```

　　接下来就是示例代码了，如下所示：

```
package cn.com.sparktest
 
import org.apache.spark.SparkConf
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.util.collection.CompactBuffer
 
object SparkPairMap {
   
  val conf:SparkConf = new SparkConf().setAppName("spark pair map").setMaster("local[2]")
  val sc:SparkContext = new SparkContext(conf)
  
  /**
   * 构建Pair RDD
   */
  def createPairMap():Unit = {
    val rdd:RDD[(String,Int)] = sc.makeRDD(List(("k01",3),("k02",6),("k03",2),("k01",26)))
    val r:RDD[(String,Int)] = rdd.reduceByKey((x,y) => x + y)
    println("=========createPairMap=========")
    println(r.collect().mkString(","))// (k01,29),(k03,2),(k02,6)
    println("=========createPairMap=========")
     
    /*
     * 测试文件数据:
     * x01,1,4
             x02,11,1
             x01,3,9
             x01,2,6
       x02,18,12
       x03,7,9
     *
     * */
    val rddFile:RDD[(String,String)] = sc.textFile("file:///F:/sparkdata01.txt", 1).map { x => (x.split(",")(0),x.split(",")(1) + "," + x.split(",")(2)) }
    val rFile:RDD[String] = rddFile.keys
    println("=========createPairMap File=========")
    println(rFile.collect().mkString(","))// x01,x02,x01,x01,x02,x03
    println("=========createPairMap File=========")
  }
   
  /**
   * 关于Pair RDD的转化操作和行动操作
   */
  def pairMapRDD(path:String):Unit = {
    val rdd:RDD[(String,Int)] = sc.makeRDD(List(("k01",3),("k02",6),("k03",2),("k01",26)))
    val other:RDD[(String,Int)] = sc.parallelize(List(("k01",29)), 1)
     
    // 转化操作
    val rddReduce:RDD[(String,Int)] = rdd.reduceByKey((x,y) => x + y)
    println("====reduceByKey===:" + rddReduce.collect().mkString(","))// (k01,29),(k03,2),(k02,6)
    val rddGroup:RDD[(String,Iterable[Int])] = rdd.groupByKey()
    println("====groupByKey===:" + rddGroup.collect().mkString(","))// (k01,CompactBuffer(3, 26)),(k03,CompactBuffer(2)),(k02,CompactBuffer(6))
    val rddKeys:RDD[String] = rdd.keys
    println("====keys=====:" + rddKeys.collect().mkString(","))// k01,k02,k03,k01
    val rddVals:RDD[Int] = rdd.values
    println("======values===:" + rddVals.collect().mkString(","))// 3,6,2,26
    val rddSortAsc:RDD[(String,Int)] = rdd.sortByKey(true, 1)
    val rddSortDes:RDD[(String,Int)] = rdd.sortByKey(false, 1)
    println("====rddSortAsc=====:" + rddSortAsc.collect().mkString(","))// (k01,3),(k01,26),(k02,6),(k03,2)
    println("======rddSortDes=====:" + rddSortDes.collect().mkString(","))// (k03,2),(k02,6),(k01,3),(k01,26)
    val rddFmVal:RDD[(String,Int)] = rdd.flatMapValues { x => List(x + 10) }
    println("====flatMapValues===:" + rddFmVal.collect().mkString(","))// (k01,13),(k02,16),(k03,12),(k01,36)
    val rddMapVal:RDD[(String,Int)] = rdd.mapValues { x => x + 10 }
    println("====mapValues====:" + rddMapVal.collect().mkString(","))// (k01,13),(k02,16),(k03,12),(k01,36)
    val rddCombine:RDD[(String,(Int,Int))] = rdd.combineByKey(x => (x,1), (param:(Int,Int),x) => (param._1 + x,param._2 + 1), (p1:(Int,Int),p2:(Int,Int)) => (p1._1 + p2._1,p1._2 + p2._2))
    println("====combineByKey====:" + rddCombine.collect().mkString(","))//(k01,(29,2)),(k03,(2,1)),(k02,(6,1))
    val rddSubtract:RDD[(String,Int)] = rdd.subtractByKey(other);
    println("====subtractByKey====:" + rddSubtract.collect().mkString(","))// (k03,2),(k02,6)
    val rddJoin:RDD[(String,(Int,Int))] = rdd.join(other)
    println("=====rddJoin====:" + rddJoin.collect().mkString(","))// (k01,(3,29)),(k01,(26,29))
    val rddRight:RDD[(String,(Option[Int],Int))] = rdd.rightOuterJoin(other)
    println("====rightOuterJoin=====:" + rddRight.collect().mkString(","))// (k01,(Some(3),29)),(k01,(Some(26),29))
    val rddLeft:RDD[(String,(Int,Option[Int]))] = rdd.leftOuterJoin(other)
    println("=====rddLeft=====:" + rddLeft.collect().mkString(","))// (k01,(3,Some(29))),(k01,(26,Some(29))),(k03,(2,None)),(k02,(6,None))
    val rddCogroup: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd.cogroup(other)
    println("=====cogroup=====:" + rddCogroup.collect().mkString(","))// (k01,(CompactBuffer(3, 26),CompactBuffer(29))),(k03,(CompactBuffer(2),CompactBuffer())),(k02,(CompactBuffer(6),CompactBuffer()))
     
    // 行动操作
    val resCountByKey = rdd.countByKey()
    println("=====countByKey=====:" + resCountByKey)// Map(k01 -> 2, k03 -> 1, k02 -> 1)
    val resColMap = rdd.collectAsMap()
    println("=====resColMap=====:" + resColMap)//Map(k02 -> 6, k01 -> 26, k03 -> 2)
    val resLookup = rdd.lookup("k01")
    println("====lookup===:" + resLookup) // WrappedArray(3, 26)
  }
   
  /**
   * 其他一些不常用的RDD操作
   */
  def otherRDDOperate(){
    val rdd:RDD[(String,Int)] = sc.makeRDD(List(("k01",3),("k02",6),("k03",2),("k01",26)))
     
    println("=====first=====:" + rdd.first())//(k01,3)
    val resTop = rdd.top(2).map(x => x._1 + ";" + x._2)
    println("=====top=====:" + resTop.mkString(","))// k03;2,k02;6
    val resTake = rdd.take(2).map(x => x._1 + ";" + x._2)
    println("=======take====:" + resTake.mkString(","))// k01;3,k02;6
    val resTakeSample = rdd.takeSample(false, 2).map(x => x._1 + ";" + x._2)
    println("=====takeSample====:" + resTakeSample.mkString(","))// k01;26,k03;2
    val resSample1 = rdd.sample(false, 0.25)
    val resSample2 = rdd.sample(false, 0.75)
    val resSample3 = rdd.sample(false, 0.5)
    println("=====sample======:" + resSample1.collect().mkString(","))// 无
    println("=====sample======:" + resSample2.collect().mkString(","))// (k01,3),(k02,6),(k01,26)
    println("=====sample======:" + resSample3.collect().mkString(","))// (k01,3),(k01,26)
  }
   
  def main(args: Array[String]): Unit = {
    createPairMap()
    pairMapRDD("file:///F:/sparkdata01.txt")
    otherRDDOperate()
  }
   
}
```

　　本篇到此就将我知道的spark的API全部讲完了，两篇文章里的示例代码都是经过测试的，可以直接运行，大家在阅读代码时候最好注意这个特点：我在写RDD转化代码时候都是很明确的写上了转化后的RDD的数据类型，这样做的目的就是让读者更加清晰的认识不同RDD转化后的数据类型，这点在实际开发里非常重要，在实际的计算里我们经常会不同的计算算法不停的转化RDD的数据类型，而使用scala开发spark程序时候，我发现scala和javascript很类似，我们不去指定返回值数据类型，scala编译器也会自动推算结果的数据类型，因此编码时候我们可以不指定具体数据类型。这个特点就会让我们在实际开发里碰到种种问题，因此我在示例代码里明确了RDD转化后的数据类型。

　　在使用Pair RDD时候，我们要引入：

```
import org.apache.spark.SparkContext._
```

　　否则代码就有可能报错，说找不到对应的方法，这个引入就是scala里导入的隐世类型转化的功能，原理和上段文字说到的内容差不多。

      开发spark程序不仅仅只可以使用scala，还可以使用python，java，不过scala使用起来更加方便，spark的API简单清晰，这样的编程大大降低了原先使用mapreduce编程的难度，但是如果我们要深入掌握这些API那么就要更加深入的学习下scala。














