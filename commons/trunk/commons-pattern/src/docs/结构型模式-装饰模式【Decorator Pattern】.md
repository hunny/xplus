# 结构型模式-装饰模式【Decorator Pattern】

[原文地址](http://blog.csdn.net/janice0529/article/details/44160091)

## 一、概述 
动态地给一个对象增加一些额外的职责，就增加对象功能来说，装饰模式比生成子类实现更为灵活。装饰模式是一种对象结构型模式。装饰模式是一种用于替代继承的技术,使用对象之间的关联关系取代类之间的继承关系。在装饰模式中引入了装饰类，在装饰类中既可以调用待装饰的原有类的方法，还可以增加新的方法，以扩充原有类的功能。

## 二、适用场景 
装饰原有对象、在不改变原有对象的情况下扩展增强新功能/新特征.。当不能采用继承的方式对系统进行扩展或者采用继承不利于系统扩展和维护时可以使用装饰模式。

## 三、UML类图 

参见原文

## 四、参与者 
①Component（抽象构件）：它是具体构件和抽象装饰类的共同父类，声明了在具体构件中实现的业务方法。 
②ConcreteComponent（具体构件）：它是抽象构件类的子类，用于定义具体的构件对象（被装饰者），实现了在抽象构件中声明的方法，装饰器可以给它增加额外的职责（方法）。 
③Decorator（抽象装饰类）：它也是抽象构件类的子类，用于给具体构件增加职责，但是具体职责在其子类中实现。它维护一个指向抽象构件对象的引用，通过该引用可以调用装饰之前构件对象的方法，并通过其子类扩展该方法，以达到装饰的目的。 
④ConcreteDecorator（具体装饰类）：它是抽象装饰类的子类，负责向构件添加新的职责。每一个具体装饰类都定义了一些新的行为，它可以调用在抽象装饰类中定义的方法，并可以增加新的方法用以扩充对象的行为。

## 五、用例学习 
1、角色：抽象构件 Component.java

```java
/**
 * 设计模式之 装饰模式<br/>
 * 角色：抽象构件<br/>
 * 被装饰的对象抽象类<br/>
 * @author lvzb.software@qq.com
 *
 */
public abstract class Component {

    public abstract void operation();
}
```

2、角色：具体构件类 ConcreteComponent.java

```java
/**
 * 设计模式之 装饰模式<br/>
 * 角色：具体构件类<br/>
 * 抽象构件类的子类，定义具体的构件对象，也就是具体的被装饰对象<br/>
 * @author  lvzb.software@qq.com
 *
 */
public class ConcreteComponent extends Component {

    @Override
    public void operation() {
        System.out.println("我是具体的构件类(被装饰对象)，这是我的原有方法");
    }

}
```

3、角色：抽象装饰类 Decorator.java

```java
/**
 * 设计模式之 装饰模式<br/>
 * 角色：抽象装饰类<br/>
 * 也是抽象构件类的子类，目的是能够进行多次装饰<br/>
 * 作用：引入抽象构件类， 给具体构件类增加职责，但是具体职责在其子类中实现<br/>
 * @author lvzb.software@qq.com
 *
 */
public class Decorator extends Component {

    // 维持一个对抽象构件对象的引用
    private Component component;

    // 注入一个抽象构件类型的对象
    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void operation() {
        // 调用原有业务方法
        component.operation();
    }

}
```

4、角色：具体装饰类 ConcreteDecoratorA.java

```java
/**
 * 设计模式之 装饰模式<br/>
 * 角色：具体装饰类<br/>
 * 向构件添加新的具体的职责、扩充原有对象的行为
 * @author lvzb.software@qq.com
 *
 */
public class ConcreteDecoratorA extends Decorator {

    public ConcreteDecoratorA(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        super.operation();
        // 调用增强业务方法，对原有对象进行装饰、扩展、增强
        addedBehavior();
    }

    private void addedBehavior(){
        System.out.println("我是具体的装饰类A，我可以增强原有对象方法");
    }

}
```

5、角色：具体装饰类 ConcreteDecoratorB.java

```java
/**
 * 设计模式之 装饰模式<br/>
 * 角色：具体装饰类<br/>
 * 向构件添加新的具体的职责、扩充原有对象的行为
 * @author  lvzb.software@qq.com
 *
 */
public class ConcreteDecoratorB extends Decorator {

    public ConcreteDecoratorB(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        super.operation();
        // 调用增强业务方法，对原有对象进行装饰、扩展、增强
        addedOtherBehavior();
    }

    private void addedOtherBehavior(){
        System.out.println("我是具体的装饰类B，我也可以增强原有对象方法");
    }
}
```

6、客户端测试类 Client.java

```java
public class Client {

    public static void main(String[] args) {
        Component component,decoratorA;
        component = new ConcreteComponent();

        decoratorA = new ConcreteDecoratorA(component);
        // 对原有具体构件类ConcreteComponent的增强行为
        decoratorA.operation();

        System.out.println("---------------------------\n");

        System.out.println("---对原有构件对象进行 decoratorA、decoratorB 二次装饰---");
        // 如果需要对原有构件对象装饰后的decoratorA对象上进行再次装饰
        Component decoratorB;
        decoratorB = new ConcreteDecoratorB(decoratorA);
        decoratorB.operation();
    }

}
```

7、运行结果：

```
我是具体的构件类(被装饰对象)，这是我的原有方法
我是具体的装饰类A，我可以增强原有对象方法
---------------------------

---对原有构件对象进行 decoratorA、decoratorB 二次装饰---
我是具体的构件类(被装饰对象)，这是我的原有方法
我是具体的装饰类A，我可以增强原有对象方法
我是具体的装饰类B，我也可以增强原有对象方法
```




