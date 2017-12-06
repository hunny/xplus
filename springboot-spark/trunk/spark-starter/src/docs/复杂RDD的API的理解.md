# 复杂RDD的API的理解

　　本篇接着讲解RDD的API，讲解那些不是很容易理解的API，同时本篇文章还将展示如何将外部的函数引入到RDD的API里使用，最后通过对RDD的API深入学习，我们还讲讲一些和RDD开发相关的scala语法。

## 1)  aggregate(zeroValue)(seqOp,combOp)

　  该函数的功能和reduce函数一样，也是对数据进行聚合操作，不过aggregate可以返回和原RDD不同的数据类型，使用时候还要提供初始值。

　　我们来看看下面的用法，代码如下：
```
val rddInt: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5), 1)
 
val rddAggr1: (Int, Int) = rddInt.aggregate((0, 0))((x, y) => (x._1 + y, x._2 + 1), (x, y) => (x._1 + y._1, x._2 + y._2))
println("====aggregate 1====:" + rddAggr1.toString()) // (15,5)
```

　　该方法是将有数字组成的RDD的数值进行求和，同时还要统计元素的个数，这样我们就可以计算出一个平均值，这点在实际运算中是非常有用的。

　　假如读者不太懂scala语言，或者就算懂那么一点点scala语法，该API的使用还是让人很难理解的，这个x是什么东西，这个y又是什么东西，为什么它们放在一起这么运算就可以得到预期结果呢？

　　其实aggregate方法使用了scala里元组的结构，元组是scala里很具特色的数据结构，我们看看下面的代码：

```
val tuple2Param1:Tuple2[String,Int] = Tuple2("x01",12)// 标准定义二元组
val tuple2Param2:(String,Int) = ("x02",29)// 字面量定义二元组
 
/* 结果: x01:12*/
println("====tuple2Param1====:" + tuple2Param1._1 + ":" + tuple2Param1._2)
/* 结果: x02:29 */
println("====tuple2Param2====:" + tuple2Param2._1 + ":" + tuple2Param2._2)
 
val tuple6Param1:Tuple6[String,Int,Int,Int,Int,String] = Tuple6("xx01",1,2,3,4,"x1x")// 标准定义6元组
val tuple6Param2:(String,Int,Int,Int,Int,String) = ("xx02",1,2,3,4,"x2x")// 字面量定义6元组
 
/* 结果: xx01:1:2:3:4:x1x */
println("====tuple6Param1====:" + tuple6Param1._1 + ":" + tuple6Param1._2 + ":" + tuple6Param1._3 + ":" + tuple6Param1._4 + ":" + tuple6Param1._5 + ":" + tuple6Param1._6)
/* 结果: xx02:1:2:3:4:x2x */
println("====tuple6Param2====:" + tuple6Param2._1 + ":" + tuple6Param2._2 + ":" + tuple6Param2._3 + ":" + tuple6Param2._4 + ":" + tuple6Param2._5 + ":" + tuple6Param2._6)
```

　　元组在scala里使用Tuple来构造，不过实际运用中我们会给Tuple带上数字后缀，例如Tuple2就是二元组它包含两个元素，Tuple6是6元组它包含6个元素，元组看起来很像数组，但是数组只能存储相同数据类型的数据结构，而元组是可以存储不同数据类型的数据结构，元组里元素访问使用_1,_2这样的形式，第一个元素是从1开始标记的，这点和数组是不同的。实际使用中我们很少使用Tuple构造元组，而是使用字面量定义方式（参见代码注释），由此我们可以看出spark里键值对RDD其实就是使用二元组来表示键值对数据结构，回到aggregate方法，它的运算也是通过二元组这种数据结构完成的。

　　下面我们来看看aggregate的运算过程，这里我将aggregate方法里的算子都使用外部函数，代码如下所示：

