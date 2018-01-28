# 行为模式-模板方法模式【Template Pattern】

## 一、概述

定义一个操作中算法的框架，而将一些步骤延迟到子类中。模板方法模式使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。它是一种类行为型模式。

## 二、适用场景

适用于对一些复杂的操作/算法进行步骤分割、抽取公共部分由抽象父类实现、将不同部分在父类模板方法中定义抽象实现、而将具体的实现过程由子类完成。

## 四、参与者

1>、AbstractClass（抽象类）:提供模板方法、并且定义算法框架。
2>、ConcreteClass（具体子类）：它是抽象类的子类，用于实现在父类中声明的抽象基本操作以完成子类特定算法的步骤，也可以覆盖在父类中已经实现的具体基本操作。

## 五、用例学习

1、抽象父类：AbstractClass.java

```java
/** 
 * JAVA设计模式之 模板方法模式 
 * 抽象类:提供模板方法 
 * @author   
 * 
 */  
public abstract class AbstractClass {  
      
    /** 
     * 模板方法<br/> 
     * 定义/封装复杂流程/操作的实现步骤和执行次序 
     */  
    public void TemplateMethod(){  
        Operation1();  
        Operation2();  
        Operation3();  
    }  
      
    /** 
     * 算法/操作一<br/> 
     * 公用的实现逻辑 
     */  
    protected void Operation1() {  
        // 完成操作一的具体算法/逻辑实现  
    }  
      
    /** 
     * 算法/操作二<br/> 
     * 提供抽象方法，具体的算法/逻辑实现由子类去完成 
     */  
    protected abstract void Operation2();  
      
    /** 
     * 算法/操作三<br/> 
     * 提供抽象方法，具体的算法/逻辑实现由子类去完成 
     */  
    protected abstract void Operation3();  
  
}  
```

2、具体子类：ConcreteClass.java

```java
/** 
 * 具体子类<br/> 
 * 实现抽象类的抽象方法，完成具体的算法/逻辑 
 * @author   
 * 
 */  
public class ConcreteClass extends AbstractClass {  
  
    @Override  
    protected void Operation2() {  
        // 实现父类中模板方法中的步骤二的具体算法/逻辑  
    }  
  
    @Override  
    protected void Operation3() {  
        // 实现父类中模板方法中的步骤三的具体算法/逻辑  
    }  
  
}  
```

## 六、其他

模板方法模式是结构最简单的行为型设计模式，在其结构中只存在父类与子类之间的继承关系。通过使用模板方法模式，可以将一些复杂流程的实现步骤封装在一系列基本方法中，在抽象父类中提供一个称之为模板方法的方法来定义这些基本方法的执行次序，而通过其子类来覆盖某些步骤，从而使得相同的算法框架可以有不同的执行结果。模板方法模式提供了一个模板方法来定义算法框架，而某些具体步骤的实现可以在其子类中完成。

## 概念

模板方法模式(Template Method)：在一个方法中定义了一个算法的骨架，而将一些步骤延迟到子类中。模板方法使得子类可以再不改变算法结构的情况下，重新定义算法中的某些步骤。简而言之：模板方法定义了一个算法的步骤，并允许子类为一个或多个步骤提供实现。

```java
/** 
 * 示例：模板方法定义了一个算法的步骤，并允许子类为一个或多个步骤提供实现。 
 *  
 * 以吃饭为例：有几个步骤 --> 煮饭+烧菜+吃饭中+吃完了 
 */  
abstract class AbstractTemplate {  
  
    public final void haveMeal() {  
        zhuFan();  
        shaoCai();  
        duringMeal();  
        finish();  
    }  
  
    public abstract void zhuFan();  
  
    public abstract void shaoCai();  
  
    public void duringMeal() {  
        System.out.println("吃饭中...");  
    }  
  
    public void finish() {  
        System.out.println("吃完了...");  
    }  
}  
  
class Sub1 extends AbstractTemplate {  
  
    @Override  
    public void zhuFan() {  
        System.out.println("使用电饭煲煮饭...");  
  
    }  
  
    @Override  
    public void shaoCai() {  
        System.out.println("使用炉灶烧菜...");  
    }  
}  
  
class Sub2 extends AbstractTemplate {  
  
    @Override  
    public void zhuFan() {  
        System.out.println("使用高压锅煮饭...");  
  
    }  
  
    @Override  
    public void shaoCai() {  
        System.out.println("使用电磁炉烧菜...");  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        AbstractTemplate at1 = new Sub1();  
        at1.haveMeal();  
        System.out.println("\n");  
        AbstractTemplate at2 = new Sub2();  
        at2.haveMeal();  
    }  
}  
```

## 总结
模板方法模式：一个抽象类中，有一个主方法，再定义1...n个方法，可以抽象，可以不抽象，定义子类继承该抽象类，重写抽象方法，通过调用抽象类，实现对子类的调用。

