# 锁

## 锁介绍

在Java多线程中，可以使用`synchronized`关键字可以实现线程间的同步互斥操作，还有一个更加优秀的机制去完成这个同步互斥操作，就是使用`Lock`对象，主要有两种锁：重入锁和读写锁，它们具有比`synchronized`更为强大的功能，并且有嗅探锁定、多路分支等功能。

在java中可以使用 synchronized 来实现多线程下对象的同步访问，为了获得更加灵活使用场景、高效的性能，java还提供了Lock接口及其实现类ReentrantLock和读写锁 ReentrantReadWriteLock。

```java
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
```

### Lock使用说明

在Lock中声明了四个方法来获取锁

#### `lock()`

首先lock()方法是平常使用得最多的一个方法，就是用来获取锁。如果锁已被其他线程获取，则进行等待。
如果采用Lock，必须主动去释放锁，并且在发生异常时，不会自动释放锁。因此一般来说，使用Lock必须在try{}catch{}块中进行，并且将释放锁的操作放在finally块中进行，以保证锁一定被被释放，防止死锁的发生。通常使用Lock来进行同步的话，是以下面这种形式去使用的：

```java
Lock lock = ...;
lock.lock();
try{
    //处理任务
} catch(Exception ex) {
     
} finally {
    lock.unlock();   //释放锁
}
```

#### `tryLock()`

tryLock()方法是有返回值的，它表示用来尝试获取锁，如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false，也就说这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待。
tryLock(long time, TimeUnit unit)方法和tryLock()方法是类似的，只不过区别在于这个方法在拿不到锁时会等待一定的时间，在时间期限之内如果还拿不到锁，就返回false。如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
所以，一般情况下通过tryLock来获取锁时是这样使用的：

```java
Lock lock = ...;
if (lock.tryLock()) {
     try{
         //处理任务
     } catch(Exception ex) {
         
     } finally {
         lock.unlock();   //释放锁
     } 
} else {
    //如果不能获取锁，则直接做其他事情
}
```

#### `lockInterruptibly()`

lockInterruptibly()方法比较特殊，当通过这个方法去获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。也就使说，当两个线程同时通过lock.lockInterruptibly()想获取某个锁时，假若此时线程A获取到了锁，而线程B只有在等待，那么对线程B调用threadB.interrupt()方法能够中断线程B的等待过程。
由于lockInterruptibly()的声明中抛出了异常，所以lock.lockInterruptibly()必须放在try块中或者在调用lockInterruptibly()的方法外声明抛出InterruptedException。
因此lockInterruptibly()一般的使用形式如下：

```java
public void method() throws InterruptedException {
    lock.lockInterruptibly();
    try {  
     //.....
    }
    finally {
        lock.unlock();
    }  
}
```

注意，当一个线程获取了锁之后，是不会被interrupt()方法中断的。单独调用interrupt()方法不能中断正在运行过程中的线程，只能中断阻塞过程中的线程。
因此当通过lockInterruptibly()方法获取某个锁时，如果不能获取到，只有进行等待的情况下，是可以响应中断的。
而用synchronized修饰的话，当一个线程处于等待某个锁的状态，是无法被中断的，只有一直等待下去。

### ReadWriteLock

ReadWriteLock也是一个接口，在它里面只定义了两个方法：

```java
public interface ReadWriteLock {
    /**
     * Returns the lock used for reading.
     *
     * @return the lock used for reading.
     */
    Lock readLock();
    /**
     * Returns the lock used for writing.
     *
     * @return the lock used for writing.
     */
    Lock writeLock();
}
```

一个用来获取读锁，一个用来获取写锁。也就是说将文件的读写操作分开，分成2个锁来分配给线程，从而使得多个线程可以同时进行读操作。下面的ReentrantReadWriteLock实现了ReadWriteLock接口。

#### ReentrantReadWriteLock

ReentrantReadWriteLock里面提供了很多丰富的方法，不过最主要的有两个方法：readLock()和writeLock()用来获取读锁和写锁。

### 读写锁的锁降级

锁降级是指写锁降级成为读锁。如果当前线程持有写锁，然后将其释放再获取读锁的过程不能称为锁降级。锁降级指的在持有写锁的时候再获取读锁,获取到读锁后释放之前写锁的过程称为锁释放。

