package com.xplus.commons.topease;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * 应用程序主入口
 * 
 * @author huzexiong
 *
 */
public class Application {

  public static boolean RIGHT_TO_LEFT = false;

  public static void addComponentsToPane(Container contentPane) {
    // Use BorderLayout. Default empty constructor with no horizontal and
    // vertical gaps
    contentPane.setLayout(new BorderLayout(5, 5));
    if (!(contentPane.getLayout() instanceof BorderLayout)) {
      contentPane.add(new JLabel("Container doesn't use BorderLayout!"));
      return;
    }

    JButton jbnSampleButtons = new JButton("Button 1 (PAGE_START)");
    contentPane.add(jbnSampleButtons, BorderLayout.PAGE_START);

    jbnSampleButtons = new JButton("Button 2 (CENTER)");
    jbnSampleButtons.setPreferredSize(new Dimension(200, 100));
    contentPane.add(jbnSampleButtons, BorderLayout.CENTER);

    jbnSampleButtons = new JButton("Button 3 (LINE_START)");
    contentPane.add(jbnSampleButtons, BorderLayout.LINE_START);

    jbnSampleButtons = new JButton("Long-Named Button 4 (PAGE_END)");
    contentPane.add(jbnSampleButtons, BorderLayout.PAGE_END);

    jbnSampleButtons = new JButton("5 (LINE_END)");
    contentPane.add(jbnSampleButtons, BorderLayout.LINE_END);
    jbnSampleButtons.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo tmp : lafInfo) {
          System.out.println(tmp);
        }
      }

    });
  }

  private static void createAndShowGUI() {
    JFrame.setDefaultLookAndFeelDecorated(true);

    JFrame frame = new JFrame("BorderLayout Source Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Set up the content pane and add swing components to it
    addComponentsToPane(frame.getContentPane());

    frame.pack();
    frame.setVisible(true);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // 设置样式
    JFrame.setDefaultLookAndFeelDecorated(true); // windows功能失效
    JDialog.setDefaultLookAndFeelDecorated(true); // Dialog功能失效
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

}
