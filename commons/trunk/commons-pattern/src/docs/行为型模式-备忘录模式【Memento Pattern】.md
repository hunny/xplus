# 行为型模式-备忘录模式【Memento Pattern】

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