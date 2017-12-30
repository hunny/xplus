package com.xplus.commons.compiler;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

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
public class MemoryOutputJavaFileObject extends SimpleJavaFileObject {

  private Map<String, byte[]> classBytes = null;
  private final String name;

  /**
   * 向内存中输出Java Class字节码。
   * 
   * @param name
   * @param classBytes
   */
  public MemoryOutputJavaFileObject(String name, Map<String, byte[]> classBytes) {
    super(URI.create("string:///" + name), //
        Kind.CLASS // Java虚拟机的类文件。 例如，以.class结尾的常规文件。
    );
    this.name = name;
    this.classBytes = classBytes;
  }

  @Override
  public OutputStream openOutputStream() {
    return new FilterOutputStream(new ByteArrayOutputStream()) {
      @Override
      public void close() throws IOException {
        out.close();
        ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
        classBytes.put(name, //
            bos.toByteArray() // 创建一个新分配的字节数组。 它的大小是这个输出流的当前大小，缓冲区的有效内容已被复制到这个输出流中。
        );
      }
    };
  }

}