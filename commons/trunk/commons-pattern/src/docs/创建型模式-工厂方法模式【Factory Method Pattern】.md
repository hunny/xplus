# 创建型模式-工厂方法模式【Factory Method Pattern】

## 一、概述

定义一个用于创建对象(Product)的工厂接口（Factory），让子类(ProductFactoryA/ProductFactoryB)决定将哪一个类(ConcreteProductA/ConcreteProductB)实例化。工厂方法模式让一个类的实例化延迟到其子类。提供一个抽象工厂接口来声明抽象工厂方法，而由其子类来具体实现工厂方法，创建具体的产品对象,工厂方法模式是一种类创建型模式。

## 二、适用场景

 (1)  客户端不知道它所需要的对象的类。在工厂方法模式中，客户端不需要知道具体产品类的类名，只需要知道所对应的工厂即可，具体的产品对象由具体工厂类创建；
 (2)  抽象工厂类通过其子类来指定创建哪个对象。在工厂方法模式中，对于抽象工厂类只需要提供一个创建产品的接口，而由其子类来确定具体要创建的对象；

## 四、参与者

(1) Product（抽象产品）：它是定义产品的接口；
(2) ConcreteProduct（具体产品）：
它实现了抽象产品接口，某种类型的具体产品由专门的具体工厂创建，具体工厂和具体产品之间一一对应。UML图中的ConcreteProductA 和 ConcreteProductB 都是具体产品；
(3) Factory（抽象工厂）：在抽象工厂类中，声明了工厂方法(Factory Method)，用于创建一个产品。抽象工厂是工厂方法模式的核心，所有创建对象的工厂类都必须实现该接口；
(4) ConcreteFactory（具体工厂）：它是抽象工厂类的子类，实现了抽象工厂中定义的工厂方法，并可由客户端调用，返回一个具体产品类的实例。UML图中的ProductFactoryA 和 ProductFactoryB 都是具体工厂；

## 五、用例学习

(1)  产品接口：Product.java

```java
/** 
 * 产品接口 
 * @author  
 * 
 */  
public interface Product {  
  
} 
```

(2) 具体的产品A： ConcreteProductA.java

```java
/** 
 * 具体的产品A 
 * @author  
 * 
 */  
public class ConcreteProductA implements Product {  
  
    public ConcreteProductA(){  
        System.out.println("产品A 已创建...");  
    }  
}
```

(3) 具体的产品B： ConcreteProductB.java

```java
/** 
 * 具体的产品B 
 * @author   
 * 
 */  
public class ConcreteProductB implements Product {  
  
    public ConcreteProductB() {  
        System.out.println("产品B 已创建...");  
    }  
} 
```

(4) 创建产品的工厂接口：Factory.java

```java
/** 
 * 创建产品的工厂接口 
 * @author   
 * 
 */  
public interface Factory {  
      
    /**  
     * 创建具体产品实体的方法 
     * @return 
     */  
    public Product createProduct();  
  
} 
```

(5) 具体的A产品工厂类：ProductFactoryA.java

```java
/** 
 * 具体的A产品工厂类：只负责生产A产品（ConcreteProductA） 
 * @author   
 * 
 */  
public class ProductFactoryA implements Factory {  
  
    @Override  
    public Product createProduct() {  
        return new ConcreteProductA();  
    }  
  
} 
```

(6) 具体的B产品工厂类：ProductFactoryB.java

```java
/** 
 * 具体的B产品工厂类：只负责生产B产品（ConcreteProductB） 
 * @author   
 * 
 */  
public class ProductFactoryB implements Factory {  
  
    @Override  
    public Product createProduct() {  
        return new ConcreteProductB();  
    }  
  
}  
```

 (7) 负责调用工厂类生产产品的客户端：Client.java 
 
 ```java
 public class Client {  
  
    public static void main(String[] args) {  
        Factory factory = null;  
          
        factory = new ProductFactoryA();  
        Product productA = null;  
        productA = factory.createProduct();  
          
        factory = new ProductFactoryB();  
        Product productB = null;  
        productB = factory.createProduct();  
          
    }  
}  
 ```
 
(8) 运行结果如下：

```
产品A 已创建...
产品B 已创建...
```

## 六、其他/扩展/优缺点

