# Java Executors和ThreadPoolExecutor 线程池

标签（空格分隔）： Java线程 Executors ThreadPoolExecutor 线程池

---

## Java Executors四种线程池

- newCachedThreadPool

创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。 

    创建方式： Executors.newCachedThreadPool()；

* newFixedThreadPool

创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。定长线程池的大小最好根据系统资源进行设置，如Runtime.getRuntime().availableProcessors()。 

    创建方式： Executors.newFixedThreadPool()；

* newScheduledThreadPool 

创建一个定长线程池，支持定时及周期性任务执行。 

    创建方式： Executors.newScheduledThreadPool ()；

* newSingleThreadExecutor

创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。 

    创建方式： Executors.newSingleThreadExecutor ()；

## 一个newScheduledThreadPool使用示例

* 其他的创建完线程池后，使用 threadPool.execute(new Runnable())方式执行任务。

```
public static void main(String[] args) {  
  ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);  
  // 表示延迟3秒执行
  scheduledThreadPool.schedule(new Runnable() {  
   public void run() {  
    System.out.println("delay 3 seconds");  
   }  
  }, 3, TimeUnit.SECONDS);  
 }  
 // 表示延迟1秒后每3秒执行一次
 scheduledThreadPool.scheduleAtFixedRate(new Runnable() {  
   public void run() {  
    System.out.println("delay 1 seconds, and excute every 3 seconds");  
   }  
  }, 1, 3, TimeUnit.SECONDS);  
 }  
```

## Java Executors 简述

Executors 类提供了使用了 ThreadPoolExecutor 的简单的 ExecutorService 实现，也就是上面所说的四种Executors线程池，但是 ThreadPoolExecutor 提供的功能远不止于此。 

    在Java doc中，并不提倡我们直接使用ThreadPoolExecutor，而是使用Executors类中提供的几个静态方法来创建线程池。
我们可以在创建 ThreadPoolExecutor 实例时指定活动线程的数量，我们也可以限制线程池的大小并且创建我们自己的 RejectedExecutionHandler 实现来处理不能适应工作队列的工作。 
下面我们就先了解一下ThreadPoolExecutor，然后在看个示例代码。

### Executors 源码：

```
public class Executors {

    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }

    public static ExecutorService newWorkStealingPool(int parallelism) {
        return new ForkJoinPool
            (parallelism,
             ForkJoinPool.defaultForkJoinWorkerThreadFactory,
             null, true);
    }

    public static ExecutorService newWorkStealingPool() {
        return new ForkJoinPool
            (Runtime.getRuntime().availableProcessors(),
             ForkJoinPool.defaultForkJoinWorkerThreadFactory,
             null, true);
    }

    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(),
                                      threadFactory);
    }

    public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>(),
                                    threadFactory));
    }

    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>(),
                                      threadFactory);
    }
    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return new DelegatedScheduledExecutorService
            (new ScheduledThreadPoolExecutor(1));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return new DelegatedScheduledExecutorService
            (new ScheduledThreadPoolExecutor(1, threadFactory));
    }

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize);
    }

    public static ScheduledExecutorService newScheduledThreadPool(
            int corePoolSize, ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
    }
}
```

### ThreadPoolExecutor类

java.uitl.concurrent.ThreadPoolExecutor类是线程池中最核心的一个类，因此如果要透彻地了解Java中的线程池，必须先了解这个类。下面我们来看一下ThreadPoolExecutor类的具体实现源码。

在ThreadPoolExecutor类中提供了四个构造方法：
```
public class ThreadPoolExecutor extends AbstractExecutorService {
    .....
    public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
            BlockingQueue<Runnable> workQueue);

    public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
            BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory);

    public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
            BlockingQueue<Runnable> workQueue,RejectedExecutionHandler handler);

    public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
        BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory,RejectedExecutionHandler handler);
    ...
}
```
ThreadPoolExecutor继承了AbstractExecutorService类，并提供了四个构造器，事实上，通过观察每个构造器的源码具体实现，发现前面三个构造器都是调用的第四个构造器进行的初始化工作。

- 构造器中各个参数的含义：
  + corePoolSize：核心池的大小，这个参数跟后面讲述的线程池的实现原理有非常大的关系。在创建了线程池后，默认情况下，线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，除非调用了prestartAllCoreThreads()或者prestartCoreThread()方法，从这2个方法的名字就可以看出，是预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程。默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
  + maximumPoolSize：线程池最大线程数，这个参数也是一个非常重要的参数，它表示在线程池中最多能创建多少个线程；
  + keepAliveTime：表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0；
  + unit：参数keepAliveTime的时间单位，有7种取值。TimeUnit.DAYS、TimeUnit.HOURS、TimeUnit.MINUTES、TimeUnit.SECONDS、TimeUnit.MILLISECONDS、TimeUnit.MICROSECONDS、TimeUnit.NANOSECONDS
  + workQueue：一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：ArrayBlockingQueue、LinkedBlockingQueue、SynchronousQueue。 ArrayBlockingQueue和PriorityBlockingQueue使用较少，一般使用LinkedBlockingQueue和Synchronous。线程池的排队策略与BlockingQueue有关。
  + threadFactory：线程工厂，主要用来创建线程；
  + handler：表示当拒绝处理任务时的策略，有以下四种取值： 
        + ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。 
        + ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。 
        + ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程） 
        + ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务

