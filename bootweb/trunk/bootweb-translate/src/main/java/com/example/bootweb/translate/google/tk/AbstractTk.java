package com.example.bootweb.translate.google.tk;

import com.example.bootweb.translate.google.script.Javascript;

public abstract class AbstractTk implements Tk {

  @Override
  public String calc(String val) {
    return Javascript.eval(getScript(), getFunc(), val);
  }
  
  /** 脚本中需要执行的函数。 */
  protected abstract String getFunc();

  /** 获取脚本。 */
  protected abstract String getScript();

}
