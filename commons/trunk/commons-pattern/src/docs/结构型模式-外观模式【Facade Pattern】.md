# 结构型模式-外观模式【Facade Pattern】

## 概念

外观模式(Facade)：是为了解决类与类之间的依赖关系的，像spring一样，可以将类与类之间的关系配置到配置文件中，而外观模式就是将他们的关系放在一个Facade类中，降低了类与类之间的耦合度，该模式中没有涉及到接口。

```java
/** 
 * 示例：外观模式，也称门面模式 
 *  
 * 优点：为了解决类与类之间的依赖关系，降低了类与类之间的耦合度 
 *  
 * 该模式中没有涉及到接口 
 */  
  
class Memory {  
    public void startup() {  
        System.out.println("this is memory startup...");  
    }  
  
    public void shutdown() {  
        System.out.println("this is memory shutdown...");  
    }  
  
}  
  
class CPU {  
    public void startup() {  
        System.out.println("this is CPU startup...");  
    }  
  
    public void shutdown() {  
        System.out.println("this is CPU shutdown...");  
    }  
}  
  
/** 
 * 作为facade，持有Memory、CPU的实例 
 *  
 * 任务让Computer帮们处理，我们无需直接和Memory、CPU打交道 
 *  
 * 这里有点像去商店里买东西：们买东西只需要到商店去买，而无需去生产厂家那里买。 
 *  
 * 商店就可以称为是一个facade外观(门面)模式。--> 商品都在商店里 
 */  
class Computer {  
    private Memory memory;  
    private CPU cpu;  
  
    public Computer() {  
        memory = new Memory();  
        cpu = new CPU();  
    }  
  
    public void startup() {  
        System.out.println("begin to start the computer...");  
        memory.startup();  
        cpu.startup();  
        System.out.println("computer start finished...");  
    }  
  
    public void shutdown() {  
        System.out.println("begin to close the computer...");  
        memory.shutdown();  
        cpu.shutdown();  
        System.out.println("computer close finished...");  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Computer computer = new Computer();  
        computer.startup();  
        System.out.println("\n");  
        computer.shutdown();  
    }  
}  
```

## 总结
如果我们没有Computer类，那么，CPU、Memory他们之间将会相互持有实例，产生关系，这样会造成严重的依赖，修改一个类，可能会带来其他类的修改，这不是们想要看到的，有了Computer类，他们之间的关系被放在了Computer类里，这样就起到了解耦的作用，这就是外观Facade模式。


