package com.xplus.commons.topease;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeMenuItem extends JMenuItem {
  
  private static final long serialVersionUID = 8167427837164187922L;
  
  private final Logger logger = LoggerFactory.getLogger(CodeMenuItem.class);

  private JFrame frame = null;
  
  public JFrame getFrame() {
    return frame;
  }
  
  public void setFrame(JFrame frame) {
    this.frame = frame;
  }

  public void init() {
    this.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        logger.info("点击菜单。");
      }
    });
  }
  
}