工厂方法模式是对简单工厂模式的设计优化，简单工厂模式中 如果新增一类产品类型时，需要修改工厂静态方法的产品创建逻辑，而使用工厂方法模式只需新扩展出一个新的工厂子类或是实现类来针对新增类型产品的创建工作、使系统具有了良好的扩展性与维护性；



## 工厂方法模式（Factory Method）

工厂方法：顾名思义，就是调用工厂里的方法来生产对象(产品)的。
工厂方法实现方式有3种：

## 一、普通工厂模式。

就是建立一个工厂类，对实现了同一接口的一些类进行实例的创建。

```java
/** 
 * 示例(一)：普通工厂方法 
 *  
 * 缺点：如果传递的字符串出错，则不能正确创建对象 
 */  
interface Sender {  
    public void send();  
}  
  
class EmailSender implements Sender {  
  
    @Override  
    public void send() {  
        System.out.println("使用电子邮箱发送...");  
    }  
  
}  
  
class SmsSender implements Sender {  
  
    @Override  
    public void send() {  
        System.out.println("使用短信发送...");  
    }  
  
}  
  
/** 
 * 产品工厂 
 */  
class SendFactory {  
    public Sender produceSender(String type) {  
        if ("email".equals(type)) {  
            return new EmailSender();  
        } else if ("sms".equals(type)) {  
            return new SmsSender();  
        } else {  
            System.out.println("没有这种类型...");  
            return null;  
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
        // 创建工厂  
        SendFactory sendFactory = new SendFactory();  
        // 生产产品  
        Sender sender = sendFactory.produceSender("email");  
        // 发货  
        sender.send();  
    }  
  
}  
```

## 二、多个工厂方法模式。

是对普通工厂方法模式的改进，在普通工厂方法模式中，如果传递的字符串出错，则不能正确创建对象，而多个工厂方法模式是提供多个工厂方法，分别创建对象。

```java
/** 
 * 示例(二)：多个工厂方法 
 *  
 * 优点：多个工厂方法模式是提供多个工厂方法，分别创建对象 
 */  
interface Sender {  
    public void send();  
}  
  
class EmailSender implements Sender {  
  
    @Override  
    public void send() {  
        System.out.println("使用电子邮箱发送...");  
    }  
  
}  
  
class SmsSender implements Sender {  
  
    @Override  
    public void send() {  
        System.out.println("使用短信发送...");  
    }  
  
}  
  
/** 
 * 不同方法分别生产相应的产品 
 */  
class SendFactory {  
    public Sender produceEmail() {  
        return new EmailSender();  
    }  
  
    public Sender produceSms() {  
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
        // 创建工厂  
        SendFactory sendFactory = new SendFactory();  
        // 生产产品  
        Sender senderEmail = sendFactory.produceEmail();  
        // 发货  
        senderEmail.send();  
    }  
  
}  
```

## 三、静态工厂方法模式。

将上面的多个工厂方法模式里的方法置为静态的，不需要创建实例，直接调用即可。

```java
/** 
 * 示例(三)：静态工厂方法 
 *  
 * 优点：多个工厂方法模式是提供多个工厂方法，分别创建对象 
 */  
interface Sender {  
    public void send();  
}  
  
class EmailSender implements Sender {  
  
    @Override  
    public void send() {  
        System.out.println("使用电子邮箱发送...");  
    }  
  
}  
  
class SmsSender implements Sender {  
  
    @Override  
    public void send() {  
        System.out.println("使用短信发送...");  
    }  
  
}  
  
/** 
 * 静态工厂：不同实例化工厂 
 *  
 * 不同方法分别生产相应的产品 
 */  
class SendFactory {  
    public static Sender produceEmail() {  
        return new EmailSender();  
    }  
  
    public static Sender produceSms() {  
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
        // 直接生产产品  
        Sender senderEmail = SendFactory.produceEmail();  
        // 发货  
        senderEmail.send();  
    }  
  
}  
```

## 四、总结

总体来说，凡是出现了大量的产品需要创建，并且具有共同的接口时，可以通过工厂方法模式进行创建。在以上的三种模式中，第一种如果传入的字符串有误，不能正确创建对象，第三种相对于第二种，不需要实例化工厂类，所以，大多数情况下，我们会选用第三种——静态工厂方法模式。

