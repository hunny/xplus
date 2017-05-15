package com.xplus.commons.topease;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class MainFrame extends JFrame {

  private static final long serialVersionUID = 3976209541974109976L;

  static {
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);
  }

  private JMenuBar menuBar = new JMenuBar();
  private List<JMenu> menus = new ArrayList<JMenu>();

  public MainFrame() {
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setLocationByPlatform(false);
    if (!menus.isEmpty()) {
    	this.setJMenuBar(menuBar);
    	for (JMenu menu : menus) {
    		menuBar.add(menu);
    	}
    }
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
