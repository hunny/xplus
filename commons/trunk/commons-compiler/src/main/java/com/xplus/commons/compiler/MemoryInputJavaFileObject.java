package com.xplus.commons.compiler;

import java.net.URI;
import java.nio.CharBuffer;

import javax.tools.SimpleJavaFileObject;

/**
 * File abstraction for tools operating on Java™ programming language source and
 * class files.
 * 
 * 在Java™编程语言源文件和类文件上运行的工具的文件抽象。
 * 
 * All methods in this interface might throw a SecurityException if a security
 * exception occurs.
 * 
 * 如果发生安全异常，此接口中的所有方法都可能会引发SecurityException。
 * 
 * Unless explicitly allowed, all methods in this interface might throw a
 * NullPointerException if given a null argument.
 * 
 * 除非明确允许，否则此接口中的所有方法如果给定null参数，则可能会引发NullPointerException。
 * 
 * Provides simple implementations for most methods in JavaFileObject. This
 * class is designed to be subclassed and used as a basis for JavaFileObject
 * implementations. Subclasses can override the implementation and specification
 * of any method of this class as long as the general contract of JavaFileObject
 * is obeyed.
 * 
 * 为JavaFileObject中的大多数方法提供简单的实现。 这个类被设计成子类，并被用作JavaFileObject实现的基础。
 * 只要符合JavaFileObject的通用契约，子类就可以覆盖此类的任何方法的实现和规范。
 */
public class MemoryInputJavaFileObject extends SimpleJavaFileObject {

  // Java编程语言编写的源文件
  private final String code;

  /**
   * 在内存中使用Java源代码构造Java Class。
   * 
   * @param name，
   *          Java Class类名
   * @param code，
   *          Java Class源代码
   */
  public MemoryInputJavaFileObject(String name, String code) {
    // 构造给定类型的SimpleJavaFileObject和给定的URI。
    super(URI.create("string:///" + name), //
        Kind.SOURCE // 以Java编程语言编写的源文件。 例如，以.java结尾的常规文件。
    );
    this.code = code;
  }

  @Override
  public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
    return CharBuffer.wrap(code);// 将字符序列包装到缓冲区中。
  }
}
