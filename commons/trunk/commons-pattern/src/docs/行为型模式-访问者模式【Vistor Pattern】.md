# 行为型模式-访问者模式【Vistor Pattern】

## 概念

访问者模式(Visitor)：把数据结构和作用于结构上的操作解耦合，使得操作集合可相对自由地演化。访问者模式适用于数据结构相对稳定而算法又容易变化的系统。访问者模式的优点是增加操作很容易，因为增加操作意味着增加新的访问者；而它的缺点就是增加新的数据结构很困难。

```java
/** 
 * 访问者模式(Visitor)：把数据结构和作用于结构上的操作解耦合，使得操作集合可相对自由地演化。 
 *  
 * 访问者模式就是一种分离对象数据结构与行为的方法，通过这种分离， 
 *  
 * 可达到为一个被访问者动态添加新的操作而无需做其它的修改的效果。 
 */  
interface Visitor {  
    /** 
     * 访问对象 
     *  
     * @param subject 
     *            待访问的对象 
     */  
    public void visitor(Subject subject);  
}  
  
class MyVisitor implements Visitor {  
    @Override  
    public void visitor(Subject subject) {  
        System.out.println("MyVisitor 访问的属性值为：" + subject.getField());  
    }  
}  
  
class OtherVisitor implements Visitor {  
    @Override  
    public void visitor(Subject subject) {  
        System.out.println("OtherVisitor 访问的属性值为：" + subject.getField());  
    }  
}  
  
interface Subject {  
    /** 接受将要访问它的对象 */  
    public void accept(Visitor visitor);  
  
    /** 获取将要被访问的属性 */  
    public String getField();  
}  
  
class MySubject implements Subject {  
    private String name;  
  
    public MySubject(String name) {  
        this.name = name;  
    }  
  
    /** 
     * 这是核心：接收【指定的访问者】来访问咱自身的MySubject类的状态或特征 
     */  
    @Override  
    public void accept(Visitor visitor) {  
        visitor.visitor(this);  
    }  
  
    @Override  
    public String getField() {  
        return name;  
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
         * 创建待访问的对象 
         */  
        Subject subject = new MySubject("张三");  
        /** 
         * 接受访问对象：这里只接收MyVisitor访问者对象，不接收OtherVisitor访问者对象 
         */  
        subject.accept(new MyVisitor());  
    }  
}  
```

## 总结
访问者模式就是一种分离对象数据结构与行为的方法，通过这种分离，可达到为一个被访问者动态添加新的操作而无需做其它的修改的效果。