# 创建型模式-原型模式【Prototype Pattern】

## 一、概述： 

使用原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对象。简单的说就是对象的拷贝生成新的对象（对象的克隆），原型模式是一种对象创建型模式。

## 二、使用场景：

创建新的对象可以通过对已有对象进行复制来获得，如果是相似对象，则只需对其成员变量稍作修改。

## 四、参与者

(1)    Prototype（抽象原型类）：它是声明克隆方法的接口，是所有具体原型类的公共父类，可以是抽象类也可以是接口，甚至还可以是具体实现类。

(2)    ConcretePrototype（具体原型类）：它实现在抽象原型类中声明的克隆方法，在克隆方法中返回自己的一个克隆对象。

(3)    Client（客户类）：让一个原型对象克隆自身从而创建一个全新的对象。

## 五、用例学习：

1、抽象原型类：Prototype.java

```java
/** 
 * 抽象原型类 
 * @author   
 * 
 */  
public abstract class Prototype {  
      
    /** 
     * 提供抽象克隆方法 
     */  
    public abstract Prototype clone();  
  
} 
```

2、具体原型类：ConcretePrototypeA.java

```java
/** 
 * 具体原型类A 
 * @author   
 * 
 */  
public class ConcretePrototypeA extends Prototype {  
  
    /** 
     * 浅克隆 
     */  
    @Override  
    public Prototype clone() {  
        Prototype prototype = new ConcretePrototypeA();  
        return prototype;  
    }  
  
}
```

3、客户端测试类：Client.java

```java
public class Client {  
  
    public static void main(String[] args) {  
        Prototype prototypeA = new ConcretePrototypeA();  
        Prototype prototypeB = prototypeA.clone();  
          
        System.out.println(prototypeB.equals(prototypeA));   // return false  
        System.out.println(prototypeB == prototypeA);   // return false  
        System.out.println(prototypeB.getClass() == prototypeA.getClass());  // return true  
  
    }  
  
} 
```

这里我们可以看到 prototypeA对象克隆了一个对象prototypeB，但是prototypeA  != prototypeB， 说明prototypeB是一个全新的Prototype对象。

注意：原型模式通过克隆方法所创建的对象是全新的对象，它们在内存中拥有新的地址，通常对克隆所产生的对象进行修改对原型对象不会造成任何影响，每一个克隆对象都是相互独立的。

## 六、扩展：

关于浅克隆与深克隆的简单介绍：

(1) 在Java语言中，数据类型分为值类型（基本数据类型）和引用类型，值类型包括int、double、byte、boolean、char等简单数据类型，引用类型包括类、接口、数组等复杂类型。如下Person对象：

```java
public class Person {  
// 姓名  
private String name;  
// 年龄  
private int age;  
// 他的父亲  
private Father father;  
} 
```

name、age 为基本数据类型，father就为引用类型。

浅克隆和深克隆的主要区别在于是否支持引用类型的成员变量的复制

(2)浅克隆：


在Java语言中，通过覆盖Object类的clone()方法就是实现浅克隆，在浅克隆中，当对象被复制时只复制它本身和其中包含的值类型的成员变量，而引用类型的成员对象并没有复制，也就是说原型对象只是将引用对象的地址复制一份给克隆对象，克隆对象和原型对象的引用类型成员变量还是指向相同的内存地址。

注意：能够实现克隆的Java类必须实现一个标识接口Cloneable，表示这个Java类支持被复制。如果一个类没有实现这个接口但是调用了clone()方法，Java编译器将抛出一个CloneNotSupportedException异常。

用代码说话：

1、引用对象：Father.java

```java
public class Father{  
    // 姓名  
    private String name;  
    // 年龄  
    private int age;  
      
    public Father(String name, int age) {  
        this.name = name;  
        this.age = age;  
    }  
      
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getAge() {  
        return age;  
    }  
    public void setAge(int age) {  
        this.age = age;  
    }  
      
} 
```

2、克隆原型类: Person.java

```java
public class Person implements Cloneable{  
    // 姓名  
    private String name;  
    // 年龄  
    private int age;  
    // 他的父亲  
    private Father father;  
  
    /** 
     * 重写 Object对象的clone方法实现Person对象的克隆 
     */  
    public Person clone(){  
        Object obj = null;  
        try {  
            obj = super.clone();  
            return (Person)obj;  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
      
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public int getAge() {  
        return age;  
    }  
  
    public void setAge(int age) {  
        this.age = age;  
    }  
  
    public Father getFather() {  
        return father;  
    }  
  
    public void setFather(Father father) {  
        this.father = father;  
    }  
}  
```

3、测试类：CloneClient.java

