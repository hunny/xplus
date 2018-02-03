# Semaphore类

```
A counting semaphore. Conceptually, a semaphore maintains a set of permits. Each acquire() blocks if necessary until a permit is available, and then takes it. Each release() adds a permit, potentially releasing a blocking acquirer. However, no actual permit objects are used; the Semaphore just keeps a count of the number available and acts accordingly.
```

从JavaDoc的释义中我们可以总结如下几点： 
1. Semaphore是一个计数信号量，允许n个任务同时访问某个资源 
2. 如果没有达到可允许的线程数量，当前使用acquire()方法获取许可，使用release()方法再访问完后释放许可 
3. Semaphore并不存在真正的允许对象(permit objects),仅仅是维护了一个允许访问的数量集

## 构造函数

```java
public Semaphore(int permits) {
    sync = new NonfairSync(permits);
}
```

permits参数表示许可数，也就是最大访问线程数。permits参数一经初始化就不能修改

```java
public Semaphore(int permits, boolean fair) {
    sync = fair ? new FairSync(permits) : new NonfairSync(permits);
}
```

permits参数上面已介绍。 
fair默认值为false.表示获取许可的顺序是无序的，也就是说新县城可能会比等待的老线程会先获得许可；当设置为true时，信号量保证它们调用的顺序级先进先出

## 常用方法

* void acquire() 从信号量获取一个许可，如果无可用许可前 将一直阻塞等待
* void acquire(int permits) 获取指定数目的许可，如果无可用许可前 也将会一直阻塞等待
* boolean tryAcquire() 从信号量尝试获取一个许可，如果无可用许可，直接返回false，不会阻塞
* boolean tryAcquire(int permits) 尝试获取指定数目的许可，如果无可用许可直接返回false，
* boolean tryAcquire(int permits, long timeout, TimeUnit unit) 在指定的时间内尝试从信号量中获取许可，如果在指定的时间内获取成功，返回true，否则返回false
* void release() 释放一个许可，别忘了在finally中使用，注意：多次调用该方法，会使信号量的许可数增加，达到动态扩展的效果，如：初始permits 为1， 调用了两次release，最大许可会改变为2
* int availablePermits() 获取当前信号量可用的许可

