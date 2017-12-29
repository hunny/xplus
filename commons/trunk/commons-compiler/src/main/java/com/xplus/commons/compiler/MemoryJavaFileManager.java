package com.xplus.commons.compiler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

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

  @Override
  public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, Kind kind,
      FileObject sibling) throws IOException {
    if (kind == Kind.CLASS) {
      return new MemoryOutputJavaFileObject(className, classBytes);
    } else {
      return super.getJavaFileForOutput(location, className, kind, sibling);
    }
  }

  public JavaFileObject makeStringSource(String name, String code) {
    return new MemoryInputJavaFileObject(name, code);
  }

}
