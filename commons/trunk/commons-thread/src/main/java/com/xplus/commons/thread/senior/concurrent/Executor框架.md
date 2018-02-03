# Executor框架

Executors是线程工厂，通过Executors可以创建特定功能的线程池。

## Executors创建线程池的方法：

### newFixedThreadPool()

该方法返回一个固定数量的线程池，该方法的线程数始终不变，当有一个任务提交时，若线程池中空闲，则立即执行，若没有，则会被暂缓在一个任务队列中等待有空闲的线程去执行。

### newSigleThreadExecutor()

创建一个线程的线程池，若空闲则执行，若没有空闲线程则暂缓在任务队列中。

### newCachedThreadPool()

返回一个可根据实际情况调整线程个数的线程池，不限制最大线程数量，若有空闲的线程则执行任务，若无任务，则不创建线程，并且每一个空闲线程会在60秒后自动回收。

### newScheduleThreadPool()

返回一个ScheduleExecutorService对象，但该线程池可以指定线程的数量。

## 自定义线程池

若Executors工厂类无法满足要求，可以去创建一个自定义的线程池，其实Executors工厂类里面的创建线程方法其内部实现均是用了ThreadPoolExecutor这个类，可以自定义线程：

```java

    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters and default thread factory and rejected execution handler.
     * It may be more convenient to use one of the {@link Executors} factory
     * methods instead of this general purpose constructor.
     *
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *        pool
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     * @param unit the time unit for the {@code keepAliveTime} argument
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), defaultHandler);
    }

```
