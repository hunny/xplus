/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	commons-topease
 * 文件名：	TopeaseMenu.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月15日 - huzexiong - 创建。
 */
package com.xplus.commons.topease;

import javax.swing.JMenu;

import org.springframework.stereotype.Component;

/**
 * @author huzexiong
 *
 */
@Component(TopeaseMenu.BEAN_ID)
public class TopeaseMenu extends JMenu {

  private static final long serialVersionUID = 655099742116411600L;
  
  public static final String BEAN_ID = "commons-topease.swing.menu.TopeaseMenu";
  
  public TopeaseMenu() {
    super.setName("特易");
  }

}
