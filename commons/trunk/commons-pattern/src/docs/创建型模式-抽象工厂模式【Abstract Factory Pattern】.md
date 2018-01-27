# 创建型模式-抽象工厂模式【Abstract Factory Pattern】

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