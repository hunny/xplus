# 结构型模式-享元模式【Flyweight Pattern】

## 概念
享元模式（Flyweight）：运用共享的技术有效地支持大量细粒度的对象。主要目的是实现对象的共享，即共享池，当系统中对象多的时候可以减少内存的开销。在某种程度上，你可以把单例看成是享元的一种特例。

```java
/** 
 * 享元模式(Flyweight)：运用共享的技术有效地支持大量细粒度的对象。 
 *  
 * 主要目的是实现对象的共享，即共享池，当系统中对象多的时候可以减少内存的开销。 
 */  
abstract class FlyWeight {  
    public abstract void method();  
}  
  
/** 
 * 创建持有key的子类 
 */  
class SubFlyWeight extends FlyWeight {  
    private String key;  
  
    public SubFlyWeight(String key) {  
        this.key = key;  
    }  
  
    @Override  
    public void method() {  
        System.out.println("this is the sub method，and the key is " + this.key);  
    }  
}  
  
/** 
 * 享元工厂：负责创建和管理享元对象 
 */  
class FlyweightFactory {  
    private Map<String, FlyWeight> map = new HashMap<String, FlyWeight>();  
  
    /** 
     * 获取享元对象 
     */  
    public FlyWeight getFlyWeight(String key) {  
        FlyWeight flyWeight = map.get(key);  
        if (flyWeight == null) {  
            flyWeight = new SubFlyWeight(key);  
            map.put(key, flyWeight);  
        }  
        return flyWeight;  
    }  
  
    /** 
     * 获取享元对象数量 
     */  
    public int getCount() {  
        return map.size();  
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
         * 创建享元工厂 
         */  
        FlyweightFactory factory = new FlyweightFactory();  
        /***** 第一种情况：key相同时 ***************/  
        FlyWeight flyWeightA = factory.getFlyWeight("aaa");  
        FlyWeight flyWeightB = factory.getFlyWeight("aaa");  
        /** 
         * 透过打印结果为true可以知道： 由于key都为"aaa"，所以flyWeightA和flyWeightB指向同一块内存地址 
         */  
        System.out.println(flyWeightA == flyWeightB);  
        flyWeightA.method();  
        flyWeightB.method();  
        /** 
         * 享元对象数量：1 
         */  
        System.out.println(factory.getCount());  
  
        /***** 第二种情况：key不相同时 ***************/  
        System.out.println("\n======================================");  
        FlyWeight flyWeightC = factory.getFlyWeight("ccc");  
        /** 
         * 打印结果为false 
         */  
        System.out.println(flyWeightA == flyWeightC);  
        flyWeightC.method();  
        /** 
         * 享元对象数量：2 
         */  
        System.out.println(factory.getCount());  
    }  
}  
```

## 总结
享元与单例的区别：1、与单例模式不同，享元模式是一个类可以有很多对象(共享一组对象集合)，而单例是一个类仅一个对象；2、它们的目的也不一样，享元模式是为了节约内存空间，提升程序性能(避免大量的new操作)，而单例模式则主要是共享单个对象的状态及特征。