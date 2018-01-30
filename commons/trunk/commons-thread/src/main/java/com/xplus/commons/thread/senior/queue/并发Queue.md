# 并发Queue

## ConcurrentLinkedQueue非阻塞高性能队列

适用于高并发场景下的队列，通过无锁的方式，实现了高并发状态下的高性能，通常ConcurrentLinkedQueue性能好于BlockingQueue。它是一个基于链接节点的无界线程安全队列。头是最先加入的，尾是最近加入的，该队列不允许null元素。

* 重要方法
add()和offer()都是加入元素的方法（在ConcurrentLinkedQueue中，这两个方法没有区别）
poll()和peek()都是取元素节点，区别在于poll()会删除元素。

## BlockingQueue阻塞队列

### ArrayBlockingQueue

基于数组的阻塞队列实现，在ArrayBlockingQueue内部，维护一个定长数组，以便缓存队列中的数据对象，其内部没实现读写分离，也就意味着生产和消费不能完全并行，长度是需要定义的，可以指定先进先出或者先进后出，也叫有界队列，在很多场合非常适合使用。

### LinkedBlockingQueue

基于链表的阻塞队列，同ArrayBlockingQueue类似，其内部也维持着一个数据缓冲队列（该队列由一个链表构成），LinkedBlockingQueue之所以能够高效的处理并发数据，是因为其内部实现采用分离锁（读写分离两个锁），从而实现生产者和消费者操作的完全并行运行，它是一个无界队列。

### PriorityBlockingQueue

基于优先级的阻塞队列（优先级的判断通过构造函数传入的Compator对象来决定，也就是说传入队列的对象必须实现Comparable接口），在实现PriorityBlockingQueue时，内部控制线程同步的锁来用的是公平锁，它也是一个无界的队列。

### DelayQueue

带有延迟时间的Queue，其中的元素只有当其指定的延迟时间到了，才能够从队列中获取到该元素。DelayQueue中的元素必须实现Delayed接口，DelayQueue是一个没有大小限制的队列，应用场景很多，比如对缓存超时的数据进行移除，任务超时处理、空闲连接的关闭等等。

### SynchronizedQueue

一种没有缓冲的队列，生产者产生的数据直接会被消费者获取并消费。