```java
public class CloneClient {  
  
    public static void main(String[] args) {  
        Father father = new Father("老子", 50);  
        Person son = new Person();  
        son.setName("儿子");  
        son.setAge(24);  
        son.setFather(father);  
          
        // 浅克隆出一个兄弟Person对象  
        Person brother = son.clone();  
        System.out.println(brother == son);  // return false  
        System.out.println(brother.getFather() == son.getFather());  // return true  
    }  
  
}  
```

以上 我们可以分析看到son 浅克隆出一个"兄弟"对象 brother，但是他们的引用对象"父亲"都是同一个对象，所有事实证明浅克隆没有对引用类型对象进行复制。


(3)深克隆：

在深克隆中，无论原型对象的成员变量是值类型还是引用类型，都将复制一份给克隆对象，简单来说，在深克隆中，除了对象本身被复制外，对象所包含的所有成员变量也将复制。

那么如何实现深克隆呢？

在Java语言中，如果需要实现深克隆，可以通过序列化(Serialization)等方式来实现。序列化就是将对象写到流的过程，写到流中的对象是原有对象的一个拷贝，而原对象仍然存在于内存中。通过序列化实现的拷贝不仅可以复制对象本身，而且可以复制其引用的成员对象，因此通过序列化将对象写到一个流中，再从流里将其读出来，可以实现深克隆。需要注意的是能够实现序列化的对象其类必须实现Serializable接口，否则无法实现序列化操作。

用代码说话：

1、引用类：Father.java

```java
import java.io.Serializable;  
  
public class Father implements Serializable{  
    // 姓名  
    private String name;  
    // 年龄  
    private int age;  
      
    public Father(String name, int age) {  
        this.name = name;  
        this.age = age;  
    }  
      
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getAge() {  
        return age;  
    }  
    public void setAge(int age) {  
        this.age = age;  
    }  
      
}  
```

2、克隆原型类: Person.java

```java
import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.IOException;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.io.OptionalDataException;  
import java.io.Serializable;  
  
public class Person implements Serializable{  
    // 姓名  
    private String name;  
    // 年龄  
    private int age;  
    // 他的父亲  
    private Father father;  
      
    /** 
     * 深克隆 
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws OptionalDataException 
     */  
    public Person deepClone() throws IOException, ClassNotFoundException, OptionalDataException  
    {  
           //将对象写入流中  
           ByteArrayOutputStream bao=new  ByteArrayOutputStream();  
           ObjectOutputStream oos=new  ObjectOutputStream(bao);  
           oos.writeObject(this);  
            
           //将对象从流中取出  
           ByteArrayInputStream bis=new  ByteArrayInputStream(bao.toByteArray());  
           ObjectInputStream ois=new  ObjectInputStream(bis);  
           return  (Person) ois.readObject();  
    }  
      
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public int getAge() {  
        return age;  
    }  
  
    public void setAge(int age) {  
        this.age = age;  
    }  
  
    public Father getFather() {  
        return father;  
    }  
  
    public void setFather(Father father) {  
        this.father = father;  
    }  
}  
```

3、深克隆测试类：DeepCloneClient.java

```java
public class DeepCloneClient {  
  
    public static void main(String[] args) {  
        Father father = new Father("老子", 50);  
        Person son = new Person();  
        son.setName("儿子");  
        son.setAge(24);  
        son.setFather(father);  
          
        try {  
            Person brother = son.deepClone();  
            System.out.println(brother == son);  // false  
            System.out.println(brother.getFather() == son.getFather());  // false  
              
        } catch (OptionalDataException e) {  
            e.printStackTrace();  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
    }  
  
}  
```

以上我们可以分析看到通过深克隆出来的"兄弟"对象brother 和 son 不仅不等、就连他们的引用类型Father也不等啦。所有证明：通过深克隆 克隆出了一个完全独立的全新的对象。


## 概念

原型模式(Prototype)：该模式的思想就是将一个对象作为原型，对其进行复制、克隆，产生一个和原对象类似的新对象。而这里的复制有两种：浅复制、深复制。

浅复制：将一个对象复制后，基本数据类型的变量都会重新创建，而引用类型，指向的还是原对象所指向的。

深复制：将一个对象复制后，不论是基本数据类型还有引用类型，都是重新创建的。简单来说，就是深复制进行了完全彻底的复制，而浅复制不彻底。

## 浅复制

