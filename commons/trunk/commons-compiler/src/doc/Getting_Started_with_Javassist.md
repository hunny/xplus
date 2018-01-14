# Getting Started with Javassist

## Reading and writing bytecode (读写字节码)

Javassist is a class library for dealing with Java bytecode. Java bytecode is stored in a binary file called a class file. Each class file contains one Java class or interface.
Javassist是一个处理Java字节码的类库。 Java字节码存储在称为类文件的二进制文件中。 每个类文件都包含一个Java类或接口。

The class Javassist.CtClass is an abstract representation of a class file. A CtClass (compile-time class) object is a handle for dealing with a class file. The following program is a very simple example:
类Javassist.CtClass是类文件的抽象表示。 CtClass（编译时类(compile-time class)）对象是处理类文件的句柄。 以下程序是一个非常简单的例子：

```java
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("test.Rectangle");
cc.setSuperclass(pool.get("test.Point"));
cc.writeFile();
```

This program first obtains a `ClassPool` object, which controls bytecode modification with `Javassist`. The `ClassPool` object is a container of `CtClass` object representing a class file. It reads a class file on demand for constructing a `CtClass` object and records the constructed object for responding later accesses.
这个程序首先获得一个`ClassPool`对象，它用`Javassist`控制字节码修改。 `ClassPool`对象是代表类文件的`CtClass`对象的容器。 它根据需要读取一个类文件来构造`CtClass`对象，并记录构造的对象以响应以后的访问。

To modify the definition of a class, the users must first obtain from a `ClassPool` object a reference to a `CtClass` object representing that class.`get()` in ClassPool is used for this purpose. In the case of the program shown above, the `CtClass` object representing a class `test.Rectangle` is obtained from the `ClassPool` object and it is assigned to a variable cc. The `ClassPool` object returned by `getDefault()` searches the default system search path.
要修改一个类的定义，用户必须首先从一个`ClassPool`对象获得一个对表示该类的`CtClass`对象的引用。 `ClassPool`中的`get()`用于此目的。 在上面显示的程序的情况下，表示类`test.Rectangle`的`CtClass`对象从`ClassPool`对象中获得，并被分配给变量cc。 `getDefault()`返回的ClassPool对象搜索默认的系统搜索路径。

From the implementation viewpoint, `ClassPool` is a hash table of `CtClass` objects, which uses the class names as keys. `get()` in `ClassPool` searches this hash table to find a CtClass object associated with the specified key. If such a CtClass object is not found, get() reads a class file to construct a new CtClass object, which is recorded in the hash table and then returned as the resulting value of get().
从实现的角度来看，`ClassPool`是`CtClass`对象的哈希表，它使用类名称作为关键字。 在`ClassPool`中的`get（）`会搜索这个散列表来查找与指定键相关联的CtClass对象。 如果没有找到这样的`CtClass`对象，`get（）`会读取一个类文件来构造一个新的`CtClass`对象，该对象记录在哈希表中，然后作为get（）的结果值返回。

The `CtClass` object obtained from a `ClassPool` object can be modified (details of how to modify a CtClass will be presented later). In the example above, it is modified so that the superclass of `test.Rectangle` is changed into a class `test.Point`. This change is reflected on the original class file when `writeFile()` in `CtClass()` is finally called.
从`ClassPool`对象中获取的`CtClass`对象可以被修改（稍后将介绍如何修改`CtClass`的细节）。 在上面的例子中，它被修改，以便`test.Rectangle`的超类更改为一个类`test.Point`。 当`CtClass（）`中的`writeFile（）`最终被调用时，这个改变反映在原来的类文件上。

`writeFile()` translates the `CtClass` object into a class file and writes it on a local disk. `Javassist` also provides a method for directly obtaining the modified bytecode. To obtain the bytecode, call `toBytecode()`:
`writeFile（）`将`CtClass`对象转换为一个类文件，并将其写入本地磁盘。 Javassist还提供了一种直接获取修改字节码的方法。 要获取字节码，请调用字节码方法`toBytecode`：

```java
byte[] b = cc.toBytecode();
```

You can directly load the `CtClass` as well:
你也可以直接加载`CtClass`：

```java
Class clazz = cc.toClass();
```

`toClass()` requests the context class loader for the current thread to load the class file represented by the `CtClass`. It returns a `java.lang.Class` object representing the loaded class. For more details, please see this section below.
`toClass（）`请求当前线程的上下文类加载器加载由`CtClass`表示的类文件。 它返回一个代表加载类的`java.lang.Class`对象。 有关更多详细信息，请参阅下面的这个部分。

### Defining a new class (定义一个新的类)

To define a new class from scratch, `makeClass()` must be called on a `ClassPool`.
要从头开始定义一个新类，必须在ClassPool上调用makeClass（）。

```java
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.makeClass("Point");
```

This program defines a class `Point` including no members. Member methods of Point can be created with factory methods declared in `CtNewMethod` and appended to `Point` with `addMethod()` in `CtClass`.
这个程序定义了一个包含没有成员的类Point。 Point的成员方法可以使用CtNewMethod中声明的工厂方法创建，并通过CtClass中的addMethod（）附加到Point。

`makeClass()` cannot create a new interface; `makeInterface()` in `ClassPool` can do. Member methods in an interface can be created with `abstractMethod()` in `CtNewMethod`. Note that an interface method is an abstract method.
makeClass（）不能创建一个新的接口; 在ClassPool中的makeInterface（）可以做到。 可以使用CtNewMethod中的abstractMethod（）创建接口中的成员方法。 请注意，接口方法是一种抽象方法。

### Frozen classes (冻结的类)

If a `CtClass` object is converted into a class file by `writeFile()`, `toClass()`, or `toBytecode()`, Javassist freezes that `CtClass` object. Further modifications of that `CtClass` object are not permitted. This is for warning the developers when they attempt to modify a class file that has been already loaded since the JVM does not allow reloading a class.
如果通过`writeFile()`，`toClass()`或`toBytecode()`将CtClass对象转换为类文件，Javassist将冻结该CtClass对象。 该CtClass对象的进一步修改是不允许的。 这是为了警告开发者当他们试图修改一个已经被加载的类文件，因为JVM不允许重新加载一个类。

A frozen CtClass can be defrost so that modifications of the class definition will be permitted. For example,
一个冻结的CtClass可以被解冻，这样就可以修改类的定义。 例如，

```java
CtClasss cc = ...;
    :
cc.writeFile();
cc.defrost();
cc.setSuperclass(...);    // OK since the class is not frozen.
```

After `defrost()` is called, the `CtClass` object can be modified again.
在调用`defrost()`之后，可以再次修改`CtClass`对象。

If `ClassPool.doPruning` is set to true, then Javassist prunes the data structure contained in a `CtClass` object when Javassist freezes that object. To reduce memory consumption, pruning discards unnecessary attributes (attribute_info structures) in that object. For example, Code_attribute structures (method bodies) are discarded. Thus, after a CtClass object is pruned, the bytecode of a method is not accessible except method names, signatures, and annotations. The pruned CtClass object cannot be defrost again. The default value of ClassPool.doPruning is false.
如果ClassPool.doPruning设置为true，那么当Javassist冻结该对象时，Javassist会修剪CtClass对象中包含的数据结构。 为了减少内存消耗，修剪会丢弃该对象中不必要的属性（attribute_info结构）。 例如，Code_attribute结构（方法体）被丢弃。 因此，在删除`CtClass`对象之后，除了方法名称，签名和注释之外，方法的字节码是不可访问的。 修剪后的`CtClass`对象不能再次解冻。 `ClassPool.doPruning`的默认值是false。

To disallow pruning a particular CtClass, stopPruning() must be called on that object in advance:
为了不允许修剪特定的`CtClass`，必须事先在该对象上调用`stopPruning()`：

```java
CtClasss cc = ...;
cc.stopPruning(true);
    // :
cc.writeFile(); // convert to a class file.
// cc is not pruned.
```

The `CtClass` object cc is not pruned. Thus it can be defrost after `writeFile()` is called.
`CtClass`对象cc不被修剪。 因此可以在`writeFile()`被调用后解冻。

Note: While debugging, you might want to temporarily stop pruning and freezing and write a modified class file to a disk drive. debugWriteFile() is a convenient method for that purpose. It stops pruning, writes a class file, defrosts it, and turns pruning on again (if it was initially on).
注：在调试时，您可能需要暂时停止修剪和冻结，并将已修改的类文件写入磁盘驱动器。 `debugWriteFile()`是一个方便的方法。 它会停止修剪，写一个类文件，解冻它，然后再次修剪（如果它是最初的话）。

### Class search path (类搜索路径)

The default `ClassPool` returned by a static method `ClassPool.getDefault()` searches the same path that the underlying JVM (Java virtual machine) has. If a program is running on a web application server such as JBoss and Tomcat, the `ClassPool` object may not be able to find user classes since such a web application server uses multiple class loaders as well as the system class loader. In that case, an additional class path must be registered to the `ClassPool`. Suppose that pool refers to a `ClassPool` object:
由静态方法`ClassPool.getDefault()`返回的默认`ClassPool`搜索底层JVM（Java虚拟机）具有的相同路径。 如果程序在Web应用程序服务器上运行，比如JBoss和Tomcat，那么ClassPool对象可能无法找到用户类，因为这样的Web应用程序服务器使用多个类加载器以及系统类加载器。 在这种情况下，一个额外的类路径必须注册到`ClassPool`。 假设该池引用了一个`ClassPool`对象：

