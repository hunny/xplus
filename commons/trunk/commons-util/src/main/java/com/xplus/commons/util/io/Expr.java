package com.xplus.commons.util.io;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Expr {

  private final static ScriptEngine SCRIPT_ENGINE = //
      new ScriptEngineManager().getEngineByName("JavaScript");

  public static Object cal(String expression) throws ScriptException {
    return SCRIPT_ENGINE.eval(expression);
  }

}
