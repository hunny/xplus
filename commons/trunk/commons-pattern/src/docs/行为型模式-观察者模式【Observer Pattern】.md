# 行为型模式-观察者模式【Observer Pattern】

## 概念

观察者(Observer)模式定义：在对象之间定义了一对多的依赖关系，这样一来，当一个对象改变状态时，依赖它的对象都会收到通知并自动跟新。Java已经提供了对观察者Observer模式的默认实现， Java对观察者模式的支持主要体现在Observable类和Observer接口。

```java
/** 示例：咱们去菜市场买菜 
 *  
 * 小商贩--主题 
 */  
class Food extends Observable {  
    /** 菜名 */  
    private String name;  
    /** 菜价 */  
    private float price;  
  
    public Food(String name, float price) {  
        this.name = name;  
        this.price = price;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public float getPrice() {  
        return price;  
    }  
  
    public void setPrice(float price) {  
        this.price = price;  
        /** 
         * 设置菜价的状态已经被改变 
         */  
        this.setChanged();  
        /** 
         * 通知【所有】正在看菜(已经注册了)的顾客，然后回调Observer的update方法进行更新 
         *  
         * 这里可以体现对象的一对多：一个小商贩一旦更新价格(即一个对象改变状态)，便会自动通知所有的顾客(依赖它的对象都会收到通知) 
         * 并自动update 
         */  
        this.notifyObservers(price);  
    }  
  
}  
  
/** 
 * 顾客 --观察者 
 */  
class Customer implements Observer {  
    private String name;  
  
    public Customer(String name) {  
        this.name = name;  
    }  
  
    public String getName() {  
        return name;  
    }  
  
    @Override  
    public void update(Observable o, Object arg) {  
        if (o instanceof Food && arg instanceof Float) {  
            Food food = (Food) o;  
            float price = (Float) arg;  
            System.out.println("您好：" + this.name + " ," + food.getName()  
                    + "的价格已经发生改变，现在的价格为：" + price + "元/斤");  
        }  
    }  
  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Food food = new Food("土豆", 1.0f);  
        Customer zhangsan = new Customer("张三");  
        Customer lisi = new Customer("李四");  
        /** 
         * 添加顾客 
         */  
        food.addObserver(zhangsan);  
        food.addObserver(lisi);  
        /** 
         * 更新价格 
         */  
        food.setPrice(1.5f);  
    }  
} 
```

