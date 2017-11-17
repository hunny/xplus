## Quartz Concepts

### Job

Job - an interface to be implemented by components that you wish to have executed by the scheduler. The interface has one method `execute(...)`. This is where your scheduled task runs. Information on the `JobDetail` and `Trigger` is retrieved using the `JobExecutionContext`.

作业——期望由调度程序执行的组件需要实现的接口。接口有一个方法`execute(...)`，这是自定义计划任务开始运行的起点。使用Job`execute(...)`方法参数` jobexecutioncontext `可以取得`JobDetail`和`Trigger`的信息。

* Job接口

```
package org.quartz;

public interface Job {
  public void execute(JobExecutionContext context) throws JobExecutionException;
}
```

### JobDetail

JobDetail - used to define instances of `Jobs`. This defines how a job is run. Whatever data you want available to the Job when it is instantiated is provided through the `JobDetail`.
Quartz provides a Domain Specific Language (DSL) in the form of `JobBuilder` for constructing `JobDetail` instances.

任务明细——用于定义`Jobs`实例。这定义了一个作业是如何运行的。任何提供给`Job`使用的数据，都是通过`JobDetail`在`Job`被实例化时传给`Job`的。Quartz提供特定领域说明语言`Domain Specific Language`（DSL）以`JobBuilder`形式来构建` JobDetail `实例。

* JobDetail接口

```
package org.quartz;
public interface JobDetail extends Serializable, Cloneable {
  // ....
}
```

* JobDetail实例化

```
// define the job and tie it to the Job implementation
JobDetail job = newJob(DemoJob.class)
  .withIdentity("myJob", "group1") // name "myJob", group "group1"
  .build();
```

```
JobDetail jobDetail = JobBuilder.newJob(DemoJob.class) //
    .withIdentity("myJob", "group1") //
    .usingJobData(jobDataMap) //
    .build();
```

### Trigger

Trigger - a component that defines the schedule upon which a given Job will be executed. The trigger
provides instruction on when the job is run. Quartz provides a DSL (TriggerBuilder) for constructing Trigger instances.

触发器——定义给定作业的执行时间的组件。Trigger提供有关作业何时运行的说明。Quartz提供DSL（TriggerBuilder）构建`Trigger`实例。

```
// Trigger the job to run now, and then every 40 seconds
Trigger trigger = newTrigger()
  .withIdentity("myTrigger", "group1")
  .startNow()
  .withSchedule(simpleSchedule()
    .withIntervalInSeconds(40)
    .repeatForever())            
  .build();
```

### Scheduler

Scheduler - the main API for interacting with the scheduler. A Scheduler’s life-cycle is bounded by it’s creation, via a SchedulerFactory and a call to its `shutdown()` method. Once created the Scheduler interface can be used to add, remove, and list `Jobs` and `Triggers`, and perform other scheduling-related operations (such as pausing a trigger). However, the Scheduler will not actually act on any triggers (execute jobs) until it has been started with the `start()` method.

调度器——与调度程序交互的主要API。调度程序的生命周期是有界限的，经过创建，再通过`SchedulerFactory`调用它的` shutdown() `方法来结束。一旦创建，调度器接口可用于添加、删除和列出“作业”和“触发器”，并执行与调度相关的其他操作（如暂停触发器）。然而，直到它调用`start()`方法，调度器才会开始调用触发器（执行任务）。

```
SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();

Scheduler scheduler = schedulerFactory.getScheduler();
scheduler.start();

// Tell quartz to schedule the job using our trigger
scheduler.scheduleJob(job, trigger);
```

## About Jobstores

JobStore’s are responsible for keeping track of all the “work data” that you give to the scheduler: jobs, triggers, calendars, etc. Selecting the appropriate JobStore for your Quartz scheduler instance is an important step. Luckily, the choice should be a very easy one once you understand the differences between them.

JobStore的负责跟踪所有给调度程序的“工作数据”，包括任务、触发器、调度时间等。为调度器实例选择合适的JobStore是重要的一步。幸运的是，一旦你理解了它们之间的区别，选择使用哪一种应该是一个非常简单的事件。

You declare which JobStore your scheduler should use (and it’s configuration settings) in the properties file (or object) that you provide to the SchedulerFactory that you use to produce your scheduler instance.

可以申明一种在属性文件（或对象）中已经设置好的JobStore提供给`SchedulerFactory`来控制调度器实例。

There are three types of Jobstores that are available in Quartz:

在Quartz中有三种类型的Jobstore可以使用：

* RAMJobStore - is the simplest JobStore to use, it is also the most performant (in terms of CPU time). RAMJobStore gets its name in the obvious way: it keeps all of its data in RAM. This is why it’s lightning-fast, and also why it’s so simple to configure. The drawback is that when your application ends (or crashes) all of the scheduling information is lost - this means RAMJobStore cannot honor the setting of “non-volatility” on jobs and triggers. For some applications this is acceptable - or even the desired behavior, but for other applications, this may be disastrous. In this part of the series we will be using RAMJobStore.
* RAMJobStore -是JobStore最简单的使用方式，也是性能最优的（就CPU处理时间而言）。RAMJobStore从字面上可以明显地看出：它把所有数据都放在RAM内存中。这就是为什么它闪电般的快，也是为什么配置这么简单的原因。缺点是，当应用程序结束（或崩溃）所有的调度信息丢失-这意味着RAMJobStore不能存储jobs和triggers配置和计算的相关信息。对于某些应用程序，这是可以接受的，甚至是期望的行为，但对于其他应用程序，这可能是灾难性的。

* JDBCJobStore - is also aptly named - it keeps all of its data in a database via JDBC. Because of this it is a bit more complicated to configure than RAMJobStore, and it also is not as fast. However, the performance
draw-back is not terribly bad, especially if you build the database tables with indexes on the primary keys. 
On fairly modern set of machines with a decent LAN (between the scheduler and database) the time to retrieve and update a firing trigger will typically be less than 10 milliseconds. 
* JDBCJobStore - 顾名思义-它通过JDBC来管理所有的数据。因为要通过JDBC连接数据库，所以它的配置比RAMJobStore稍微复杂一点，而且还没有RAMJobStore快。然而，它的性能不是想像中的那样糟糕，尤其是在主键上使用索引构建数据库表时。
在具有相当局域网（在调度器和数据库之间）的现代的机器上，检索和更新触发触发器的时间通常小于10毫秒。

* TerracottaJobStore - provides a means for scaling and robustness without the use of a database. This means your database can be kept free of load from Quartz, and can instead have all of its resources saved for the rest of your application.

  - TerracottaJobStore can be ran clustered or non-clustered, and in either case provides a storage medium for your job data that is persistent between application restarts, because the data is stored in the Terracotta server. It’s performance is much better than using a database via JDBCJobStore (about an order of magnitude better), but fairly slower than RAMJobStore. 
* TerracottaJobStore -为不使用数据库调用Quartz在保证规模性和健壮性方面上使用提供了一种手段。这意味着您的数据库可以免于来自Quartz的负载，并且可以将其所有资源保存在应用程序的其余部分中。
  - TerracottaJobStore可以集群或非集群，换句话讲，在应用程序重新启动时，从Terracotta服务器中获取工作数据并且在应用停止把存储它的数据到Terracotta服务器，因为数据是存储在Terracotta服务器，所以它的性能比使用数据库通过JDBCJobStore要好得多（约一个数量级的），但相当比RAMJobStore慢。


