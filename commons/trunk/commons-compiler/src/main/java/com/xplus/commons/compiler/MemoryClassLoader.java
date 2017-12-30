package com.xplus.commons.compiler;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class loader is used to load classes and resources from a search path of
 * URLs referring to both JAR files and directories. Any URL that ends with a
 * '/' is assumed to refer to a directory. Otherwise, the URL is assumed to
 * refer to a JAR file which will be opened as needed.
 * <p>
 * 这个类加载器用于从指向JAR文件和目录的URL的搜索路径加载类和资源。 任何以“/”结尾的URL都被认为是指向一个目录。
 * 否则，假定该URL是指将根据需要打开的JAR文件。
 * 
 * The AccessControlContext of the thread that created the instance of
 * URLClassLoader will be used when subsequently loading classes and resources.
 * <p>
 * 随后加载类和资源时将使用创建URLClassLoader实例的线程的AccessControlContext。
 * 
 * The classes that are loaded are by default granted permission only to access
 * the URLs specified when the URLClassLoader was created.
 * <p>
 * 加载的类默认被授予权限，只能访问在创建URLClassLoader时指定的URL
 */
public class MemoryClassLoader extends URLClassLoader {

  // class name to class bytes:
  // 类名对应的字节码
  Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

  /**
   * Constructs a new URLClassLoader for the given URLs. The URLs will be searched
   * in the order specified for classes and resources after first searching in the
   * specified parent class loader. Any URL that ends with a '/' is assumed to
   * refer to a directory. Otherwise, the URL is assumed to refer to a JAR file
   * which will be downloaded and opened as needed.
   * <p>
   * 为给定的URL构造一个新的URLClassLoader。 首先在指定的父类加载器中搜索后，将按照指定的类和资源的顺序搜索URL。
   * 任何以“/”结尾的URL都被认为是指向一个目录。 否则，假定该URL是指将根据需要下载和打开的JAR文件。
   * 
   * If there is a security manager, this method first calls the security
   * manager's checkCreateClassLoader method to ensure creation of a class loader
   * is allowed.
   * <p>
   * 如果有安全管理器，则此方法首先调用安全管理器的checkCreateClassLoader方法，以确保允许创建类加载器。
   * 
   */
  public MemoryClassLoader(Map<String, byte[]> classBytes) {
    /**
     * 参数:
     * <p>
     * 第一个参数url，要从中加载类和资源的URL，禁止为null，此处用于从内存中加载；
     * <p>
     * 第二个参数类加载器，用于委派的父类加载器；
     */
    super(new URL[0], MemoryClassLoader.class.getClassLoader());
    this.classBytes.putAll(classBytes);
  }

  /**
   * Finds and loads the class with the specified name from the URL search path.
   * Any URLs referring to JAR files are loaded and opened as needed until the
   * class is found.
   * <p>
   * 从URL搜索路径中查找并加载具有指定名称的类。 任何引用JAR文件的URL都会根据需要加载和打开，直到找到类。
   */
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
