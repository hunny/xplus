# 行为型模式-备忘录模式【Memento Pattern】

## 一、概述

在不破坏封装的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态，这样就可以在以后将对象恢复至原先保存的状态。它是一种对象行为型模式。

## 二、适用场景

1、类似于撤销功能的实现，保存一个对象在某一时间的部分状态或全部状态，当以后需要它时就可以恢复至先前的状态。

2、对对象历史状态的封装、避免将对象的历史状态的实现细节暴露给外界。

## 四、参与者

1、Originator（原发器）：它是一个普通类，可以创建一个备忘录，并存储它的当前内部状态，也可以使用备忘录来恢复其内部状态，一般将需要保存内部状态的类设计为原发器。

2、Memento（备忘录)：存储原发器的内部状态，根据原发器来决定保存哪些内部状态。备忘录的设计一般可以参考原发器的设计，根据实际需要确定备忘录类中的属性。需要注意的是，除了原发器本身与负责人类之外，备忘录对象不能直接供其他类使用。

3、Caretaker（备忘录管理者或负责人）：负责保存备忘录，但是不能对备忘录的内容进行操作或检查。在该类中可以存储一个或多个备忘录对象，它只负责存储对象，而不能修改对象，也无须知道对象的实现细节。

五、用例学习

1、备忘录模式角色之  原发器类：Originator.java

```java
/** 
 * 备忘录模式角色之  原发器<br/> 
 * 含有内部需要保存的状态属性 
 * @author  
 * 
 */  
public class Originator {  
      
    private String state;  
  
    /** 
     * 创建一个备忘录对象  
     * @return 
     */  
    public Memento createMemento(){  
        return new Memento(this);  
    }  
      
    /** 
     * 根据备忘录对象恢复原发器先前状态   
     * @param o 
     */  
    public void restoreMemento(Memento o){  
        this.state = o.getState();  
    }  
      
    public String getState() {  
        return state;  
    }  
  
    public void setState(String state) {  
        this.state = state;  
    }  
      
}  
```

2、备忘录模式角色之  备忘录类：Memento.java

```java
/** 
 * 备忘录模式角色之  备忘录<br/> 
 * 存储原发器的内部状态 
 * @author  
 * 
 */  
public class Memento {  
  
    private String state;  
  
    public Memento(Originator originator) {  
        this.state = originator.getState();  
    }  
  
    public String getState() {  
        return state;  
    }  
  
    public void setState(String state) {  
        this.state = state;  
    }  
      
}  
```

3、备忘录模式角色之 备忘录管理者或负责人类：Caretaker.java

```java
/** 
 * 备忘录模式角色之 备忘录管理者或负责人<br/> 
 * 只负责存储备忘录对象，而不能修改备忘录对象，也无须知道备忘录对象的实现细节<br/> 
 * <b>扩展：<br/> 
 * 如果要实现可以多步撤销的备忘录模式 则只需要在此类中使用一个数组集合来装载每一步状态的备忘录对象 如：<br/> 
 *  // 定义一个数组集合来存储多个备忘录对象  <br/> 
    List<Memento> mementos = new ArrayList<Memento>(); 
 * </b> 
 * @author  
 * 
 */  
public class Caretaker {  
  
    private Memento memento;  
  
    public Memento getMemento() {  
        return memento;  
    }  
  
    public void setMemento(Memento memento) {  
        this.memento = memento;  
    }  
      
}  
```

4、客户端测试类Client.java

```java
public class Client {  
  
    public static void main(String[] args) {  
        Caretaker caretaker = new Caretaker();  
        Originator originator = new Originator();  
        // 初始化状态标识 "0"  
        originator.setState("0");  
        // 创建状态为"0"的备忘录对象  
        Memento memento_1 = originator.createMemento();  
        // 将记录了Originator状态的备忘录 交给 Caretaker备忘录管理者储存  
        caretaker.setMemento(memento_1);  
        showState(originator);  
          
        System.out.println("----- 更改原发器的状态 -----");  
        // 更改原发器的状态标识为"1"  
        originator.setState("1");  
        showState(originator);  
          
        System.out.println("----- 撤销至原发器的先前状态 -----");  
        originator.restoreMemento(caretaker.getMemento());  
        showState(originator);  
    }  
  
      
    private static void showState(Originator originator) {  
        System.out.println("Originator 的当前状态：" + originator.getState());  
    }  
  
} 
```

5、运行结果：

```
Originator 的当前状态：0  
----- 更改原发器的状态 -----  
Originator 的当前状态：1  
----- 撤销至原发器的先前状态 -----  
Originator 的当前状态：0  
```

## 概念

备忘录模式(Memento)： 主要目的是保存一个对象的某个状态，以便在适当的时候恢复对象。

```java
/** 
 * 备忘录模式(Memento)：主要目的是保存一个对象的某个状态，以便在适当的时候恢复对象 
 *  
 * 示例：原始类--> 创建、恢复备忘录 
 */  
class Original {  
    private String state;  
  
    public Original(String state) {  
        this.state = state;  
    }  
  
    public String getState() {  
        return state;  
    }  
  
    public void setState(String state) {  
        this.state = state;  
    }  
  
    /** 
     * 创建备忘录 
     */  
    public Memento createMemento() {  
        return new Memento(state);  
    }  
  
    /** 
     * 恢复备忘录 
     */  
    public void recoverMemento(Memento memento) {  
        this.state = memento.getState();  
    }  
}  
  
/** 
 * 备忘录 
 */  
class Memento {  
    private String state;  
  
    public Memento(String state) {  
        this.state = state;  
    }  
  
    public String getState() {  
        return state;  
    }  
  
    public void setState(String state) {  
        this.state = state;  
    }  
}  
  
/** 
 * 用来存储备忘录(持有备忘录实例)：只能存储，不能修改 
 */  
class Storage {  
    private Memento memento;  
  
    public Storage(Memento memento) {  
        this.memento = memento;  
    }  
  
    public Memento getMemento() {  
        return memento;  
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
         * 创建原始对象 
         */  
        Original original = new Original("白天模式");  
        System.out.println("original初始状态为：" + original.getState());  
        /** 
         * 创建备忘录 
         *  
         * 注意：original.createMemento()会将初始state(白天模式)传给Memento对象 
         *  
         * 以备需要的时候可以调用storage.getMemento()来拿到该state(白天模式)状态 
         *  
         * 相当于state(白天模式)这个状态已经委托给了storage这个对象来保存 
         */  
        Storage storage = new Storage(original.createMemento());  
        original.setState("夜间模式");  
        System.out.println("original修改后的状态为：" + original.getState());  
        /** 
         * 恢复备忘录 
         */  
        original.recoverMemento(storage.getMemento());  
        System.out.println("original恢复后的状态为：" + original.getState());  
    }  
}  
```

## 总结
Memento备忘录设计模式用来备份一个对象的当前状态，当需要的时候，用这个备份来恢复这个对象在某一个时刻的状态。