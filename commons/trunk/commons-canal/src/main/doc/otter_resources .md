# 官方开源地址 
 + [otter](https://github.com/alibaba/otter/)
 + [canal](https://github.com/alibaba/canal/)

# 资料
 + [Otter-入门篇1(阿里开源项目Otter介绍)](https://my.oschina.net/wenzhenxi/blog/719095)
 + [Otter-入门篇2(Manager安装配置)](https://my.oschina.net/wenzhenxi/blog/719722)
 + [Otter-入门篇3(Node搭建)](https://my.oschina.net/wenzhenxi/blog/729339)
 + [Otter-入门篇4(单向同步实践)](https://my.oschina.net/wenzhenxi/blog/734673)
 + [Otter调度模型](https://my.oschina.net/sansom/blog/157867)
 + [Canal+Otter - Canal篇（1）](https://my.oschina.net/zhxdick/blog/665229)
 + [Canal+Otter - 前日篇（2）](https://my.oschina.net/zhxdick/blog/665198)
 + [阿里巴巴分布式数据库同步系统 otter](https://www.oschina.net/p/otter)

# otter 原理描述

原理描述：
1. 基于Canal开源产品，获取数据库增量日志数据。
2. 典型管理系统架构，manager(web管理)+node(工作节点)
 a. manager运行时推送同步配置到node节点
 b. node节点将同步状态反馈到manager上
3. 基于zookeeper，解决分布式状态调度的，允许多node节点之间协同工作.

# otter QuickStart

## Manager_Quickstart

### 环境准备

1. otter manager依赖于mysql进行配置信息的存储，所以需要预先安装mysql，并初始化otter manager的系统表结构

```
a. 安装mysql，这里不展开，网上一搜一大把
b. 初始化otter manager系统表：
```

下载：
wget https://raw.github.com/alibaba/otter/master/manager/deployer/src/main/resources/sql/otter-manager-schema.sql 
载入：
source otter-manager-schema.sql
2. 整个otter架构依赖了zookeeper进行多节点调度，所以需要预先安装zookeeper，不需要初始化节点，otter程序启动后会自检.

    a. manager需要在otter.properties中指定一个就近的zookeeper集群机器

启动步骤

1. 下载otter manager

直接下载 ，可访问：https://github.com/alibaba/otter/releases ，会列出所有历史的发布版本包下载方式，比如以x.y.z版本为例子：

wget https://github.com/alibaba/otter/releases/download/otter-x.y.z/manager.deployer-x.y.z.tar.gz
or
自己编译

git clone git@github.com:alibaba/otter.git
cd otter; 
mvn clean install -Dmaven.test.skip -Denv=release
编译完成后，会在根目录下产生target/manager.deployer-$version.tar.gz

2. 解压缩

mkdir /tmp/manager
tar zxvf manager.deployer-$version.tar.gz  -C /tmp/manager
3. 配置修改

## otter manager domain name #修改为正确访问ip，生成URL使用
otter.domainName = 127.0.0.1    
## otter manager http port
otter.port = 8080
## jetty web config xml
otter.jetty = jetty.xml

otter manager database config ，修改为正确数据库信息


otter.database.driver.class.name = com.mysql.jdbc.Driver
otter.database.driver.url = jdbc:mysql://127.0.01:3306/ottermanager
otter.database.driver.username = root
otter.database.driver.password = hello



otter communication port


otter.communication.manager.port = 1099



otter communication pool size


otter.communication.pool.size = 10



default zookeeper address，修改为正确的地址，手动选择一个地域就近的zookeeper集群列表


otter.zookeeper.cluster.default = 127.0.0.1:2181



default zookeeper sesstion timeout = 90s


otter.zookeeper.sessionTimeout = 90000



otter arbitrate connect manager config


otter.manager.address = ${otter.domainName}:${otter.communication.manager.port}

4. 准备启动

sh startup.sh
5. 查看日志

vi logs/manager.log
2013-08-14 13:19:45.911 [] WARN  com.alibaba.otter.manager.deployer.JettyEmbedServer - ##Jetty Embed Server is startup!
2013-08-14 13:19:45.911 [] WARN  com.alibaba.otter.manager.deployer.OtterManagerLauncher - ## the manager server is running now ......
出现类似日志，代表启动成功
6. 验证

访问： http://127.0.0.1:8080/，出现otter的页面，即代表启动成功

访问：http://127.0.0.1:8080/login.htm，初始密码为：admin/admin，即可完成登录. 目前：匿名用户只有只读查看的权限，登录为管理员才可以有操作权限

7. 关闭
sh stop.sh
it's over.

### Manager配置介绍

操作演示

演示视频（5分钟教你配置一个同步任务）：请点击图片或者这里

[![ScreenShot](http://dl2.iteye.com/upload/attachment/0088/3012/4409999b-486f-36d7-a425-962b941b3b15.jpg)](http://www.tudou.com/programs/view/Q-qnCg7d-ew)
演示说明：

   1. 搭建一个数据库同步任务，源数据库ip为：10.20.144.25，目标数据库ip为：10.20.144.29. 源数据库已开启binlog，并且binlog_format为ROW.

mysql> show variables like '%binlog_format%';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| binlog_format | ROW   |
+---------------+-------+
   2. 数据同步精确到一张表进行测试，测试的表名为test.example，简单包含两个子段，测试过程中才创建.

   3. 配置完成后，手动在源库插入数据，然后快速在目标库进行查看数据，验证数据是否同步成功.

-------
视频中的演示文本：

CREATE TABLE  `test`.`example` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL ,
   PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into test.example(id,name) values(null,'hello');

-----
Otter QuickStart 如何配置一个任务
-----
操作步骤：
1.  添加数据库
    a.  源库 jdbc:mysql://10.20.144.25:3306
    b.  目标库 jdbc:mysql://10.20.144.29:3306
2.  添加canal
    a.  提供数据库ip信息 
3.  添加同步表信息
    a.  源数据表 test.example
    b.  目标数据表 test.example
4.  添加channel
5.  添加pipeline
    a.  选择node节点
    b.  选择canal
6.  添加同步映射规则
    a.  定义源表和目标表的同步关系
7.  启动
8.  测试数据 
通道配置说明

多种同步方式配置

a. 单向同步

单向同步为最基本的同步方式，目前支持mysql -> mysql/oracle的同步.

基本配置方式就如操作视频中所演示的，操作步骤：

配置一个channel
配置一个pipeline
对应node机器选择时，建议遵循：S/E节点选择node需尽可能离源数据库近，T/L节点选择node则离目标数据库近. 如果无法提供双节点，则选择离目标数据库近的node节点相对合适.
配置一个canal
定义映射关系. 
canal中配置解析的数据库ip需要和映射关系中源表对应的数据库ip一致. ps. 映射关系进行匹配的时候是基于表名，虽然数据库ip不匹配也会是有效.
b. 双向同步

双向同步可以理解为两个单向同步的组合，但需要额外处理避免回环同步. 回环同步算法： Otter双向回环控制 .

同时，因为双向回环控制算法会依赖一些系统表，需要在需要做双向同步的数据库上初始化所需的系统表.

获取初始sql:

wget https://raw.github.com/alibaba/otter/master/node/deployer/src/main/resources/sql/otter-system-ddl-mysql.sql
配置上相比于单向同步有一些不同，操作步骤：

配置一个channel
配置两个pipeline 
注意：两个单向的canal和映射配置，在一个channel下配置为两个pipeline. 如果是两个channel，每个channel一个pipeline，将不会使用双向回环控制算法，也就是会有重复回环同步.
每个pipeline各自配置canal，定义映射关系
c. 双A同步

双A同步相比于双向同步，主要区别是双A机房会在两地修改同一条记录，而双向同步只是两地的数据做互相同步，两地修改的数据内容无交集.

所以双A同步需要额外处理数据同步一致性问题. 同步一致性算法：Otter数据一致性 ，目前开源版本主要是提供了单向回环补救的一致性方案.

双A同步相比于双向同步，整个配置主要是一些参数上有变化，具体步骤：

配置一个channel 

配置两个pipeline

* 注意：除了需要定义一个主站点外，需要在高级设置中将一个pipeline的“支持DDL”设置为false，另一个设置为true，否则将提示“一个channel中只允许开启单向ddl同步!”错误
每个pipeline各自配置canal，定义映射关系
d. 级联同步

单向同步/双向同步，都是针对一个channel下的多pipeline配置进行控制，是否可以使用多个channel完成类似级联同步的功能.

几种级联同步.

A->B->C ，A单向同步到B，B再单向同步到C
A<->B->C，A和B组成一个双向，B再单向同步到C
A<->B<-C，A和B组成一个双向，C将数据单向同步B，也就是B是一个接受多M同步写入的的节点，目前mysql不支持
A<->B->C，B-/->D，A和B组成一个双向，B再单向同步到C，但A同步到B的数据不同步到D，但B地写入的数据同步到D，目前mysql不支持.
对应操作步骤：

目前channel之间的级联同步，不需要设置任何参数，只要通过canal进行binlog解析即可.
针对级联屏蔽同步，需要利用到自定义同步标记的功能，比如A->B，B同步到C但不同步到D。需要在A->B的同步高级参数里定义NOT_DDD，然后在B同步到D的高级参数里也定义NOT_DDD. 
原理：这样在B解析到A->B写入的同步标记为NOT_DDD，与当前同步定义的NOT_DDD进行匹配，就会忽略此同步. 


e. 多A同步

基于以上的单向/双向/双A/级联同步，可以随意搭建出多A同步，不过目前受限于同步数据的一致性算法，只能通过星形辐射，通过级联同步的方式保证全局多A机房的数据一致性. 
比如图中B和C之前的一致性同步，需要通过主站点A来保证.


自定义数据同步(自 由 门)

主要功能是在不修改原始表数据的前提下，触发一下数据表中的数据同步。

可用于：

同步数据订正
全量数据同步. (自 由 门触发全量，同时otter增量同步，需要配置为行记录模式，避免update时因目标库不存在记录而丢失update操作)
主要原理：

a. 基于otter系统表retl_buffer，插入特定的数据，包含需要同步的表名，pk信息。

b. otter系统感知后会根据表名和pk提取对应的数据(整行记录)，和正常的增量同步一起同步到目标库。

目前otter系统感知的自 由 门数据方式为：

日志记录. (插入表数据的每次变更，需要开启binlog，otter获取binlog数据，提取同步的表名，pk信息，然后回表查询整行记录)
retl_buffer表结构：

  CREATE TABLE retl_buffer 
   (    
    ID BIGINT AUTO_INCREMENT,   ## 无意义，自增即可
    TABLE_ID INT(11) NOT NULL,   ## tableId, 可通过该链接查询：http://otter.alibaba-inc.com/data_media_list.htm，即序号这一列，如果配置的是正则，需要指定full_name，当前table_id设置为0. 
    FULL_NAME varchar(512),  ## schemaName + '.' +  tableName  (如果明确指定了table_id，可以不用指定full_name)
    TYPE CHAR(1) NOT NULL,   ## I/U/D ，分别对应于insert/update/delete
    PK_DATA VARCHAR(256) NOT NULL, ## 多个pk之间使用char(1)进行分隔
    GMT_CREATE TIMESTAMP NOT NULL, ## 无意义，系统时间即可
    GMT_MODIFIED TIMESTAMP NOT NULL,  ## 无意义，系统时间即可
    CONSTRAINT RETL_BUFFER_ID PRIMARY KEY (ID) 
   )  ENGINE=InnoDB DEFAULT CHARSET=utf8;
全量同步操作示例：

insert into retl.retl_buffer(ID,TABLE_ID, FULL_NAME,TYPE,PK_DATA,GMT_CREATE,GMT_MODIFIED) (select null,0,'$schema.table$','I',id,now(),now() from $schema.table$); 
如果针对多主键时，对应的PK_DATA需要将需要同步表几个主键按照(char)1进行拼接,比如 concat(id,char(1),name)
具体参数详解

channel参数

同步一致性. ==> 基于数据库反查(根据binlog反查数据库)，基于当前变更(binlog数据)。针对数据库反查，在延迟比较大时比较有效，可将最新的版本快速同步到目标，但会对源库有压力.
同步模式. ==> 行模式，列模式。行模式特点：如果目标库不存在记录时，执行插入。列模式主要是变更哪个字段，只会单独修改该字段，在双Ａ同步时，为减少数据冲突，建议选择列模式。
是否开启数据一致性. ==>　请查看数据一致性文档：Otter数据一致性
a. 数据一致性算法
b. 一致性反查数据库延迟阀值
pipeline参数

并行度. ==> 查看文档：Otter调度模型，主要是并行化调度参数.(滑动窗口大小)
数据反查线程数. ==> 如果选择了同步一致性为反查数据库，在反查数据库时的并发线程数大小
数据载入线程数. ==> 在目标库执行并行载入算法时并发线程数大小
文件载入线程数. ==> 数据带文件同步时处理的并发线程数大小
主站点. ==> 双Ａ同步中的主站点设置　
消费批次大小. ==> 获取canal数据的batchSize参数
获取批次超时时间. ==> 获取canal数据的timeout参数
pipeline 高级设置
使用batch. ==> 是否使用jdbc batch提升效率，部分分布式数据库系统不一定支持batch协议
跳过load异常. ==> 比如同步时出现目标库主键冲突，开启该参数后，可跳过数据库执行异常
仲裁器调度模式. ==> 查看文档：Otter调度模型
负载均衡算法. ==> 查看文档：Otter调度模型
传输模式. ==> 多个node节点之间的传输方式，RPC或HTTP. HTTP主要就是使用aria2c，如果测试环境不装aria2c，可强制选择为RPC
记录selector日志. ==> 是否记录简单的canal抓取binlog的情况
记录selector详细日志. ==> 是否记录canal抓取binlog的数据详细内容
记录load日志. ==> 是否记录otter同步数据详细内容
dryRun模式. ==> 只记录load日志，不执行真实同步到数据库的操作
支持ddl同步. ==> 是否同步ddl语句
是否跳过ddl异常. ==> 同步ddl出错时，是否自动跳过
文件重复同步对比 ==> 数据带文件同步时，是否需要对比源和目标库的文件信息，如果文件无变化，则不同步，减少网络传输量.
文件传输加密 ==> 基于HTTP协议传输时，对应文件数据是否需要做加密处理
启用公网同步 ==> 每个node节点都会定义一个外部ip信息，如果启用公网同步，同步时数据传递会依赖外部ip.
跳过自 由 门数据 ==> 自定义数据同步的内容
跳过反查无记录数据 ==> 反查记录不存在时，是否需要进行忽略处理，不建议开启.
启用数据表类型转化 ==> 源库和目标库的字段类型不匹配时，开启改功能，可自动进行字段类型转化
兼容字段新增同步 ==> 同步过程中，源库新增了一个字段(必须无默认值)，而目标库还未增加，是否需要兼容处理
自定义同步标记 ==> 级联同步中屏蔽同步的功能.
Canal参数

数据源信息
单库配置： 10.20.144.34:3306;
多库合并配置： 10.20.144.34:3306,10.20.144.35:3306; (逗号分隔)
主备库配置：10.20.144.34:3306;10.20.144.34:3307; (分号分隔)
数据库帐号
数据库密码
connectionCharset ==> 获取binlog时指定的编码
位点自定义设置 ==> 格式：{"journalName":"","position":0,"timestamp":0}; 
指定位置：{"journalName":"","position":0}; 
指定时间：{"timestamp":0};
内存存储batch获取模式　==> MEMSIZE/ITEMSIZE，前者为内存控制，后者为数量控制. 　针对MEMSIZE模式的内存大小计算 = 记录数 * 记录单元大小
内存存储buffer记录数
内存存储buffer记录单元大小
HA机制
心跳SQL配置 ==> 可配置对应心跳SQL，如果配置 是否启用心跳HA，当心跳ＳＱＬ检测失败后，canal就会自动进行主备切换.
Node参数

机器名称 ==> 自定义名称，方便记忆
机器ip ==> 机器外部可访问的ip，不能选择127.0.0.1
机器端口 ==> 和manager/node之间RPC通讯的端口
下载端口 ==> 和node之间HTTP通讯的端口
外部Ip ==> node机器可以指定多IP，通过pipeline配置决定是否启用
zookeeper集群 ==> 就近选择zookeeper集群
Zookeeper集群参数

集群名字 ==> 自定义名称，方便记忆
zookeeper集群 ==> zookeeper集群机器列表，逗号分隔，最后以分号结束
主备配置参数

group Key ==> 自定义名称，otter其他地方基于该名称进行引用
master / slave ==> 主备库ip信息
生成了groupKey，1. 可以在数据库配置时，设置url：jdbc:mysql://groupKey=key (更改 key). 2. 在canal配置时，选择HA机制为media，可填入该groupKey进行引用

### 映射规则配置

背景

因为alibaba的特殊业务，比如：

同步数据同时，需要同步数据关联的文件 (需要做数据join)
同步会员数据，敏感字段信息不能同步到美国站点. (需要做数据过滤)
两地机房的数据库可能为异构数据库，(需要做字段类型，名字等转化.)
为解决这些业务，otter引入了映射规则这一概念，用于描述这样的一种同步业务的关系，其粒度可以精确到一张表，或者是一整个库.

映射规则

表映射

otter中每个pipeline可以设置多个映射规则，每个映射规则描述一个数据库同步的内容，比如源表是哪张，同步到哪张目标表。



权重的概念

可以先看下：Otter数据入库算法 ， 因为otter采用了pk hash的并行载入算法，会将原先binlog中的事务进行打散做并行处理提升同步性能。原先事务被拆散后，通过这个权重概念，来提供“业务上类事务功能".

举个实际点的例子来看：

比如有两张表，product(记录产品的属性信息)和product_detail(记录产品的详情信息)，product_detail上有个字段为product_id与product进行关联. 目前为1:1的关系
业务上插入数据可以通过事务，先插入product，然后插入product_detail，最后一起提交到数据库. 正常，页面上通过查询用户的product表的数据，发现有产品记录，然后通过product_id去查询product_detail表，查到后才显示产品页面
假如同步到目标库后，打散事务后，同步过程如果先插入了product表，后插入product_detail表，这时如果有用户访问该产品记录，可能就会遇到product_detail不存在的情况，从而导致页面出错.
所以，我们通过权重定义，比如将product_detail权重定义为5，将product定义为10。 otter会优先同步权重低的表数据，最终可以保证查询product有数据后，product_detail一定有数据，避免业务出错.
视图映射

如何进入视图编辑：



点击下一步后，进入视图编辑页面：




说明：

映射规则配置页面，可以选择视图模式为：include或exclude，代表正向匹配或者逆向排除.
视图配置页面，只支持存在的数据表(因为要获取数据表结构，所以.*等正则的数据表不支持配置该功能)
视图配置列表，左右选中列表会按照顺序进行对应，做映射时需按照顺序进行选择.
举个例子：

如果要排除表字段A的同步，则只需要选择为exclude模式，然后视图编辑页面选择左右皆选择A字段即可，点击保存.

字段组映射

首先解释一下，需要字段组同步的需求.

文件同步. 一条记录对应的图片，可能会有一个或者多个字段，比如会有image_path,image_version来决定图片，所以我们可以定义这两个字段为一组，只要满足组内任意一个字段的变更，就会认为需要文件同步.
数据上的组同步，比如国家，省份，城市，可能在数据库为三个字段. 如果是双A同步，两地同时修改这些字段，但业务上可能在A地修改了国家为美国，在B地修改为省份为浙江，然后一同步，最终就会变成美国，浙江这样的情况. 这种情况可以通过group来解决，将国家，省份，城市做一个group，组内任何一个字段发生了变更，其余字段会做为整体一起变更.
再来看一下配置：(点击视图编辑页面的下一步，即可进入)


说明：

也可不配置视图，单独配置字段组，此时可选择的字段即为当前所有字段(映射规则按照同名映射).
高级映射

主要分为两类：

文件同步
自定义数据同步
具体代码扩展方式和配置可参见： Otter扩展性

配置方式：



文件同步

首先解释一下文件同步的需求，阿里巴巴国际站业务，主要模式为对外贸易，卖家基本在国内，买家在国外. 所以，目前我们的机房部署为杭州和美国各一个，卖家访问杭州机房，买家访问美国机房。卖家在国内发布产品和图片，通过otter同步到美国，同步产品数据记录的同时，同样需要把图片同步过去，保证买家用户的访问体验. 所以，基于这个业务场景，衍生出了文件同步的需求.

所以，做文件同步的几个前提：

异地同步 (需要部署为两个node，S/E和T/L分为两地. )
数据触发文件同步 (数据库记录做为类似文件索引信息，不支持单独同步文件)
本地文件同步 (需要同步的文件需要和node部署在一台机器上或者通过nfs mount，如果要同步 公司自带的分布式文件系统的数据，otter需要做额外扩展)
文件同步准备工作：

准备两台机器，分别部署上两个node
配置channel/pipeline同步，配置映射规则
编写FileResolver解析类，根据binlog中的变更数据，转化为文件的路径信息. 例子：TestFileResolver
 public class TestFileResolver extends AbstractFileResolver {
public FileInfo[] getFileInfo(Map&lt;String, String&gt; rowMap) {
    // 基本步骤：
    // 1. 获取binlog中的变更字段，比如组成文件有多个字段组成version+path
    // 2. 基于字段内容，构造一个文件路径，目前开源版本只支持本地文件的同步.(如果是网络文件，建议进行NFS mount到ndde机器).
    // 3. 返回FileInfo数组，(目前不支持目录同步，如果是目录需要展开为多个FileInfo的子文件)，如果不需要同步，则返回null.
    String path = rowMap.get("FIELD"); //注意为大写
    FileInfo fileInfo = null;
    if (StringUtils.isNotEmpty(path)) {
        fileInfo = new FileInfo(path);
        return new FileInfo[] { fileInfo };
    } else {
        return null;
    }
}

}

自定义数据同步

通过前面的字段视图映射，或许可以解决80%的需求，但总会有一小撮的特殊用户，希望可以自定义自己的同步数据内容，所以otter引入了自定义数据同步为EventProcessor，允许你任意改变整个同步过程中的数据内容.

可以支持的需求：

根据字段内容，判断是否需要屏蔽本记录同步
动态增加/减少字段
动态修改字段内容
动态改变事件类型(Insert/Update/Delete)
几点注意：

EventProcessor主要是在E模块进行数据处理，也就是EventProcessor处理后的数据，会再次经过视图配置，字段组映射，文件同步，最后进入Transform处理.
EventProcessor修改数据中的schema/table name需要谨慎，因为会继续后续的E/T/L流程，所以需要保证修改后的name在映射规则列表里有配置，否则同步会出错.
一个例子：(比如我想将源库的每条binlog变更，记录到一个日志表binlog，映射规则配置为.*所有表的同步)

代码：TestEventProcessor

public class TestEventProcessor extends AbstractEventProcessor {
public boolean process(EventData eventData) {
    // 基本步骤：
    // 1. 获取binlog中的变更字段
    // 2. 根据业务逻辑进行判断，如果需要忽略本条数据同步，直接返回false，否则返回true
    // 3. 根据业务逻辑进行逻辑转化，比如可以修改整个EventData数据.  

    // 本文例子：源库的每条binlog变更，记录到一个日志表binlog
    // create table test.binlog(
    //        id bigint(20) auto_increment,
    //        oschema varchar(256),
    //        otable varchar(256),
    //        gtime varchar(32)
    //        ovalue text,
    //        primary key(id);
    //    )
    // 在process处理中，可以修改EventData的任何数据，达到数据转换的效果, just have fun.
    JSONObject col = new JSONObject();
    JSONArray array = new JSONArray();
    for (EventColumn column : eventData.getColumns()) {
        JSONObject obj = this.doColumn(column);
        array.add(obj);
    }

    for (EventColumn key : eventData.getKeys()) {
        JSONObject obj = this.doColumn(key);
        array.add(obj);
    }
    // 记录原始的表信息
    col.put("schema", eventData.getSchemaName());
    col.put("table", eventData.getTableName());
    col.put("columns", array);
    col.put("dml", eventData.getEventType());
    col.put("exectime", eventData.getExecuteTime());

    // 构造新的主键
    EventColumn id = new EventColumn();
    id.setColumnValue(eventData.getSchemaName());
    id.setColumnType(Types.BIGINT);
    id.setColumnName("id");
    // 构造新的字段
    EventColumn schema = new EventColumn();
    schema.setColumnValue(eventData.getSchemaName());
    schema.setColumnType(Types.VARCHAR);
    schema.setColumnName("oschema");

    EventColumn table = new EventColumn();
    table.setColumnValue(eventData.getTableName());
    table.setColumnType(Types.VARCHAR);
    table.setColumnName("otable");

    EventColumn ovalue = new EventColumn();
    ovalue.setColumnValue(col.toJSONString());
    ovalue.setColumnType(Types.VARCHAR);
    ovalue.setColumnName("ovalue");

    EventColumn gtime = new EventColumn();
    gtime.setColumnValue(eventData.getExecuteTime() + "");
    gtime.setColumnType(Types.VARCHAR);
    gtime.setColumnName("gtime");

    // 替换为新的字段和主键信息
    List&lt;EventColumn&gt; cols = new ArrayList&lt;EventColumn&gt;();
    cols.add(schema);
    cols.add(table);
    cols.add(gtime);
    cols.add(ovalue);
    eventData.setColumns(cols);

    List&lt;EventColumn&gt; keys = new ArrayList&lt;EventColumn&gt;();
    keys.add(id);
    eventData.setKeys(keys);

    //修改数据meta信息
    eventData.setEventType(EventType.INSERT);
    eventData.setSchemaName("test");
    eventData.setTableName("binlog");
    return true;
}

private JSONObject doColumn(EventColumn column) {
    JSONObject obj = new JSONObject();
    obj.put("name", column.getColumnName());
    obj.put("update", column.isUpdate());
    obj.put("key", column.isKey());
    if (column.getColumnType() != Types.BLOB &amp;&amp; column.getColumnType() != Types.CLOB) {
        obj.put("value", column.getColumnValue());
    } else {
        obj.put("value", "");
    }
    return obj;
}

}