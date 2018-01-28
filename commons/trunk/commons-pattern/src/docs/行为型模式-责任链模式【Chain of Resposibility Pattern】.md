# 行为型模式-责任链模式【Chain of Resposibility Pattern】

## 一、概述

避免请求发送者与接收者耦合在一起，让多个对象都有可能接收请求，将这些对象连接成一条链，并且沿着这条链传递请求，直到有对象处理它为止。职责链模式是一种对象行为型模式。

核心在于引入一个抽象处理者类

## 二、适用场景

请求的链式处理，多个对象可以处理同一请求、但是具体由哪个对象来处理由运行时系统根据条件判断确定。

## 四、参与者

1、Handler（抽象处理者）：它定义了一个处理请求的接口，一般设计为抽象类，由于不同的具体处理者处理请求的方式不同，因此在其中定义了抽象请求处理方法。因为每一个处理者的下家还是一个处理者，因此在抽象处理者中定义了一个抽象处理者类型的对象，作为其对下家的引用。通过该引用，处理者可以连成一条链。

2、ConcreteHandler（具体处理者）：它是抽象处理者的子类，可以处理用户请求，在具体处理者类中实现了抽象处理者中定义的抽象请求处理方法，在处理请求之前需要进行判断，看是否有相应的处理权限，如果可以处理请求就处理它，否则将请求转发给后继者；在具体处理者中可以访问链中下一个对象，以便请求的转发。

注意：职责链模式并不创建职责链，职责链的创建工作必须由系统的其他部分来完成，一般是在使用该职责链的客户端中创建职责链。

## 五、用例学习

1、抽象处理者类：Handler.java

```java
/** 
 * 抽象处理者类 
 * @author   
 * 
 */  
public abstract class Handler {  
      
    protected Handler nextHandler;  
      
    public void setNextHandler(Handler nextHandler){  
        this.nextHandler = nextHandler;  
    }  
      
    public abstract void handleRequest(int request);  
  
} 
```

2、具体处理者类A：部门组长 ConcreteHandlerA.java

```java
/** 
 * 具体职责处理者A：案例中的 部门组长角色 
 * @author   
 * 
 */  
public class ConcreteHandlerA extends Handler {  
  
    @Override  
    public void handleRequest(int leaveDay) {  
        if(leaveDay <= 1){  
            // 满足处理条件  处理请求  
            System.out.println("请假天数小于2天  由部门组长审批");  
        } else {  
            nextHandler.handleRequest(leaveDay);  
        }  
  
    }  
  
} 
```

3、具体处理者类B：部门经理 ConcreteHandlerB.java

```java
/** 
 * 具体职责处理者B：案例中的 部门经理角色 
 * @author   
 * 
 */  
public class ConcreteHandlerB extends Handler {  
  
    @Override  
    public void handleRequest(int leaveDay) {  
        if(1 < leaveDay && leaveDay <= 5){  
            // 满足处理条件  处理请求  
            System.out.println("请假天数大于1天且小于等于5天  由部门经理审批");  
        } else {  
            nextHandler.handleRequest(leaveDay);  
        }  
  
    }  
  
}  
```

4、具体处理者类C：总经理 ConcreteHandlerC.java

```java
/** 
 * 具体职责处理者C：案例中的 总经理角色 
 * @author   
 * 
 */  
public class ConcreteHandlerC extends Handler {  
  
    @Override  
    public void handleRequest(int leaveDay) {  
        if(leaveDay > 5){  
            System.out.println("当请假天数大于5天的情况下 由总经理亲自操刀审批。总经理的职责已经是最大的啦，还有他没有权限处理的事吗？");  
        }  
    }  
  
}  
```

5、客户端类：Client.java

