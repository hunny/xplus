# 创建型模式-原型模式【Prototype Pattern】

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