```
def aggrFtnOne(par: ((Int, Int), Int)): (Int, Int) = {
  /*
     *aggregate的初始值为(0,0):
      ====aggrFtnOne Param===:((0,0),1)
  ====aggrFtnOne Param===:((1,1),2)
  ====aggrFtnOne Param===:((3,2),3)
  ====aggrFtnOne Param===:((6,3),4)
  ====aggrFtnOne Param===:((10,4),5)*/
  /*
     *aggregate的初始值为(1,1):
      ====aggrFtnOne Param===:((1,1),1)
      ====aggrFtnOne Param===:((2,2),2)
      ====aggrFtnOne Param===:((4,3),3)
      ====aggrFtnOne Param===:((7,4),4)
      ====aggrFtnOne Param===:((11,5),5)
     * */
  println("====aggrFtnOne Param===:" + par.toString())
  val ret: (Int, Int) = (par._1._1 + par._2, par._1._2 + 1)
  ret
}
 
def aggrFtnTwo(par: ((Int, Int), (Int, Int))): (Int, Int) = {
  /*aggregate的初始值为(0,0):::::((0,0),(15,5))*/
  /*aggregate的初始值为(1,1):::::((1,1),(16,6))*/
  println("====aggrFtnTwo Param===:" + par.toString())
  val ret: (Int, Int) = (par._1._1 + par._2._1, par._1._2 + par._2._2)
  ret
}
 
  val rddAggr2: (Int, Int) = rddInt.aggregate((0, 0))((x, y) => aggrFtnOne(x, y), (x, y) => aggrFtnTwo(x, y)) // 参数可以省略元组的括号
  println("====aggregate 2====:" + rddAggr2.toString()) // (15,5)
 
  val rddAggr3: (Int, Int) = rddInt.aggregate((1, 1))((x, y) => aggrFtnOne((x, y)), (x, y) => aggrFtnTwo((x, y))) // 参数使用元组的括号
  println("====aggregate 3====:" + rddAggr3.toString()) // (17,7)
```

　　由以上代码我们就可以清晰看到aggregate方法的实际运算过程了。

　　aggrFtnOne方法的参数格式是((Int, Int), Int)，这个复合二元组里第二个元素才是实际的值，而第一个元素就是我们给出的初始化值，第一个元素里的第一个值就是我们实际求和的值，里面第二个元素就是累计记录元素个数的值。

　　aggrFtnTwo方法的参数里的二元组第一个元素还是初始化值，第二个元素则是aggrFtnOne计算的结果，这样我们就可以计算出我们要的结果。

　　作为对比我将初始化参数改为(1,1)二元组，最终结果在求和运算以及计算元素个数上都会加2，这是因为初始化值两次参入求和所致的，由上面代码我们可以很清晰的看到原因所在。

　　如果我们想要结果二元组里第一个元素求积那么初始化值就不能是(0,0)，而应该是(1,0),理解了原理我们就很清晰知道初始值该如何设定了，具体代码如下：

```
val rddAggr4: (Int, Int) = rddInt.aggregate((1, 0))((x, y) => (x._1 * y, x._2 + 1), (x, y) => (x._1 * y._1, x._2 + y._2))
println("====aggregate 4====:" + rddAggr4.toString()) // (120,5)
```

## 2) fold(zero)(func)

　该函数和reduce函数功能一样，只不过使用时候需要加上初始化值。

　代码如下所示：

```
def foldFtn(par: (Int, Int)): Int = {
  /*fold初始值为0：
      =====foldFtn Param====:(0,1)
      =====foldFtn Param====:(1,2)
      =====foldFtn Param====:(3,3)
      =====foldFtn Param====:(6,4)
      =====foldFtn Param====:(10,5)
      =====foldFtn Param====:(0,15)
     * */
  /*
     * fold初始值为1:
      =====foldFtn Param====:(1,1)
      =====foldFtn Param====:(2,2)
      =====foldFtn Param====:(4,3)
      =====foldFtn Param====:(7,4)
      =====foldFtn Param====:(11,5)
      =====foldFtn Param====:(1,16)
     * */
  println("=====foldFtn Param====:" + par.toString())
  val ret: Int = par._1 + par._2
  ret
}
 
  val rddFold2: Int = rddInt.fold(0)((x, y) => foldFtn(x, y)) // 参数可以省略元组的括号
  println("====fold 2=====:" + rddFold2) // 15
 
  val rddFold3: Int = rddInt.fold(1)((x, y) => foldFtn((x, y))) // 参数使用元组的括号
  println("====fold 3====:" + rddFold3) // 17
```

　　我们发现当初始化值为1时候，求和增加的不是1而是2，原因就是fold计算时候为了凑齐一个完整的二元组，在第一个元素计算以及最后一个元素计算时候都会让初始化值凑数组成二元组，因此初始值会被使用两遍求和，因此实际结果就不是增加1，而是增加2了。

　　作为对比我们看看reduce实际运算过程，代码如下：

```
def reduceFtn(par:(Int,Int)):Int = {
  /*
   * ======reduceFtn Param=====:1:2
           ======reduceFtn Param=====:3:3
     ======reduceFtn Param=====:6:4
     ======reduceFtn Param=====:10:5
   */
  println("======reduceFtn Param=====:" + par._1 + ":" + par._2)
  par._1 + par._2
}
 
  val rddReduce1:Int = rddInt.reduce((x,y) => x + y)
  println("====rddReduce 1====:" + rddReduce1)// 15
   
  val rddReduce2:Int = rddInt.reduce((x,y) => reduceFtn(x,y))
  println("====rddReduce 2====:" + rddReduce2)// 15
```

