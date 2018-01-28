# 行为型模式-中介者模式【Mediator Pattern】

## 一、概述

用一个中介对象（中介者）来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。中介者模式又称为调停者模式，它是一种对象行为型模式。

## 二、适用场景

协调多个对象之间的交互。

## 四、参与者

1、Mediator（抽象中介者）：它定义一个接口，该接口用于与各同事对象之间进行通信。

2、ConcreteMediator（具体中介者）：它是抽象中介者的子类，通过协调各个同事对象来实现协作行为，它维持了对各个同事对象的引用。

3、Colleague（抽象同事类）：它定义各个同事类公有的方法，并声明了一些抽象方法来供子类实现，同时它维持了一个对抽象中介者类的引用，其子类可以通过该引用来与中介者通信。

4、ConcreteColleague（具体同事类）：它是抽象同事类的子类；每一个同事对象在需要和其他同事对象通信时，先与中介者通信，通过中介者来间接完成与其他同事类的通信；在具体同事类中实现了在抽象同事类中声明的抽象方法。

## 五、用例学习[用例背景：求租者 - 房屋出租中介 - 房东 的故事]

1、抽象同事类：Person.java

```java
/** 
 * 抽象同事类： 
 * @author   
 * 
 */  
public abstract class Person {  
    // 维持一个抽象中介者的引用  
    protected Mediator mediator;  
      
    protected String name;  
      
    public Person(String name, Mediator mediator){  
        this.mediator = mediator;  
        this.name = name;  
    }  
      
    /** 
     * 设置中介者对象 
     * @param mediator 
     */  
    public void setMediator(Mediator mediator){  
        this.mediator = mediator;  
    }  
  
    /** 
     * 向中介 发送消息 
     */  
    protected abstract void sendMessage(String msg);  
      
    /** 
     * 从中介 获取消息 
     */  
    protected abstract void getMessage(String msg);  
} 
```

2、房屋求租者 Renter.java

```java
/** 
 * 具体同事类：这里的角色是 租房者 
 * @author   
 * 
 */  
public class Renter extends Person {  
  
    public Renter(String name, Mediator mediator) {  
        super(name, mediator);  
    }  
      
  
    @Override  
    protected void sendMessage(String msg) {  
        mediator.operation(this, msg);  
    }  
  
    @Override  
    protected void getMessage(String msg) {  
        System.out.println("求租者[" + name + "]收到中介发来的消息： " + msg);  
    }  
  
} 
```

3、具体同事类 房屋出租者[房东] Landlord.java

```java
/** 
 * 具体同事类：这里的角色是 房东 
 * @author   
 * 
 */  
public class Landlord extends Person {  
  
    public Landlord(String name, Mediator mediator) {  
        super(name,mediator);  
    }  
      
    @Override  
    protected void sendMessage(String msg) {  
        mediator.operation(this, msg);  
    }  
  
    @Override  
    protected void getMessage(String msg) {  
        System.out.println("房东["+ name +"]收到中介发来的消息：" + msg);  
    }  
  
}  
```

4、抽象中介者类：Mediator.java

```java
/** 
 * 抽象中介者类 
 * @author   
 * 
 */  
public abstract class Mediator {  
    // 用于添加储存 "房东"角色  
    protected List<Person> landlordList = new ArrayList<Person>();  
      
    // 用于添加储存 "求租者"角色  
    protected List<Person> renterList = new ArrayList<Person>();  
      
    /** 
     * 中介者注册房东信息 
     * @param landlord 房东实体 
     */  
    public void registerLandlord(Person landlord){  
        landlordList.add(landlord);  
    }  
      
    /** 
     * 中介者注册 求租者信息 
     * @param landlord 房东实体 
     */  
    public void registerRenter(Person landlord){  
        renterList.add(landlord);  
    }  
      
    /** 
     * 声明抽象方法 由具体中介者子类实现 消息的中转和协调 
     */  
    public abstract void operation(Person person, String message);  
  
}
```

5、具体中介者类：房屋中介 HouseMediator.java

