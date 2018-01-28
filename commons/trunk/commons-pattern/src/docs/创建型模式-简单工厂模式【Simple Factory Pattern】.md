# 创建型模式-简单工厂模式【Simple Factory Pattern】

## 一、概述

定义一个工厂类，它可以根据参数的不同返回不同类的实例，被创建的实例通常都具有共同的父类。因为在简单工厂模式中用于创建实例的方法是静态(static)方法，因此简单工厂模式又被称为静态工厂方法(Static Factory Method)模式，它属于类创建型模式。

当我们需要创建各种不同对象,这些类称为具体产品类（ConcreteProduct），而将它们公共的代码进行抽象和提取后封装在一个抽象产品类或产品接口（Product）中，每一个具体产品类都是抽象产品类的子类；然后提供一个工厂类(Factory)用于创建各种产品，在工厂类中提供一个创建产品的工厂方法，该方法可以根据所传入的参数不同创建不同的具体产品对象；客户端只需调用工厂类的工厂方法并传入相应的参数即可得到一个产品对象。而无须直接使用new关键字来创建对象，也就是对象的创建由工厂来帮你完成、你不需要知道他的创建过程；

## 二、适用场景

(1) 工厂类负责创建的对象比较少，由于创建的对象较少，不会造成工厂方法中的业务逻辑太过复杂。
(2) 客户端只知道传入工厂类的参数，对于如何创建对象并不关心。

## 四、参与者

1、Factory (工厂角色)：

工厂角色即工厂类，它是简单工厂模式的核心，负责实现创建所有产品实例的内部逻辑；工厂类可以被外界直接调用，创建所需的产品对象；

2、Product (抽象产品类或接口):

它是工厂类所创建的所有对象的父类，所创建的具体产品对象都是其子类对象；

3、ConcreteProduct (具体产品角色)：

它是简单工厂模式的创建目标；

## 五、用例学习

（1）产品接口类：Product.java

```java
/** 
 * 产品接口 
 * @author  
 * 
 */  
public interface Product {  
  
}  
```

（2） 具体的产品类A：ConcreteProductA.java

```java
/** 
 * 具体的产品A 
 * @author  
 * 
 */  
public class ConcreteProductA implements Product {  
  
      
} 
```

 (3) 具体的产品类B：ConcreteProductB.java
 
 ```java
 /** 
 * 具体的产品B 
 * @author   
 * 
 */  
public class ConcreteProductB implements Product {  
  
} 
 ```
 
 （4）具体的产品类C：ConcreteProductC.java
 
 ```java
 /** 
 * 具体的产品C 
 * @author   
 * 
 */  
public class ConcreteProductC implements Product {  
  
}  
 ```
 
 （5）生产产品的工厂类 (简单的工厂类/静态工厂类)：ProductFactory.java
 
 ```java
 /** 
 * 生产产品的工厂类 (简单的工厂类/静态工厂类) 
 * @author   
 * 
 */  
public class ProductFactory {  
      
    /** 
     * 静态工厂方法<br> 
     * 根据传进来的参数的产品类型来 生产/创建 真实的产品实体对象 
     * @param productType 产品类型 
     * @return 产品实体对象 
     */  
    public static Product createProduct(String productType){  
        Product product = null;  
          
        if ("A".equals(productType)) {  
            product = new ConcreteProductA();  
            System.out.println("工厂 创建了产品对象：ConcreteProductA");  
        } else if ("B".equals(productType)) {  
            product = new ConcreteProductB();  
            System.out.println("工厂 创建了产品对象：ConcreteProductB");  
        } else if ("C".equals(productType)) {  
            product = new ConcreteProductC();  
            System.out.println("工厂 创建了产品对象：ConcreteProductC");  
        } else {  
            System.out.println("没有该类型的产品，生产产品哪家强 ? 工厂方法模式  : 抽象工厂模式");  
        }  
          
        return product;  
    }  
  
} 
 ```
 
 （6）客户端调用工厂类静态方法创建产品：Client.java
 
 ```java
 public class Client {  
  
    public static void main(String[] args) {  
        ProductFactory.createProduct("A");  
        ProductFactory.createProduct("C");  
        ProductFactory.createProduct("B");  
        ProductFactory.createProduct("D");  
    }  
} 
 ```
 
## 六、其他/扩展/优缺点

简单工厂类负责了所有产品的创建逻辑，当我们需要新引进一个新产品时，就不得不修改工厂类的产品创建逻辑，在产品类型较多时有可能会造成工厂类的产品创建逻辑过于负责，不利于系统的维护性和扩展性；

