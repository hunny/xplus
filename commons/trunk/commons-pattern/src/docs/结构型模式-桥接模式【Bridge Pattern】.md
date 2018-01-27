# 桥接型模式-桥接模式【Bridge Pattern】

[原文地址](http://blog.csdn.net/janice0529/article/details/44102637)

## 一、概述 
将抽象部分与它的实现部分分离，使它们都可以独立地变化。它是一种对象结构型模式，又称为柄体(Handle and Body)模式。

## 二、适用场景 
处理多维度变化。 
业务场景：某功能为将数据库中的数据转换成多种文件格式，例如txt、xml、pdf等格式，同时需要支持多种不同类型的数据库的读取。便可使用桥接模式对其进行设计。 
这里的维度有两个 分别为：不同的文件格式 和 不同的数据库类型。例如：

| - | txt | xml | pdf |
| --- | --- | --- | --- |
| SQLServer | - | - | - |
| MySQL | - | - | - |
| Oracle | - | - | - | 

## 三、UML类图 

参见原文

## 四、参与者 

①Abstraction（抽象类）：用于定义抽象类的接口，它一般是抽象类而不是接口，其中定义了一个Implementor（实现类接口）类型的对象并可以维护该对象，它与Implementor之间具有关联关系，它既可以包含抽象业务方法，也可以包含具体业务方法。 
②RefinedAbstraction（扩充抽象类）：扩充由Abstraction定义的接口，通常情况下它不再是抽象类而是具体类，它实现了在Abstraction中声明的抽象业务方法，在RefinedAbstraction中可以调用在Implementor中定义的业务方法。 
③Implementor（实现类接口）：定义实现类的接口，这个接口不一定要与Abstraction的接口完全一致，事实上这两个接口可以完全不同，一般而言，Implementor接口仅提供基本操作，而Abstraction定义的接口可能会做更多更复杂的操作。Implementor接口对这些基本操作进行了声明，而具体实现交给其子类。通过关联关系，在Abstraction中不仅拥有自己的方法，还可以调用到Implementor中定义的方法，使用关联关系来替代继承关系。 
④ConcreteImplementor（具体实现类）：具体实现Implementor接口，在不同的ConcreteImplementor中提供基本操作的不同实现，在程序运行时，ConcreteImplementor对象将替换其父类对象，提供给抽象类具体的业务操作方法。

## 五、用例学习<以适用场景里的业务场景作为代码设计> 
1、JDBC 驱动连接管理类：JdbcDriverManager.java

```java
/**
 * JDBC 驱动连接管理类
 * @author lvzb.software@qq.com
 *
 */
public class JdbcDriverManager {

    public String connectAndReadOracle(){
        // 模拟连接Oracle数据库的代码
        System.out.println("已成功连接到Oracle数据库");
        // 模式 省略 从数据库中获取内容的代码
        String content = "已成功从Oracle数据库中读取到了内容";
        return content;
    }

    public String connectAndReadMySql(){
        // 模拟连接MySql数据库的代码
        System.out.println("已成功连接到MySql数据库");
        // 模式 省略 从数据库中获取内容的代码
        String content = "已成功从MySql数据库中读取到了内容";
        return content;
    }

    public String connectAndReadSqlServer(){
        // 模拟连接Sql Server数据库的代码
        System.out.println("已成功连接到Sql Server数据库");
        // 模式 省略 从数据库中获取内容的代码
        String content = "已成功从Sql Server数据库中读取到了内容";
        return content;
    }
}
```
2、<角色：实现类接口> FileExportImpl.java

```java
/**
 * 获取文件内容、连接数据库来源接口
 * @author lvzb.software@qq.com
 *
 */
public interface FileExportImpl {
    /**
     * 读取数据库中的内容
     * @param jdbcDriver
     * @return
     */
    public String readContent();
}
```

3、<角色：具体实现类> FileExportFromOracle.java

```java
/**
 * 从Oracle数据库获取内容
 * @author lvzb.software@qq.com
 *
 */
public class FileExportFromOracle implements FileExportImpl {

    @Override
    public String readContent() {
        JdbcDriverManager jdbcDriver = new JdbcDriverManager();
        return jdbcDriver.connectAndReadOracle();
    }

}
```

4、<角色：具体实现类> FileExportFromMySql.java

```java
/**
 * 从MySql数据库获取内容
 * @author lvzb.software@qq.com
 *
 */
public class FileExportFromMySql implements FileExportImpl {

    @Override
    public String readContent() {
        JdbcDriverManager jdbcDriver = new JdbcDriverManager();
        return jdbcDriver.connectAndReadMySql();
    }

}
```

5、<角色：具体实现类> FileExportFromSqlServer.java

```java
/**
 * 从Sql Server数据库获取内容
 * @author lvzb.software@qq.com
 *
 */
public class FileExportFromSqlServer implements FileExportImpl {

    @Override
    public String readContent() {
        JdbcDriverManager jdbcDriver = new JdbcDriverManager();
        return jdbcDriver.connectAndReadSqlServer();
    }

}
```

6、<角色：抽象类> FileExportAbstraction.java

```java
/**
 * 文件格式导出 抽象类
 * @author lvzb.software@qq.com
 *
 */
public abstract class FileExportAbstraction {

    protected FileExportImpl fileSouce;

    public void setFileSource(FileExportImpl fileSouce){
        this.fileSouce = fileSouce;
    }

    public abstract void exportFile();

}
```

7、<角色：扩充抽象类> TxtFileExport.java

```java
/**
 * Txt文件格式导出具体类
 * @author lvzb.software@qq.com
 *
 */
public class TxtFileExport extends FileExportAbstraction {

    @Override
    public void exportFile() {
        String readContent = fileSouce.readContent();
        System.out.println(readContent + "，将内容导出为.txt格式");
    }

}
```
8、<角色：扩充抽象类> XmlFileExport.java

```java
/**
 * xml文件格式导出具体类
 * @author  lvzb.software@qq.com
 *
 */
public class XmlFileExport extends FileExportAbstraction {

    @Override
    public void exportFile() {
        String readContent = fileSouce.readContent();
        System.out.println(readContent + "，将内容导出为.xml格式");
    }
}
```

9、<角色：扩充抽象类> PdfFileExport.java

```java
/**
 * pdf文件格式导出具体类
 * @author  lvzb.software@qq.com
 *
 */
public class PdfFileExport extends FileExportAbstraction {

    @Override
    public void exportFile() {
        String readContent = fileSouce.readContent();
        System.out.println(readContent + "，将内容导出为.pdf格式");
    }
}
```

10、客户端测试类: Client.java

```java
public class Client {

    public static void main(String[] args) {
        FileExportImpl fileOracle = new FileExportFromOracle();
        FileExportImpl fileMySql = new FileExportFromMySql();
        FileExportImpl fileSqlServer = new FileExportFromSqlServer();

        FileExportAbstraction fileTxtExport = new TxtFileExport();
        FileExportAbstraction fileXmlExport = new XmlFileExport();
        FileExportAbstraction filePdfExport = new PdfFileExport();

        // 如果我们要从Oracle中导出xml格式的数据
        fileXmlExport.setFileSource(fileOracle);
        fileXmlExport.exportFile();

        System.out.println("--------------------\n");
        // 如果我们要从Oracle中导出txt格式的数据
        fileTxtExport.setFileSource(fileOracle);
        fileTxtExport.exportFile();

        System.out.println("--------------------\n");
        // 如果我们要从MySql中导出pdf格式的数据
        filePdfExport.setFileSource(fileMySql);
        filePdfExport.exportFile();
    }

}
```

11、执行结果 如下：

```
已成功连接到Oracle数据库
已成功从Oracle数据库中读取到了内容，将内容导出为.xml格式
--------------------

已成功连接到Oracle数据库
已成功从Oracle数据库中读取到了内容，将内容导出为.txt格式
--------------------

已成功连接到MySql数据库
已成功从MySql数据库中读取到了内容，将内容导出为.pdf格式
```

12、后续系统扩展 
①、现如果需求要求新增加一种文件输入格式 如:html格式 
则只需新写一个FileExportAbstraction抽象类的子类 用以输出html格式即可，而不需要修改其他任何类、不需要修改另一维度的代码。 
②、现如果需求要求新增加一类型数据库类型 如：Sybase 
则只需新写一个FileExportImpl接口的实现类、用以从Sybase数据库中获取内容、并修改JdbcDriverManager.java类、添加对Sybase数据库的连接和访问。

由此可见 在两个变化维度中任意扩展一个维度，都不需要修改原有系统、提高了系统的扩展性和可维护性。

## 六、其他 
主要优点： 
(1)分离抽象接口及其实现部分。桥接模式使用“对象间的关联关系”解耦了抽象和实现之间固有的绑定关系，使得抽象和实现可以沿着各自的维度来变化。所谓抽象和实现沿着各自维度的变化，也就是说抽象和实现不再在同一个继承层次结构中，而是“子类化”它们，使它们各自都具有自己的子类，以便任何组合子类，从而获得多维度组合对象。 
(2)在很多情况下，桥接模式可以取代多层继承方案，多层继承方案违背了“单一职责原则”，复用性较差，且类的个数非常多，桥接模式是比多层继承方案更好的解决方法，它极大减少了子类的个数。 
(3)桥接模式提高了系统的可扩展性，在两个变化维度中任意扩展一个维度，都不需要修改原有系统，符合“开闭原则”。

## 其它参考

### 概念

桥接模式(Bridge)： 把事物和其具体实现分开(抽象化与实现化解耦)，使他们可以各自独立的变化。假设你的电脑是双系统(WinXP、Win7)，而且都安装了mysql、oracle、sqlserver、DB2这4种数据库,那么你有2*4种选择去连接数据库。按平常的写法，咱要写2*4个类，但是使用了桥接模式，你只需写2+4个类,可以看出桥接模式其实就是一种将N*M转化成N+M组合的思想。

```java
/** 
 * 桥接模式(Bridge)： 把事物和其具体实现分开(抽象化与实现化解耦)，使他们可以各自独立的变化。 
 *  
 * 假设你的电脑是双系统(WinXP、Win7)，而且都安装了mysql、oracle、sqlserver、DB2这4种数据库 
 *  
 * 那么你有2*4种选择去连接数据库。按平常的写法，咱要写2*4个类，但是使用了桥接模式，你只需写2+4个类 
 *  
 * 可以看出桥接模式其实就是一种将N*M转化成N+M组合的思想。 
 */  
interface Driver {  
    public void method();  
}  
  
class MysqlDriver implements Driver {  
  
    @Override  
    public void method() {  
        System.out.println("use mysql driver to connection db...\n");  
    }  
}  
  
class OracleDriver implements Driver {  
  
    @Override  
    public void method() {  
        System.out.println("use oracle driver to connection db...\n");  
    }  
}  
  
/** 
 * 这里你还可以写SqlserverDriver、DB2Driver... 
 */  
  
abstract class Computer {  
    public abstract void connection(Driver driver);  
}  
  
class WinXP extends Computer {  
    @Override  
    public void connection(Driver driver) {  
        System.out.println("WinXP Computer");  
        driver.method();  
    }  
}  
  
class Win7 extends Computer {  
    @Override  
    public void connection(Driver driver) {  
        System.out.println("Win7 Computer");  
        driver.method();  
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
         * 第一种组合：winXP使用mysql驱动连接数据库 
         */  
        Computer winxp = new WinXP();  
        winxp.connection(new MysqlDriver());  
        /** 
         * 第二种组合：win7使用mysql驱动连接数据库 
         */  
        Computer win7 = new Win7();  
        win7.connection(new MysqlDriver());  
        /** 
         * 第三种组合：winXP使用oracle驱动连接数据库 
         */  
        Computer cwinxp = new WinXP();  
        cwinxp.connection(new OracleDriver());  
        /** 
         * 第四种组合：winXP使用oracle驱动连接数据库 
         */  
        Computer cwin7 = new Win7();  
        cwin7.connection(new OracleDriver());  
  
    }  
}  
```

### 总结
桥接的核心思想是：将抽象化与实现化解耦，使得二者可以独立变化。



