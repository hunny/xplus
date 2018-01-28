# 创建型模式-抽象工厂模式【Abstract Factory Pattern】

## 一、概述

抽象工厂模式为创建一组对象提供了一种解决方案。与工厂方法模式相比，抽象工厂模式中的具体工厂不只是创建一种产品，它负责创建一族产品。


## 二、适用场景

为创建一组对象提供了一种解决方案。

## 四、参与者

(1)    AbstractFactory（抽象工厂）：它声明了一组用于创建一族产品的方法，每一个方法对应一种产品。如上图UML类图中的Factory。

(2)    ConcreteFactory（具体工厂）：它实现了在抽象工厂中声明的创建产品的方法，生成一组具体产品，这些产品构成了一个产品族，每一个产品都位于某个产品等级结构中。如上图UML类图中的Factory1 和Factory2。

(3)    AbstractProduct（抽象产品）：它为每种产品声明接口，如上图UML类图中的ProductA和ProductB。

(4)    ConcreteProduct（具体产品）：它定义具体工厂生产的具体产品对象，实现抽象产品接口中声明的业务方法。如上图UML类图中的ConcreteProductA1/A2/B1/B2。

## 五、用例学习

(1) 产品类型A接口: ProductA.java

```java
/** 
 * 产品类型A接口 
 * @author  
 * 
 */  
public interface ProductA {  
  
}  
```


(2)产品类型B接口: ProductB.java

```java
/** 
 * 产品类型B接口 
 * @author   
 * 
 */  
public interface ProductB {  
  
}  
```
(3) 具体的A类型产品A1: ConcreteProductA1.java

```java
/** 
 * 具体的A类型产品A1 
 * @author  
 * 
 */  
public class ConcreteProductA1 implements ProductA {  
  
    public ConcreteProductA1(){  
        System.out.println("产品A1 已创建...");  
    }  
}  
```
(4) 具体的A类型产品A2: ConcreteProductA2.java

```java
/** 
 * 具体的A类型产品A2 
 * @author   
 * 
 */  
public class ConcreteProductA2 implements ProductA {  
  
    public ConcreteProductA2() {  
        System.out.println("产品A2 已创建...");  
    }  
}  
```
(5) 具体的B类型产品B1: ConcreteProductB1.java

```java
/** 
 * 具体的B类型产品B1 
 * @author   
 * 
 */  
public class ConcreteProductB1 implements ProductB {  
  
    public ConcreteProductB1(){  
        System.out.println("产品B1 已创建...");  
    }  
}  
```

(6) 具体的B类型产品B2: ConcreteProductB2.java

```java
/** 
 * 具体的B类型产品B2 
 * @author   
 * 
 */  
public class ConcreteProductB2 implements ProductB {  
  
    public ConcreteProductB2(){  
        System.out.println("产品B2 已创建...");  
    }  
}  
```

(7) 创建产品的工厂接口: Factory.java

```java
/** 
 * 创建产品族的工厂接口 
 * @author   
 * 
 */  
public interface Factory {  
      
    public ProductA createProductA();  
      
    public ProductB createProductB();  
  
}  
```

(8) 具体的"工厂一"类：只负责生产A1、B1等1族甲级优产品: ProductFactory1.java

```java
/** 
 * 具体的"工厂一"类：只负责生产A1、B1等1族甲级优产品 
 * @author   
 * 
 */  
public class ProductFactory1 implements Factory {  
  
    @Override  
    public ProductA createProductA() {  
        return new ConcreteProductA1();  
    }  
  
    @Override  
    public ProductB createProductB() {  
        return new ConcreteProductB1();  
    }  
  
}  
```

(9) 具体的"工厂二"类：只负责生产A2、B2等2族乙级劣产品: ProductFactory2.java

```java
/** 
 * 具体的"工厂二"类：只负责生产A2、B2等2族乙级劣产品 
 * @author   
 * 
 */  
public class ProductFactory2 implements Factory {  
  
    @Override  
    public ProductA createProductA() {  
        return new ConcreteProductA2();  
    }  
  
    @Override  
    public ProductB createProductB() {  
        return new ConcreteProductB2();  
    }  
  
}  
```

(10) 客户端 Client：Client.java

```java
public class Client {  
  
    public static void main(String[] args) {  
        Factory factory1 = null;  
        System.out.println("工厂一 是正规工厂  生产的产品是正品");  
        factory1 = new ProductFactory1();  
        factory1.createProductA();  
        factory1.createProductB();  
          
        System.out.println("-------------");  
          
        Factory factory2 = null;  
        System.out.println("工厂二 是黑工厂  生产的产品是次品");  
        factory2 = new ProductFactory2();  
        factory2.createProductA();  
        factory2.createProductB();  
          
    }  
}  
```

(11) 运行结果 如下：

```
工厂一 是正规工厂  生产的产品是正品  
产品A1 已创建...  
产品B1 已创建...  
-------------  
工厂二 是黑工厂  生产的产品是次品  
产品A2 已创建...  
产品B2 已创建...
```

## 其它用例参考

```java
/** 
 * 示例：抽象工厂--顾名思义，就是把工厂抽象出来，不同的工厂生产不同的产品 
 *  
 * 优点：一旦需要增加新的功能，直接增加新的工厂类就可以了，不需要修改之前的代码 
 */  
interface Sender {  
    public void send();  
}  
  
class EmailSender implements Sender {  
  
    @Override  
    public void send() {  
        System.out.println("this is a email...");  
    }  
}  
  
class SmsSender implements Sender {  
  
    @Override  
    public void send() {  
        System.out.println("this is a sms...");  
    }  
  
}  
  
/** 
 * 角色：抽象工厂 
 */  
interface AbstractFactory {  
    public Sender produce();  
}  
  
/** 
 * 邮件工厂 
 */  
class EmailSendFactory implements AbstractFactory {  
  
    @Override  
    public Sender produce() {  
        return new EmailSender();  
    }  
}  
  
/** 
 * 短信工厂 
 */  
class SmsSendFactory implements AbstractFactory {  
  
    @Override  
    public Sender produce() {  
        return new SmsSender();  
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
         * 创建工厂 
         */  
        AbstractFactory factory = new SmsSendFactory();  
        /** 
         * 生产产品 
         */  
        Sender sender = factory.produce();  
        /** 
         * 执行业务逻辑 
         */  
        sender.send();  
    }  
  
}  
```