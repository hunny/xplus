#Mac单机版Spark

## 安装流程：

1. 点击下载，在“Download Spark”的三个下拉框里选择。
2. 点击“4. Download Spark: xxxxx” 的链接就可以下载。
3. 解压下载好的tgz文件，并将这个文件夹（spark-2.2.0-bin-hadoop2.7）放到你想存放的地方（比如“/usr/local/spark-2.2.0-bin-hadoop2.7”）。
4. 然后这就算装好！

## 配置

1. 到目录`/usr/local/spark-2.2.0-bin-hadoop2.7/conf`下有一个文件`spark-env.sh.template`。
2. 复制并重新命名为`spark-env.sh`，加入如下配置
```
SPARK_MASTER_HOST=127.0.0.1
SPARK_LOCAL_IP=127.0.0.1
```
## 运行

1. ［启动主机］
* 点开官方简介，标题“Starting a Cluster Manually”。 你可以看到主机启动命令，复制它：

```
./sbin/start-master.sh
```
2. 回到Finder，右键点击刚才解压并移动过的文件夹，点击最下面的“New Tab Terminal at Folder”. 在命令框里黏贴上面那条命令。回车。出来2，3行路径。这时候主机已经开始运行了。

3. 打开浏览器。 输入下面的默认端口就能看见主机运行情况。

```
http://localhost:8080
```
4. 现在的主机是个光杆司令，要给他派兵啊。

在官方简介“Starting a Cluster Manually”下复制命令

```
./bin/spark-class org.apache.spark.deploy.worker.Worker spark://IP:PORT
```
回到terminal命令行，点击右上角的“＋”号。打开一个拥有同样路径的新界面。黏贴复制的命令，按后退键删除掉 spark://IP:PORT （别急着回车！我们要贴上自己的主机地址。）

回到浏览器的“http://localhost:8080/”页面

复制第二行黑体字URL:后面的部分，“spark://xxxx:xxxx”也就是你的主机地址。前四个x是一串字符，冒号后的四个x是四个数字。（这里我们是在同一个电脑上创建主机和副机。如果你是把别的电脑当作副机加入主机，那么要求主机和副机是在同一个网络中。同样，需要在spark文件夹路径下打开terminal输入相应的命令）

URL:spark://xxxx:xxxx
5. 这样整条命令就变成“./bin/spark-class org.apache.spark.deploy.worker.Worker spark://xxxx:xxxx” 安心的拍下回车吧！

6. 刷刷几页信息跳过后，这个窗口就像死了机一样了。别担心，这是因为这个命令端已经作为副机为你刚建立的主机开始卖命了。不信的话你刷新一下主机信息网页。

http://localhost:8080
在“Workers”列表下赫然出现了你的第一个副机。其状态State为ALIVE。表示它正在运作。这时候你需要记住，当前Terminal的窗口对应就是那个刚加入的Worker Id。

7. ［删除副机］工作干完了，把小崽子们放出去玩吧。只需要在那个看似死机了teminal里按下“Ctrl ＋ C”就行了。

这时候命令行回复工作，而主机信息网页刷新后会将对应副机的state改成DEAD。说明和副机的联系切断了。此信息回保留一段时间，然后会被完全删除。

8. ［关闭主机］

和启动主机一样，在主文件夹路径下输入相应命令即可。

./sbin/stop-master.sh
这时http://localhost:8080/ 刷新后就是空网页了。

如果你有没有关闭的副机。在其terminal窗口中会显示其试图重连主机的信息。如果你再次打开主机（而且port号没变的话），没关的副机会重新连上去。

＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

小结 Sum up：

0.知道怎么在Mac上装Spark。（就是解压缩嘛！）
1.知道怎么建立和关闭主机。（一个命令）
2.知道怎么建立副机连上主机。（查好主机URL然后一个命令）
3.知道怎么关闭副机。（在相应terminal中拍“Ctrl+C”）