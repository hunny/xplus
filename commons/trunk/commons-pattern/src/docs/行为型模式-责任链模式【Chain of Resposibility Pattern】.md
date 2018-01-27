# 行为型模式-责任链模式【Chain of Resposibility Pattern】

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
         * 【注意2】责任链不是链表：链表有个头结点，咱每次必须通过头结点才能访问后面的节点 
         *  
         * 而责任链它可以从头访问，也可以从中间开始访问，如：handler2.operator(); 
         */  
  
    }  
}  
```

## 总结
1、责任链模式可以实现，在隐瞒客户端(不知道具体处理的人是谁)的情况下，对系统进行动态的调整。
2、链接上的请求可以是一条链，可以是一个树，还可以是一个环，模式本身不约束这个，需要自己去实现，同时，在一个时刻，命令只允许由一个对象传给另一个对象，而不允许传给多个对象。