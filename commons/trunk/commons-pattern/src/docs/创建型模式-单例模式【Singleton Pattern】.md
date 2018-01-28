# 创建型模式-单例模式【Singleton Pattern】

## 一、概述

确保某一个类只有一个实例，而且自行实例化并向整个系统提供这个实例，这个类称为单例类，它提供全局访问的方法。单例模式是一种对象创建型模式。

## 二、适用场景

系统/应用只需要一个实例对象，确保对象的唯一性。

## 四、参与者

Singleton（单例类）：在单例类的内部实现只生成一个实例，同时它提供一个静态的getInstance()工厂方法，让客户可以访问它的唯一实例；为了防止在外部对其实例化，将其构造函数设计为私有；在单例类内部定义了一个Singleton类型的静态对象，作为外部共享的唯一实例。

## 用例学习

单例模式两种不同的实现方式：

### 饿汉式单例模式：

```java
/** 
 * 单例类：饿汉式单例模式 
 * @author   
 * 
 */  
public class EagerSingleton {  
    /** 定义私有静态变量  类加载的时候就已经创建了单例对象 */  
    private static final EagerSingleton instance = new EagerSingleton();  
      
    /** 
     * 私有构造函数  只能被自身调用实例化 
     */  
    private EagerSingleton(){  
          
    }  
      
    /** 
     * 获取单例实例对象 
     * @return 
     */  
    public static EagerSingleton getInstance(){  
        return instance;  
    }  
  
}  
```

### 懒汉式单例模式：

```java
/** 
 * 单例模式一：懒汉式单例模式 
 * @author   
 * 
 */  
public class LazySingleton {  
    /** 私有静态成员变量 存储唯一实例  */  
    private static LazySingleton instance;  
      
    /** 
     * 私有构造函数 只能被自身调用实例化 
     */  
    private LazySingleton() {   
          
    }  
      
    /** 
     * 获取单例实例对象 
     * @return 
     */  
    public synchronized static LazySingleton getInstance(){  
        if (instance == null){  
            instance = new LazySingleton();  
        }  
        return instance;  
    }  
  
}  
```

## 懒汉式

```java
/** 
 * 示例：单例--单例对象能保证在一个JVM中，该对象只有一个实例存在。 
 *  
 * 缺点：这种做法在多线程环境下，不安全 
 *  
 * 懒汉式 
 */  
  
class Singleton {  
    /** 
     * 持有私有静态变量(也称类变量)，防止被引用 
     *  
     * 此处赋值为null，目的是实现延迟加载 (因为有些类比较庞大，所以延迟加载有助于提升性能) 
     */  
    private static Singleton instance = null;  
  
    /** 私有构造方法，防止被实例化 */  
    private Singleton() {  
  
    }  
  
    /** 静态工厂方法，创建实例 --只不过这里是创建自己，而且只能创建一个 */  
    public static Singleton getInstance() {  
        if (instance == null) {  
            instance = new Singleton();  
        }  
        return instance;  
    }  
  
    public void info() {  
        System.out.println("this is a test method...");  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Singleton s1 = Singleton.getInstance();  
        /** 
         * 调用普通方法 
         */  
        s1.info();  
        Singleton s2 = Singleton.getInstance();  
        /** 
         * 运行结果为true，说明s1、s2这两个类变量都指向内存中的同一个对象 
         */  
        System.out.println(s1 == s2);  
    }  
}  
```

## 饿汉式

```java
/** 
 * 饿汉式 
 */  
  
class Singleton {  
    private static Singleton instance = new Singleton();  
  
    private Singleton() {  
  
    }  
  
    public static Singleton getInstance() {  
        return instance;  
    }  
  
    public void info() {  
        System.out.println("this is a test method...");  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Singleton s1 = Singleton.getInstance();  
        /** 
         * 调用普通方法 
         */  
        s1.info();  
        Singleton s2 = Singleton.getInstance();  
        /** 
         * 运行结果为true，说明s1、s2这两个类变量都指向内存中的同一个对象 
         */  
        System.out.println(s1 == s2);  
    }  
  
}  
```

## 如果考虑多线程，那么getInstance()方法要加同步synchronized，这时饿汉式比懒汉式要好，尽管资源利用率要差，但是不用同步。

```java
/** 
 *  
 * 考虑多线程的时候，下面这种做法可以参考一下：--懒汉式 
 *  
 * 在创建类的时候进行同步，所以只要将创建和getInstance()分开，单独为创建加synchronized关键字 
 *  
 * 这种做法考虑性能的话，整个程序只需创建一次实例，所以性能也不会有什么影响。 
 *  
 */  
public class SingletonTest {  
  
    private static SingletonTest instance = null;  
  
    private SingletonTest() {  
    }  
  
    private static synchronized void syncInit() {  
        if (instance == null) {  
            instance = new SingletonTest();  
        }  
    }  
  
    public static SingletonTest getInstance() {  
        if (instance == null) {  
            syncInit();  
        }  
        return instance;  
    }  
}  
```
