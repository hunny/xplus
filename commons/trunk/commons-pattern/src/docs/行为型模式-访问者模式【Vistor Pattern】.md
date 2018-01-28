# 行为型模式-访问者模式【Vistor Pattern】

## 一、概述

访问者模式是一种较为复杂的行为型设计模式，它包含访问者和被访问元素两个主要组成部分，这些被访问的元素通常具有不同的类型，且不同的访问者可以对它们进行不同的访问操作。在使用访问者模式时，被访问元素通常不是单独存在的，它们存储在一个集合中，这个集合被称为“对象结构”，访问者通过遍历对象结构实现对其中存储的元素的逐个操作。访问者模式是一种对象行为型模式。


## 二、适用场景

当有多种类型的访问者(或是操作者) 对一组被访问者对象集合（或是对象结构）进行操作（其中对象集合也包含多种类型对象），不同的访问者类型对每一种具体的被访问者对象提供不同的访问操作，每种访问者类型对象对不同的被访问者也有不同的访问操作，那么这种场景就非常适用访问者模式。

如果前面这几句比较绕的文字说明没看明白，那么就举例一个生活中的业务场景：

你所在的公司每个月人力资源部要对所有员工进行上班时长、加班时长统计，而财务部要对所有员工进行工资核算，不同职位的员工薪资核算标准肯定不一样啊，这个大家都明白。在这个案例中人力资源部和财务部是两个不同类型的部门（访问者），所有员工（被访问者）是一个对象集合，而员工又划分为管理者和技术者两种类型（备注：这里只是简单划分为两类），在每月的统计中，人力资源部需要分别对员工进行上班时长和加班时长进行统计，而财务部需要对不同职位的员工进行薪资核算，可见不同部门职责不同，及对员工的访问操作不同、每个部门对不同类型的员工的访问操作也不同。那么针对这种场景  我们有必要了解一下访问者模式。

## 四、参与者

1>、Visitor(抽象访问者)：为每种具体的被访问者(ConcreteElement)声明一个访问操作;
2>、ConcreteVisitor(具体访问者)：实现对被访问者(ConcreteElement)的具体访问操作;
3>、Element(抽象被访问者)：通常有一个Accept方法，用来接收/引用一个抽象访问者对象;
4>、ConcreteElement(具体被访问者对象)：实现Accept抽象方法，通过传入的具体访问者参数、调用具体访问者对该对象的访问操作方法实现访问逻辑;
5>、Clent、ObjectStructure(客户端访问过程测试环境)：该过程中，被访问者通常为一个集合对象，通过对集合的遍历完成访问者对每一个被访问元素的访问操作;

## 五、用例学习

1.抽象被访问者：公司员工抽象类  Employee.java

```java
/** 
 * 公司员工（被访问者）抽象类 
 * @author   
 * 
 */  
public abstract class Employee {  
      
    /** 
     * 接收/引用一个抽象访问者对象 
     * @param department 抽象访问者 这里指的是公司部门如 人力资源部、财务部 
     */  
    public abstract void accept(Department department);  
  
}  
```

2.具体被访问者：公司管理岗位员工类 ManagerEmployee.java

```java
/** 
 * 公司员工：管理者（具体的被访问者对象） 
 * @author   
 *  
 */  
public class ManagerEmployee extends Employee {  
    // 员工姓名  
    private String name;  
    // 每天上班时长  
    private int timeSheet;   
    // 每月工资  
    private double wage;  
    // 请假/迟到 惩罚时长  
    private int punishmentTime;  
      
    public ManagerEmployee(String name, int timeSheet, double wage, int punishmentTime) {  
        this.name = name;  
        this.timeSheet = timeSheet;  
        this.wage = wage;  
        this.punishmentTime = punishmentTime;  
    }  
  
      
    @Override  
    public void accept(Department department) {  
        department.visit(this);  
    }  
      
      
    /** 
     * 获取每月的上班实际时长 = 每天上班时长 * 每月上班天数 - 惩罚时长 
     * @return 
     */  
    public int getTotalTimeSheet(){  
        return timeSheet * 22 - punishmentTime;  
    }  
      
      
    /** 
     * 获取每月实际应发工资 = 每月固定工资 - 惩罚时长 * 5<br/> 
     * <作为公司管理者 每迟到1小时 扣5块钱> 
     * @return 
     */  
    public double getTotalWage(){  
        return wage - punishmentTime * 5;  
    }  
      
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public double getWage() {  
        return wage;  
    }  
  
    public void setWage(double wage) {  
        this.wage = wage;  
    }  
      
    public int getPunishmentTime() {  
        return punishmentTime;  
    }  
  
    public void setPunishmentTime(int punishmentTime) {  
        this.punishmentTime = punishmentTime;  
    }  
      
}  
```