```java
pool.insertClassPath(new ClassClassPath(this.getClass()));
```

This statement registers the class path that was used for loading the class of the object that this refers to. You can use any Class object as an argument instead of this.getClass(). The class path used for loading the class represented by that Class object is registered.
此语句注册用于加载所引用的对象的类的类路径。 您可以使用任何Class对象作为参数，而不是this.getClass（）。 用于加载由该Class对象表示的类的类路径已注册。

You can register a directory name as the class search path. For example, the following code adds a directory `/usr/local/javalib` to the search path:
您可以注册一个目录名称作为类搜索路径。 例如，下面的代码将一个目录`/usr/local/javalib`添加到搜索路径中：

```java
ClassPool pool = ClassPool.getDefault();
pool.insertClassPath("/usr/local/javalib");
```

The search path that the users can add is not only a directory but also a URL:
用户可以添加的搜索路径不仅是一个目录，而且还是一个URL：

```java
ClassPool pool = ClassPool.getDefault();
ClassPath cp = new URLClassPath("www.javassist.org", 80, "/java/", "org.javassist.");
pool.insertClassPath(cp);
```

This program adds "http://www.javassist.org:80/java/" to the class search path. This URL is used only for searching classes belonging to a package `org.javassist`. For example, to load a class `org.javassist.test.Main`, its class file will be obtained from:
该程序将`http://www.javassist.org:80/java/`添加到类搜索路径中。 该URL仅用于搜索属于包org.javassist的类。 例如，要加载一个类org.javassist.test.Main，它的类文件将从以下位置获得：

```java
http://www.javassist.org:80/java/org/javassist/test/Main.class
```

Furthermore, you can directly give a byte array to a ClassPool object and construct a CtClass object from that array. To do this, use ByteArrayClassPath. For example,
此外，你可以直接给一个ClassPool对象一个字节数组，并从该数组构造一个CtClass对象。 为此，请使用ByteArrayClassPath。 例如，

```java
ClassPool cp = ClassPool.getDefault();
byte[] b = a byte array;
String name = class name;
cp.insertClassPath(new ByteArrayClassPath(name, b));
CtClass cc = cp.get(name);
```

The obtained CtClass object represents a class defined by the class file specified by b. The ClassPool reads a class file from the given ByteArrayClassPath if get() is called and the class name given to get() is equal to one specified by name.
获得的`CtClass`对象表示由b指定的类文件定义的类。如果调用`get()`并且给予`get()`的类名称等于名称指定的名称，`ClassPool`将从给定的`ByteArrayClassPath`中读取一个类文件。

If you do not know the fully-qualified name of the class, then you can use `makeClass()` in ClassPool:
如果您不知道该类的完全限定名称，则可以在`ClassPool`中使用`makeClass()`：

```java
ClassPool cp = ClassPool.getDefault();
InputStream ins = an input stream for reading a class file;
CtClass cc = cp.makeClass(ins);
```

## ClassPool

A ClassPool object is a container of CtClass objects. Once a CtClass object is created, it is recorded in a ClassPool for ever. This is because a compiler may need to access the CtClass object later when it compiles source code that refers to the class represented by that CtClass.
ClassPool对象是CtClass对象的容器。 一旦创建了一个CtClass对象，它就永远记录在一个ClassPool中。 这是因为编译器在编译引用该CtClass表示的类的源代码时可能需要稍后访问CtClass对象。

For example, suppose that a new method getter() is added to a CtClass object representing Point class. Later, the program attempts to compile source code including a method call to getter() in Point and use the compiled code as the body of a method, which will be added to another class Line. If the CtClass object representing Point is lost, the compiler cannot compile the method call to getter(). Note that the original class definition does not include getter(). Therefore, to correctly compile such a method call, the ClassPool must contain all the instances of CtClass all the time of program execution.
例如，假设一个新的方法getter（）被添加到代表Point类的CtClass对象中。 稍后，程序将尝试编译源代码，包括在Point中调用getter（）的方法，并使用编译后的代码作为方法的主体，将其添加到另一个类Line中。 如果表示Point的CtClass对象丢失，则编译器无法将方法调用编译为getter（）。 请注意，原始类定义不包括getter（）。 因此，要正确编译这样的方法调用，ClassPool必须包含所有程序执行时的CtClass的所有实例。

### Avoid out of memory (避免内存不足)

This specification of ClassPool may cause huge memory consumption if the number of CtClass objects becomes amazingly large (this rarely happens since Javassist tries to reduce memory consumption in various ways). To avoid this problem, you can explicitly remove an unnecessary CtClass object from the ClassPool. If you call detach() on a CtClass object, then that CtClass object is removed from the ClassPool. For example,
如果CtClass对象的数量变得非常大（这很少发生，因为Javassist试图以各种方式减少内存消耗），这种ClassPool规范可能会导致巨大的内存消耗。 为了避免这个问题，你可以显式地从ClassPool中删除一个不必要的CtClass对象。 如果在CtClass对象上调用detach（），那么CtClass对象将从ClassPool中移除。 例如，

```java
CtClass cc = ... ;
cc.writeFile();
cc.detach();
```

You must not call any method on that CtClass object after detach() is called. However, you can call get() on ClassPool to make a new instance of CtClass representing the same class. If you call get(), the ClassPool reads a class file again and newly creates a CtClass object, which is returned by get().
调用detach（）之后，您不得在该CtClass对象上调用任何方法。 但是，您可以调用ClassPool上的get（）来创建一个代表相同类的CtClass的新实例。 如果调用get（），则ClassPool将再次读取一个类文件，并新创建一个由get（）返回的CtClass对象。

Another idea is to occasionally replace a ClassPool with a new one and discard the old one. If an old ClassPool is garbage collected, the CtClass objects included in that ClassPool are also garbage collected. To create a new instance of ClassPool, execute the following code snippet:
另一个想法是偶尔用新的ClassPool替换掉旧的。 如果一个旧的ClassPool被垃圾收集，那么包含在该ClassPool中的CtClass对象也被垃圾收集。 要创建ClassPool的新实例，请执行以下代码片段：

```java
ClassPool cp = new ClassPool(true);
// if needed, append an extra search path by appendClassPath()
```

This creates a ClassPool object that behaves as the default ClassPool returned by ClassPool.getDefault() does. Note that ClassPool.getDefault() is a singleton factory method provided for convenience. It creates a ClassPool object in the same way shown above although it keeps a single instance of ClassPool and reuses it. A ClassPool object returned by getDefault() does not have a special role. getDefault() is a convenience method.
这将创建一个ClassPool对象，其行为与ClassPool.getDefault（）所返回的默认ClassPool一样。 请注意，ClassPool.getDefault（）是为了方便而提供的单例工厂方法。 它以上面所示的相同方式创建一个ClassPool对象，虽然它保留了一个ClassPool的实例并重用它。 getDefault（）返回的ClassPool对象没有特殊的作用。 getDefault（）是一个方便的方法。

Note that new ClassPool(true) is a convenient constructor, which constructs a ClassPool object and appends the system search path to it. Calling that constructor is equivalent to the following code:
请注意，新的ClassPool（true）是一个方便的构造函数，它构造一个ClassPool对象并将系统搜索路径附加到它。 调用该构造函数等价于下面的代码：

```java
ClassPool cp = new ClassPool();
cp.appendSystemPath();  // or append another path by appendClassPath()
```

### Cascaded ClassPools (级联的ClassPools)

If a program is running on a web application server, creating multiple instances of ClassPool might be necessary; an instance of ClassPool should be created for each class loader (i.e. container). The program should create a ClassPool object by not calling getDefault() but a constructor of ClassPool.
如果程序在Web应用程序服务器上运行，则可能需要创建多个ClassPool实例; 应该为每个类加载器（即容器）创建一个ClassPool的实例。 程序应该通过不调用getDefault（）而是ClassPool的构造函数来创建一个ClassPool对象。

Multiple ClassPool objects can be cascaded like java.lang.ClassLoader. For example,
多个ClassPool对象可以像java.lang.ClassLoader一样级联。 例如，

```java
ClassPool parent = ClassPool.getDefault();
ClassPool child = new ClassPool(parent);
child.insertClassPath("./classes");
```

If child.get() is called, the child ClassPool first delegates to the parent ClassPool. If the parent ClassPool fails to find a class file, then the child ClassPool attempts to find a class file under the ./classes directory.
如果调用`child.get()`，则子类`ClassPool`将首先委托给父类`ClassPool`。 如果父级`ClassPool`无法找到一个类文件，那么子`ClassPool`将尝试在`./classes`目录下找到一个类文件。

If child.childFirstLookup is true, the child ClassPool attempts to find a class file before delegating to the parent ClassPool. For example,
如果`child.childFirstLookup`为true，那么子类`ClassPool`会在委托给父类ClassPool之前尝试查找类文件。 例如，

```java
ClassPool parent = ClassPool.getDefault();
ClassPool child = new ClassPool(parent);
child.appendSystemPath();         // the same class path as the default one.
child.childFirstLookup = true;    // changes the behavior of the child.
```