## 3) combineByKey[C](createCombiner: Int => C, mergeValue: (C, Int) => C, mergeCombiners: (C, C) => C): RDD[(String, C)]

　　combineByKey作用是使用不同的返回类型合并具有相同键的值，combineByKey适用键值对RDD，普通RDD是没有这个方法。

　　有上面定义我们看到combineByKey会经过三轮运算，前一个运算步骤结果就是下一个运算步骤的参数，我们看下面的代码：

```
def combineFtnOne(par:Int):(Int,Int) = {
  /*
   * ====combineFtnOne Param====:2
     ====combineFtnOne Param====:5
     ====combineFtnOne Param====:8
     ====combineFtnOne Param====:3
   */
  println("====combineFtnOne Param====:" + par)
  val ret:(Int,Int) = (par,1)
  ret
}
 
def combineFtnTwo(par:((Int,Int),Int)):(Int,Int) = {
  /*
    ====combineFtnTwo Param====:((2,1),12)
    ====combineFtnTwo Param====:((8,1),9)
   * */
  println("====combineFtnTwo Param====:" + par.toString())
  val ret:(Int,Int) = (par._1._1 + par._2,par._1._2 + 1)
  ret
}
 
def combineFtnThree(par:((Int,Int),(Int,Int))):(Int,Int) = {
  /*
   * 无结果打印
   */
  println("@@@@@@@@@@@@@@@@@@")
  println("====combineFtnThree Param===:" + par.toString())
  val ret:(Int,Int) = (par._1._1 + par._2._1,par._1._2 + par._2._2)
  ret
}
 
  val rddPair: RDD[(String, Int)] = sc.parallelize(List(("x01", 2), ("x02", 5), ("x03", 8), ("x04", 3), ("x01", 12), ("x03", 9)), 1)
   
  /* def combineByKey[C](createCombiner: Int => C, mergeValue: (C, Int) => C, mergeCombiners: (C, C) => C): RDD[(String, C)] */   
  val rddCombine1:RDD[(String,(Int,Int))] = rddPair.combineByKey(x => (x, 1), (com: (Int, Int), x) => (com._1 + x, com._2 + 1), (com1: (Int, Int), com2: (Int, Int)) => (com1._1 + com2._1, com1._2 + com2._2))
  println("====combineByKey 1====:" + rddCombine1.collect().mkString(",")) // (x02,(5,1)),(x03,(17,2)),(x01,(14,2)),(x04,(3,1))
   
  val rddCombine2:RDD[(String,(Int,Int))] = rddPair.combineByKey(x => combineFtnOne(x), (com: (Int, Int), x) => combineFtnTwo(com,x), (com1: (Int, Int), com2: (Int, Int)) => combineFtnThree(com1,com2))
  println("=====combineByKey 2====:" + rddCombine2.collect().mkString(",")) // (x02,(5,1)),(x03,(17,2)),(x01,(14,2)),(x04,(3,1))
```

　　这个算法和上面aggregate求和方法很像，不过combineByKey很奇怪，它第三个算子似乎并没有被执行，第二个算子打印的信息也不齐备，不过我认为它们都执行了，只不过有些语句没有打印出来。

## 1)   flatMapValues：针对Pair RDD中的每个值应用一个返回迭代器的函数，然后对返回的每个元素都生成一个对应原键的键值对记录

　　这个方法我最开始接触时候，总是感觉很诧异，不是太理解，现在回想起来主要原因是我接触的第一个flatMapValues的例子是这样的，代码如下：

```
val rddPair: RDD[(String, Int)] = sc.parallelize(List(("x01", 2), ("x02", 5), ("x03", 8), ("x04", 3), ("x01", 12), ("x03", 9)), 1)
val rddFlatMapVals1:RDD[(String,Int)] = rddPair.flatMapValues { x => x to (6) }
/* 结果：(x01,2),(x01,3),(x01,4),(x01,5),(x01,6),(x02,5),(x02,6),(x04,3),(x04,4),(x04,5),(x04,6) */
println("====flatMapValues 1====:" + rddFlatMapVals1.collect().mkString(","))
```

　　这个实例使用的是scala里Range这种数据类型，Range数据类型是一个数字的范围，细细讲它的理论也没啥意思，我们看下面的例子吧，代码如下：

