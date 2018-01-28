# 行为型模式-观察者模式【Observer Pattern】

## 一、概念：

简单地说，观察者模式定义了一个一对多的依赖关系，让一个或多个观察者对象监察一个主题对象。这样一个主题对象在状态上的变化能够通知所有的依赖于此对象的那些观察者对象，使这些观察者对象能够自动更新。

观察者（Observer）模式是对象的行为型模式，又叫做发表-订阅（Publish/Subscribe）模式、模型-视图（Model/View）模式、源-收听者（Source/Listener）模式或从属者（Dependents）模式。

主要参与角色：观察者（Observer）与  被观察者(Observable)

## 代码DEMO:

1、观察者接口类：Observer.java

```java
/** 
 * 设计模式之 观察者模式学习<br> 
 * 定义观察者 通用接口 
 */  
public interface Observer {  
  
    /** 
     * 一发现别人<被观察者>有动静，自己也要行动起来 
     * @param content 
     */  
    public void update(String content);  
}  
```

2、被观察者接口类：Observable.java

```java
/** 
 * 设计模式之 观察者模式学习<br> 
 * 定义被观察者   通用接口 
 */  
public interface Observable {  
  
    /** 
     * 增加/注册一个观察者 
     * @param observer 
     */  
    public void addObserver(Observer observer);  
      
    /** 
     * 当被观察者不想被某某暗暗偷窥啦、便可删除/注销该观察者 
     * @param observer 
     */  
    public void removeObserver(Observer observer);  
      
    /** 
     * 通知观察者   我干了什么事  也就是我干了什么事都让观察者知道 
     * @param content 
     */  
    public void notifyObservers(String content);  
      
}  
```
3、定义一个被观察者类：Xiaolv.java

```java 
import java.util.ArrayList;  
  
/** 
 * 这里把自己定义成  被观察者<br> 
 * 会被哪些人观察呢？呵呵... 女朋友？老爸？... 
 * 
 */  
public class Xiaolv implements Observable {  
  
    /** 存储所有添加的 观察者  */  
    private ArrayList<Observer> observerList = new ArrayList<Observer>();  
      
    /** 
     * 添加观察者 
     */  
    @Override  
    public void addObserver(Observer observer) {  
        this.observerList.add(observer);  
    }  
  
    /** 
     * 删除观察者 
     */  
    @Override  
    public void removeObserver(Observer observer) {  
        this.observerList.remove(observer);  
    }  
  
    /** 
     * 通知所有的观察者 
     */  
    @Override  
    public void notifyObservers(String content) {  
        for(Observer observer : observerList){  
            observer.update(content);  
        }  
    }  
  
    /** 
     * <一个被观察者>在开心的玩游戏 
     */  
    public void playGameHappy(){  
        System.out.println(": 正在开心的玩游戏");  
        notifyObservers(" 正在玩游戏");  
    }  
      
    /** 
     * <一个被观察者>在努力的写代码 
     */  
    public void hardCoding(){  
        System.out.println("：正在努力的写代码");  
        notifyObservers(" 正在写代码");  
    }  
}  
```
4、定义二个观察者类：GirlFriend.java

```java 
/** 
 * 定义一个观察者类：的女朋友 
 */  
public class GirlFriend implements Observer {  
  
    @Override  
    public void update(String content) {  
        System.out.println("的女朋友观察到："+content);  
    }  
  
}  
```

MyFather.java

```java 
/** 
 * 定义的又一观察者类:我的老爸 
 * @author ice 
 * 
 */  
public class MyFather implements Observer {  
  
    @Override  
    public void update(String content) {  
        System.out.println("的老爸观察到："+content);  
    }  
}  
```
5、客户端测试类 Client.java

```java 
public class Client {  
  
    public static void main(String[] args) {  
        /** 定义两个观察者对象:的女朋友和老爸  */  
        GirlFriend janice = new GirlFriend();  
        MyFather myDad = new MyFather();  
        /** 定义被观察者对象：<ice> */  
        Xiaolv ice = new Xiaolv();  
        /** 添加janice去观察  */  
        ice.addObserver(janice);  
        /** 添加老爸来观察  */  
        ice.addObserver(myDad);  
          
        ice.playGameHappy();  
        System.out.println("-------------");  
        ice.hardCoding();  
    }  
  
}  
```

6、程序运行效果如下：

```
: 正在开心的玩游戏  
的女朋友观察到： 正在玩游戏  
的老爸观察到： 正在玩游戏  
-------------  
：正在努力的写代码  
的女朋友观察到： 正在写代码  
的老爸观察到： 正在写代码  
```






    




```

## 概念

观察者(Observer)模式定义：在对象之间定义了一对多的依赖关系，这样一来，当一个对象改变状态时，依赖它的对象都会收到通知并自动跟新。Java已经提供了对观察者Observer模式的默认实现， Java对观察者模式的支持主要体现在Observable类和Observer接口。

```java
/** 示例：们去菜市场买菜 
 *  
 * 小商贩--主题 
 */  
class Food extends Observable {  
    /** 菜名 */  
    private String name;  
    /** 菜价 */  
    private float price;  
  
    public Food(String name, float price) {  
        this.name = name;  
        this.price = price;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public float getPrice() {  
        return price;  
    }  
  
    public void setPrice(float price) {  
        this.price = price;  
        /** 
         * 设置菜价的状态已经被改变 
         */  
        this.setChanged();  
        /** 
         * 通知【所有】正在看菜(已经注册了)的顾客，然后回调Observer的update方法进行更新 
         *  
         * 这里可以体现对象的一对多：一个小商贩一旦更新价格(即一个对象改变状态)，便会自动通知所有的顾客(依赖它的对象都会收到通知) 
         * 并自动update 
         */  
        this.notifyObservers(price);  
    }  
  
}  
  
/** 
 * 顾客 --观察者 
 */  
class Customer implements Observer {  
    private String name;  
  
    public Customer(String name) {  
        this.name = name;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    @Override  
    public void update(Observable o, Object arg) {  
        if (o instanceof Food && arg instanceof Float) {  
            Food food = (Food) o;  
            float price = (Float) arg;  
            System.out.println("您好：" + this.name + " ," + food.getName()  
                    + "的价格已经发生改变，现在的价格为：" + price + "元/斤");  
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
        Food food = new Food("土豆", 1.0f);  
        Customer zhangsan = new Customer("张三");  
        Customer lisi = new Customer("李四");  
        /** 
         * 添加顾客 
         */  
        food.addObserver(zhangsan);  
        food.addObserver(lisi);  
        /** 
         * 更新价格 
         */  
        food.setPrice(1.5f);  
    }  
} 
```

