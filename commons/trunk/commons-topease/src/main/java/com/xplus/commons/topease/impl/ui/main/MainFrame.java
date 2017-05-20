package com.xplus.commons.topease.impl.ui.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
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

  private int width = 800;
  private int height = 600;
  private JMenuBar menuBar = new JMenuBar();
  private List<JMenu> menus = new ArrayList<JMenu>();
  private JDesktopPane desktopPane = null;

  public JDesktopPane getDesktopPane() {
    return desktopPane;
  }

  public void setDesktopPane(JDesktopPane desktopPane) {
    this.desktopPane = desktopPane;
  }

  public List<JMenu> getMenus() {
    return menus;
  }

  public void setMenus(List<JMenu> menus) {
    this.menus = menus;
  }
  
  public void init() {
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setLocationByPlatform(false);
    if (!menus.isEmpty()) {
        this.setJMenuBar(menuBar);
        for (JMenu menu : menus) {
            menuBar.add(menu);
        }
    }
    this.setPreferredSize(new Dimension(width, height));
    if (null != desktopPane) {
      //TODO 
      Container contentPane = getContentPane();
      contentPane.add(desktopPane, BorderLayout.CENTER);
      this.setContentPane(desktopPane);
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