```
val list:List[Int] = List(1,2,3,4,5,6)
val len:Int = list.size - 1
val r:Range = 0 to len
 
for (ind <- r){
  print(list(ind) + ";")// 1;2;3;4;5;6;
}
 println("")
for (ind <- 0 to len){
  print(list(ind) + ";")// 1;2;3;4;5;6;
}
println("") 
```

　　由以上代码我们可以看到0 to 3就是指代0，1，2，3这三个数字，所以我们可以在for循环里指代这个范围。

　　其实flatMapValues接受的外部方法的返回类型是一个Seq类型，Seq类型在scala里就是一个序列，一个有序的序列，我们可以把他理解成数组，我们来看看下面的例子：

```
def flatMapRange(par:Int):Range = {
  par to 6
}
 
def flatMapList(par:Int):List[Int] = {
  List(par + 1000)
}
 
def flatMapSeq(par:Int):Seq[Int] = {
  Seq(par + 6000)
}
  val rddFlatMapVals2:RDD[(String,Int)] = rddPair.flatMapValues { x => flatMapRange(x) }
  /* 结果：(x01,2),(x01,3),(x01,4),(x01,5),(x01,6),(x02,5),(x02,6),(x04,3),(x04,4),(x04,5),(x04,6) */
  println("====flatMapValues 2====:" + rddFlatMapVals2.collect().mkString(","))
  val rddFlatMapVals3:RDD[(String,Int)] = rddPair.flatMapValues { x => flatMapList(x) }
  /* 结果：(x01,1002),(x02,1005),(x03,1008),(x04,1003),(x01,1012),(x03,1009) */
  println("====flatMapValues 3====:" + rddFlatMapVals3.collect().mkString(","))
  val rddFlatMapVals4:RDD[(String,Int)] = rddPair.flatMapValues { x => flatMapSeq(x) }
  /* 结果：(x01,6002),(x02,6005),(x03,6008),(x04,6003),(x01,6012),(x03,6009) */
  println("====flatMapValues 4====:" + rddFlatMapVals4.collect().mkString(","))
```

　　谈到flatMapValues这个方法，让我不得不回忆起另外一个与之类似的方法flatMap，我们来看看这个方法的实例吧，代码如下：

```
val rddFlatMap1:RDD[(String,Int)] = rddPair.flatMap(x => List((x._1,x._2 + 3000)))
 // 结果：(x01,3002),(x02,3005),(x03,3008),(x04,3003),(x01,3012),(x03,3009)
 println("=====flatMap 1======:" + rddFlatMap1.collect().mkString(","))
 val rddFlatMap2:RDD[Int] = rddPair.flatMap(x => List(x._2 + 8000))
 // 结果:8002,8005,8008,8003,8012,8009
 println("=====flatMap 2======:" + rddFlatMap2.collect().mkString(","))
 val rddFlatMap3:RDD[String] = rddPair.flatMap(x => List(x._1 + "@!@" + x._2))
 // 结果：x01@!@2,x02@!@5,x03@!@8,x04@!@3,x01@!@12,x03@!@9
println("=====flatMap 3======:" + rddFlatMap3.collect().mkString(","))
```

　由此可见flatMap方法里的参数也是一个Seq，而且他们之间可以相互替代使用，只不过flatMapValues是让二元组里的第一个元素保持不变的情况下进行计算的（及key值不发生变化）。不过spark不会无缘无故的定义一个flatMapValues,它其实和spark里的分区紧密相关，关于spark的分区知识我会在后面文章里谈谈的。

## 2) rightOuterJoin，leftOuterJoin，rddCogroup及右连接，左连接和分组函数

    我们首先看看他们的使用吧，代码如下：

```
val rdd:RDD[(String,Int)] = sc.makeRDD(List(("x01",2),("x02",5),("x03",9),("x03",21),("x04",76)))
val other:RDD[(String,Int)] = sc.makeRDD(List(("x01",4),("x02",6),("x03",11)))
 
val rddRight:RDD[(String,(Option[Int],Int))] = rdd.rightOuterJoin(other)
/* 结果：(x02,(Some(5),6)),(x03,(Some(9),11)),(x03,(Some(21),11)),(x01,(Some(2),4)) */
println("====rightOuterJoin====:" + rddRight.collect().mkString(","))
 
val rddLeft:RDD[(String,(Int,Option[Int]))] = rdd.leftOuterJoin(other)
/* 结果： (x02,(5,Some(6))),(x04,(76,None)),(x03,(9,Some(11))),(x03,(21,Some(11))),(x01,(2,Some(4))) */
println("====leftOuterJoin====:" + rddLeft.collect().mkString(","))
val rddSome = rddLeft.filter(x => x._2._2.isEmpty == false)// 过滤掉None的记录
/* 结果: (x02,(5,Some(6))),(x03,(9,Some(11))),(x03,(21,Some(11))),(x01,(2,Some(4)))*/
println("====rddSome===:" + rddSome.collect().mkString(","))
 
val rddCogroup: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd.cogroup(other)
/* 结果: (x02,(CompactBuffer(5),CompactBuffer(6))),(x04,(CompactBuffer(76),CompactBuffer())),(x03,(CompactBuffer(9, 21),CompactBuffer(11))),(x01,(CompactBuffer(2),CompactBuffer(4)))*/
println("===cogroup====:" + rddCogroup.collect().mkString(","))
```

