# 结构型模式-适配器模式【Adapter Pattern】

[原文地址](http://blog.csdn.net/lhy_ycu/article/details/39805069)

## 概念

适配器模式(Adapter)：将某个类的接口转换成客户端期望的另一个接口表示，目的是消除由于接口不匹配所造成的类的兼容性问题。
主要分为三类：类的适配器模式、对象的适配器模式、接口的适配器模式。

## 一、类的适配器模式

代码示例：

```java
/** 
 * 示例(一)：类的适配器模式 
 *  
 * 原类拥有一个待适配的方法originMethod 
 */  
class Original {  
    public void originMethod() {  
        System.out.println("this is a original method...");  
    }  
}  
  
interface Targetable {  
    /** 
     * 与原类的方法相同 
     */  
    public void originMethod();  
  
    /** 
     * 目标类的方法 
     */  
    public void targetMethod();  
}  
  
/** 
 * 该Adapter类的目的：将Original类适配到Targetable接口上 
 */  
class Adapter extends Original implements Targetable {  
    /** 
     * 可以看到该类只需要实现targetMethod即可。 
     *  
     * 因为Targetable接口里的originMethod方法已经由Original实现了。 
     *  
     * 这就是Adapter适配器这个类的好处：方法实现的转移(或称嫁接) --> 将Adapter的责任转移到Original身上 
     *  
     * 这样就实现了类适配器模式 --> 将Original类适配到Targetable接口上 
     *  
     * 如果Original又添加了一个新的方法originMethod2，那么只需在Targetable接口中声明即可。 
     */  
    @Override  
    public void targetMethod() {  
        System.out.println("this is a target method...");  
    }  
  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Targetable target = new Adapter();  
        target.originMethod();  
        target.targetMethod();  
    }  
} 
```

## 二、对象的适配器模式

```java
/** 
 * 示例(二)：对象的适配器模式 
 *  
 * 原类拥有一个待适配的方法originMethod 
 */  
class Original {  
    public void originMethod() {  
        System.out.println("this is a original method...");  
    }  
}  
  
interface Targetable {  
    /** 
     * 与原类的方法相同 
     */  
    public void originMethod();  
  
    /** 
     * 目标类的方法 
     */  
    public void targetMethod();  
}  
  
/** 
 * 持有Original类的实例 
 */  
class Adapter implements Targetable {  
    private Original original;  
  
    public Adapter(Original original) {  
        this.original = original;  
    }  
  
    @Override  
    public void targetMethod() {  
        System.out.println("this is a target method...");  
    }  
  
    @Override  
    public void originMethod() {  
        original.originMethod();  
    }  
  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Original original = new Original();  
        Targetable target = new Adapter(original);  
        target.originMethod();  
        target.targetMethod();  
    }  
} 
```

## 三、接口的适配器模式

```java
/** 
 * 示例(三)：接口的适配器模式 
 *  
 * 这次咱们直接将原类做成一个接口 --> 原始接口 
 */  
interface Originable {  
    public void originMethod1();  
  
    public void originMethod2();  
}  
  
/** 
 * 该抽象类实现了原始接口，实现了所有的方法。 
 *  
 * 空实现即可，具体实现靠子类，子类只需实现自身需要的方法即可。 
 *  
 * 以后咱们就不用跟原始的接口打交道，只和该抽象类取得联系即可。 
 */  
abstract class Adapter implements Originable {  
    public void originMethod1() {  
  
    }  
  
    public void originMethod2() {  
  
    }  
}  
  
/** 
 * 子类只需选择你所需要的方法进行实现即可 
 */  
class OriginSub1 extends Adapter {  
    @Override  
    public void originMethod1() {  
        System.out.println("this is Originable interface's first sub1...");  
    }  
  
    /** 
     * 此时：originMethod2方法默认空实现 
     */  
}  
  
class OriginSub2 extends Adapter {  
    /** 
     * 此时：originMethod1方法默认空实现 
     */  
  
    @Override  
    public void originMethod2() {  
        System.out.println("this is Originable interface's second sub2...");  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Originable origin1 = new OriginSub1();  
        Originable origin2 = new OriginSub2();  
        origin1.originMethod1();  
        origin1.originMethod2();  
        origin2.originMethod1();  
        origin2.originMethod2();  
    }  
}  
```

## 四、总结

1、类的适配器模式：当希望将一个类转换成满足另一个新接口的类时，可以使用类的适配器模式，创建一个新类，继承原有的类，实现新的接口即可。
2、对象的适配器模式：当希望将一个对象转换成满足另一个新接口的对象时，可以创建一个Adapter类，持有原类的一个实例，在Adapter类的方法中，调用实例的方法就行。
3、接口的适配器模式：当不希望实现一个接口中所有的方法时，可以创建一个抽象类Adapter实现所有方法，我们写别的类的时候，继承抽象类即可。




