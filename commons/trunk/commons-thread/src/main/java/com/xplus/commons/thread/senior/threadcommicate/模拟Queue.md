# 模拟Queue

## BlockQueue: 它是一个队列，并且支持阻塞的机制，阻塞的放入和得到数据。要实现LinkedBlockingQueue，两个简单的方法put和take。

* put(anObject)：把anObject加到BlockingQueue里，如果BlockQueue没有空间，则调用此方法的线程被阻断，直到BlockingQueue里面有空间再继续。
* take：取走BlockingQueue里排在首位的对象，若BlockingQueue为空，阻断进入等待状态直到BlockingQueue有新的数据被加入。
