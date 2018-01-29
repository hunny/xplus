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

12
基于链表的阻塞队列，同ArrayBlockingQueue类似，其内部也维持着一个数据缓冲队列（该队列由一个链表构成），LinkedBlockingQueue之所以能够高效的处理并发数据，是因为其内部实现采用分离锁（读写分离两个锁），从而实现生产者和消费者操作的完全并行运行，它是一个无界队列。

### PriorityBlockingQueue

### DelayQueue

### SynchronizedQueue