锁降级在某些情况下是非常必要的，主要是为了保证数据的可见性。如果当前线程不获取读锁而直接释放写锁，假设此时另外一个线程获取了写锁并修改了数据。那么当前线程无法感知该线程的数据更新。

## `Lock`与`synchronized`比较

1、使用synchronized关键字时，锁的控制和释放是在synchronized同步代码块的开始和结束位置。而在使用Lock实现同步时，锁的获取和释放可以在不同的代码块、不同的方法中。这一点是基于使用者手动获取和释放锁的特性。
2、Lock接口提供了试图获取锁的tryLock()方法，在调用tryLock()获取锁失败时返回false，这样线程可以执行其它的操作 而不至于使线程进入休眠。tryLock()方法可传入一个long型的时间参数，允许在一定的时间内来获取锁。
3、Lock接口的实现类ReentrantReadWriteLock提供了读锁和写锁，允许多个线程获得读锁、而只能有一个线程获得写锁。读锁和写锁不能同时获得。实现了读和写的分离，这一点在需要并发读的应用中非常重要，如lucene允许多个线程读取索引数据进行查询但只能有一个线程负责索引数据的构建。
4、基于以上3点，lock来实现同步具备更好的性能。

## Lock和synchronized几点不同：

1、Lock是一个接口，而synchronized是Java中的关键字，synchronized是内置的语言实现，synchronized是在JVM层面上实现的，不但可以通过一些监控工具监控synchronized的锁定，而且在代码执行时出现异常，JVM会自动释放锁定，但是使用Lock则不行，lock是通过代码实现的，要保证锁定一定会被释放，就必须将 unLock()放到finally{} 中；
2、synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而Lock在发生异常时，如果没有主动通过unLock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁；
3、Lock可以让等待锁的线程响应中断，线程可以中断去干别的事务，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断；
4、通过Lock可以知道有没有成功获取锁，而synchronized却无法办到。
5、Lock可以提高多个线程进行读操作的效率。

在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized。所以说，在具体使用时要根据适当情况选择。

## Lock可实现读写分离

当有多个线程读写文件时，读操作和写操作会发生冲突现象，写操作和写操作会发生冲突现象，但是读操作和读操作不会发生冲突现象。
但是采用synchronized关键字来实现同步的话，就会导致一个问题：
* 如果多个线程都只是进行读操作，所以当一个线程在进行读操作时，其他线程只能等待无法进行读操作。
* 因此就需要一种机制来使得多个线程都只是进行读操作时，线程之间不会发生冲突，通过Lock就可以办到。
* 另外，通过Lock可以知道线程有没有成功获取到锁。这个是synchronized无法办到的

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

## ReentrantLock获取锁定与三种方式：
1. `lock()`: 如果获取了锁立即返回，如果别的线程持有锁，当前线程则一直处于休眠状态，直到获取锁
2. `tryLock()`: 如果获取了锁立即返回true，如果别的线程正持有锁，立即返回false；
3. `tryLock(long timeout,TimeUnit unit)`: 如果获取了锁定立即返回true，如果别的线程正持有锁，会等待参数给定的时间，在等待的过程中，如果获取了锁定，就返回true，如果等待超时，返回false；
4. `lockInterruptibly`: 如果获取了锁定立即返回，如果没有获取锁定，当前线程处于休眠状态，直到或者锁定，或者当前线程被别的线程中断

## ReentrantLock是可重入锁

ReentrantLock持有一个所计数器，当已持有所的线程再次获得该锁时计数器值加1，每调用一次lock.unlock()时所计数器值减一，直到所计数器值为0，此时线程释放锁。

## 总结

不管是synchronized关键字还是Lock锁，都是用来在多线程的环境下对资源的同步访问进行控制，用以避免因多个线程对数据的并发读写造成的数据混乱问题。与synchronized不同的是，Lock锁实现同步时需要使用者手动控制锁的获取和释放，其灵活性使得可以实现更复杂的多线程同步和更高的性能，但同时，使用者一定要在获取锁后及时捕获代码运行过程中的异常并在finally代码块中释放锁。