3.具体被访问者：公司普通岗位员工类 GeneralEmployee.java

```java
/** 
 * 公司普通员工（具体的被访问者对象） 
 * @author   
 * 
 */  
public class GeneralEmployee extends Employee {  
    // 员工姓名  
    private String name;  
    // 每天上班时长  
    private int timeSheet;  
    // 每月工资  
    private double wage;  
    // 请假/迟到 惩罚时长  
    private int punishmentTime;  
  
    public GeneralEmployee(String name, int timeSheet, double wage, int punishmentTime) {  
        this.name = name;  
        this.timeSheet = timeSheet;  
        this.wage = wage;  
        this.punishmentTime = punishmentTime;  
    }  
  
    @Override  
    public void accept(Department department) {  
        department.visit(this);  
    }  
  
    /** 
     * 获取每月的上班实际时长 = 每天上班时长 * 每月上班天数 - 惩罚时长 
     * @return 
     */  
    public int getTotalTimeSheet() {  
        return timeSheet * 22 - punishmentTime;  
    }  
  
    /** 
     * 获取每月实际应发工资 = 每月固定工资 - 惩罚时长 * 10<br/> 
     * <作为公司普通员工  每迟到1小时 扣10块钱  坑吧？  哈哈> 
     *  
     * @return 
     */  
    public double getTotalWage() {  
        return wage - punishmentTime * 10;  
    }  
      
      
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public double getWage() {  
        return wage;  
    }  
  
    public void setWage(double wage) {  
        this.wage = wage;  
    }  
  
    public int getPunishmentTime() {  
        return punishmentTime;  
    }  
  
    public void setPunishmentTime(int punishmentTime) {  
        this.punishmentTime = punishmentTime;  
    }  
  
}  
```

4.抽象访问者：公司部门抽象类 Department.java

```java
/** 
 * 公司部门（访问者）抽象类 
 * @author   
 * 
 */  
public abstract class Department {  
      
    // 声明一组重载的访问方法，用于访问不同类型的具体元素（这里指的是不同的员工）    
      
    /** 
     * 抽象方法 访问公司管理者对象<br/> 
     * 具体访问对象的什么  就由具体的访问者子类（这里指的是不同的具体部门）去实现 
     * @param me 
     */  
    public abstract void visit(ManagerEmployee me);  
      
    /** 
     * 抽象方法 访问公司普通员工对象<br/> 
     * 具体访问对象的什么  就由具体的访问者子类（这里指的是不同的具体部门）去实现 
     * @param ge 
     */  
    public abstract void visit(GeneralEmployee ge);  
  
}  
```

5.具体访问者：公司财务部类 FADepartment.java

```java
/** 
 * 具体访问者对象：公司财务部<br/> 
 * 财务部的职责就是负责统计核算员工的工资 
 * @author   
 * 
 */  
public class FADepartment extends Department {  
  
    /** 
     * 访问公司管理者对象的每月工资 
     */  
    @Override  
    public void visit(ManagerEmployee me) {  
        double totalWage = me.getTotalWage();  
        System.out.println("管理者: " + me.getName() +   
                "  固定工资 =" + me.getWage() +   
                ", 迟到时长 " + me.getPunishmentTime() + "小时"+  
                ", 实发工资="+totalWage);  
    }  
  
    /** 
     * 访问公司普通员工对象的每月工资 
     */  
    @Override  
    public void visit(GeneralEmployee ge) {  
        double totalWage = ge.getTotalWage();  
        System.out.println("普通员工: " + ge.getName() +   
                "  固定工资 =" + ge.getWage() +   
                ", 迟到时长 " + ge.getPunishmentTime() + "小时"+  
                ", 实发工资="+totalWage);  
    }  
  
} 
```

6.具体访问者：公司人力资源部类 HRDepartment.java

```java
/** 
 * 具体访问者对象：公司人力资源部<br/> 
 * 人力资源部的职责就是负责统计核算员工的每月上班时长 
 * @author   
 * 
 */  
public class HRDepartment extends Department {  
  
    /** 
     * 访问公司管理者对象的每月实际上班时长统计 
     */  
    @Override  
    public void visit(ManagerEmployee me) {  
        me.getTotalTimeSheet();  
    }  
  
    /** 
     * 访问公司普通员工对象的每月实际上班时长统计 
     */  
    @Override  
    public void visit(GeneralEmployee ge) {  
        ge.getTotalTimeSheet();  
    }  
  
}  
```

