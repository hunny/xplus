# spark创建RDD

## 使用parallelize方法创建RDD
通过调用SparkContext的parallelize方法，在一个已经存在的Scala集合上创建的（一个Seq对象）。集合的对象将会被拷贝，创建出一个可以被并行操作的分布式数据集。

```
data = [1, 2, 3, 4, 5]  
distData = sc.parallelize(data)  
```

一旦分布式数据集(RDD)（distData）被创建好，它们将可以被并行操作。例如，我们可以调用distData.reduce(lambda a, b: a + b)来将数组的元素相加。并行集合的一个重要参数是slices，表示数据集切分的份数。Spark将会在集群上为每一份数据起一个任务。典型地，你可以在集群的每个CPU上分布2-4个slices. 一般来说，Spark会尝试根据集群的状况，来自动设定slices的数目。然而，你也可以通过传递给parallelize的第二个参数来进行手动设置。（例如：sc.parallelize(data, 10)).

## 使用textFile方法创建RDD

* 调用SparkContext.textFile()方法，从外部存储中读取数据来创建 RDD。

```
 JavaRDD<String> lines = sc.textFile("C:\\dataexample\\wordcount\\input");
```

* 注: textFile支持分区，支持模式匹配，例如把C:\dataexample\wordcount\目录下inp开头的给转换成RDD

```
var lines = sc.textFile("C:\\dataexample\\wordcount\\inp*")
```

* 多个路径可以使用逗号分隔，例如

```
var lines = sc.textFile("dir1,dir2",3)
```
