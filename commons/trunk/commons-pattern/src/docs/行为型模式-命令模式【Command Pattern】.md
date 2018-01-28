# 行为模式-命令模式【Command Pattern】

## 一、概述

命令模式可以将请求发送者和接收者完全解耦，发送者与接收者之间没有直接引用关系，发送请求的对象只需要知道如何发送请求，而不必知道如何完成请求。核心在于引入了命令类，通过命令类来降低发送者和接收者的耦合度，请求发送者只需指定一个命令对象，再通过命令对象来调用请求接收者的处理方法，命令模式是一种对象行为型模式。

## 二、使用场景

1>、系统要求请求发送者和接收者的解耦，使得调用者和接收者不直接交互；
2>、类似消息 请求/命令 队列处理；[命令装载在集合或队列中,通过遍历集合,达到命令的批量处理]
3>、当执行一个命令而触发多个具体命令的执行，从而实行对命令的批量处理，这种命令被称为组合命令或宏命令；

## 四、参与者

1>、Command（抽象命令类）：抽象命令类一般是一个抽象类或接口，在其中声明了用于执行请求的execute()等方法，通过这些方法可以调用请求接收者的相关操作。
2>、ConcreteCommand（具体命令类）：具体命令类是抽象命令类的子类，实现了在抽象命令类中声明的方法，它对应具体的命令接收者对象，将接收者对象的动作绑定其中。在实现execute()方法时，将调用接收者对象的相关操作(operation)。
3>、Invoker（命令发起者/消息请求者）：与抽象命令类存在关联关系，在命令发起时 将一个具体命令对象注入其中 再调用具体命令对象的执行方法(execute) 从而间接实现调用请求接收者的相关操作。
4>、Receiver（命令/消息 接收者）：接收者执行与请求相关的操作，它具体实现对请求的业务处理（operation方法执行）。

## 五、用例学习

1、命令接收者A：ReceiverA.java

```java
/** 
 * 命令接收者A 
 * @author   
 * 
 */  
public class ReceiverA {  
  
    public void operation(){  
        System.out.println("我是命令接收者A，我已接收到命令，正在执行相应的业务操作方法");  
    }  
}  
```

2、命令接收者B：ReceiverB.java

```java
/** 
 * 命令接收者B 
 * @author   
 * 
 */  
public class ReceiverB {  
  
    public void operation(){  
        System.out.println("我是命令接收者B，我已接收到命令，正在执行相应的业务操作方法");  
    }  
}  
```

3、抽象命令类：Command.java

```java
/** 
 * 命令抽象类 
 * @author   
 * 
 */  
public abstract class Command {  
  
    public abstract void execute();  
} 
```

4、具体命令类A：ConcreteCommandA.java

```java
/** 
 * 具体命令类A<br/> 
 * 与命令接收者ReceiverA 关联 
 * @author   
 * 
 */  
public class ConcreteCommandA extends Command {  
  
    private ReceiverA receiver;  
      
    public ConcreteCommandA(){  
        receiver = new ReceiverA();  
    }  
      
    @Override  
    public void execute() {  
        // 调用具体命令接收者的执行方法  
        receiver.operation();  
    }  
  
} 
```

5、具体命令类B：ConcreteCommandB.java

```java
/** 
 * 具体命令类B<br/> 
 * 与命令接收者ReceiverB 关联 
 * @author   
 * 
 */  
public class ConcreteCommandB extends Command {  
  
    private ReceiverB receiver;  
      
    public ConcreteCommandB(){  
        receiver = new ReceiverB();  
    }  
      
    @Override  
    public void execute() {  
        // 调用具体命令接收者的执行方法  
        receiver.operation();  
    }  
  
} 
```

6、命令发送者/消息请求者：Invoker.java

