# 动态码加载

Java是一门静态语言，通常，我们需要的class在编译的时候就已经生成了。但是如果要动态生成字节码，自己直接输出二进制格式的字节码，如何做？
在完成这个任务前，必须先认真阅读JVM规范第4章，详细了解class文件结构。
所以，第一种方法，自己动手，从零开始创建字节码，理论上可行，实际上很难。第二种方法，可不可以使用已有的一些能操作字节码的库，帮助我们创建class。
目前，能够操作字节码的开源库主要有[CGLib](https://github.com/cglib/cglib)和[Javassist](https://github.com/jboss-javassist/javassist)两种，它们都提供了比较高级的API来操作字节码，最后输出为class文件。

比如CGLib，典型的用法如下：

```java
Enhancer e = new Enhancer();
e.setSuperclass(...);
e.setStrategy(new DefaultGeneratorStrategy() {
    protected ClassGenerator transform(ClassGenerator cg) {
        return new TransformingGenerator(cg,
            new AddPropertyTransformer(new String[]{ "foo" },
                    new Class[] { Integer.TYPE }));
    }});
Object obj = e.create();
````

比自己生成class要简单。稍后我们会介绍CGLib和Javassist，但是这个都是第三方的开源库。

第三种方式，使用原生Java自带的api来产生字节码。
如果我们能创建`.java`这个源文件，再调用Java编译器，直接把源码编译成class，再加载进虚拟机，就可以实现！
创建一个字符串格式的源码比较简单，就是拼字符串，高级点的做法可以用一个模版引擎。
Java的编译器是javac，但是，在很早很早的时候，Java的编译器就已经用纯Java重写了，自己能编译自己，行业黑话叫“自举”。从Java 1.6开始，编译器接口正式放到JDK的公开API中，于是，我们不需要创建新的进程来调用javac，而是直接使用编译器API来编译源码。

使用起来也很简单：

```java
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
int compilationResult = compiler.run(null, null, null, '/path/to/Test.java');
```
这么写编译是没啥问题，问题是我们在内存中创建了Java代码后，必须先写到文件，再编译，最后还要手动读取class文件内容并用一个ClassLoader加载。

一种更简单的实现方式：其实Java编译器根本不关心源码的内容是从哪来的，你给它一个String当作源码，它就可以输出byte[]作为class的内容。

所以，我们需要参考Java Compiler API的文档，让Compiler直接在内存中完成编译，输出的class内容就是byte[]。

代码改造如下：

```java
Map<String, byte[]> results;
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);
try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
    JavaFileObject javaFileObject = manager.makeStringSource(fileName, source);
    CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
    if (task.call()) {
        results = manager.getClassBytes();
    }
}
```

* 上述代码的几个关键在于：
  - 用MemoryJavaFileManager替换JDK默认的StandardJavaFileManager，以便在编译器请求源码内容时，不是从文件读取，而是直接返回String；
  - 用MemoryOutputJavaFileObject替换JDK默认的SimpleJavaFileObject，以便在接收到编译器生成的byte[]内容时，不写入class文件，而是直接保存在内存中。
  - 编译的结果放在Map<String, byte[]>中，Key是类名，对应的byte[]是class的二进制内容。

* 为什么编译后不是一个byte[]呢？
  - 因为一个`.java`的源文件编译后可能有多个`.class文件`，只要包含了静态类、匿名类等，编译出的class就多于一个。

* 如何加载编译后的class呢？
  - 需要创建一个ClassLoader，覆写findClass()方法：

```java
class MemoryClassLoader extends URLClassLoader {

    Map<String, byte[]> classBytes = new HashMap<String, byte[]>();
    public MemoryClassLoader(Map<String, byte[]> classBytes) {
        super(new URL[0], MemoryClassLoader.class.getClassLoader());
        this.classBytes.putAll(classBytes);
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] buf = classBytes.get(name);
        if (buf == null) {
            return super.findClass(name);
        }
        classBytes.remove(name);
        return defineClass(name, buf, 0, buf.length);
    }
}
```