### Changing a class name for defining a new class (改变一个类名来定义一个新的类)

A new class can be defined as a copy of an existing class. The program below does that:
一个新的类可以被定义为一个现有类的副本。 下面的程序是这样做的：

```java
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
cc.setName("Pair");
```

This program first obtains the CtClass object for class Point. Then it calls setName() to give a new name Pair to that CtClass object. After this call, all occurrences of the class name in the class definition represented by that CtClass object are changed from Point to Pair. The other part of the class definition does not change.
这个程序首先获得类Point的CtClass对象。 然后它调用setName（）给这个CtClass对象一个新的名字Pair。 在这个调用之后，由该CtClass对象表示的类定义中的所有类名称的所有出现都从点对点变为对。 类定义的另一部分不会改变。

Note that setName() in CtClass changes a record in the ClassPool object. From the implementation viewpoint, a ClassPool object is a hash table of CtClass objects. setName() changes the key associated to the CtClass object in the hash table. The key is changed from the original class name to the new class name.
请注意，`CtClass中的setName()`更改了`ClassPool`对象中的记录。 从实现的角度来看，一个`ClassPool`对象是一个`CtClass`对象的哈希表。 `setName()`更改与哈希表中的CtClass对象关联的键。 键值从原来的类名更改为新的类名。

Therefore, if get("Point") is later called on the ClassPool object again, then it never returns the CtClass object that the variable cc refers to. The ClassPool object reads a class file Point.class again and it constructs a new CtClass object for class Point. This is because the CtClass object associated with the name Point does not exist any more. See the followings:
因此，如果稍后在`ClassPool`对象上调用get(“Point”)，则它永远不会返回变量cc引用的CtClass对象。 ClassPool对象再次读取类文件Point.class，并为类Point构造一个新的CtClass对象。 这是因为与名称Point关联的CtClass对象不再存在。 看到以下内容：

```java
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
CtClass cc1 = pool.get("Point");   // cc1 is identical to cc.
cc.setName("Pair");
CtClass cc2 = pool.get("Pair");    // cc2 is identical to cc.
CtClass cc3 = pool.get("Point");   // cc3 is not identical to cc.
```

cc1 and cc2 refer to the same instance of CtClass that cc does whereas cc3 does not. Note that, after cc.setName("Pair") is executed, the CtClass object that cc and cc1 refer to represents the Pair class.
cc1和cc2指代cc所做的CtClass的相同实例，而cc3则不然。 请注意，执行cc.setName（“Pair”）后，cc和cc1引用的CtClass对象表示Pair类。

The ClassPool object is used to maintain one-to-one mapping between classes and CtClass objects. Javassist never allows two distinct CtClass objects to represent the same class unless two independent ClassPool are created. This is a significant feature for consistent program transformation.
`ClassPool`对象用于维护类和`CtClass`对象之间的一对一映射。 `Javassist`从不允许两个不同的CtClass对象表示相同的类，除非创建了两个独立的ClassPool。 这是一致的程序转换的一个重要特征。

To create another copy of the default instance of ClassPool, which is returned by ClassPool.getDefault(), execute the following code snippet (this code was already shown above):
要创建由ClassPool.getDefault（）返回的ClassPool的默认实例的另一个副本，请执行以下代码片段（此代码已在上面显示）：

```java
ClassPool cp = new ClassPool(true);
```

If you have two ClassPool objects, then you can obtain, from each ClassPool, a distinct CtClass object representing the same class file. You can differently modify these CtClass objects to generate different versions of the class.
如果您有两个ClassPool对象，则可以从每个ClassPool获取表示相同类文件的不同CtClass对象。 你可以修改这些CtClass对象来生成不同版本的类。

### Renaming a frozen class for defining a new class (重命名一个冻结的类来定义一个新的类)

Once a CtClass object is converted into a class file by writeFile() or toBytecode(), Javassist rejects further modifications of that CtClass object. Hence, after the CtClass object representing Point class is converted into a class file, you cannot define Pair class as a copy of Point since executing setName() on Point is rejected. The following code snippet is wrong:
一旦CtClass对象被writeFile（）或toBytecode（）转换成类文件，Javassist将拒绝对该CtClass对象的进一步修改。 因此，在代表Point类的CtClass对象被转换为类文件后，由于在Point上执行setName（）被拒绝，因此无法将Pair类定义为Point的副本。 下面的代码片段是错误的：

```java
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
cc.writeFile();
cc.setName("Pair");    // wrong since writeFile() has been called.
```

To avoid this restriction, you should call getAndRename() in ClassPool. For example,
为了避免这个限制，你应该在ClassPool中调用getAndRename（）。 例如，

```java
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
cc.writeFile();
CtClass cc2 = pool.getAndRename("Point", "Pair");
```

If getAndRename() is called, the ClassPool first reads Point.class for creating a new CtClass object representing Point class. However, it renames that CtClass object from Point to Pair before it records that CtClass object in a hash table. Thus getAndRename() can be executed after writeFile() or toBytecode() is called on the the CtClass object representing Point class.
如果调用getAndRename（），则ClassPool将首先读取Point.class，以创建一个代表Point类的新CtClass对象。 但是，它将CtClass对象从“点对点”重命名为CtClass对象，并将其记录在散列表中。 因此getAndRename（）可以在代表Point类的CtClass对象上调用writeFile（）或toBytecode（）之后执行。

## Class loader (类加载器)

If what classes must be modified is known in advance, the easiest way for modifying the classes is as follows:
如果事先知道哪些类必须被修改，修改这些类最简单的方法如下：
* 1. Get a CtClass object by calling ClassPool.get(),
* 1.通过调用ClassPool.get（）获取一个CtClass对象，
* 2. Modify it, and
* 2.修改它，然后
* 3. Call writeFile() or toBytecode() on that CtClass object to obtain a modified class file.
* 3.对该CtClass对象调用writeFile（）或toBytecode（）以获取修改的类文件。

If whether a class is modified or not is determined at load time, the users must make Javassist collaborate with a class loader. Javassist can be used with a class loader so that bytecode can be modified at load time. The users of Javassist can define their own version of class loader but they can also use a class loader provided by Javassist.
如果在加载时确定类是否被修改，则用户必须使Javassist与类加载器协作。 Javassist可以与类加载器一起使用，以便字节码在加载时可以被修改。 Javassist的用户可以定义他们自己的类加载器的版本，但是他们也可以使用Javassist提供的类加载器。

### The toClass method in CtClass (CtClass中的toClass方法)

The CtClass provides a convenience method toClass(), which requests the context class loader for the current thread to load the class represented by the CtClass object. To call this method, the caller must have appropriate permission; otherwise, a SecurityException may be thrown.
CtClass提供了一个方便的方法toClass（），该方法请求当前线程的上下文类加载器加载由CtClass对象表示的类。 要调用这个方法，调用者必须有适当的权限; 否则，可能会抛出SecurityException。

The following program shows how to use toClass():
以下程序显示如何使用toClass（）：

```java
public class Hello {
  public void say() {
    System.out.println("Hello");
  }
}
public class Test {
  public static void main(String[] args) throws Exception {
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.get("Hello");
    CtMethod m = cc.getDeclaredMethod("say");
    m.insertBefore("{ System.out.println("Hello.say():"); }");
    Class c = cc.toClass();
    Hello h = (Hello)c.newInstance();
    h.say();
  }
}
```

Test.main() inserts a call to println() in the method body of say() in Hello. Then it constructs an instance of the modified Hello class and calls say() on that instance.
Test.main()在Hello中的say()的方法主体中插入对println()的调用。 然后它构造修改后的Hello类的实例，并在该实例上调用say()。

Note that the program above depends on the fact that the Hello class is never loaded before toClass() is invoked. If not, the JVM would load the original Hello class before toClass() requests to load the modified Hello class. Hence loading the modified Hello class would be failed (LinkageError is thrown). For example, if main() in Test is something like this:
请注意，上面的程序取决于在调用toClass（）之前，Hello类永远不会被加载。 否则，JVM会在toClass（）请求加载修改后的Hello类之前加载原始的Hello类。 因此，加载修改后的Hello类将失败（引发LinkageError）。 例如，如果Test中的main（）是这样的：

```java
public static void main(String[] args) throws Exception {
    Hello orig = new Hello();
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.get("Hello");
        :
}
```

then the original Hello class is loaded at the first line of main and the call to toClass() throws an exception since the class loader cannot load two different versions of the Hello class at the same time.
那么原始的Hello类将被加载到main的第一行，并且对toClass（）的调用将抛出异常，因为类加载器不能同时加载两个不同版本的Hello类。

If the program is running on some application server such as JBoss and Tomcat, the context class loader used by toClass() might be inappropriate. In this case, you would see an unexpected ClassCastException. To avoid this exception, you must explicitly give an appropriate class loader to toClass(). For example, if bean is your session bean object, then the following code:
如果程序在JBoss和Tomcat等应用服务器上运行，toClass（）使用的上下文类加载器可能不合适。 在这种情况下，您会看到一个意外的ClassCastException。 为了避免这个异常，你必须明确的给一个合适的类加载器toClass（）。 例如，如果bean是你的会话bean对象，那么下面的代码：

```java
CtClass cc = ...;
Class c = cc.toClass(bean.getClass().getClassLoader());
```

