package com.xplus.commons.compiler;

import java.net.URI;
import java.nio.CharBuffer;

import javax.tools.SimpleJavaFileObject;

public class MemoryInputJavaFileObject extends SimpleJavaFileObject {

  final String code;

  MemoryInputJavaFileObject(String name, String code) {
    super(URI.create("string:///" + name), Kind.SOURCE);
    this.code = code;
  }

  @Override
  public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
    return CharBuffer.wrap(code);
  }
}
