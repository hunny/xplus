# 行为型模式-中介者模式【Mediator Pattern】

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
 * 咱(User)让中介帮我们推荐房子 
 *  
 * 所以咱需要持有一个中介实例 
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
        // TODO Auto-generated constructor stub  
    }  
  
    @Override  
    public void selectHouse() {  
        System.out.println("张三在选房子...");  
    }  
}  
  
class LiSi extends User {  
  
    public LiSi(Mediator mediator) {  
        super(mediator);  
        // TODO Auto-generated constructor stub  
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