would work. You should give toClass() the class loader that has loaded your program (in the above example, the class of the bean object).
会工作。 你应该给toClass（）加载你的程序的类加载器（在上面的例子中，这个bean对象的类）。

toClass() is provided for convenience. If you need more complex functionality, you should write your own class loader.
toClass（）是为了方便而提供的。 如果你需要更复杂的功能，你应该写你自己的类加载器。

### Class loading in Java (Java中的类加载)

In Java, multiple class loaders can coexist and each class loader creates its own name space. Different class loaders can load different class files with the same class name. The loaded two classes are regarded as different ones. This feature enables us to run multiple application programs on a single JVM even if these programs include different classes with the same name.
在Java中，多个类加载器可以共存，每个类加载器创建自己的名称空间。 不同的类加载器可以使用相同的类名加载不同的类文件。 装载的两个类集被认为是不同的类集。 此功能使我们能够在单个JVM上运行多个应用程序，即使这些程序包含具有相同名称的不同类。

Note: The JVM does not allow dynamically reloading a class. Once a class loader loads a class, it cannot reload a modified version of that class during runtime. Thus, you cannot alter the definition of a class after the JVM loads it. However, the JPDA (Java Platform Debugger Architecture) provides limited ability for reloading a class. See Section 3.6.
注：JVM不允许动态重载一个类。 一旦类加载器加载一个类，它就不能在运行时重新加载该类的修改版本。 因此，在JVM加载之后，您不能改变类的定义。 但是，JPDA（Java平台调试器体系结构）为重新加载类提供了有限的能力。 参见第3.6节。

If the same class file is loaded by two distinct class loaders, the JVM makes two distinct classes with the same name and definition. The two classes are regarded as different ones. Since the two classes are not identical, an instance of one class is not assignable to a variable of the other class. The cast operation between the two classes fails and throws a ClassCastException.
如果同一个类文件是由两个不同的类加载器加载的，则JVM将创建两个具有相同名称和定义的不同类。 这两个类集被认为是不同的类集。 由于两个类不相同，一个类的实例不能分配给另一个类的变量。 两个类之间的转换操作失败并抛出ClassCastException。

For example, the following code snippet throws an exception:
例如，下面的代码片段会引发一个异常：

```java
MyClassLoader myLoader = new MyClassLoader();
Class clazz = myLoader.loadClass("Box");
Object obj = clazz.newInstance();
Box b = (Box)obj;    // this always throws ClassCastException.
```

The Box class is loaded by two class loaders. Suppose that a class loader CL loads a class including this code snippet. Since this code snippet refers to MyClassLoader, Class, Object, and Box, CL also loads these classes (unless it delegates to another class loader). Hence the type of the variable b is the Box class loaded by CL. On the other hand, myLoader also loads the Box class. The object obj is an instance of the Box class loaded by myLoader. Therefore, the last statement always throws a ClassCastException since the class of obj is a different verison of the Box class from one used as the type of the variable b.
Box类由两个类加载器加载。 假设一个类加载器CL加载一个包含这个代码片段的类。 由于此代码片段引用了MyClassLoader，Class，Object和Box，因此CL还会加载这些类（除非委托给另一个类加载器）。 因此，变量b的类型是由CL加载的Box类。 另一方面，myLoader也加载Box类。 obj对象是由myLoader加载的Box类的一个实例。 因此，最后一条语句总是抛出一个ClassCastException，因为obj类是Box类的一个不同的版本，与用作变量b类型的不同。

Multiple class loaders form a tree structure. Each class loader except the bootstrap loader has a parent class loader, which has normally loaded the class of that child class loader. Since the request to load a class can be delegated along this hierarchy of class loaders, a class may be loaded by a class loader that you do not request the class loading. Therefore, the class loader that has been requested to load a class C may be different from the loader that actually loads the class C. For distinction, we call the former loader the initiator of C and we call the latter loader the real loader of C.
多个类加载器形成一个树形结构。 除了引导加载程序之外，每个类加载程序都有一个父类加载程序，通常加载该子类加载程序的类。 由于加载类的请求可以沿着这个类加载器的层次结构进行委托，所以一个类可以通过一个类加载器加载，而不需要加载类。 因此，被请求加载类C的类加载器可能与实际加载类C的加载器不同。为了区分，我们称前加载器为C的启动器，我们称后加载器为C的实际加载器。

Furthermore, if a class loader CL requested to load a class C (the initiator of C) delegates to the parent class loader PL, then the class loader CL is never requested to load any classes referred to in the definition of the class C. CL is not the initiator of those classes. Instead, the parent class loader PL becomes their initiators and it is requested to load them. The classes that the definition of a class C referes to are loaded by the real loader of C.
此外，如果请求加载类C（C的发起者）的类加载器CL委派给父类加载器PL，则类加载器CL永远不会被请求加载在类C CL的定义中引用的任何类。 不是这些类的发起者。 相反，父类加载器PL成为它们的启动器，并且请求加载它们。 C类的定义所参照的类是由C的实际加载器加载的。

To understand this behavior, let's consider the following example.
为了理解这个行为，我们来考虑下面的例子。

```java
public class Point {    // loaded by PL
  private int x, y;
  public int getX() { return x; }
        :
}
public class Box {      // the initiator is L but the real loader is PL
  private Point upperLeft, size;
  public int getBaseX() { return upperLeft.x; }
  :
}
public class Window {    // loaded by a class loader L
  private Box box;
  public int getBaseX() { return box.getBaseX(); }
}
```

Suppose that a class Window is loaded by a class loader L. Both the initiator and the real loader of Window are L. Since the definition of Window refers to Box, the JVM will request L to load Box. Here, suppose that L delegates this task to the parent class loader PL. The initiator of Box is L but the real loader is PL. In this case, the initiator of Point is not L but PL since it is the same as the real loader of Box. Thus L is never requested to load Point.
假设一个类Window由一个类加载器L加载.Window的启动器和实际加载器都是L.由于Window的定义指向Box，所以JVM将请求L加载Box。 这里，假设L将这个任务委托给父类加载器PL。 Box的发起者是L，但真正的装载者是PL。 在这种情况下，Point的发起者不是L，而是PL，因为它与Box的真实加载器相同。 因此L永远不会被要求加载点。

Next, let's consider a slightly modified example.
接下来，让我们考虑一个稍微修改的例子。

```java
public class Point {
  private int x, y;
  public int getX() { return x; }
        :
}
public class Box {      // the initiator is L but the real loader is PL
  private Point upperLeft, size;
  public Point getSize() { return size; }
  :
}
public class Window {    // loaded by a class loader L
  private Box box;
  public boolean widthIs(int w) {
  Point p = box.getSize();
    return w == p.getX();
  }
}
```

Now, the definition of Window also refers to Point. In this case, the class loader L must also delegate to PL if it is requested to load Point. You must avoid having two class loaders doubly load the same class. One of the two loaders must delegate to the other.
现在Window的定义也指向Point。 在这种情况下，如果请求加载Point，则类加载器L也必须委托给PL。 你必须避免让两个类加载器加载同一个类。 两个装载者之一必须委托给另一个。

If L does not delegate to PL when Point is loaded, widthIs() would throw a ClassCastException. Since the real loader of Box is PL, Point referred to in Box is also loaded by PL. Therefore, the resulting value of getSize() is an instance of Point loaded by PL whereas the type of the variable p in widthIs() is Point loaded by L. The JVM regards them as distinct types and thus it throws an exception because of type mismatch.
如果L在加载Point时没有委托给PL，则widthIs（）将引发ClassCastException。 由于Box的实际加载器是PL，因此Box中的Point也由PL加载。 因此，getSize（）的结果值是由PL加载的Point实例，而widthIs（）中的变量p的类型是由L加载的Point。JVM将它们视为不同的类型，因此它会引发异常不匹配。

This behavior is somewhat inconvenient but necessary. If the following statement:
这种行为有些不方便，但是必要的。 如果以下声明：

```java
Point p = box.getSize();
```

did not throw an exception, then the programmer of Window could break the encapsulation of Point objects. For example, the field x is private in Point loaded by PL. However, the Window class could directly access the value of x if L loads Point with the following definition:
没有抛出异常，那么Window的程序员可能会破坏Point对象的封装。 例如，字段x在由PL加载的Point中是私有的。 但是，如果L用以下定义加载Point，则Window类可以直接访问x的值：

```java
public class Point {
  public int x, y;    // not private
  public int getX() { return x; }
        :
}
```

For more details of class loaders in Java, the following paper would be helpful:
有关Java中类加载器的更多细节，以下文章将有所帮助：
> Sheng Liang和Gilad Bracha，“Java虚拟机中的动态类加载”，ACM OOPSLA'98，pp.36-44,1998。

### Using javassist.Loader (使用javassist.Loader)

Javassist provides a class loader javassist.Loader. This class loader uses a javassist.ClassPool object for reading a class file.
Javassist提供了一个类加载器javassist.Loader。 这个类加载器使用javassist.ClassPool对象来读取一个类文件。

For example, javassist.Loader can be used for loading a particular class modified with Javassist.
例如，javassist.Loader可用于加载用Javassist修改的特定类。

