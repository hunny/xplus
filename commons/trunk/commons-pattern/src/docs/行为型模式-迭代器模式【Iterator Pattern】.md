# 行为模式-迭代器模式【Iterator Pattern】

## 一、概述

提供一种方法来访问聚合对象(容器container)，而不用暴露这个对象的内部细节。属于对象行为型模式。

## 二、适用场景

1>遍历访问聚合对象中的元素，而无须暴露它的内容表示，将聚合对象的访问和内部数据的存储分离。使得访问聚合对象时无须了解其内部的实现细节。

2>需要为一个聚合对象提供多种遍历实现。

## 四、参与者

 1>Iterator（抽象迭代器）：它定义了访问和遍历元素的接口，声明了用于遍历数据元素的方法，例如：用于获取第一个元素的first()方法，用于访问下一个元素的next()方法，用于判断是否还有下一个元素的hasNext()方法，用于获取当前元素的currentItem()方法等，在具体迭代器中将实现这些方法。

 2>ConcreteIterator（具体迭代器）：它实现了抽象迭代器接口，完成对聚合对象的遍历，同时在具体迭代器中通过游标来记录在聚合对象中所处的当前位置，在具体实现时，游标通常是一个表示位置的非负整数。

 3>Aggregate（抽象聚合类）：它用于存储和管理元素对象，声明一个createIterator()方法用于创建一个迭代器对象，充当抽象迭代器工厂角色。

 4>ConcreteAggregate（具体聚合类）：它实现了在抽象聚合类中声明的createIterator()方法，该方法返回一个与该具体聚合类对应的具体迭代器ConcreteIterator实例。
 
## 五、用例学习

1、抽象迭代器 Iterator.java

```java
/** 
 * 自定义迭代器接口<br/> 
 * <b>说明：</b> 
 * 此处没有使用JDK内置的迭代器接口 java.util.Iterator<E> 
 * @author   
 * 
 */  
public interface Iterator {  
    /** 将游标指向第一个元素  */  
    public Object first();  
    /** 将游标指向下一个元素  */  
    public Object next();  
    /** 判断是否存在下一个元素  */  
    public boolean hasNext();  
    /** 返回游标指向的当前元素  */  
    public Object currentItem();  
}  
```

2、具体迭代器 ConcreteIterator.java

```java
/** 
 * 具体迭代器实现类<br/> 
 * 访问聚合对象、对聚合对象内部元素遍历 
 * @author   
 * 
 */  
public class ConcreteIterator implements Iterator {  
    // 维持一个对具体聚合对象的引用，以便于访问存储在聚合对象中的数据    
    private Aggregate aggregate;  
    // 定义一个游标，用于记录当前访问位置    
    private int cursorIndex;   
  
    public ConcreteIterator(Aggregate aggregate){  
        this.aggregate = aggregate;  
    }  
      
    @Override  
    public Object first() {  
        cursorIndex = 0;  
        return aggregate.getObjects().get(cursorIndex);  
    }  
  
    @Override  
    public Object next() {  
        cursorIndex++ ;  
        if(hasNext()){  
            return aggregate.getObjects().get(cursorIndex);  
        }   
        return aggregate.getObjects().get(0);  
    }  
  
    @Override  
    public boolean hasNext() {  
        if (cursorIndex < aggregate.getObjects().size()) {  
            return true;  
        }  
        return false;  
    }  
  
    @Override  
    public Object currentItem() {  
        return aggregate.getObjects().get(cursorIndex);  
    }  
  
}  
```

3、抽象聚合类 Aggregate.java

```java
import java.util.ArrayList;  
import java.util.List;  
  
/** 
 * 抽象聚合对象 
 * @author   
 * 
 */  
public abstract class Aggregate {  
      
    /** 创建迭代器  具体创建什么样迭代方式的迭代器由具体的子类去实现 */  
    public abstract Iterator createIterator();  
      
    protected List<Object> objects = new ArrayList<Object>();    
      
    public Aggregate(List objects) {    
        this.objects = objects;    
    }   
      
    public void addObject(Object obj){  
        objects.add(obj);  
    }  
      
    public void deleteObject(Object obj){  
        objects.remove(obj);  
    }  
      
    public List<Object> getObjects(){  
        return objects;  
    }  
  
} 
```

4、具体聚合类 ConcreteAggregate.java

