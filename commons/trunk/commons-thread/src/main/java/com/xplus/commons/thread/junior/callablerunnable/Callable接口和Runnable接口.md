# Callable接口和Runnable接口

## 接口的定义

### Runnable接口的定义：

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
```
Runnable接口中仅仅定义了一个run()方法，返回值类型为void，当线程启动，任务执行完成后无法返回任何结果。

### Callable接口的定义：

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

Callable接口是一个泛型接口，定义了一个call()方法，返回类型为传递进来的泛型的具体类型。

### 比较分析： 

* Runnable接口和Callable接口都是定义任务，创建线程的实现方式
* 两者的不同之处在于Callable接口有返回结果，而Runnbale接口没有返回值

### Runnable使用

* 通过`Thread`类，来使用：

```java
new Thread(new Runnable() {
  @override
  pubic void run() {
    System.out.println("Runnable使用");
  }
}).start();
```

* 通过`ExecutorService`类来使用

```java
<T> Future<T> submit(Runnable task, T result);
Future<?> submit(Runnable task);
```

### Callable使用

一般情况下是配合ExecutorService来使用的，在`ExecutorService`接口中声明了若干个submit方法的重载版本:

```java
<T> Future<T> submit(Callable<T> task);
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException;
```
定义好的Callable任务通过ExecutorService的submit()方法来调用，返回结果封装到Future中。

## Future接口

### Future接口定义

```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
```

Future接口也是一个泛型接口，定义了一系列方法用来检测线程的状态或者获取线程的返回值

* cancel方法用来取消任务，如果取消任务成功则返回true，如果取消任务失败则返回false。参数mayInterruptIfRunning表示是否允许取消正在执行却没有执行完毕的任务，如果设置true，则表示可以取消正在执行过程中的任务。如果任务已经完成，则无论mayInterruptIfRunning为true还是false，此方法肯定返回false，即如果取消已经完成的任务会返回false；如果任务正在执行，若mayInterruptIfRunning设置为true，则返回true，若mayInterruptIfRunning设置为false，则返回false；如果任务还没有执行，则无论mayInterruptIfRunning为true还是false，肯定返回true。
* isCancelled方法表示任务是否被取消成功，如果在任务正常完成前被取消成功，则返回 true。
* isDone方法表示任务是否已经完成，若任务完成，则返回true。
* get()方法用来获取执行结果，这个方法会产生阻塞，会一直等到任务执行完毕才返回。
* get(long timeout, TimeUnit unit)用来获取执行结果，如果在指定时间内，还没获取到结果，就直接返回null。

## FutureTask类

FutureTask类是Future接口的唯一实现类。 