```java
import javassist.*;
import test.Rectangle;
public class Main {
  public static void main(String[] args) throws Throwable {
    ClassPool pool = ClassPool.getDefault();
    Loader cl = new Loader(pool);

    CtClass ct = pool.get("test.Rectangle");
    ct.setSuperclass(pool.get("test.Point"));

    Class c = cl.loadClass("test.Rectangle");
    Object rect = c.newInstance();
    // :
  }
}
```

This program modifies a class test.Rectangle. The superclass of test.Rectangle is set to a test.Point class. Then this program loads the modified class, and creates a new instance of the test.Rectangle class.
这个程序修改了一个类test.Rectangle。 test.Rectangle的超类设置为test.Point类。 然后这个程序加载修改过的类，并创建一个新的test.Rectangle类的实例。

If the users want to modify a class on demand when it is loaded, the users can add an event listener to a javassist.Loader. The added event listener is notified when the class loader loads a class. The event-listener class must implement the following interface:
如果用户想要在加载时按需修改类，则可以将一个事件侦听器添加到javassist.Loader。 当类加载器加载一个类时，通知添加的事件监听器。 事件侦听器类必须实现以下接口：

```java
public interface Translator {
  public void start(ClassPool pool)
    throws NotFoundException, CannotCompileException;
  public void onLoad(ClassPool pool, String classname)
    throws NotFoundException, CannotCompileException;
}
```

The method start() is called when this event listener is added to a javassist.Loader object by addTranslator() in javassist.Loader. The method onLoad() is called before javassist.Loader loads a class. onLoad() can modify the definition of the loaded class.
当通过javassist.Loader中的addTranslator（）将此事件侦听器添加到javassist.Loader对象时，会调用start（）方法。 在javassist.Loader加载一个类之前调用onLoad（）方法。 onLoad（）可以修改加载类的定义。

For example, the following event listener changes all classes to public classes just before they are loaded.
例如，以下事件侦听器在加载之前将所有类更改为公共类。

```java
public class MyTranslator implements Translator {
  void start(ClassPool pool)
    throws NotFoundException, CannotCompileException {}
  void onLoad(ClassPool pool, String classname)
    throws NotFoundException, CannotCompileException
  {
    CtClass cc = pool.get(classname);
    cc.setModifiers(Modifier.PUBLIC);
  }
}
```

Note that `onLoad()` does not have to call `toBytecode()` or `writeFile()` since `javassist.Loader` calls these methods to obtain a class file.
请注意`onLoad（）`不必调用`toBytecode（）`或`writeFile（）`，因为`javassist.Loader`调用这些方法来获得一个类文件。

To run an application class MyApp with a MyTranslator object, write a main class as following:
要使用MyTranslator对象运行应用程序类MyApp，请按如下所示编写一个主类：

```java
import javassist.*;
public class Main2 {
  public static void main(String[] args) throws Throwable {
    Translator t = new MyTranslator();
    ClassPool pool = ClassPool.getDefault();
    Loader cl = new Loader();
    cl.addTranslator(pool, t);
    cl.run("MyApp", args);
  }
}
```

To run this program, do:
要运行这个程序，请执行：

```
% java Main2 arg1 arg2...
```

The class MyApp and the other application classes are translated by MyTranslator.
MyApp类和其他应用程序类由MyTranslator翻译。

Note that application classes like MyApp cannot access the loader classes such as Main2, MyTranslator, and ClassPool because they are loaded by different loaders. The application classes are loaded by javassist.Loader whereas the loader classes such as Main2 are by the default Java class loader.
请注意，像MyApp这样的应用程序类不能访问诸如Main2，MyTranslator和ClassPool的加载器类，因为它们是由不同的加载器加载的。 应用程序类由javassist.Loader加载，而像Main2这样的加载器类则由默认的Java类加载器加载。

javassist.Loader searches for classes in a different order from java.lang.ClassLoader. ClassLoader first delegates the loading operations to the parent class loader and then attempts to load the classes only if the parent class loader cannot find them. On the other hand, javassist.Loader attempts to load the classes before delegating to the parent class loader. It delegates only if:
javassist.Loader以与java.lang.ClassLoader不同的顺序搜索类。 ClassLoader首先将加载操作委托给父类加载器，然后仅当父类加载器找不到时才尝试加载类。 另一方面，javassist.Loader尝试在委托父类加载器之前加载这些类。 只有在下列情况下，

* the classes are not found by calling get() on a ClassPool object, or
* the classes have been specified by using delegateLoadingOf() to be loaded by the parent class loader.
* 通过调用ClassPool对象的get（）方法找不到这些类
* 通过使用delegateLoadingOf（）来指定父类加载器加载的类。

This search order allows loading modified classes by Javassist. However, it delegates to the parent class loader if it fails to find modified classes for some reason. Once a class is loaded by the parent class loader, the other classes referred to in that class will be also loaded by the parent class loader and thus they are never modified. Recall that all the classes referred to in a class C are loaded by the real loader of C. If your program fails to load a modified class, you should make sure whether all the classes using that class have been loaded by javassist.Loader.
这个搜索顺序允许Javassist加载修改后的类。 但是，如果由于某种原因无法找到修改的类，它将委托给父类加载器。 一旦一个类被父类加载器加载，那么在该类中引用的其他类也将由父类加载器加载，因此它们不会被修改。 回想一下，C类中引用的所有类都由C的真实加载器加载。如果程序未能加载修改后的类，则应确保使用该类的所有类是否已由javassist.Loader加载。

### Writing a class loader (编写一个类加载器)

A simple class loader using Javassist is as follows:
一个简单的使用Javassist的类加载器如下所示：

```java
import javassist.*;
public class SampleLoader extends ClassLoader {
    /* Call MyApp.main(). */
    public static void main(String[] args) throws Throwable {
        SampleLoader s = new SampleLoader();
        Class c = s.loadClass("MyApp");
        c.getDeclaredMethod("main", new Class[] { String[].class })
            .invoke(null, new Object[] { args });
    }

  private ClassPool pool;

  public SampleLoader() throws NotFoundException {
    pool = new ClassPool();
    pool.insertClassPath("./class"); // <em>MyApp.class must be there.</em>
  }

  /* Finds a specified class.
   * The bytecode for that class can be modified.
   */
  protected Class findClass(String name) throws ClassNotFoundException {
    try {
        CtClass cc = pool.get(name);
        // <em>modify the CtClass object here</em>
        byte[] b = cc.toBytecode();
        return defineClass(name, b, 0, b.length);
    } catch (NotFoundException e) {
        throw new ClassNotFoundException();
    } catch (IOException e) {
        throw new ClassNotFoundException();
    } catch (CannotCompileException e) {
        throw new ClassNotFoundException();
    }
  }
}
```

The class MyApp is an application program. To execute this program, first put the class file under the ./class directory, which must not be included in the class search path. Otherwise, MyApp.class would be loaded by the default system class loader, which is the parent loader of SampleLoader. The directory name ./class is specified by insertClassPath() in the constructor. You can choose a different name instead of ./class if you want. Then do as follows:
MyApp类是一个应用程序。 要执行这个程序，首先把类文件放在./class目录下，这个目录不能包含在类搜索路径中。 否则，MyApp.class将被默认的系统类加载器（它是SampleLoader的父加载器）加载。 目录名./class由构造函数中的insertClassPath（）指定。 如果你愿意，你可以选择一个不同的名字而不是./class。 然后执行如下操作：

```
% java SampleLoader
```

The class loader loads the class MyApp (./class/MyApp.class) and calls MyApp.main() with the command line parameters.
类加载器加载类MyApp（./class/MyApp.class），并用命令行参数调用MyApp.main（）。

This is the simplest way of using Javassist. However, if you write a more complex class loader, you may need detailed knowledge of Java's class loading mechanism. For example, the program above puts the MyApp class in a name space separated from the name space that the class SampleLoader belongs to because the two classes are loaded by different class loaders. Hence, the MyApp class cannot directly access the class SampleLoader.
这是使用Javassist最简单的方法。 但是，如果您编写更复杂的类加载器，则可能需要详细了解Java的类加载机制。 例如，上面的程序将MyApp类放入与SampleLoader类所属的名称空间分离的名称空间中，因为这两个类是由不同的类加载器加载的。 因此，MyApp类不能直接访问类SampleLoader。

### Modifying a system class (修改系统类)

The system classes like java.lang.String cannot be loaded by a class loader other than the system class loader. Therefore, SampleLoader or javassist.Loader shown above cannot modify the system classes at loading time.
像java.lang.String这样的系统类不能被系统类加载器以外的类加载器加载。 因此，上面显示的SampleLoader或javassist.Loader不能在加载时修改系统类。

If your application needs to do that, the system classes must be statically modified. For example, the following program adds a new field hiddenValue to java.lang.String:
如果您的应用程序需要这样做，系统类必须进行静态修改。 例如，下面的程序将一个新的字段hiddenValue添加到java.lang.String中：

```java
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("java.lang.String");
CtField f = new CtField(CtClass.intType, "hiddenValue", cc);
f.setModifiers(Modifier.PUBLIC);
cc.addField(f);
cc.writeFile(".");
```

This program produces a file "./java/lang/String.class".
这个程序生成一个文件“./java/lang/String.class”。

To run your program MyApp with this modified String class, do as follows:
要使用此修改后的String类运行程序MyApp，请执行以下操作：