```java
/** 
 * 命令/消息发送者 
 * @author   
 * 
 */  
public class Invoker {  
    // 维护一个抽象命令类的引用  
    private Command command;  
      
    /** 
     * 对具体命令对象的引用 
     * @param command 
     */  
    public void setCommand(Command command){  
        this.command = command;  
    }  
      
    /** 
     * 发送命令<br/> 
     * 调用具体命令执行类,间接的将消息/命令传递给了命令接收者执行 
     */  
    public void runCommand(){  
        command.execute();  
    }  
  
}
```

7、客户端测试类：Client.java

```java
public class Client {  
  
    public static void main(String[] args) {  
        Command command = null;  
        /* 
         * 如果要使命令发送给 命令接收者B（ReceiverB）处理 
         * 则只要实例化与ReceiverB 相关的命令类ConcreteCommandB即可 
         * 代码修改如下： 
         * command = new ConcreteCommandB(); 
         *  
         * 以下是实例化命令类ConcreteCommandA 及将命令消息发送给与之关联的ReceiverA处理 
         */  
        command = new ConcreteCommandA();  
        Invoker invoker = new Invoker();  
        invoker.setCommand(command);  
        invoker.runCommand();  
    }  
  
}
```

## 概念

命令模式(Command)：将“请求”(命令/口令)封装成一个对象，以便使用不同的请求、队列或者日志来参数化其对象。命令模式也支持撤销操作。命令模式的目的就是达到命令的发出者和执行者之间解耦，实现请求和执行分开。

## 代码实现

```java
/** 
 * 示例：以去餐馆吃饭为例，分为3步 
 *  
 * 1、和小二说，来个宫保鸡丁 --> 顾客发出口令 
 *  
 * 2、小二来了一句：宫保鸡丁一份。 这时命令被传递到了厨师。--> 口令传递到了厨师 
 *  
 * 3、然后厨师就开始做宫保鸡丁去了。 --> 厨师根据口令去执行 
 *  
 * 从这3步可以看到，宫保鸡丁并不是我想吃就我来做，而是传达给别人去做。 
 *  
 * 我要的是一个结果——宫保鸡丁这道菜做好了，而我无需去关系这道菜是怎么去做的。 
 */  
interface Command {  
    /** 
     * 口令执行 
     */  
    public void execute();  
  
    /** 
     * 口令撤销 
     */  
    public void undo();  
}  
  
/** 
 * 口令 -- 经小二传递 
 */  
class OrderCommand implements Command {  
    private CookReceiver cook;  
  
    public OrderCommand(CookReceiver cook) {  
        this.cook = cook;  
    }  
  
    @Override  
    public void execute() {  
        cook.cooking();  
    }  
  
    @Override  
    public void undo() {  
        cook.unCooking();  
    }  
}  
  
/** 
 * 厨师--真正的口令执行者 
 */  
class CookReceiver {  
    public void cooking() {  
        System.out.println("开始炒宫保鸡丁了...");  
    }  
  
    public void unCooking() {  
        System.out.println("不要炒宫保鸡丁了...");  
    }  
}  
  
/** 
 * 顾客--真正的口令发出者 
 */  
class Customer {  
    private Command command;  
  
    public Customer(Command command) {  
        this.command = command;  
    }  
  
    /** 
     * 将命令的发出与执行分开 
     */  
    public void order() {  
        command.execute();  
    }  
  
    public void unOrder() {  
        command.undo();  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        /** 
         * 等待口令的执行者 --炒菜总得有个厨师吧. 
         */  
        CookReceiver receiver = new CookReceiver();  
        /** 
         * 等待将口令传达给厨师 --因为顾客要什么菜还不知道，但口令始终要传达到厨师耳朵里这是肯定的。 
         */  
        Command cmd = new OrderCommand(receiver);  
        Customer customer = new Customer(cmd);  
        /** 
         * 执行口令 
         */  
        customer.order();  
        /** 
         * 撤销口令 
         */  
        customer.unOrder();  
    }  
}  
```
## 应用场景
菜馆点餐、遥控器、队列请求、日志请求。
