# Getting Started

http://ifeve.com/disruptor-getting-started/

Disruptor是一个高性能的异步处理框架，或者可以认为是最快的消息框架（轻量的JMS），也可以认为是一个观察者模式的实现，或者事件监听模式的实现。 

## Hello World

* 第一：建立一个Event类
* 第二：建立一个工厂Event类，用于创建Event类实例对象
* 第三：需要有一个监听事件类，用于处理数据（Event类）
* 第四：需要进行测试代码编写。实例化Disruptor实例，配置一系列参数。然后我们对Disruptor实例绑定监听事件类，接受并处理数据。
* 第五：在Disruptor中，真正存储数据的核心叫做RingBuffer，我们通过Disruptor实例拿到它，然后把数据生产出来，把数据加入到RingBuffer的实例对象中即可。

## Disruptor术语说明

### RingBuffer: 

被看作Disruptor最主要的组件，然而从3.0开始RingBuffer仅仅负责存储和更新在Disruptor中流通的数据。对一些特殊的使用场景能够被用户(使用其他数据结构)完全替代。

### Sequence: 

Disruptor使用Sequence来表示一个特殊组件处理的序号。和Disruptor一样，每个消费者(EventProcessor)都维持着一个Sequence。大部分的并发代码依赖这些Sequence值的运转，因此Sequence支持多种当前为AtomicLong类的特性。

### Sequencer: 

这是Disruptor真正的核心。实现了这个接口的两种生产者（单生产者和多生产者）均实现了所有的并发算法，为了在生产者和消费者之间进行准确快速的数据传递。

### SequenceBarrier: 

由Sequencer生成，并且包含了已经发布的Sequence的引用，这些的Sequence源于Sequencer和一些独立的消费者的Sequence。它包含了决定是否有供消费者来消费的Event的逻辑。

### WaitStrategy：

决定一个消费者将如何等待生产者将Event置入Disruptor。

- BlockingWaitStrategy 是最代效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现。
- SleepingWaitStrategy 性能表现跟BlockingWaitStrategy差不多，对CPU的消耗也类似，但其对生产者线程的影响较小，适合用于异步日志类似。
- YieldingWaitStrategy 性能是最好的，适合于低延迟的系统。在要求极高性能且事件处理线数小于CPU逻辑核心的场景中，推荐使用此策略。

### Event：

从生产者到消费者过程中所处理的数据单元。Disruptor中没有代码表示Event，因为它完全是由用户定义的。

### EventProcessor：

主要事件循环，处理Disruptor中的Event，并且拥有消费者的Sequence。它有一个实现类是BatchEventProcessor，包含了event loop有效的实现，并且将回调到一个EventHandler接口的实现对象。

### EventHandler：

由用户实现并且代表了Disruptor中的一个消费者的接口。

### Producer：

由用户实现，它调用RingBuffer来插入事件(Event)，在Disruptor中没有相应的实现代码，由用户实现。

### WorkProcessor：

确保每个sequence只被一个processor消费，在同一个WorkPool中的处理多个WorkProcessor不会消费同样的sequence。

### WorkerPool：

一个WorkProcessor池，其中WorkProcessor将消费Sequence，所以任务可以在实现WorkHandler接口的worker吃间移交

### LifecycleAware：

当BatchEventProcessor启动和停止时，于实现这个接口用于接收通知。