```
% java -Xbootclasspath/p:. MyApp arg1 arg2...
```

Suppose that the definition of MyApp is as follows:
假设MyApp的定义如下：

```java
public class MyApp {
    public static void main(String[] args) throws Exception {
        System.out.println(String.class.getField("hiddenValue").getName());
    }
}
```

If the modified String class is correctly loaded, MyApp prints hiddenValue.
如果修改后的String类被正确加载，MyApp将打印hiddenValue。

Note: Applications that use this technique for the purpose of overriding a system class in rt.jar should not be deployed as doing so would contravene the Java 2 Runtime Environment binary code license.
注意：不应该使用这种技术来覆盖rt.jar中的系统类，因为这样做会违反Java 2 Runtime Environment二进制代码许可证。

### Reloading a class at runtime (在运行时重新加载类)

If the JVM is launched with the JPDA (Java Platform Debugger Architecture) enabled, a class is dynamically reloadable. After the JVM loads a class, the old version of the class definition can be unloaded and a new one can be reloaded again. That is, the definition of that class can be dynamically modified during runtime. However, the new class definition must be somewhat compatible to the old one. The JVM does not allow schema changes between the two versions. They have the same set of methods and fields.
如果在启用JPDA（Java平台调试器体系结构）的情况下启动JVM，则可以动态地重新加载一个类。 在JVM加载一个类之后，可以卸载旧版本的类定义，并且可以重新加载一个新的类。 也就是说，该类的定义可以在运行时动态修改。 但是，新的类定义必须与旧的定义兼容。 JVM不允许在两个版本之间进行架构更改。 他们有相同的方法和领域。

Javassist provides a convenient class for reloading a class at runtime. For more information, see the API documentation of javassist.tools.HotSwapper.
Javassist为在运行时重新加载类提供了一个方便的类。 有关更多信息，请参阅javassist.tools.HotSwapper的API文档。

## Introspection and customization (自我检查和定制)

CtClass provides methods for introspection. The introspective ability of Javassist is compatible with that of the Java reflection API. CtClass provides getName(), getSuperclass(), getMethods(), and so on. CtClass also provides methods for modifying a class definition. It allows adding a new field, constructor, and method. Instrumenting a method body is also possible.
CtClass提供了自我检查的方法。 Javassist的自我检查能力与Java反射API兼容。 CtClass提供了getName（），getSuperclass（），getMethods（）等。 CtClass还提供了修改类定义的方法。 它允许添加一个新的字段，构造函数和方法。 检测方法体也是可能的。

Methods are represented by CtMethod objects. CtMethod provides several methods for modifying the definition of the method. Note that if a method is inherited from a super class, then the same CtMethod object that represents the inherited method represents the method declared in that super class. A CtMethod object corresponds to every method declaration.
方法由CtMethod对象表示。 CtMethod提供了几种修改方法定义的方法。 请注意，如果方法是从超类继承的，则表示继承方法的相同CtMethod对象表示在该超类中声明的方法。 CtMethod对象对应于每个方法声明。

For example, if class Point declares method move() and a subclass ColorPoint of Point does not override move(), the two move() methods declared in Point and inherited in ColorPoint are represented by the identical CtMethod object. If the method definition represented by this CtMethod object is modified, the modification is reflected on both the methods. If you want to modify only the move() method in ColorPoint, you first have to add to ColorPoint a copy of the CtMethod object representing move() in Point. A copy of the the CtMethod object can be obtained by CtNewMethod.copy().
例如，如果类Point声明方法move（）并且Point的子类ColorPoint不覆盖move（），则在Point中声明并在ColorPoint中继承的两个move（）方法由相同的CtMethod对象表示。 如果修改了此CtMethod对象表示的方法定义，则修改将反映在这两个方法上。 如果只想修改ColorPoint中的move（）方法，则首先必须向ColorPoint添加表示Point中的move（）的CtMethod对象的副本。 CtMethod对象的副本可以通过CtNewMethod.copy（）获得。

Javassist does not allow removing a method or field, but it allows changing the name. So if a method is not necessary any more, it should be renamed and changed to be a private method by calling setName() and setModifiers() declared in CtMethod.
Javassist不允许删除方法或字段，但它允许更改名称。 所以如果一个方法不再是必需的，应该通过调用在CtMethod中声明的setName（）和setModifiers（）来重命名并更改为私有方法。

Javassist does not allow adding an extra parameter to an existing method, either. Instead of doing that, a new method receiving the extra parameter as well as the other parameters should be added to the same class. For example, if you want to add an extra int parameter newZ to a method:
Javassist不允许向现有方法添加额外的参数。如果要实现这种功能，一个新的方法接收额外的参数以及其他参数应该被添加到同一个类。 例如，如果你想添加一个额外的int参数newZ到一个方法：

```java
void move(int newX, int newY) { x = newX; y = newY; }
```

in a Point class, then you should add the following method to the Point class:
在Point类中，则应该将以下方法添加到Point类中：

```
void move(int newX, int newY, int newZ) {
    // do what you want with newZ.
    move(newX, newY);
}
```

Javassist also provides low-level API for directly editing a raw class file. For example, getClassFile() in CtClass returns a ClassFile object representing a raw class file. getMethodInfo() in CtMethod returns a MethodInfo object representing a method_info structure included in a class file. The low-level API uses the vocabulary from the Java Virtual machine specification. The users must have the knowledge about class files and bytecode. For more details, the users should see the javassist.bytecode package.
Javassist还提供低级API以直接编辑原始类文件。 例如，CtClass中的getClassFile（）返回表示原始类文件的ClassFile对象。 CtMethod中的getMethodInfo（）返回一个MethodInfo对象，表示一个包含在类文件中的method_info结构。低级API使用Java虚拟机规范中的词汇表。用户必须具有关于类文件和字节码的知识。 有关更多详细信息，用户应该看到javassist.bytecode包。

The class files modified by Javassist requires the javassist.runtime package for runtime support only if some special identifiers starting with $ are used. Those special identifiers are described below. The class files modified without those special identifiers do not need the javassist.runtime package or any other Javassist packages at runtime. For more details, see the API documentation of the javassist.runtime package.
由Javassist修改的类文件只有在使用以$开头的特殊标识符时才需要javassist.runtime包来支持运行时。 这些特殊的标识符如下所述。 在没有这些特殊标识符的情况下修改的类文件在运行时不需要javassist.runtime包或任何其他Javassist包。 有关更多详细信息，请参阅javassist.runtime包的API文档。

### Inserting source text at the beginning/end of a method body (在方法主体的开始/结尾处插入源文本)

CtMethod and CtConstructor provide methods insertBefore(), insertAfter(), and addCatch(). They are used for inserting a code fragment into the body of an existing method. The users can specify those code fragments with source text written in Java. Javassist includes a simple Java compiler for processing source text. It receives source text written in Java and compiles it into Java bytecode, which will be inlined into a method body.
CtMethod和CtConstructor提供了insertBefore（），insertAfter（）和addCatch（）方法。 它们用于将代码片段插入到现有方法的主体中。 用户可以用Java编写的源文本指定这些代码片段。 Javassist包含一个用于处理源文本的简单Java编译器。 它接收用Java编写的源文本并将其编译为Java字节码，该字节码将被内联到方法体中。

Inserting a code fragment at the position specified by a line number is also possible (if the line number table is contained in the class file). insertAt() in CtMethod and CtConstructor takes source text and a line number in the source file of the original class definition. It compiles the source text and inserts the compiled code at the line number.
在由行号指定的位置插入代码片段也是可能的（如果行号表包含在类文件中）。 CtMethod和CtConstructor中的insertAt（）将原始类定义的源文件中的源文本和行号取回。 它编译源文本并在行号处插入编译的代码。

The methods insertBefore(), insertAfter(), addCatch(), and insertAt() receive a String object representing a statement or a block. A statement is a single control structure like if and while or an expression ending with a semi colon (;). A block is a set of statements surrounded with braces {}. Hence each of the following lines is an example of valid statement or block:
insertBefore（），insertAfter（），addCatch（）和insertAt（）方法接收表示语句或块的String对象。 声明是一个单一的控制结构，如if和while或以分号（;）结尾的表达式。 块是用大括号{}包围的一组语句。 因此，以下每行都是有效语句或块的示例：

```java
System.out.println("Hello");
{ System.out.println("Hello"); }
if (i < 0) { i = -i; }
```

The statement and the block can refer to fields and methods. They can also refer to the parameters to the method that they are inserted into if that method was compiled with the -g option (to include a local variable attribute in the class file). Otherwise, they must access the method parameters through the special variables $0, $1, $2, ... described below. Accessing local variables declared in the method is not allowed although declaring a new local variable in the block is allowed. However, insertAt() allows the statement and the block to access local variables if these variables are available at the specified line number and the target method was compiled with the -g option.
语句和块可以参考字段和方法。 如果使用-g选项编译该方法（在类文件中包括本地变量属性），则也可以将参数引用到插入的方法中。 否则，他们必须通过下面描述的特殊变量$ 0，$ 1，$ 2，...来访问方法参数。 访问方法中声明的局部变量是不允许的，但是允许在块中声明一个新的局部变量。 但是，insertAt（）允许语句和块访问本地变量（如果这些变量在指定行号可用并且目标方法是使用-g选项编译的）。

