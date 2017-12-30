package com.xplus.commons.compiler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * In-memory compile Java source code as String.
 */
public class JavaStringCodeCompiler {

  /**
   * Interface to invoke Java™ programming language compilers from programs.
   * <p>
   * 从程序调用Java™编程语言编译器的接口。
   */
  private JavaCompiler compiler;
  /**
   * File manager based on java.io.File. A common way to obtain an instance of
   * this class is using getStandardFileManager
   * <p>
   * 基于java.io.File的文件管理器。 获取此类的一个常用方法是使用getStandardFileManager
   */
  private StandardJavaFileManager stdManager;

  public JavaStringCodeCompiler() {
    /**
     * Gets the Java™ programming language compiler provided with this platform.
     * <p>
     * 获取此平台提供的Java™编程语言编译器。
     */
    this.compiler = ToolProvider.getSystemJavaCompiler();
    /**
     * Gets a new instance of the standard file manager implementation for this
     * tool. The file manager will use the given diagnostic listener for producing
     * any non-fatal diagnostics. Fatal errors will be signaled with the appropriate
     * exceptions.
     * <p>
     * 获取此工具的标准文件管理器实现的新实例。
     * <p>
     * 文件管理器将使用给定的诊断监听器来产生任何非致命的诊断信息。 如果发生致命错误，将会有信息输出并带有适当的异常信息。
     * 
     * The standard file manager will be automatically reopened if it is accessed
     * after calls to flush or close. The standard file manager must be usable with
     * other tools.
     * <p>
     * 如果在调用flush或close之后访问标准文件管理器，标准文件管理器将自动重新打开。
     * <p>
     * 标准文件管理器必须与其他工具一起使用。
     */
    this.stdManager = compiler.getStandardFileManager( //
        null, // DiagnosticListener<? super JavaFileObject>，diagnosticListener，用于非致命诊断的诊断监听器;
              // 如果null使用编译器的默认方法报告诊断
        null, //java.util.Locale, 格式化诊断时应用的区域设置; null表示默认的语言环境( java.util.Locale.getDefault())。
        null //java.nio.charset.Charset，用于解码字节的字符集; 如果null使用平台默认值
    );
  }

  /**
   * Compile a Java source file in memory.
   * 
   * @param fileName
   *          Java file name, e.g. "Test.java"
   * @param source
   *          The source code as String.
   * @return The compiled results as Map that contains class name as key, class
   *         binary as value.
   * @throws IOException
   *           If compile error.
   */
  public Map<String, byte[]> compile(String fileName, String source) throws IOException {
    MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager);
    JavaFileObject memoryInputJavaFileObject = new MemoryInputJavaFileObject(fileName, source);
    /**
     * 用给定的组件和参数为编译任务创建一个异步任务。 编译可能没有完成，如CompilationTask接口中所述。
     * <p>
     * 如果提供文件管理器，则必须能够处理在StandardLocation中定义的所有位置。
     * <p>
     * 请注意，注释处理可以同时处理要编译的源代码的编译单元，通过compilationUnits参数传递的类以及使用classes参数传递名称的类文件。
     */
    CompilationTask task = compiler.getTask( //
        null, // java.io.Writer, 一个Writer用于编译器的额外输出; 如果为null，将使用System.err输出信息。
        manager, // javax.tools.JavaFileManager, 如果为null，将使用编译器的标准文件管理器。
        null, // javax.tools.DiagnosticListener<S>, 如果为null，将使用编译器的默认方法报告诊断。
        null, // Iterable<String>, compilerOptions, 编译器选项，如果为null，表示没有选项。
        null, // Iterable<String>, classes, 注解处理的类名，null表示没有类名。
        Arrays.asList(memoryInputJavaFileObject) // Iterable<? extends JavaFileObject>,
                                                 // compilationUnits，编译的单元编译，null表示没有编译单元。
    );
    // 执行此编译任务。 编译只能执行一次。 随后对此方法的调用将引发IllegalStateException。
    Boolean result = task.call(); // 当且仅当编译的所有文件没有错误时才返回true; 否则为false。
    if (result == null || !result.booleanValue()) {
      throw new RuntimeException("Compilation failed.");
    }
    return manager.getClassBytes();// 输出编译的字节码。
  }

  /**
   * Load class from compiled classes.
   * 
   * @param name
   *          Full class name.
   * @param classBytes
   *          Compiled results as a Map.
   * @return The Class instance.
   * @throws ClassNotFoundException
   *           If class not found.
   * @throws IOException
   *           If load error.
   */
  public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
    MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
    Class<?> clazz = classLoader.loadClass(name);
    classLoader.close();
    return clazz;
  }
}
