package com.xplus.commons.compiler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

/**
 * Forwards calls to a given file manager. Subclasses of this class might
 * override some of these methods and might also provide additional fields and
 * methods.
 * <p>
 * 转发给指定文件管理器的调用。 这个类的子类可能会覆盖其中的一些方法，也可能会提供额外的字段和方法。
 * 
 * <code>JavaFileManager</code>
 * <p>
 * File manager for tools operating on Java™ programming language source and
 * class files. In this context, file means an abstraction of regular files and
 * other sources of data.
 * <p>
 * 用于在Java™编程语言源文件和类文件上运行的工具的文件管理器。 在这种情况下，文件意味着对常规文件和其他数据源的抽象。
 * 
 * <p>
 * When constructing new JavaFileObjects, the file manager must determine where
 * to create them. For example, if a file manager manages regular files on a
 * file system, it would most likely have a current/working directory to use as
 * default location when creating or finding files. A number of hints can be
 * provided to a file manager as to where to create files. Any file manager
 * might choose to ignore these hints.
 * 
 * <p>
 * 在构建新的JavaFileObjects时，文件管理器必须确定在哪里创建它们。
 * 例如，如果文件管理器管理文件系统上的常规文件，则在创建或查找文件时，很可能将当前/工作目录用作默认位置。
 * 可以向文件管理器提供许多提示，以便在哪里创建文件。 任何文件管理器可能会选择忽略这些提示。
 * 
 * <p>
 * Some methods in this interface use class names. Such class names must be
 * given in the Java Virtual Machine internal form of fully qualified class and
 * interface names. For convenience '.' and '/' are interchangeable. The
 * internal form is defined in chapter four of The Java™ Virtual Machine
 * Specification.
 * <p>
 * 这个接口中的一些方法使用类名。 这些类名必须在Java虚拟机内部形式的完全限定的类和接口名称中给出。 为了方便 '。' 和'/'是可以互换的。
 * 内部表单在Java™虚拟机规范的第四章中定义。
 */
public class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

  // compiled classes in bytes:
  private final Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

  public MemoryJavaFileManager(JavaFileManager fileManager) {
    super(fileManager);
  }

  public Map<String, byte[]> getClassBytes() {
    return new HashMap<String, byte[]>(this.classBytes);
  }

  @Override
  public void flush() throws IOException {
  }

  @Override
  public void close() throws IOException {
    classBytes.clear();
  }

  /**
   * Gets a file object for output representing the specified class of the
   * specified kind in the given location.
   * <p>
   * 获取表示给定位置中指定类的指定类的输出的文件对象。
   * 
   * <p>
   * Optionally, this file manager might consider the sibling as a hint for where
   * to place the output. The exact semantics of this hint is unspecified. The JDK
   * compiler, javac, for example, will place class files in the same directories
   * as originating source files unless a class file output directory is provided.
   * To facilitate this behavior, javac might provide the originating source file
   * as sibling when calling this method.
   * <p>
   * 可选地，这个文件管理器可以把兄弟节点当作提示放置输出的提示。 这个提示的确切语义是未指定的。
   * 例如，JDK编译器javac将把类文件放在与源文件相同的目录中，除非提供了类文件输出目录。
   * 为了促进这种行为，javac可能会在调用此方法时提供源文件作为兄弟。
   * 
   */
  @Override
  public JavaFileObject getJavaFileForOutput( //
      JavaFileManager.Location location, //
      String className, //
      Kind kind, //
      FileObject sibling // 一个文件对象，用作放置提示
  ) throws IOException {
    if (kind == Kind.CLASS) {
      return new MemoryOutputJavaFileObject(className, classBytes);
    } else {
      return super.getJavaFileForOutput(location, className, kind, sibling);
    }
  }

}
