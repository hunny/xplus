# 行为模式-模板方法模式【Template Pattern】

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