```java
/** 
 * 客户端类<br/> 
 * 负责创建职责链和发送请求<br/> 
 * 当职责链（职责的传递顺序/请求的处理顺序）建好之后，客户端只负责将请求发送出去， 
 * 而具体请求在职责链上的传递和最终由链上的哪个对象进行处理不由客户端关心 
 * @author   
 * 
 */  
public class Client {  
  
    public static void main(String[] args) {  
        Handler handlerA,handlerB,handlerC;  
        handlerA = new ConcreteHandlerA();  
        handlerB = new ConcreteHandlerB();  
        handlerC = new ConcreteHandlerC();  
          
        // 创建职责链  handlerA ——> handlerB ——> handlerC  
        handlerA.setNextHandler(handlerB);  
        handlerB.setNextHandler(handlerC);  
          
        // 发送请假请求一  
        handlerA.handleRequest(1);  
          
        // 发送请假请求二  
        handlerA.handleRequest(7);  
          
        // 发送请假请求二  
        handlerA.handleRequest(3);   
    }  
  
}  
```

## 概念

责任链模式(ChainOfResponsibility)： 有多个对象，每个对象持有下一个对象的引用，形成一条链，请求在这条链上传递，直到某一对象决定处理该请求，但是发出者并不清楚最终哪个对象会处理该请求。

```java
/** 
 * 责任链模式：有多个对象，每个对象持有下一个对象的引用，形成一条链， 
 *  
 * 请求在这条链上传递，直到某一对象决定处理该请求， 
 *  
 * 但是发出者并不清楚最终哪个对象会处理该请求。 
 */  
interface Handler {  
    public void operator();  
}  
  
/** 
 * 这里单独对Handler进行封装，方便修改引用对象 
 */  
abstract class AbstractHandler implements Handler {  
    private Handler handler;  
  
    public Handler getHandler() {  
        return handler;  
    }  
  
    public void setHandler(Handler handler) {  
        this.handler = handler;  
    }  
}  
  
class MyHandler extends AbstractHandler implements Handler {  
    private String name;  
  
    public MyHandler(String name) {  
        this.name = name;  
    }  
  
    @Override  
    public void operator() {  
        if (getHandler() != null) {  
            System.out.print(name + "，将BUG交给——>");  
            /** 
             * 这里是关键。【注意1】这里不是递归哦~ 
             *  
             * 递归：A(operator)——>A(operator)——>A(operator) 
             *  
             * 责任链：A(operator)——>B(operator)——>C(operator) 
             */  
            getHandler().operator();  
        } else {  
            System.out.println(name + "处理BUG...\n");  
        }  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        MyHandler handler1 = new MyHandler("技术总监");  
        MyHandler handler2 = new MyHandler("项目经理");  
        MyHandler handler3 = new MyHandler("程序员");  
        /** 
         * 如果没有下家，将会自行处理 
         *  
         * 打印结果：技术总监处理BUG... 
         */  
        handler1.operator();  
        /** 
         * 只要有下家，就传给下家处理 
         *  
         * 下面的打印结果：技术总监，将BUG交给——>项目经理，将BUG交给——>程序员处理BUG... 
         *  
         * 就这样，原本是技术总监自行处理的BUG，现在一层一层的把责任推给了程序员去处理 
         */  
        handler1.setHandler(handler2);  
        handler2.setHandler(handler3);  
        /** 
         * 透过打印结果可以知道：MyHandler实例化后将生成一系列相互持有的对象(handler)，构成一条链。 
         */  
        handler1.operator();  
        /** 
         * 【注意2】责任链不是链表：链表有个头结点，每次必须通过头结点才能访问后面的节点 
         *  
         * 而责任链它可以从头访问，也可以从中间开始访问，如：handler2.operator(); 
         */  
  
    }  
}  
```

6、运行结果：

```
请假天数小于2天  由部门组长审批  
当请假天数大于5天的情况下 由总经理亲自操刀审批。总经理的职责已经是最大的啦，还有他没有权限处理的事吗？  
请假天数大于1天且小于等于5天  由部门经理审批  
```

## 总结
1、责任链模式可以实现，在隐瞒客户端(不知道具体处理的人是谁)的情况下，对系统进行动态的调整。
2、链接上的请求可以是一条链，可以是一个树，还可以是一个环，模式本身不约束这个，需要自己去实现，同时，在一个时刻，命令只允许由一个对象传给另一个对象，而不允许传给多个对象。