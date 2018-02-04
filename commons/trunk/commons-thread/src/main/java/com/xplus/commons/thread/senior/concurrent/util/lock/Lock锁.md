# 锁

## 锁介绍

在Java多线程中，可以使用`synchronized`关键字可以实现线程间的同步互斥操作，还有一个更加优秀的机制去完成这个同步互斥操作，就是使用`Lock`对象，主要有两种锁：重入锁和读写锁，它们具有比`synchronized`更为强大的功能，并且有嗅探锁定、多路分支等功能。

在java中可以使用 synchronized 来实现多线程下对象的同步访问，为了获得更加灵活使用场景、高效的性能，java还提供了Lock接口及其实现类ReentrantLock和读写锁 ReentrantReadWriteLock。

## `Lock`与`synchronized`比较

1、使用synchronized关键字时，锁的控制和释放是在synchronized同步代码块的开始和结束位置。而在使用Lock实现同步时，锁的获取和释放可以在不同的代码块、不同的方法中。这一点是基于使用者手动获取和释放锁的特性。

2、Lock接口提供了试图获取锁的tryLock()方法，在调用tryLock()获取锁失败时返回false，这样线程可以执行其它的操作 而不至于使线程进入休眠。tryLock()方法可传入一个long型的时间参数，允许在一定的时间内来获取锁。

3、Lock接口的实现类ReentrantReadWriteLock提供了读锁和写锁，允许多个线程获得读锁、而只能有一个线程获得写锁。读锁和写锁不能同时获得。实现了读和写的分离，这一点在需要并发读的应用中非常重要，如lucene允许多个线程读取索引数据进行查询但只能有一个线程负责索引数据的构建。

4、基于以上3点，lock来实现同步具备更好的性能。

## Lock锁与条件同步：

与synchronized类似，Lock锁也可以实现条件同步。在java的concurrent包中提供了 Condition 接口及其实现类ConditionObject。

当满足一定条件时，调用Condition的await()方法使当前线程进入休眠状态进行等待。调用Condition的signalAll()方法唤醒因await()进入休眠的线程。

### ReentrantLock锁

ReentrantLock 是java.unti.concurrent包下的一个类，它的一般使用结构如下

```java
public void lockMethod() {  
    ReentrantLock myLock = new ReentrantLock();  
    myLock.lock();  
    try{  
        // 受保护的代码段  
        //critical section  
    } finally {  
        // 可以保证发生异常 锁可以得到释放 避免死锁的发生  
        myLock.unlock();  
    }  
}  
```
把解锁操作括在finally字句之内是至关重要的，如果受保护的代码抛出异常，锁可以得到释放，这样可以避免死锁的发生。

## ReentrantLock是可重入锁

ReentrantLock持有一个所计数器，当已持有所的线程再次获得该锁时计数器值加1，每调用一次lock.unlock()时所计数器值减一，直到所计数器值为0，此时线程释放锁。

## 总结

不管是synchronized关键字还是Lock锁，都是用来在多线程的环境下对资源的同步访问进行控制，用以避免因多个线程对数据的并发读写造成的数据混乱问题。与synchronized不同的是，Lock锁实现同步时需要使用者手动控制锁的获取和释放，其灵活性使得可以实现更复杂的多线程同步和更高的性能，但同时，使用者一定要在获取锁后及时捕获代码运行过程中的异常并在finally代码块中释放锁。