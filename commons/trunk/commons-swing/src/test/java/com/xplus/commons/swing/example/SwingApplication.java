/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	commons-swing
 * 文件名：	SwingApplication.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月19日 - huzexiong - 创建。
 */
package com.xplus.commons.swing.example;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.xplus.commons.swing.api.Frame;

/**
 * @author huzexiong
 *
 */
public class SwingApplication {

  /**
   * @param args
   */
  public static void main(String[] args) {
    JFrame.setDefaultLookAndFeelDecorated(true);
    final Frame frame = new MainFrame();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        frame.createAndShowGUI();
      }
    });
  }

}
