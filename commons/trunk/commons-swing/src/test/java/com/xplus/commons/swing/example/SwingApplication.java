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