```java
/** 
 * 原型模式：将一个对象作为原型，对其进行复制、克隆，产生一个和原对象类似的【新对象】。 
 *  
 * 而这里的复制有两种：浅复制、深复制 
 *  
 * 示例(一) 浅复制：将一个对象复制后，基本数据类型的变量都会重新创建， 
 *  
 * 而引用类型，指向的还是原对象所指向的，【不会重新创建】。 
 */  
class Prototype implements Cloneable {  
    private int age;  
    private int[] array = new int[] { 1, 2, 3 };  
  
    public Prototype() {  
    }  
  
    public Prototype(int age) {  
        this.age = age;  
    }  
  
    public int getAge() {  
        return age;  
    }  
  
    public void setAge(int age) {  
        this.age = age;  
    }  
  
    public int[] getArray() {  
        return array;  
    }  
  
    public void setArray(int[] array) {  
        this.array = array;  
    }  
  
    /** 
     * 因为Cloneable接口是个空接口 
     *  
     * 此处的重点是super.clone()这句话，super.clone()调用的是Object的clone()方法，而在Object类中，clone()是native的 
     *  
     * 这就涉及到JNI，关于JNI还有NDK以后会讲到，这里你只要记住浅复制的核心是super.clone()。 
     */  
    public Object cloneObject() throws CloneNotSupportedException {  
        Prototype prototype = (Prototype) super.clone();  
        return prototype;  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) throws CloneNotSupportedException {  
        Prototype prototype = new Prototype(20);  
        Prototype cloneProto = (Prototype) prototype.cloneObject();  
        /** 
         * 通过打印可以看到：prototype和cloneProto这两个同一类型的变量指向的是两个不同的内存地址 
         *  
         * 这说明克隆成功 
         */  
        System.out.println("prototype = " + prototype);  
        System.out.println("cloneProto = " + cloneProto);  
        /** 
         * 要完全复制一个对象的话，那么它的引用类型变量array指向的肯定是不同的内存地址 
         *  
         * 而这里的引用类型变量array，指向的还是原对象所指向的。可以看到打印的内存地址是相同的。 
         *  
         * 这说明对象复制不彻底 
         */  
        System.out.println("prototype.getArray() = " + prototype.getArray());  
        System.out.println("cloneProto.getArray() = " + cloneProto.getArray());  
        /** 
         * 透过这个例子可以看到：浅复制并没有将对象进行完全复制 
         */  
    }  
}  
```

## 深复制

```java
/** 
 * 示例(二) 深复制：将一个对象复制后，不论是基本数据类型还有引用类型，都是【重新创建】的。 
 *  
 * 简单来说，就是深复制进行了完全彻底的复制，而浅复制不彻底。 
 *  
 * 由于这里涉及到对对象的读写，所以这里用到了对象的序列化--实现了Serializable接口 
 */  
class Prototype implements Cloneable, Serializable {  
    private static final long serialVersionUID = 1L;  
    private int age;  
    private int[] array = new int[] { 1, 2, 3 };  
  
    public Prototype() {  
    }  
  
    public Prototype(int age) {  
        this.age = age;  
    }  
  
    public int getAge() {  
        return age;  
    }  
  
    public void setAge(int age) {  
        this.age = age;  
    }  
  
    public int[] getArray() {  
        return array;  
    }  
  
    public void setArray(int[] array) {  
        this.array = array;  
    }  
  
    /* 深复制 */  
    public Object deepClone() throws IOException, ClassNotFoundException {  
  
        /* 写入当前对象的二进制流 */  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        ObjectOutputStream oos = new ObjectOutputStream(bos);  
        oos.writeObject(this);  
  
        /* 读出二进制流产生的新对象 */  
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());  
        ObjectInputStream ois = new ObjectInputStream(bis);  
        return ois.readObject();  
    }  
  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) throws IOException,  
            ClassNotFoundException {  
        Prototype prototype = new Prototype(20);  
        Prototype cloneProto = (Prototype) prototype.deepClone();  
        /** 
         * 通过打印可以看到：prototype和cloneProto这两个同一类型的变量指向的是两个不同的内存地址 
         *  
         * 这说明克隆成功 
         */  
        System.out.println("prototype = " + prototype);  
        System.out.println("cloneProto = " + cloneProto);  
        /** 
         * 通过打印可以看到，两个对象的引用类型变量array指向的是不同的内存地址 
         *  
         * 这说明对象进行了完全彻底的复制 
         */  
        System.out.println("prototype.getArray() = " + prototype.getArray());  
        System.out.println("cloneProto.getArray() = " + cloneProto.getArray());  
  
        /** 
         * 当然我们也可以试着打印一下引用变量的内容， 
         *  
         * 可以看到：内容是不变的(1 2 3)，改变的只是引用变量指向的内存地址。 
         */  
        int[] proArray = prototype.getArray();  
        int[] cloneProtoArray = cloneProto.getArray();  
        for (int p : proArray) {  
            System.out.print(p + "\t");  
        }  
        System.out.println();  
        for (int p : cloneProtoArray) {  
            System.out.print(p + "\t");  
        }  
  
    }  
} 
```

## 总结
1、浅复制的核心是super.clone()，它调用的是Object的clone()方法，而在Object类中，clone()是native的。
2、要实现深复制，需要采用二进制流的形式写入当前对象，再对其进行读取。