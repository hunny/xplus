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

## 详情

Queue接口与List、Set同一级别，都是继承了Collection接口。LinkedList实现了Queue接 口。Queue接口窄化了对LinkedList的方法的访问权限（即在方法中的参数类型如果是Queue时，就完全只能访问Queue接口所定义的方法 了，而不能直接访问 LinkedList的非Queue的方法），以使得只有恰当的方法才可以使用。BlockingQueue 继承了Queue接口。

队列是一种数据结构．它有两个基本操作：在队列尾部加人一个元素，和从队列头部移除一个元素就是说，队列以一种先进先出的方式管理数据，如果你试图向一个 已经满了的阻塞队列中添加一个元素或者是从一个空的阻塞队列中移除一个元索，将导致线程阻塞．在多线程进行合作时，阻塞队列是很有用的工具。工作者线程可 以定期地把中间结果存到阻塞队列中而其他工作者线线程把中间结果取出并在将来修改它们。队列会自动平衡负载。如果第一个线程集运行得比第二个慢，则第二个 线程集在等待结果时就会阻塞。如果第一个线程集运行得快，那么它将等待第二个线程集赶上来。下表显示了jdk1.5中的阻塞队列的操作：

add        增加一个元索                     如果队列已满，则抛出一个IIIegaISlabEepeplian异常
remove   移除并返回队列头部的元素    如果队列为空，则抛出一个NoSuchElementException异常
element  返回队列头部的元素             如果队列为空，则抛出一个NoSuchElementException异常
offer       添加一个元素并返回true       如果队列已满，则返回false
poll         移除并返问队列头部的元素    如果队列为空，则返回null
peek       返回队列头部的元素             如果队列为空，则返回null
put         添加一个元素                      如果队列满，则阻塞
take        移除并返回队列头部的元素     如果队列为空，则阻塞

remove、element、offer 、poll、peek 其实是属于Queue接口。 

阻塞队列的操作可以根据它们的响应方式分为以下三类：aad、removee和element操作在你试图为一个已满的队列增加元素或从空队列取得元素时 抛出异常。当然，在多线程程序中，队列在任何时间都可能变成满的或空的，所以你可能想使用offer、poll、peek方法。这些方法在无法完成任务时 只是给出一个出错示而不会抛出异常。

注意：poll和peek方法出错进返回null。因此，向队列中插入null值是不合法的。

还有带超时的offer和poll方法变种，例如，下面的调用：
boolean success = q.offer(x,100,TimeUnit.MILLISECONDS);
尝试在100毫秒内向队列尾部插入一个元素。如果成功，立即返回true；否则，当到达超时进，返回false。同样地，调用：
Object head = q.poll(100, TimeUnit.MILLISECONDS);
如果在100毫秒内成功地移除了队列头元素，则立即返回头元素；否则在到达超时时，返回null。

最后，我们有阻塞操作put和take。put方法在队列满时阻塞，take方法在队列空时阻塞。

java.ulil.concurrent包提供了阻塞队列的4个变种。默认情况下，LinkedBlockingQueue的容量是没有上限的（说的不准确，在不指定时容量为Integer.MAX_VALUE，不要然的话在put时怎么会受阻呢），但是也可以选择指定其最大容量，它是基于链表的队列，此队列按 FIFO（先进先出）排序元素。

ArrayBlockingQueue在构造时需要指定容量， 并可以选择是否需要公平性，如果公平参数被设置true，等待时间最长的线程会优先得到处理（其实就是通过将ReentrantLock设置为true来 达到这种公平性的：即等待时间最长的线程会先操作）。通常，公平性会使你在性能上付出代价，只有在的确非常需要的时候再使用它。它是基于数组的阻塞循环队 列，此队列按 FIFO（先进先出）原则对元素进行排序。

PriorityBlockingQueue是一个带优先级的 队列，而不是先进先出队列。元素按优先级顺序被移除，该队列也没有上限（看了一下源码，PriorityBlockingQueue是对 PriorityQueue的再次包装，是基于堆数据结构的，而PriorityQueue是没有容量限制的，与ArrayList一样，所以在优先阻塞 队列上put时是不会受阻的。虽然此队列逻辑上是无界的，但是由于资源被耗尽，所以试图执行添加操作可能会导致 OutOfMemoryError），但是如果队列为空，那么取元素的操作take就会阻塞，所以它的检索操作take是受阻的。另外，往入该队列中的元 素要具有比较能力。

