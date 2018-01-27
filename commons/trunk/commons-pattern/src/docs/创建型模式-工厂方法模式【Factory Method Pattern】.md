# 创建型模式-工厂方法模式【Factory Method Pattern】

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

