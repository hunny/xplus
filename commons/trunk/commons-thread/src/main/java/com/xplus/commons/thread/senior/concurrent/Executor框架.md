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
     * parameters.
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
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code threadFactory} or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)
                              
/**
 * An object that creates new threads on demand.  Using thread factories
 * removes hardwiring of calls to {@link Thread#Thread(Runnable) new Thread},
 * enabling applications to use special thread subclasses, priorities, etc.
 *
 * <p>
 * The simplest implementation of this interface is just:
 *  <pre> {@code
 * class SimpleThreadFactory implements ThreadFactory {
 *   public Thread newThread(Runnable r) {
 *     return new Thread(r);
 *   }
 * }}</pre>
 *
 * The {@link Executors#defaultThreadFactory} method provides a more
 * useful simple implementation, that sets the created thread context
 * to known values before returning it.
 * @since 1.5
 * @author Doug Lea
 */
public interface ThreadFactory {

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     *         create a thread is rejected
     */
    Thread newThread(Runnable r);
}

/**
 * A handler for tasks that cannot be executed by a {@link ThreadPoolExecutor}.
 *
 * @since 1.5
 * @author Doug Lea
 */
public interface RejectedExecutionHandler {

    /**
     * Method that may be invoked by a {@link ThreadPoolExecutor} when
     * {@link ThreadPoolExecutor#execute execute} cannot accept a
     * task.  This may occur when no more threads or queue slots are
     * available because their bounds would be exceeded, or upon
     * shutdown of the Executor.
     *
     * <p>In the absence of other alternatives, the method may throw
     * an unchecked {@link RejectedExecutionException}, which will be
     * propagated to the caller of {@code execute}.
     *
     * @param r the runnable task requested to be executed
     * @param executor the executor attempting to execute this task
     * @throws RejectedExecutionException if there is no remedy
     */
    void rejectedExecution(Runnable r, ThreadPoolExecutor executor);
}
```
### `ThreadPoolExecutor`注意事项

该构造方法对于队列是什么类型的比较关键：

* 使用有界队列时

若有新的任务需要执行，如果线程池实际线程数小于corePoolSize，则优先创建线程，若大于corePoolSize，则会将任务加入队列，若队列已满，则在总线程不大于maximumPoolSize的前提下，创建新的线程，若线程数大于maximumPoolSize，则执行拒绝策略。或其它自定义方式。

* 使用无界的任务队列时

`LinkedBlockingQueue`，与有界队列相比，除非系统资源耗尽，否则无界的任务队列不存在任务入队失败的情况。当有新任务到来，系统的线程数小于corePoolSize时，则新建线程执行任务。当达到corePoolSize后，就不会继续增加。若后续仍有新的任务加入，而有没有空闲的线程资源，则任务直接进入队列等待。若任务创建和处理的速度差异很大，无界队列会保持快速增长，直到耗尽系统内存。

### JDK拒绝策略

* AbortPolicy: 直接抛出异常阻止系统正常工作。
* CallerRunsPolicy: 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。
* DiscardOldestPolicy: 丢弃最老的一个请求，尝试再次提交当前任务。
* DiscardPolicy: 丢弃无法处理的任务，不给予任何处理。

如果需要自定义拒绝策略，可以实现`RejectedExecutionHandler`接口。