　　这三个方法很好理解，就和关系数据库里的左右连接，分组一样，不过它们的返回值在我刚学习spark时候很是疑惑了半天，这里就好好说下它们的返回值，这其实就是学习下scala的数据类型了。

　　首先是Some数据类型，Some并不是一个直接操作的数据类型，它属于Option这个数据结构的，其实None也是Option里的数据结构，Some里面只能放一个元素，例如Some(1),Some((1,2)),为什么scala里还要这么繁琐的定义一个Option，并在其中还定义一个Some和一个None的结构呢？我们首先看看下面代码：

```
def optionSome():Unit = {
    /*
     * =======option for 1=========
      0:3
      2:8
      3:11
      =======option for 1=========
      =======option for 2=========
      0:3
      1:None
      2:8
      3:11
      =======option for 2=========
     */
    val list:List[Option[Int]] = List(Some(3),None,Some(8),Some(11))
    println("=======option for 1=========")
    for (i <- 0 until list.size){
      if (!list(i).isEmpty){
        println(i + ":" + list(i).get)
      }
    }
    println("=======option for 1=========")
    println("=======option for 2=========")
    for (j <- 0 until list.size){
      val res = list(j) match {       
        case None => println(j + ":None")
        case _ => println(j + ":" + list(j).get)
      }
    }
    println("=======option for 2=========")
  }
```

　　Option数据结构其实想要表达的是一个数据集合，这个数据集合里要么有值，要么没值，这点在左右连接查询里就非常有用，其实左右连接最后的结果就是要么关联上了要么没有关联上。

　　分组cogroup返回的结构是CompactBuffer，CompactBuffer并不是scala里定义的数据结构，而是spark里的数据结构，它继承自一个迭代器和序列，所以它的返回值是一个很容易进行循环遍历的集合，这点很符合cogroup的返回值类型。

好了，这篇内容就写完了，下一篇文章我将要简单聊聊spark分区，后面应该暂时会停停spark的学习，要搞搞前端的一些技术，这都是因为工作需要了。

　　最后我将完整示例代码给大家分享下，代码如下：