最后，DelayQueue（基于PriorityQueue来实现的）是一个存放Delayed 元素的无界阻塞队列，只有在延迟期满时才能从中提取元素。该队列的头部是延迟期满后保存时间最长的 Delayed 元素。如果延迟都还没有期满，则队列没有头部，并且poll将返回null。当一个元素的 getDelay(TimeUnit.NANOSECONDS) 方法返回一个小于或等于零的值时，则出现期满，poll就以移除这个元素了。此队列不允许使用 null 元素。 下面是延迟接口：

```java
public interface Delayed extends Comparable<Delayed> {  
     long getDelay(TimeUnit unit);  
}
```
放入DelayQueue的元素还将要实现compareTo方法，DelayQueue使用这个来为元素排序。

下面的实例展示了如何使用阻塞队列来控制线程集。程序在一个目录及它的所有子目录下搜索所有文件，打印出包含指定关键字的文件列表。从下面实例可以看出，使用阻塞队列两个显著的好处就是：多线程操作共同的队列时不需要额外的同步，另外就是队列会自动平衡负载，即那边（生产与消费两边）处理快了就会被阻塞掉，从而减少两边的处理速度差距。下面是具体实现：

```Java

public class BlockingQueueTest {  
    public static void main(String[] args) {  
        Scanner in = new Scanner(System.in);  
        System.out.print("Enter base directory (e.g. /usr/local/jdk5.0/src): ");  
        String directory = in.nextLine();  
        System.out.print("Enter keyword (e.g. volatile): ");  
        String keyword = in.nextLine();  
  
        final int FILE_QUEUE_SIZE = 10;// 阻塞队列大小  
        final int SEARCH_THREADS = 100;// 关键字搜索线程个数  
  
        // 基于ArrayBlockingQueue的阻塞队列  
        BlockingQueue<File> queue = new ArrayBlockingQueue<File>(  
                FILE_QUEUE_SIZE);  
  
        //只启动一个线程来搜索目录  
        FileEnumerationTask enumerator = new FileEnumerationTask(queue,  
                new File(directory));  
        new Thread(enumerator).start();  
          
        //启动100个线程用来在文件中搜索指定的关键字  
        for (int i = 1; i <= SEARCH_THREADS; i++)  
            new Thread(new SearchTask(queue, keyword)).start();  
    }  
}  
class FileEnumerationTask implements Runnable {  
    //哑元文件对象，放在阻塞队列最后，用来标示文件已被遍历完  
    public static File DUMMY = new File("");  
  
    private BlockingQueue<File> queue;  
    private File startingDirectory;  
  
    public FileEnumerationTask(BlockingQueue<File> queue, File startingDirectory) {  
        this.queue = queue;  
        this.startingDirectory = startingDirectory;  
    }  
  
    public void run() {  
        try {  
            enumerate(startingDirectory);  
            queue.put(DUMMY);//执行到这里说明指定的目录下文件已被遍历完  
        } catch (InterruptedException e) {  
        }  
    }  
  
    // 将指定目录下的所有文件以File对象的形式放入阻塞队列中  
    public void enumerate(File directory) throws InterruptedException {  
        File[] files = directory.listFiles();  
        for (File file : files) {  
            if (file.isDirectory())  
                enumerate(file);  
            else  
                //将元素放入队尾，如果队列满，则阻塞  
                queue.put(file);  
        }  
    }  
}  
class SearchTask implements Runnable {  
    private BlockingQueue<File> queue;  
    private String keyword;  
  
    public SearchTask(BlockingQueue<File> queue, String keyword) {  
        this.queue = queue;  
        this.keyword = keyword;  
    }  
  
    public void run() {  
        try {  
            boolean done = false;  
            while (!done) {  
                //取出队首元素，如果队列为空，则阻塞  
                File file = queue.take();  
                if (file == FileEnumerationTask.DUMMY) {  
                    //取出来后重新放入，好让其他线程读到它时也很快的结束  
                    queue.put(file);  
                    done = true;  
                } else  
                    search(file);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
        }  
    }  
    public void search(File file) throws IOException {  
        Scanner in = new Scanner(new FileInputStream(file));  
        int lineNumber = 0;  
        while (in.hasNextLine()) {  
            lineNumber++;  
            String line = in.nextLine();  
            if (line.contains(keyword))  
                System.out.printf("%s:%d:%s%n", file.getPath(), lineNumber,  
                        line);  
        }  
        in.close();  
    }  
}
```

