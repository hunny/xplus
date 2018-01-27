# 行为模式-迭代器模式【Iterator Pattern】

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