```java
import java.util.List;  
  
/** 
 * 具体聚合对象 
 * @author   
 * 
 */  
public class ConcreteAggregate extends Aggregate {  
  
    public ConcreteAggregate(List objects) {  
        super(objects);  
    }  
  
    /** 
     * 提供工厂方法 创建具体的迭代器实例<br/> 
     * 由迭代器去执行具体的聚合对象的遍历访问<br/> 
     * 这样就将聚合对象的数据存储 和 对聚合对象元素的访问进行了分离 
     */  
    @Override  
    public Iterator createIterator() {  
        return new ConcreteIterator(this);  
    }  
  
}  
```

5、客户端测试类 Client.java

```java
import java.util.ArrayList;  
import java.util.List;  
  
public class Client {  
  
    public static void main(String[] args){  
        List<String> nameList = new ArrayList<String>();  
        nameList.add("Java");  
        nameList.add("C");  
        nameList.add("C++");  
          
        // 实例化具体聚合对象 且 创建初始化集合数据  
        ConcreteAggregate languageAggregate = new ConcreteAggregate(nameList);  
        // 获取聚合对象关联的迭代器  
        Iterator iterator = languageAggregate.createIterator();  
          
        // 通过迭代器访问聚合对象的内部数据  
        String firstLanguage = (String) iterator.first();  // 访问聚合对象集合中索引为1的元素   
        System.out.println(firstLanguage);  
          
        boolean hasNext = iterator.hasNext();  
        System.out.println("是否还有下一个元素：" + hasNext);  
          
        if(hasNext){  
            String nextLanguage = (String) iterator.next();   
            System.out.println("下一个元素：" + nextLanguage);  
        }  
    }  
      
}
```

6、运行效果

```
Java  
是否还有下一个元素：true  
下一个元素：C 
```

## 六、其他/扩展
Java JDK内置的迭代器：
说到迭代器模式 给我们的第一联想就是Java中 我们使用最最频繁的java.util.Iterator接口啦。
没错 他就是JDK中内置的迭代器。public interfaceIterator<E> 对collection进行迭代的迭代器。
这里想扩展的一个点是：关于接口Iterator的子接口ListIterator<E>的介绍
这个ListIterator子接口 也许我们平时的代码中使用的少，那么他有什么功能呢？

Iterator只能进行单向遍历，而ListIterator可以进行双向遍历(向前/向后),且可以在迭代期间修改列表。

## 概念

迭代器模式(Iterator)：提供了一种方法顺序访问一个聚合对象中的各个元素，而又不暴露其内部的表示。

```java
/** 
 * 示例：迭代器模式 
 *  
 */  
  
interface Iterator {  
    /** 前移 */  
    public Object previous();  
  
    /** 后移 */  
    public Object next();  
  
    /** 判断是否有下一个元素 */  
    public boolean hasNext();  
}  
  
interface Collection {  
    public Iterator iterator();  
  
    /** 取得集合中的某个元素 */  
    public Object get(int i);  
  
    /** 取得集合大小 */  
    public int size();  
}  
  
/** 
 * 集合 
 */  
class MyCollection implements Collection {  
    private String[] strArray = { "aa", "bb", "cc", "dd" };  
  
    @Override  
    public Iterator iterator() {  
        return new MyIterator(this);  
    }  
  
    @Override  
    public Object get(int i) {  
        return strArray[i];  
    }  
  
    @Override  
    public int size() {  
        return strArray.length;  
    }  
  
}  
  
/** 
 * 迭代器 
 */  
class MyIterator implements Iterator {  
    private Collection collection;  
    private int pos = -1;  
  
    public MyIterator(Collection collection) {  
        this.collection = collection;  
    }  
  
    @Override  
    public Object previous() {  
        if (pos > 0) {  
            pos--;  
        }  
        return collection.get(pos);  
    }  
  
    @Override  
    public Object next() {  
        if (pos < collection.size() - 1) {  
            pos++;  
        }  
        return collection.get(pos);  
    }  
  
    @Override  
    public boolean hasNext() {  
        if (pos < collection.size() - 1) {  
            return true;  
        }  
        return false;  
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
         * 实例化容器 
         */  
        Collection collection = new MyCollection();  
        /** 
         * 创建迭代器 
         */  
        Iterator iterator = collection.iterator();  
        /** 
         * 遍历集合中的元素 
         */  
        while (iterator.hasNext()) {  
            System.out.println(iterator.next());  
        }  
    }  
}  
```

## 应用场景
遍历、访问集合中的某个元素等

## 总结
迭代器模式就是顺序访问集合中的对象，这句话包含两层意思：一是需要遍历的对象，即集合对象，二是迭代器对象，用于对集合对象进行遍历访问。