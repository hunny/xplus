package com.example.bootweb.translate.google.script;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Javascript {

  private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
  
  public static String eval(String script, String func, String val) {
    try {
      engine.eval(script);
      Invocable inv = (Invocable) engine;
      return (String) inv.invokeFunction(func, val);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }
  
}
