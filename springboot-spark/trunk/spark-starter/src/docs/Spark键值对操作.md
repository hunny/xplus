# Spark——键值对操作

## 动机 
Spark为包含键值对类型的RDD提供了一些专有的操作。这些RDD被称为pairRDD。提供并行操作各个节点或跨界点重新进行数据分组的操作接口。 

## 创建Pair RDD 

* 1、在sprk中，很多存储键值对的数据在读取时直接返回由其键值对数据组成的pair RDD。 
* 2、可以调用map()函数，将一个普通的RDD转换为pair RDD。 
在Scala中，为了提取键之后的数据能够在函数中使用，同样需要返回二元数组。隐式转换可以让二元数组RDD支持附加的键值对函数。

```
val pairs=lines.map(x=>(x.split(" ")[0],x))
```

### Java中Pair RDD

在java中没有自带的二元数组，因此spark的java API让用户使用`scala.Tuple2`类来创建二元组。可以通过`new Tuple2(elem1,emel2)`来创建一个新的二元数组，并且可以通过`._1()`和`._2()`方法访问其中的元素。java中可以通过调用`mapToPair()`函数创建pair RDD。





