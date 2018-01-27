# 行为模式-命令模式【Command Pattern】

## 概念

命令模式(Command)：将“请求”(命令/口令)封装成一个对象，以便使用不同的请求、队列或者日志来参数化其对象。命令模式也支持撤销操作。命令模式的目的就是达到命令的发出者和执行者之间解耦，实现请求和执行分开。

## 代码实现

```java
/** 
 * 示例：以咱去餐馆吃饭为例，分为3步 
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