```java
/** 
 * 具体中介者类：这里的角色是 房屋出租中介 
 * @author   
 * 
 */  
public class HouseMediator extends Mediator {  
  
    @Override  
    public void operation(Person person, String message) {  
        if(person instanceof Renter){  
            // 将租屋的需求消息传递给 注册了的房东们  
            for(Person landlord: landlordList){  
                landlord.getMessage(message);  
            }  
        } else if(person instanceof Landlord){  
            // 将房东的出租房消息传递给 注册了的 房屋求租者们  
            for(Person renter : renterList){  
                renter.getMessage(message);  
            }  
        }  
          
    }  
  
}  
```

6、客户端测试类 Client.java

```java
/** 
 * 客户端测试类 
 * @author   
 * 
 */  
public class Client {  
  
    public static void main(String[] args) {  
        // 实例化房屋中介  
        Mediator mediator = new HouseMediator();  
          
        Person landlordA, landlordB, renter;  
        landlordA = new Landlord("房东李", mediator);  
        landlordB = new Landlord("房东黎", mediator);  
                  
        renter = new Renter("",mediator);  
          
        // 房东注册中介  
        mediator.registerLandlord(landlordA);  
        mediator.registerLandlord(landlordB);  
        // 求租者注册中介  
        mediator.registerRenter(renter);  
          
        // 求租者 发送求租消息  
        renter.sendMessage("在天河公园附近租套房子，价格1000元左右一个月");  
        System.out.println("--------------------------");  
        // 房东A 发送房屋出租消息  
        landlordA.sendMessage("天河公园学院站三巷27号四楼有一房一厅出租  1200/月  光线好 近公交站");  
    }  
  
} 
```

7、运行结果：

```
房东[房东李]收到中介发来的消息：在天河公园附近租套房子，价格1000元左右一个月  
房东[房东黎]收到中介发来的消息：在天河公园附近租套房子，价格1000元左右一个月  
--------------------------  
求租者[]收到中介发来的消息： 天河公园学院站三巷27号四楼有一房一厅出租  1200/月  光线好 近公交站  
```

## 概念

中介者模式(Mediator)：主要用来降低类与类之间的耦合的，因为如果类与类之间有依赖关系的话，不利于功能的拓展和维护，因为只要修改一个对象，其它关联的对象都得进行修改。

```java
/** 
 * 中介者模式(Mediator)：主要用来降低类与类之间的耦合的，因为如果类与类之间有依赖关系的话， 
 *  
 * 不利于功能的拓展和维护，因为只要修改一个对象，其它关联的对象都得进行修改。 
 *  
 * 示例：下面以房屋(出租)中介为例 
 */  
interface Mediator {  
    void createMediator();  
  
    void recommendHouse();  
}  
  
/** 
 * (User)让中介帮我们推荐房子 
 *  
 * 所以需要持有一个中介实例 
 */  
abstract class User {  
    private Mediator mediator;  
  
    public User(Mediator mediator) {  
        this.mediator = mediator;  
    }  
  
    public Mediator getMediator() {  
        return mediator;  
    }  
  
    public abstract void selectHouse();  
}  
  
class ZhangSan extends User {  
  
    public ZhangSan(Mediator mediator) {  
        super(mediator);  
    }  
  
    @Override  
    public void selectHouse() {  
        System.out.println("张三在选房子...");  
    }  
}  
  
class LiSi extends User {  
  
    public LiSi(Mediator mediator) {  
        super(mediator);  
    }  
  
    @Override  
    public void selectHouse() {  
        System.out.println("李四在选房子...");  
    }  
}  
  
/** 
 * 房屋中介：中介向要找房子的那些人推荐房子，由他们选择自己想要的房子。 
 *  
 * 因此中介得持有那些实例，才有可能把房子租出去。 
 */  
class MyMediator implements Mediator {  
    private User zhangsan;  
    private User lisi;  
  
    public User getZhangsan() {  
        return zhangsan;  
    }  
  
    public User getLisi() {  
        return lisi;  
    }  
  
    @Override  
    public void createMediator() {  
        zhangsan = new ZhangSan(this);  
        lisi = new LiSi(this);  
    }  
  
    /** 
     * 中介向要找房子的那些人推荐房子，由他们选择自己想要的房子 
     */  
    @Override  
    public void recommendHouse() {  
        zhangsan.selectHouse();  
        lisi.selectHouse();  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Mediator mediator = new MyMediator();  
        mediator.createMediator();  
        mediator.recommendHouse();  
    }  
}  
```

## 总结
中介者模式只需关心和Mediator类的关系，具体类与类之间的关系及调用交给Mediator就行。