- 几点说明
  + Executor是一个顶层接口，在它里面只声明了一个方法execute(Runnable)，返回值为void，参数为Runnable类型，从字面意思可以理解，就是用来执行传进去的任务的； 
  + ExecutorService接口继承了Executor接口，并声明了一些方法：submit、invokeAll、invokeAny以及shutDown等； 
  + 抽象类AbstractExecutorService实现了ExecutorService接口，基本实现了ExecutorService中声明的所有方法； 
  + ThreadPoolExecutor继承了类AbstractExecutorService。
 
- ThreadPoolExecutor类中有几个非常重要的方法： 
  + execute()方法实际上是Executor中声明的方法，在ThreadPoolExecutor进行了具体的实现，这个方法是ThreadPoolExecutor的核心方法，通过这个方法可以向线程池提交一个任务，交由线程池去执行。 
  + submit()方法是在ExecutorService中声明的方法，在AbstractExecutorService就已经有了具体的实现，在ThreadPoolExecutor中并没有对其进行重写，这个方法也是用来向线程池提交任务的，但是它和execute()方法不同，它能够返回任务执行的结果，去看submit()方法的实现，会发现它实际上还是调用的execute()方法，只不过它利用了Future来获取任务执行结果（Future相关内容将在下一篇讲述）。 
  + shutdown()和shutdownNow()是用来关闭线程池的。 
  + 还有一大波get的方法， 可以获取与线程池相关属性的方法。

## 线程池实现原理

- 线程池状态

```
volatile int runState;  // 前线程池的状态，它是一个volatile变量用来保证线程之间的可见性
static final int RUNNING    = 0; // 　当创建线程池后，初始时，线程池处于RUNNING状态
static final int SHUTDOWN   = 1; // 如果调用了shutdown()方法，则线程池处于SHUTDOWN状态，此时线程池不能够接受新的任务，它会等待所有任务执行完毕
static final int STOP       = 2; // 如果调用了shutdownNow()方法，则线程池处于STOP状态，此时线程池不能接受新的任务，并且会去尝试终止正在执行的任务；
static final int TERMINATED = 3; // 当线程池处于SHUTDOWN或STOP状态，并且所有工作线程已经销毁，任务缓存队列已经清空或执行结束后，线程池被设置为TERMINATED状态。
```
- 任务的执行

```
private final BlockingQueue<Runnable> workQueue;              //任务缓存队列，用来存放等待执行的任务
private final ReentrantLock mainLock = new ReentrantLock();   //线程池的主要状态锁，对线程池状态（比如线程池大小、runState等）的改变都要使用这个锁
private final HashSet<Worker> workers = new HashSet<Worker>();  //用来存放工作集
private volatile long  keepAliveTime;    //线程存货时间   
private volatile boolean allowCoreThreadTimeOut;   //是否允许为核心线程设置存活时间
private volatile int   corePoolSize;     //核心池的大小（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
private volatile int   maximumPoolSize;   //线程池最大能容忍的线程数, 当线程数大于corePoolSize时，创建新的先线程，但是创建新的线程数 + corePoolSize不能大于maximumPoolSize
private volatile int   poolSize;       //线程池中当前的线程数
private volatile RejectedExecutionHandler handler; //任务拒绝策略
private volatile ThreadFactory threadFactory;   //线程工厂，用来创建线程
private int largestPoolSize;   //用来记录线程池中曾经出现过的最大线程数
private long completedTaskCount;   //用来记录已经执行完毕的任务个数
```

- 在ThreadPoolExecutor类中，最核心的任务提交方法是execute()方法

```
public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        /*
         * Proceed in 3 steps:
         *
         * 1. If fewer than corePoolSize threads are running, try to
         * start a new thread with the given command as its first
         * task.  The call to addWorker atomically checks runState and
         * workerCount, and so prevents false alarms that would add
         * threads when it shouldn't, by returning false.
         *
         * 2. If a task can be successfully queued, then we still need
         * to double-check whether we should have added a thread
         * (because existing ones died since last checking) or that
         * the pool shut down since entry into this method. So we
         * recheck state and if necessary roll back the enqueuing if
         * stopped, or start a new thread if there are none.
         *
         * 3. If we cannot queue task, then we try to add a new
         * thread.  If it fails, we know we are shut down or saturated
         * and so reject the task.
         */
        int c = ctl.get();
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
        else if (!addWorker(command, false))
            reject(command);
    }
```

- 代码注释说明
  + 如果当前线程池中的线程数目小于corePoolSize，则每来一个任务，就会创建一个线程去执行这个任务；
  + 如果当前线程池中的线程数目>=corePoolSize，则每来一个任务，会尝试将其添加到任务缓存队列当中，若添加成功，则该任务会等待空闲线程将其取出去执行；若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程去执行这个任务；
  + 如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；
  + 如果线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。

