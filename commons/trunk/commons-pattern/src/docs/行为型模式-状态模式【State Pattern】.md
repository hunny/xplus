# 行为型模式-状态模式【State Pattern】

## 概念

状态模式(State)：允许对象在内部状态改变时改变它的行为，对象看起来好像修改了它的类。状态模式说白了就是一个对象有不同的状态，不同的状态对应不同的行为，它其实是对switch case这样的语句的拓展。

```java
/** 
 * 示例：状态模式-- 一个对像有不同的状 态，不同的状态对应不同的行为 
 *  
 * 下面四则运算为例 
 */  
  
interface State {  
    public double operate(double num1, double num2);  
}  
  
/** 
 * 加法 
 */  
class AddOperator implements State {  
  
    @Override  
    public double operate(double num1, double num2) {  
        return num1 + num2;  
    }  
}  
  
/** 
 * 减法 
 */  
class SubOperator implements State {  
  
    @Override  
    public double operate(double num1, double num2) {  
        return num1 - num2;  
    }  
}  
  
/** 
 * 学生 
 */  
class Student {  
    private State state;  
  
    public Student(State state) {  
        this.state = state;  
    }  
  
    /** 
     * 设置状态 
     */  
    public void setState(State state) {  
        this.state = state;  
    }  
  
    public double operate(double num1, double num2) {  
        return state.operate(num1, num2);  
    }  
  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Student s1 = new Student(new AddOperator());  
        System.out.println(s1.operate(12, 23));  
        /** 
         * 改变状态，即改变了行为 --> 加法运算变成了减法运算 
         */  
        s1.setState(new SubOperator());  
        System.out.println(s1.operate(12, 23));  
    }  
} 
```

## 总结
封装基类状态的行为，并将行为委托到当前状态。