The String object passed to the methods `insertBefore()`, `insertAfter()`, `addCatch()`, and `insertAt()` are compiled by the compiler included in Javassist. Since the compiler supports language extensions, several identifiers starting with $ have special meaning:
传递给方法`insertBefore（）`，`insertAfter（）`，`addCatch（）`和`insertAt（）`的String对象由Javassist中包含的编译器编译。 由于编译器支持语言扩展，因此以`$`开头的几个标识符具有特殊含义：

| $0, $1, $2, ... &nbsp &nbsp | this and actual parameters |
| $args | An array of parameters. The type of $args is Object[]. |
| $$ | All actual parameters. |
| &nbsp | For example, m($$) is equivalent to m($1,$2,...) |
| $cflow(...) | cflow variable |
| $r | The result type. It is used in a cast expression. |
| $w | The wrapper type. It is used in a cast expression. |
| $_ | The resulting value |
| $sig | An array of java.lang.Class objects representing the formal parameter types. |
| $type | A java.lang.Class object representing the formal result type. |
| $class | A java.lang.Class object representing the class currently edited. |

#### $0, $1, $2, ...

The parameters passed to the target method are accessible with $1, $2, ... instead of the original parameter names. $1 represents the first parameter, $2 represents the second parameter, and so on. The types of those variables are identical to the parameter types. $0 is equivalent to this. If the method is static, $0 is not available.
传递给目标方法的参数可以用$ 1，$ 2 ...来访问，而不是原始的参数名称。 $ 1表示第一个参数，$ 2表示第二个参数，依此类推。 这些变量的类型与参数类型相同。 $ 0相当于这个。 如果该方法是静态的，则$ 0不可用。

These variables are used as following. Suppose that a class Point:
这些变量的用法如下。 假设一个类Point：

```java
class Point {
    int x, y;
    void move(int dx, int dy) { x += dx; y += dy; }
}
```

To print the values of dx and dy whenever the method move() is called, execute this program:
要调用move（）方法时，要打印dx和dy的值，请执行以下程序：

```java
ClassPool pool = ClassPool.getDefault();
CtClass cc = pool.get("Point");
CtMethod m = cc.getDeclaredMethod("move");
m.insertBefore("{ System.out.println($1); System.out.println($2); }");
cc.writeFile();
```

Note that the source text passed to insertBefore() is surrounded with braces {}. insertBefore() accepts only a single statement or a block surrounded with braces.
请注意，传递给insertBefore（）的源文本被大括号{}包围。 insertBefore（）只接受单个语句或用大括号包围的块。

The definition of the class Point after the modification is like this:
修改后的类Point的定义如下：

```java
class Point {
    int x, y;
    void move(int dx, int dy) {
        { System.out.println(dx); System.out.println(dy); }
        x += dx; y += dy;
    }
}
```

$1 and $2 are replaced with dx and dy, respectively.
$1, $2, $3 ... are updatable. If a new value is assigend to one of those variables, then the value of the parameter represented by that variable is also updated.

$ 1和$ 2分别替换为dx和dy。
$ 1，$ 2，$ 3 ...是可更新的。 如果一个新值被分配给这些变量之一，那么由该变量表示的参数的值也被更新。

#### $args

The variable $args represents an array of all the parameters. The type of that variable is an array of class Object. If a parameter type is a primitive type such as int, then the parameter value is converted into a wrapper object such as java.lang.Integer to store in $args. Thus, $args[0] is equivalent to $1 unless the type of the first parameter is a primitive type. Note that $args[0] is not equivalent to $0; $0 represents `this`.
变量$args表示所有参数的数组。 该变量的类型是一个Object类的数组。 如果参数类型是基本类型（如int），那么将参数值转换为包装对象（如java.lang.Integer）以存储在$ args中。 因此，$args[0]相当于$ 1，除非第一个参数的类型是基本类型。 请注意，$args[0]不等于$ 0; $0表示`this`。

If an array of Object is assigned to $args, then each element of that array is assigned to each parameter. If a parameter type is a primitive type, the type of the corresponding element must be a wrapper type. The value is converted from the wrapper type to the primitive type before it is assigned to the parameter.
如果一个Object数组被分配给$args，那么该数组的每个元素都被分配给每个参数。 如果参数类型是基本类型，则相应元素的类型必须是包装类型。 在将值分配给参数之前，该值将从包装器类型转换为基元类型。

#### $$

The variable $$ is abbreviation of a list of all the parameters separated by commas. For example, if the number of the parameters to method move() is three, then
变量$$是以逗号分隔的所有参数的列表的缩写。 例如，如果方法move（）的参数数量是3，那么

```java
move($$)
```

is equivalent to this:
相当于这个：

```java
move($1, $2, $3)
```

If move() does not take any parameters, then move($$) is equivalent to move().
如果move（）不带任何参数，那么move（$$）等同于move（）。

$$ can be used with another method. If you write an expression:
$$可以与其他方法一起使用。 如果你写一个表达式：

```java
exMove($$, context)
```

then this expression is equivalent to:
那么这个表达式相当于：

```java
exMove($1, $2, $3, context)
```

Note that $$ enables generic notation of method call with respect to the number of parameters. It is typically used with $proceed shown later.
请注意，$$允许方法调用的通用符号表示参数的数量。 它通常与稍后显示的$proceed一起使用。

#### $cflow

$cflow means "control flow". This read-only variable returns the depth of the recursive calls to a specific method.
$cflow表示“控制流”。 此只读变量将递归调用的深度返回给特定的方法。

Suppose that the method shown below is represented by a CtMethod object cm:
假设下面显示的方法由CtMethod对象cm表示：

```java
int fact(int n) {
    if (n <= 1)
        return n;
    else
        return n * fact(n - 1);
}
```

To use $cflow, first declare that $cflow is used for monitoring calls to the method fact():
要使用$cflow，首先声明$cflow用于监视方法fact（）的调用：

```java
CtMethod cm = ...;
cm.useCflow("fact");
```

The parameter to useCflow() is the identifier of the declared $cflow variable. Any valid Java name can be used as the identifier. Since the identifier can also include . (dot), for example, "my.Test.fact" is a valid identifier.
useCflow（）的参数是声明的$cflow变量的标识符。 任何有效的Java名称都可以用作标识符。 由于标识符也可以包含`.`（点），例如，“my.Test.fact”是一个有效的标识符。

Then, $cflow(fact) represents the depth of the recursive calls to the method specified by cm. The value of $cflow(fact) is 0 (zero) when the method is first called whereas it is 1 when the method is recursively called within the method. For example,
然后，$ cflow（fact）表示对由cm指定的方法的递归调用的深度。 当方法被第一次调用时，$ cflow（fact）的值是0（零），而当方法在方法中被递归调用时，它的值是1。 例如，

```java
cm.insertBefore("if ($cflow(fact) == 0)"
              + "    System.out.println(\"fact \" + $1);");
```

translates the method fact() so that it shows the parameter. Since the value of $cflow(fact) is checked, the method fact() does not show the parameter if it is recursively called within fact().
翻译方法fact（），以便显示参数。 由于$ cflow（fact）的值被检查，如果在fact（）中递归调用，方法fact（）不显示参数。

The value of $cflow is the number of stack frames associated with the specified method cm under the current topmost stack frame for the current thread. $cflow is also accessible within a method different from the specified method cm.
$cflow的值是与当前线程的当前最高堆栈帧下的指定方法cm相关联的堆栈帧的数量。 $ cflow也可以在不同于指定方法cm的方法中访问。

#### $r

$r represents the result type (return type) of the method. It must be used as the cast type in a cast expression. For example, this is a typical use:
$ r表示方法的结果类型（返回类型）。 它必须用作转换表达式中的转换类型。 例如，这是一个典型的用途：

```java
Object result = ... ;
$_ = ($r)result;
```

If the result type is a primitive type, then ($r) follows special semantics. First, if the operand type of the cast expression is a primitive type, ($r) works as a normal cast operator to the result type. On the other hand, if the operand type is a wrapper type, ($r) converts from the wrapper type to the result type. For example, if the result type is int, then ($r) converts from java.lang.Integer to int.
如果结果类型是基本类型，那么（$ r）遵循特殊的语义。 首先，如果转换表达式的操作数类型是原始类型，则（$ r）作为结果类型的普通转换运算符。 另一方面，如果操作数类型是包装类型，则（$ r）将从包装类型转换为结果类型。 例如，如果结果类型是int，则（$ r）将从java.lang.Integer转换为int。

If the result type is void, then ($r) does not convert a type; it does nothing. However, if the operand is a call to a void method, then ($r) results in null. For example, if the result type is void and foo() is a void method, then
如果结果类型是void，那么（$ r）不会转换类型; 它什么都不做。 但是，如果操作数是对void方法的调用，则（$ r）的结果为null。 例如，如果结果类型是void而foo（）是一个void方法，那么

```java
$_ = ($r)foo();
```

is a valid statement.
是一个有效的陈述。

The cast operator ($r) is also useful in a return statement. Even if the result type is void, the following return statement is valid:
转换运算符（$ r）在返回语句中也很有用。 即使结果类型是无效的，下面的返回语句也是有效的：

```java
return ($r)result;
```

