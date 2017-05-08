package com.xplus.commons.topease;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.springframework.stereotype.Component;

@Component(MainFrame.BEAN_ID)
public class MainFrame extends JFrame {

  public static final String BEAN_ID = "commons-topease.swing.frame.MainFrame";

  private static final long serialVersionUID = 3976209541974109976L;

  static {
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);
  }

  private JMenuBar mBar = new JMenuBar();

  public MainFrame() {
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setLocationByPlatform(true);
    this.setJMenuBar(mBar);
  }

  public static void changeLookAndFeel(JFrame frame) {
    try {
      UIManager.setLookAndFeel(new MetalLookAndFeel());
    } catch (UnsupportedLookAndFeelException e1) {
      e1.printStackTrace();
    }
    SwingUtilities.updateComponentTreeUI(frame);
  }

}