```
package cn.com.sparktest
 
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import scala.collection.immutable.List
import org.apache.spark.util.collection.CompactBuffer
 
object ScalaTest {
  val conf: SparkConf = new SparkConf().setAppName("spark scala").setMaster("local[2]")
  val sc: SparkContext = new SparkContext(conf)
 
  def aggrFtnOne(par: ((Int, Int), Int)): (Int, Int) = {
    /*
       *aggregate的初始值为(0,0):
        ====aggrFtnOne Param===:((0,0),1)
                ====aggrFtnOne Param===:((1,1),2)
                ====aggrFtnOne Param===:((3,2),3)
                ====aggrFtnOne Param===:((6,3),4)
                ====aggrFtnOne Param===:((10,4),5)*/
    /*
       *aggregate的初始值为(1,1):
        ====aggrFtnOne Param===:((1,1),1)
        ====aggrFtnOne Param===:((2,2),2)
        ====aggrFtnOne Param===:((4,3),3)
        ====aggrFtnOne Param===:((7,4),4)
        ====aggrFtnOne Param===:((11,5),5)
       * */
    println("====aggrFtnOne Param===:" + par.toString())
    val ret: (Int, Int) = (par._1._1 + par._2, par._1._2 + 1)
    ret
  }
 
  def aggrFtnTwo(par: ((Int, Int), (Int, Int))): (Int, Int) = {
    /*aggregate的初始值为(0,0):::::((0,0),(15,5))*/
    /*aggregate的初始值为(1,1):::::((1,1),(16,6))*/
    println("====aggrFtnTwo Param===:" + par.toString())
    val ret: (Int, Int) = (par._1._1 + par._2._1, par._1._2 + par._2._2)
    ret
  }
 
  def foldFtn(par: (Int, Int)): Int = {
    /*fold初始值为0：
        =====foldFtn Param====:(0,1)
        =====foldFtn Param====:(1,2)
        =====foldFtn Param====:(3,3)
        =====foldFtn Param====:(6,4)
        =====foldFtn Param====:(10,5)
        =====foldFtn Param====:(0,15)
       * */
    /*
       * fold初始值为1:
        =====foldFtn Param====:(1,1)
        =====foldFtn Param====:(2,2)
        =====foldFtn Param====:(4,3)
        =====foldFtn Param====:(7,4)
        =====foldFtn Param====:(11,5)
        =====foldFtn Param====:(1,16)
       * */
    println("=====foldFtn Param====:" + par.toString())
    val ret: Int = par._1 + par._2
    ret
  }
   
  def reduceFtn(par:(Int,Int)):Int = {
    /*
     * ======reduceFtn Param=====:1:2
             ======reduceFtn Param=====:3:3
       ======reduceFtn Param=====:6:4
       ======reduceFtn Param=====:10:5
     */
    println("======reduceFtn Param=====:" + par._1 + ":" + par._2)
    par._1 + par._2
  }
 
  def sparkRDDHandle(): Unit = {
    val rddInt: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5), 1)
 
    val rddAggr1: (Int, Int) = rddInt.aggregate((0, 0))((x, y) => (x._1 + y, x._2 + 1), (x, y) => (x._1 + y._1, x._2 + y._2))
    println("====aggregate 1====:" + rddAggr1.toString()) // (15,5)
 
    val rddAggr2: (Int, Int) = rddInt.aggregate((0, 0))((x, y) => aggrFtnOne(x, y), (x, y) => aggrFtnTwo(x, y)) // 参数可以省略元组的括号
    println("====aggregate 2====:" + rddAggr2.toString()) // (15,5)
 
    val rddAggr3: (Int, Int) = rddInt.aggregate((1, 1))((x, y) => aggrFtnOne((x, y)), (x, y) => aggrFtnTwo((x, y))) // 参数使用元组的括号
    println("====aggregate 3====:" + rddAggr3.toString()) // (17,7)
     
    val rddAggr4: (Int, Int) = rddInt.aggregate((1, 0))((x, y) => (x._1 * y, x._2 + 1), (x, y) => (x._1 * y._1, x._2 + y._2))
    println("====aggregate 4====:" + rddAggr4.toString()) // (120,5)  
 
    val rddFold1: Int = rddInt.fold(0)((x, y) => x + y)
    println("====fold 1====:" + rddFold1) // 15
 
    val rddFold2: Int = rddInt.fold(0)((x, y) => foldFtn(x, y)) // 参数可以省略元组的括号
    println("====fold 2=====:" + rddFold2) // 15
 
    val rddFold3: Int = rddInt.fold(1)((x, y) => foldFtn((x, y))) // 参数使用元组的括号
    println("====fold 3====:" + rddFold3) // 17
     
    val rddReduce1:Int = rddInt.reduce((x,y) => x + y)
    println("====rddReduce 1====:" + rddReduce1)// 15
     
    val rddReduce2:Int = rddInt.reduce((x,y) => reduceFtn(x,y))
    println("====rddReduce 2====:" + rddReduce2)// 15
     
  }
   
  def combineFtnOne(par:Int):(Int,Int) = {
    /*
     * ====combineFtnOne Param====:2
       ====combineFtnOne Param====:5
       ====combineFtnOne Param====:8
       ====combineFtnOne Param====:3
     */
    println("====combineFtnOne Param====:" + par)
    val ret:(Int,Int) = (par,1)
    ret
  }
   
  def combineFtnTwo(par:((Int,Int),Int)):(Int,Int) = {
    /*
      ====combineFtnTwo Param====:((2,1),12)
      ====combineFtnTwo Param====:((8,1),9)
     * */
    println("====combineFtnTwo Param====:" + par.toString())
    val ret:(Int,Int) = (par._1._1 + par._2,par._1._2 + 1)
    ret
  }
   
  def combineFtnThree(par:((Int,Int),(Int,Int))):(Int,Int) = {
    /*
     * 无结果打印
     */
    println("@@@@@@@@@@@@@@@@@@")
    println("====combineFtnThree Param===:" + par.toString())
    val ret:(Int,Int) = (par._1._1 + par._2._1,par._1._2 + par._2._2)
    ret
  }
   
  def flatMapRange(par:Int):Range = {
    par to 6
  }
   
  def flatMapList(par:Int):List[Int] = {
    List(par + 1000)
  }
   
  def flatMapSeq(par:Int):Seq[Int] = {
    Seq(par + 6000)
  }
 
  def sparkPairRDD(): Unit = {
    val rddPair: RDD[(String, Int)] = sc.parallelize(List(("x01", 2), ("x02", 5), ("x03", 8), ("x04", 3), ("x01", 12), ("x03", 9)), 1)
     
    /* def combineByKey[C](createCombiner: Int => C, mergeValue: (C, Int) => C, mergeCombiners: (C, C) => C): RDD[(String, C)] */   
    val rddCombine1:RDD[(String,(Int,Int))] = rddPair.combineByKey(x => (x, 1), (com: (Int, Int), x) => (com._1 + x, com._2 + 1), (com1: (Int, Int), com2: (Int, Int)) => (com1._1 + com2._1, com1._2 + com2._2))
    println("====combineByKey 1====:" + rddCombine1.collect().mkString(",")) // (x02,(5,1)),(x03,(17,2)),(x01,(14,2)),(x04,(3,1))
     
    val rddCombine2:RDD[(String,(Int,Int))] = rddPair.combineByKey(x => combineFtnOne(x), (com: (Int, Int), x) => combineFtnTwo(com,x), (com1: (Int, Int), com2: (Int, Int)) => combineFtnThree(com1,com2))
    println("=====combineByKey 2====:" + rddCombine2.collect().mkString(",")) // (x02,(5,1)),(x03,(17,2)),(x01,(14,2)),(x04,(3,1))
     
     
    val rddKeys:RDD[String] = rddPair.keys
    /*结果:x01,x02,x03,x04,x01,x03  注意调用keys方法时候不能加上括号，否则会报错*/
    println("====keys====:" + rddKeys.collect().mkString(","))
     
    val rddVals:RDD[Int] = rddPair.values
    /*结果：2,5,8,3,12,9  注意调用values方法时候不能加上括号，否则会报错*/
    println("=====values=====:" + rddVals.collect().mkString(","))
     
    val rddFlatMapVals1:RDD[(String,Int)] = rddPair.flatMapValues { x => x to (6) }
    /* 结果：(x01,2),(x01,3),(x01,4),(x01,5),(x01,6),(x02,5),(x02,6),(x04,3),(x04,4),(x04,5),(x04,6) */
    println("====flatMapValues 1====:" + rddFlatMapVals1.collect().mkString(","))
    val rddFlatMapVals2:RDD[(String,Int)] = rddPair.flatMapValues { x => flatMapRange(x) }
    /* 结果：(x01,2),(x01,3),(x01,4),(x01,5),(x01,6),(x02,5),(x02,6),(x04,3),(x04,4),(x04,5),(x04,6) */
    println("====flatMapValues 2====:" + rddFlatMapVals2.collect().mkString(","))
    val rddFlatMapVals3:RDD[(String,Int)] = rddPair.flatMapValues { x => flatMapList(x) }
    /* 结果：(x01,1002),(x02,1005),(x03,1008),(x04,1003),(x01,1012),(x03,1009) */
    println("====flatMapValues 3====:" + rddFlatMapVals3.collect().mkString(","))
    val rddFlatMapVals4:RDD[(String,Int)] = rddPair.flatMapValues { x => flatMapSeq(x) }
    /* 结果：(x01,6002),(x02,6005),(x03,6008),(x04,6003),(x01,6012),(x03,6009) */
    println("====flatMapValues 4====:" + rddFlatMapVals4.collect().mkString(","))
     
    val rddFlatMap1:RDD[(String,Int)] = rddPair.flatMap(x => List((x._1,x._2 + 3000)))
    // 结果：(x01,3002),(x02,3005),(x03,3008),(x04,3003),(x01,3012),(x03,3009)
    println("=====flatMap 1======:" + rddFlatMap1.collect().mkString(","))
    val rddFlatMap2:RDD[Int] = rddPair.flatMap(x => List(x._2 + 8000))
    // 结果:8002,8005,8008,8003,8012,8009
    println("=====flatMap 2======:" + rddFlatMap2.collect().mkString(","))
    val rddFlatMap3:RDD[String] = rddPair.flatMap(x => List(x._1 + "@!@" + x._2))
    // 结果：x01@!@2,x02@!@5,x03@!@8,x04@!@3,x01@!@12,x03@!@9
    println("=====flatMap 3======:" + rddFlatMap3.collect().mkString(","))
  }
   
  def optionSome():Unit = {
    /*
     * =======option for 1=========
      0:3
      2:8
      3:11
      =======option for 1=========
      =======option for 2=========
      0:3
      1:None
      2:8
      3:11
      =======option for 2=========
     */
    val list:List[Option[Int]] = List(Some(3),None,Some(8),Some(11))
    println("=======option for 1=========")
    for (i <- 0 until list.size){
      if (!list(i).isEmpty){
        println(i + ":" + list(i).get)
      }
    }
    println("=======option for 1=========")
    println("=======option for 2=========")
    for (j <- 0 until list.size){
      val res = list(j) match {       
        case None => println(j + ":None")
        case _ => println(j + ":" + list(j).get)
      }
    }
    println("=======option for 2=========")
  }
   
  def pairRDDJoinGroup():Unit = {
    val rdd:RDD[(String,Int)] = sc.makeRDD(List(("x01",2),("x02",5),("x03",9),("x03",21),("x04",76)))
    val other:RDD[(String,Int)] = sc.makeRDD(List(("x01",4),("x02",6),("x03",11)))
     
    val rddRight:RDD[(String,(Option[Int],Int))] = rdd.rightOuterJoin(other)
    /* 结果：(x02,(Some(5),6)),(x03,(Some(9),11)),(x03,(Some(21),11)),(x01,(Some(2),4)) */
    println("====rightOuterJoin====:" + rddRight.collect().mkString(","))
     
    val rddLeft:RDD[(String,(Int,Option[Int]))] = rdd.leftOuterJoin(other)
    /* 结果： (x02,(5,Some(6))),(x04,(76,None)),(x03,(9,Some(11))),(x03,(21,Some(11))),(x01,(2,Some(4))) */
    println("====leftOuterJoin====:" + rddLeft.collect().mkString(","))
    val rddSome = rddLeft.filter(x => x._2._2.isEmpty == false)// 过滤掉None的记录
    /* 结果: (x02,(5,Some(6))),(x03,(9,Some(11))),(x03,(21,Some(11))),(x01,(2,Some(4)))*/
    println("====rddSome===:" + rddSome.collect().mkString(","))
     
    val rddCogroup: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd.cogroup(other)
    /* 结果: (x02,(CompactBuffer(5),CompactBuffer(6))),(x04,(CompactBuffer(76),CompactBuffer())),(x03,(CompactBuffer(9, 21),CompactBuffer(11))),(x01,(CompactBuffer(2),CompactBuffer(4)))*/
    println("===cogroup====:" + rddCogroup.collect().mkString(","))
  }
   
  def scalaBasic(){
    val its:Iterable[Int] = Iterable(1,2,3,4,5)
    its.foreach { x => print(x + ",") }// 1,2,3,4,5,
     
    val tuple2Param1:Tuple2[String,Int] = Tuple2("x01",12)// 标准定义二元组
    val tuple2Param2:(String,Int) = ("x02",29)// 字面量定义二元组
     
    /* 结果: x01:12*/
    println("====tuple2Param1====:" + tuple2Param1._1 + ":" + tuple2Param1._2)
    /* 结果: x02:29 */
    println("====tuple2Param2====:" + tuple2Param2._1 + ":" + tuple2Param2._2)
     
    val tuple6Param1:Tuple6[String,Int,Int,Int,Int,String] = Tuple6("xx01",1,2,3,4,"x1x")// 标准定义6元组
    val tuple6Param2:(String,Int,Int,Int,Int,String) = ("xx02",1,2,3,4,"x2x")// 字面量定义6元组
     
    /* 结果: xx01:1:2:3:4:x1x */
    println("====tuple6Param1====:" + tuple6Param1._1 + ":" + tuple6Param1._2 + ":" + tuple6Param1._3 + ":" + tuple6Param1._4 + ":" + tuple6Param1._5 + ":" + tuple6Param1._6)
    /* 结果: xx02:1:2:3:4:x2x */
    println("====tuple6Param2====:" + tuple6Param2._1 + ":" + tuple6Param2._2 + ":" + tuple6Param2._3 + ":" + tuple6Param2._4 + ":" + tuple6Param2._5 + ":" + tuple6Param2._6)
     
     val list:List[Int] = List(1,2,3,4,5,6)
     val len:Int = list.size - 1
     val r:Range = 0 to len
      
     for (ind <- r){
       print(list(ind) + ";")// 1;2;3;4;5;6;
     }
      println("")
     for (ind <- 0 to len){
       print(list(ind) + ";")// 1;2;3;4;5;6;
     }
     println("")
  }
 
  def main(args: Array[String]): Unit = {
    scalaBasic()
    optionSome()
     
    sparkRDDHandle()
    sparkPairRDD()
    pairRDDJoinGroup()  
 
     
  }
}
```