- addWorker()添加任务，创建Worker, Worker继承AbstractQueuedSynchronizer 实现 Runnable。
```
w = new Worker(firstTask);
 final Thread t = w.thread; // 从worker取得线程
 if (workerAdded) {
    t.start(); // worker添加成功，执行任务
     workerStarted = true;
 }
```

- 线程池中的线程初始化
默认情况下，创建线程池之后，线程池中是没有线程的，需要提交任务之后才会创建线程。 在实际中如果需要线程池创建之后立即创建线程，可以通过以下两个方法办到：
```
// 初始化一个核心线程；
public boolean prestartCoreThread() {
     return workerCountOf(ctl.get()) < corePoolSize &&
            addWorker(null, true);
}

// 初始化所有核心线程
public int prestartAllCoreThreads() {
    int n = 0;
    while (addWorker(null, true))
        ++n;
   return n;
}
```

- 任务缓存队列及排队策略
workQueue的类型为BlockingQueue，通常可以取下面三种类型： 
  + ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小； 
  + LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE； 
  + synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。
  
- 任务拒绝策略 
当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略，通常有以下四种策略： 
  + ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。 
  + ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。 
  + ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程） 
  + ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
  
- 线程池的关闭
  + shutdown()：不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务 
  + shutdownNow()：立即终止线程池，并尝试打断正在执行的任务，并且清空任务缓存队列，返回尚未执行的任务
  
- 线程池容量的动态调整 
  + ThreadPoolExecutor提供了动态调整线程池容量大小的方法：setCorePoolSize()和setMaximumPoolSize()， 
  + setCorePoolSize：设置核心池大小 
  + setMaximumPoolSize：设置线程池最大能创建的线程数目大小 

    当上述参数从小变大时，ThreadPoolExecutor进行线程赋值，还可能立即创建新的线程来执行任务。

## 使用示例

- 我们可以在创建 ThreadPoolExecutor 实例时指定活动线程的数量，我们也可以限制线程池的大小并且创建我们自己的 RejectedExecutionHandler 实现来处理不能适应工作队列的工作。
```
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {  

    @Override  
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {  
        System.out.println(r.toString() + " is rejected");  
    }  

}  
```

- ThreadPoolExecutor 提供了一些方法，我们可以使用这些方法来查询 executor 的当前状态，线程池大小，活动线程数量以及任务数量。因此我是用来一个监控线程在特定的时间间隔内打印 executor 信息。
```
public class MyMonitorThread implements Runnable  
{  
    private ThreadPoolExecutor executor;  

    private int seconds;  

    private boolean run=true;  

    public MyMonitorThread(ThreadPoolExecutor executor, int delay)  
    {  
        this.executor = executor;  
        this.seconds=delay;  
    }  

    public void shutdown(){  
        this.run=false;  
    }  

    @Override  
    public void run()  
    {  
        while(run){  
                System.out.println(  
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",  
                        this.executor.getPoolSize(),  
                        this.executor.getCorePoolSize(),  
                        this.executor.getActiveCount(),  
                        this.executor.getCompletedTaskCount(),  
                        this.executor.getTaskCount(),  
                        this.executor.isShutdown(),  
                        this.executor.isTerminated()));  
                try {  
                    Thread.sleep(seconds*1000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
        }  

    }  
}  
```

- 使用 ThreadPoolExecutor 的线程池实现例子。 

```
public class WorkerPool {  

    public static void main(String args[]) throws InterruptedException{  
        //RejectedExecutionHandler implementation  
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();  
        //Get the ThreadFactory implementation to use  
        ThreadFactory threadFactory = Executors.defaultThreadFactory();  
        //creating the ThreadPoolExecutor  
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), threadFactory, rejectionHandler);  
        //start the monitoring thread  
        MyMonitorThread monitor = new MyMonitorThread(executorPool, 3);  
        Thread monitorThread = new Thread(monitor);  
        monitorThread.start();  
        //submit work to the thread pool  
        for(int i=0; i<10; i++){  
            executorPool.execute(new WorkerThread("cmd"+i));  
        }  

        Thread.sleep(30000);  
        //shut down the pool  
        executorPool.shutdown();  
        //shut down the monitor thread  
        Thread.sleep(5000);  
        monitor.shutdown();  

    }  
}  
```
注意在初始化 ThreadPoolExecutor 时，我们保持初始池大小为 2，最大池大小为 4 而工作队列大小为 2。因此如果已经有四个正在执行的任务而此时分配来更多任务的话，工作队列将仅仅保留他们(新任务)中的两个，其他的将会被 RejectedExecutionHandlerImpl 处理。

## 合理配置线程池的大小

- 遵循两原则： 
  + 1、如果是CPU密集型任务，就需要尽量压榨CPU，参考值可以设为 NCPU+1 
  + 2、如果是IO密集型任务，参考值可以设置为2*NCPU 

当然，这只是一个参考值，具体的设置还需要根据实际情况进行调整，比如可以先将线程池大小设置为参考值，再观察任务运行情况和系统负载、资源利用率来进行适当调整。


