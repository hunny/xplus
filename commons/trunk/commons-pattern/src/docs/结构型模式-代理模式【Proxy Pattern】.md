# 结构型模式-代理模式【Proxy Pattern】

[原文地址](http://blog.csdn.net/janice0529/article/details/42583481)

## 一、概述

给某一个对象提供一个代理，并由代理对象来完成对原对象的访问。代理模式是一种对象结构型模式。

## 二、适用场景

当无法直接访问某个对象或访问某个对象存在困难时可以通过一个代理对象来间接访问，为了保证客户端使用的透明性，委托对象与代理对象需要实现相同的接口。

## 三、UML类图

参见原文

## 四、参与者

1、接口类：Subject
它声明了真实访问者和代理访问者的共同接口,客户端通常需要针对接口角色进行编程。
2、代理类：ProxySubject
包含了对真实（委托）对象(RealSubject)的引用，在实现的接口方法中调用引用对象的接口方法执行，从而达到代理的作用。看似是代理对象(ProxySubject)在操作，但其实真正的操作者是委托对象（RealSubject）。
3、委托类/真实访问类：RealSubject
它定义了代理角色所代表的真实对象，在真实角色中实现了真实的业务操作，客户端可以通过代理角色间接调用真实角色中定义的操作。

## 五、用例学习

1、接口类：Subject.java

```java
/** 
 * 接口类 
 * @author lvzb.software@qq.com 
 * 
 */  
public interface Subject {  
  
    public void visit();  
}  
```

2、接口实现类，真实访问对象/委托对象：RealSubject.java

```java
/** 
 * 接口实现类，真实访问对象/委托对象 
 * @author lvzb.software@qq.com 
 * 
 */  
public class RealSubject implements Subject {  
  
    @Override  
    public void visit() {  
        System.out.println("I am 'RealSubject',I am the execution method");  
    }  
      
} 
```

3、接口实现类，代理对象：ProxySubject.java

```java
/** 
 * 接口实现类，代理对象 
 * @author lvzb.software@qq.com 
 * 
 */  
public class ProxySubject implements Subject {  
    // 维持对真实委托对象的引用，该对象才是真正的执行者  
    private Subject realSubject;  
      
    public ProxySubject(Subject subject){  
        this.realSubject = subject;  
    }  
  
    @Override  
    public void visit() {  
        // 真实委托对象 通过 代理对象的引用 间接的实现了对目标对象的访问执行  
        realSubject.visit();  
    }  
} 
```

4、客户类 Client.java

```java
/** 
 * 客户类 
 * @author  lvzb.software@qq.com 
 * 
 */  
public class Client {  
  
    public static void main(String[] args) {  
        Subject proxySubject = new ProxySubject(new RealSubject());  
        proxySubject.visit();  
    }  
  
} 
```

## 六、其他/扩展

按照代理类的创建时期，代理类可以分为两种:

1、静态代理：由程序员创建或特定工具自动生成源代码，再对其编译。在程序运行前，代理类的.class文件就已经存在了。（上面用例介绍的就是 静态代理技术）

静态代理的优劣分析：

优点：客户端面向接口编程，符合开闭原则，使系统具有好的灵活性和扩展性。

缺点：从上面代码中我们可以发现 每一种代理类都是实现了特定的接口，及每一种代理类只能为特定接口下的实现类做代理。如果是不同接口下的其他实现类，则需要重新定义新接口下的代理类。     

那么是否可以通过一个代理类完成不同接口下实现类的代理操作呢？那么此时就必须使用动态代理来完成。

2、动态代理：在程序运行时，运用JAVA反射机制动态创建代理实例。

参见[关于动态代理技术](http://blog.csdn.net/janice0529/article/details/42884019)
[Spring AOP 实现原理与 CGLIB 应用](https://www.ibm.com/developerworks/cn/java/j-lo-springaopcglib/)


## 其它参考概念

代理模式(Proxy)：代理模式其实就是多一个代理类出来，替原对象进行一些操作。比如咱有的时候打官司需要请律师，因为律师在法律方面有专长，可以替咱进行操作表达咱的想法，这就是代理的意思。代理模式分为两类：1、静态代理(不使用jdk里面的方法)；2、动态代理(使用jdk里面的InvocationHandler和Proxy)。

### 静态代理

```java
** 
 * 示例(一)：代理模式 --静态代理(没有调用JDK里面的方法) 
 *  
 * 目标接口 
 */  
  
interface Targetable {  
    public void targetMethod();  
}  
  
class Target implements Targetable {  
  
    @Override  
    public void targetMethod() {  
        System.out.println("this is a target method...");  
    }  
}  
  
class Proxy implements Targetable {  
    private Target target;  
  
    public Proxy() {  
        this.target = new Target();  
    }  
  
    private void beforeMethod() {  
        System.out.println("this is a method before proxy...");  
    }  
  
    private void afterMethod() {  
        System.out.println("this is a method after proxy...");  
    }  
  
    /** 
     * 在执行目标方法前后加了逻辑 
     */  
    @Override  
    public void targetMethod() {  
        beforeMethod();  
        target.targetMethod();  
        afterMethod();  
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
         * 创建代理对象 
         */  
        Targetable proxy = new Proxy();  
        /** 
         * 执行代理方法 
         */  
        proxy.targetMethod();  
    }  
} 
```

### 动态代理

```java
/** 
 * 示例(二)：代理模式 --动态代理 
 *  
 * 以添加用户为例 
 */  
class User {  
    private String username;  
    private String password;  
  
    public User() {  
    }  
  
    public User(String username, String password) {  
        this.username = username;  
        this.password = password;  
    }  
  
    public String getUsername() {  
        return username;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
    }  
  
    public String getPassword() {  
        return password;  
    }  
  
    public void setPassword(String password) {  
        this.password = password;  
    }  
  
    @Override  
    public String toString() {  
        return "User [username=" + username + ", password=" + password + "]";  
    }  
}  
  
/** 
 * 目标接口 
 */  
interface IUserDao {  
    public void add(User user);  
}  
  
class UserDaoImpl implements IUserDao {  
    @Override  
    public void add(User user) {  
        System.out.println("add a user successfully...");  
    }  
}  
  
/** 
 * 日志类 --> 待织入的Log类 
 */  
class LogEmbed implements InvocationHandler {  
    private IUserDao target;  
  
    /** 
     * 对target进行封装 
     */  
    public IUserDao getTarget() {  
        return target;  
    }  
  
    public void setTarget(IUserDao target) {  
        this.target = target;  
    }  
  
    private void beforeMethod() {  
        System.out.println("add start...");  
    }  
  
    private void afterMethod() {  
        System.out.println("add end...");  
    }  
  
    /** 
     * 这里用到了反射 
     *  
     * proxy 代理对象 
     *  
     * method 目标方法 
     *  
     * args 目标方法里面参数列表 
     */  
    @Override  
    public Object invoke(Object proxy, Method method, Object[] args)  
            throws Throwable {  
        beforeMethod();  
        // 回调目标对象的方法  
        method.invoke(target, args);  
        System.out.println("LogEmbed --invoke-> method = " + method.getName());  
        afterMethod();  
        return null;  
    }  
}  
  
/** 
 * 客户端测试类 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        IUserDao userDao = new UserDaoImpl();  
        LogEmbed log = new LogEmbed();  
        log.setTarget(userDao);  
        /** 
         * 根据实现的接口产生代理 
         */  
        IUserDao userDaoProxy = (IUserDao) Proxy.newProxyInstance(userDao  
                .getClass().getClassLoader(), userDao.getClass()  
                .getInterfaces(), log);  
        /** 
         * 注意：这里在调用IUserDao接口里的add方法时， 
         * 代理对象会帮我们调用实现了InvocationHandler接口的LogEmbed类的invoke方法。 
         *  
         * 这样做，是不是有点像Spring里面的拦截器呢？ 
         */  
        userDaoProxy.add(new User("张三", "123"));  
    }  
}  
```

### 总结
代理模式好处：1、一个代理类调用原有的方法，且对产生的结果进行控制。2、可以将功能划分的更加清晰，有助于后期维护。
