package com.xplus.commons.compiler;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import javax.tools.SimpleJavaFileObject;

public class MemoryOutputJavaFileObject extends SimpleJavaFileObject {

  private Map<String, byte[]> classBytes = null;
  private final String name;

  public MemoryOutputJavaFileObject(String name, Map<String, byte[]> classBytes) {
    super(URI.create("string:///" + name), Kind.CLASS);
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
        classBytes.put(name, bos.toByteArray());
      }
    };
  }

}