Here, result is some local variable. Since ($r) is specified, the resulting value is discarded. This return statement is regarded as the equivalent of the return statement without a resulting value:
在这里，结果是一些局部变量。 由于（$ r）被指定，所以产生的值被丢弃。 这个返回语句被认为是没有结果值的返回语句的等价物：

```java
return;
```

#### $w

$w represents a wrapper type. It must be used as the cast type in a cast expression. ($w) converts from a primitive type to the corresponding wrapper type.
The following code is an example:
$ w表示一个包装类型。 它必须用作演员表演中的演员类型。 （$ w）从原始类型转换为相应的包装器类型。
下面的代码是一个例子：

```java
Integer i = ($w)5;
```

The selected wrapper type depends on the type of the expression following ($w). If the type of the expression is double, then the wrapper type is java.lang.Double.
If the type of the expression following ($w) is not a primitive type, then ($w) does nothing.

所选的包装类型取决于（$ w）后面的表达式的类型。 如果表达式的类型是double，那么包装类型是java.lang.Double。
如果（$ w）后面的表达式的类型不是原始类型，那么（$ w）不做任何事情。

#### `$_`

insertAfter() in CtMethod and CtConstructor inserts the compiled code at the end of the method. In the statement given to insertAfter(), not only the variables shown above such as `$0`, `$1`, ... but also `$_` is available.
CtMethod中的insertAfter（）和CtConstructor将编译后的代码插入到方法的末尾。 在赋给insertAfter（）的语句中，不仅上面显示的变量（如`$0`，`$1`，...还有`$_`）是可用的。

The variable `$_` represents the resulting value of the method. The type of that variable is the type of the result type (the return type) of the method. If the result type is void, then the type of `$_` is Object and the value of `$_` is null.
变量`$_`表示方法的结果值。 该变量的类型是该方法的结果类型（返回类型）的类型。 如果结果类型是void，那么`$_`的类型是Object，`$_`的值是null。

Although the compiled code inserted by insertAfter() is executed just before the control normally returns from the method, it can be also executed when an exception is thrown from the method. To execute it when an exception is thrown, the second parameter asFinally to insertAfter() must be true.
尽管insertAfter（）插入的编译代码在控件通常从方法返回之前执行，但是当方法抛出异常时也可以执行该代码。 要在引发异常时执行它，第二个参数（最后为insertAfter（））必须为true。

If an exception is thrown, the compiled code inserted by insertAfter() is executed as a finally clause. The value of `$_` is 0 or null in the compiled code. After the execution of the compiled code terminates, the exception originally thrown is re-thrown to the caller. Note that the value of `$_` is never thrown to the caller; it is rather discarded.
如果引发异常，insertAfter（）插入的编译代码将作为finally子句执行。 `$_`的值在编译的代码中为0或null。 编译后的代码执行终止后，最初抛出的异常被重新抛出给调用者。 请注意`$_`的值永远不会被抛给调用者。 这是相当丢弃。

#### `$sig`

The value of `$sig` is an array of `java.lang.Class` objects that represent the formal parameter types in declaration order.
`$ sig`的值是一个`java.lang.Class`对象的数组，代表了声明顺序中的形式参数类型。

#### `$type`

The value of `$type` is an `java.lang.Class` object representing the formal type of the result value. This variable refers to Void.class if this is a constructor.
`$ type`的值是一个`java.lang.Class`对象，表示结果值的形式类型。 如果这是一个构造函数，这个变量引用Void.class。

#### `$class`

The value of $class is an java.lang.Class object representing the class in which the edited method is declared. This represents the type of $0.
`$ class`的值是一个`java.lang.Class`对象，表示声明编辑方法的类。 这代表“$ 0”的类型。

#### `addCatch()`

`addCatch()` inserts a code fragment into a method body so that the code fragment is executed when the method body throws an exception and the control returns to the caller. In the source text representing the inserted code fragment, the exception value is referred to with the special variable `$e`.
addCatch（）将代码片段插入到方法体中，以便在方法体引发异常时执行代码片段，并返回给调用者。 在表示插入的代码段的源文本中，异常值用特殊变量“$ e”引用。

For example, this program:

```java
CtMethod m = ...;
CtClass etype = ClassPool.getDefault().get("java.io.IOException");
m.addCatch("{ System.out.println($e); throw $e; }", etype);
```

translates the method body represented by m into something like this:

```java
try {
  // the original method body
} catch (java.io.IOException e) {
  System.out.println(e);
  throw e;
}
```

Note that the inserted code fragment must end with a `throw` or `return` statement.
请注意，插入的代码段必须以a throw或return语句结尾 。

## Altering a method body(改变一个方法体)

`CtMethod` and `CtConstructor` provide setBody() for substituting a whole method body. They compile the given source text into Java bytecode and substitutes it for the original method body. If the given source text is null, the substituted body includes only a return statement, which returns zero or null unless the result type is void.
`CtMethod`和`CtConstructor`提供 setBody()替换整个方法体。他们将给定的源文本编译成Java字节码，并将其替换为原始方法体。如果给定的源文本是null，则替换的主体只包含一个 return语句，除非结果类型为，否则返回零或空值void。

In the source text given to setBody(), the identifiers starting with $ have special meaning
在给出的源文本中setBody()，开头的标识符`$`具有特殊的含义

| 参数 | 说明 |
| --- | --- |
| $0, $1, $2, ... &nbsp &nbsp | this and actual parameters this 和实际参数 |
| $args | An array of parameters. The type of $args is Object[]. 一组参数。该类型$args是Object[]。|
| $$ | All actual parameters.所有的实际参数。 |
| $cflow(...) | cflow variable. cflow 变量 |
| $r | The result type. It is used in a cast expression.结果类型。它被用在一个表演中。 |
| $w | The wrapper type. It is used in a cast expression.包装类型。它被用在一个表演中。 |
| $sig | An array of java.lang.Class objects representing the formal parameter types.java.lang.Class表示形式参数类型的对象数组。 |
| $type | A java.lang.Class object representing the formal result type. java.lang.Class表示正式结果类型对象。 |
| $class &nbsp | A java.lang.Class object representing the class that declares the method
currently edited (the type of $0). java.lang.Class表示声明
当前编辑的方法的类的对象（$ 0的类型）。 |

Note that `$_`is not available.
请注意，`$_`不可用。

### Substituting source text for an existing expression (用源文本替换现有的表达式)

Javassist allows modifying only an expression included in a method body. javassist.expr.ExprEditor is a class for replacing an expression in a method body. The users can define a subclass of ExprEditor to specify how an expression is modified.
Javassist只允许修改方法体中包含的表达式。 javassist.expr.ExprEditor是用于替换方法体中的表达式的类。用户可以定义一个子类ExprEditor 来指定如何修改表达式。

To run an ExprEditor object, the users must call instrument() in CtMethod or CtClass.
要运行的ExprEditor对象，用户必须调用instrument()的CtMethod或 CtClass。

For example,

```java
CtMethod cm = ... ;
cm.instrument(
    new ExprEditor() {
        public void edit(MethodCall m)
                      throws CannotCompileException
        {
            if (m.getClassName().equals("Point")
                          && m.getMethodName().equals("move"))
                m.replace("{ $1 = 0; $_ = $proceed($$); }");
        }
    });
```

searches the method body represented by cm and replaces all calls to move() in class Point with a block:
搜索由所表示的方法的身体cm并替换到的所有调用move()在类Point 与块：

```java
{ $1 = 0; $_ = $proceed($$); }
```

so that the first parameter to `move()` is always 0. Note that the substituted code is not an expression but a statement or a block. It cannot be or contain a try-catch statement.

The method `instrument()` searches a method body. If it finds an expression such as a method call, field access, and object creation, then it calls `edit()` on the given `ExprEditor` object. The parameter to `edit()` is an object representing the found expression. The `edit()` method can inspect and replace the expression through that object.

Calling `replace()` on the parameter to `edit()` substitutes the given statement or block for the expression. If the given block is an empty block, that is, if `replace("{}")` is executed, then the expression is removed from the method body.

If you want to insert a statement (or a block) before/after the expression, a block like the following should be passed to `replace()`:

所以move（）的第一个参数总是0.请注意，替换后的代码不是表达式，而是语句或块。 它不能是或包含一个try-catch语句。

instrument（）方法搜索一个方法体。 如果它发现一个表达式，例如方法调用，字段访问和对象创建，那么它会在给定的ExprEditor对象上调用edit（）。 edit（）的参数是表示找到的表达式的对象。 edit（）方法可以通过该对象检查和替换表达式。

调用参数的`replace（）`到`edit（）`将给定的语句或块替换为表达式。 如果给定的块是一个空块，也就是说，如果执行`replace（“{}”）`，那么该表达式将从方法主体中移除。

如果你想在表达式之前/之后插入一个语句（或者一个块），那么应该把下面的块传递给`replace（）`：

```java
{ before-statements;
  $_ = $proceed($$);
  after-statements; }
```

whichever the expression is either a method call, field access, object creation, or others. The second statement could be:
无论表达式是方法调用，字段访问，对象创建还是其他。 第二个陈述可能是：

```java
$_ = $proceed();
```

if the expression is read access, or
如果表达式是读访问，或者

```java
$proceed($$);
```



























