7.客户端测试类：模拟财务部对公司员工的工资核算和访问 Client.java

```java
import java.util.ArrayList;  
import java.util.List;  
  
public class Client {  
  
    public static void main(String[] args) {  
        List<Employee> employeeList = new ArrayList<Employee>();  
        Employee mep1,mep2,gep1,gep2,gep3;  
        // 管理者1  
        mep1 = new ManagerEmployee("王总", 8, 20000, 10);  
        // 管理者2  
        mep2 = new ManagerEmployee("谢经理", 8, 15000, 15);  
        // 普通员工1  
        gep1 = new GeneralEmployee("小杰", 8, 8000, 8);  
        // 普通员工2  
        gep2 = new GeneralEmployee("小晓", 8, 8500, 12);  
        // 普通员工3  
        gep3 = new GeneralEmployee("小虎", 8, 7500, 0);  
          
        employeeList.add(mep1);  
        employeeList.add(mep2);  
        employeeList.add(gep1);  
        employeeList.add(gep2);  
        employeeList.add(gep3);  
          
        // 财务部 对公司员工的工资核算/访问  
        FADepartment department = new FADepartment();  
        for(Employee employee : employeeList){  
            employee.accept(department);  
        }     
    }  
      
}  
```

如果要更改为人力资源部对员工的一个月的上班时长统计 则只要将上述代码中的

```java
FADepartment department = new FADepartment();  
```

修改为如下即可

```java
HRDepartment department = new HRDepartment();
```

8.程序运行结果：

```
管理者: 王总  固定工资 =20000.0, 迟到时长 10小时, 实发工资=19950.0  
管理者: 谢经理  固定工资 =15000.0, 迟到时长 15小时, 实发工资=14925.0  
普通员工: 小杰  固定工资 =8000.0, 迟到时长 8小时, 实发工资=7920.0  
普通员工: 小晓  固定工资 =8500.0, 迟到时长 12小时, 实发工资=8380.0  
普通员工: 小虎  固定工资 =7500.0, 迟到时长 0小时, 实发工资=7500.0  
```


## 概念

访问者模式(Visitor)：把数据结构和作用于结构上的操作解耦合，使得操作集合可相对自由地演化。访问者模式适用于数据结构相对稳定而算法又容易变化的系统。访问者模式的优点是增加操作很容易，因为增加操作意味着增加新的访问者；而它的缺点就是增加新的数据结构很困难。

```java
/** 
 * 访问者模式(Visitor)：把数据结构和作用于结构上的操作解耦合，使得操作集合可相对自由地演化。 
 *  
 * 访问者模式就是一种分离对象数据结构与行为的方法，通过这种分离， 
 *  
 * 可达到为一个被访问者动态添加新的操作而无需做其它的修改的效果。 
 */  
interface Visitor {  
    /** 
     * 访问对象 
     *  
     * @param subject 
     *            待访问的对象 
     */  
    public void visitor(Subject subject);  
}  
  
class MyVisitor implements Visitor {  
    @Override  
    public void visitor(Subject subject) {  
        System.out.println("MyVisitor 访问的属性值为：" + subject.getField());  
    }  
}  
  
class OtherVisitor implements Visitor {  
    @Override  
    public void visitor(Subject subject) {  
        System.out.println("OtherVisitor 访问的属性值为：" + subject.getField());  
    }  
}  
  
interface Subject {  
    /** 接受将要访问它的对象 */  
    public void accept(Visitor visitor);  
  
    /** 获取将要被访问的属性 */  
    public String getField();  
}  
  
class MySubject implements Subject {  
    private String name;  
  
    public MySubject(String name) {  
        this.name = name;  
    }  
  
    /** 
     * 这是核心：接收【指定的访问者】来访问自身的MySubject类的状态或特征 
     */  
    @Override  
    public void accept(Visitor visitor) {  
        visitor.visitor(this);  
    }  
  
    @Override  
    public String getField() {  
        return name;  
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
         * 创建待访问的对象 
         */  
        Subject subject = new MySubject("张三");  
        /** 
         * 接受访问对象：这里只接收MyVisitor访问者对象，不接收OtherVisitor访问者对象 
         */  
        subject.accept(new MyVisitor());  
    }  
}  
```

## 总结
访问者模式就是一种分离对象数据结构与行为的方法，通过这种分离，可达到为一个被访问者动态添加新的操作而无需做其它的修改的效果。