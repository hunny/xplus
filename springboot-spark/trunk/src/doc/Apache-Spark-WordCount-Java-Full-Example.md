# Apache Spark WordCount Java Example

## Apache Spark

* is an open source cluster computing framework. Originally developed at the University of California, Berkeley's AMPLab, the Spark codebase was later donated to the Apache Software Foundation, which has maintained it since. Spark provides an interface for programming entire clusters with implicit data parallelism and fault-tolerance.

## Pre Requirements

* 1) A machine with Ubuntu 14.04 LTS operating system
* 2) Apache Hadoop 2.6.4 pre installed ([How to install Hadoop on Ubuntu 14.04](http://hadoop.praveendeshmane.co.in/hadoop/hadoop-2-6-4-pseudo-distributed-mode-installation-on-ubuntu-14-04.jsp))
* 3) Apache Spark 1.6.1 pre installed ([How to install Spark on Ubuntu 14.04](http://spark.praveendeshmane.co.in/spark/spark-1-6-1-stand-alone-mode-installation-on-ubuntu-14-04.jsp))

## Spark WordCount Java Example

### Step 1 - Add these 2 spark jar files to your java project.

* Add following jars.

```
spark-assembly-1.6.1-hadoop2.6.0.jar
spark-core_2.10-0.9.0-incubating.jar
```

### Step 2 - Create Java class

* Create Java file WordCount.java

```java
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
public class WordCount {

	private static final FlatMapFunction<String, String> WORDS_EXTRACTOR = new FlatMapFunction<String, String>() {
		private static final long serialVersionUID = 1L;

		public Iterable<String> call(String s) throws Exception {
			System.out.println("text file"+s);
			return Arrays.asList(s.split(" "));
		}
	};

	private static final PairFunction<String, String, Integer> WORDS_MAPPER = new PairFunction<String, String, Integer>() {
		private static final long serialVersionUID = 1L;

		public Tuple2<String, Integer> call(String s) throws Exception {
			return new Tuple2<String, Integer>(s, 1);
		}
	};

	private static final Function2<Integer, Integer, Integer> WORDS_REDUCER = new Function2<Integer, Integer, Integer>() {
		private static final long serialVersionUID = 1L;

		public Integer call(Integer a, Integer b) throws Exception {
			return a + b;
		}
	};

	public static void main(String[] args) {

		SparkConf conf1 = new SparkConf().setAppName("com.WordCount")
				.setMaster("spark://127.0.0.1:7077")
				.set("spark.akka.heartbeat.interval", "100")
				.set("spark.local.ip", "127.0.0.1");

		JavaSparkContext context = new JavaSparkContext(conf1);

		JavaRDD<String> file = context
				.textFile("hdfs://localhost:9000/user/hduser/dcin",2);
		
		JavaRDD<String> words = file.flatMap(WORDS_EXTRACTOR);
		JavaPairRDD<String, Integer> pairs = words.mapToPair(WORDS_MAPPER);
		JavaPairRDD<String, Integer> counter = pairs.reduceByKey(WORDS_REDUCER);
		counter.saveAsTextFile("hdfs://localhost:9000/user/hduser/sparkout1/");
				System.out.println("end");

	}
}
```

### Step 3 - Change the directory to `/usr/local/hadoop/sbin`.

```
$ cd /usr/local/hadoop/sbin
```

### Step 4 - Start all hadoop daemons.

* Start up hadoop daemons:

```
$ ./start-all.sh
```

* Using `jps` to check:

```
$ jps
```

### Step 5 - Change the directory to `/usr/local/spark/sbin`.

```
$ cd /usr/local/spark/sbin
```

### Step 6 - Start all spark daemons.

* Start up spark daemons:

```
$ ./start-all.sh
```

* Using `jps` to check:

```
$ jps
```

### Step 7 - The JPS (Java Virtual Machine Process Status Tool)

* The JPS tool is limited to reporting information on JVMs for which it has the access permissions.

```
$ jps
```

### Step 8 - Run your WordCount program

* Run your WordCount program by submitting java project jar file to spark. Creating jar file is left to you.
* Run on cluster:

```
$ ./bin/spark-submit --class com.WordCount --master spark://127.0.0.1:7077 /home/hduser/Desktop/1.6\ SPARK/WordCount.jar
```

* Or Run locally:

```
$ ./bin/spark-submit --class com.WordCount --master local[2] /home/hduser/Desktop/1.6\ SPARK/WordCount.jar
```

### Step 9 - Output.

* Now you can see the output files. Browse hadoop's HDFS UI at `http://localhost:9000`

### Step 10 - Executed application. 

* Now you can see the completed application on spark UI at http://127.0.0.1:7077

### Step 11 - Dont forget to stop hadoop daemons

```
$ ./stop-all.sh
```

### Step 12 - Dont forget to stop spark daemons

```
$ ./stop-all.sh
```

### Step 13 - Over.

* Thanks.