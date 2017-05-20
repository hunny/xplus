package com.xplus.commons.topease.impl.ui.main;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

public class DesktopPan extends JDesktopPane {

	private static final long serialVersionUID = -1011126751243281194L;
	private int width = 400;
  private int height = 300;

  public void init() {
//    int x = 30, y = 30;
//    
//    for (int i = 0; i < 6; ++i) {
//      JInternalFrame jif = new JInternalFrame("Internal Frame " + i, true, true, true, true);
//
//      jif.setBounds(x, y, 250, 85);
//      Container c1 = jif.getContentPane();
//      c1.add(new JLabel("S. Nageswra Rao, Corporate Trainer."));
//      this.add(jif);
//      jif.setVisible(true);
//      y += 85;
//    }
    this.setPreferredSize(new Dimension(width, height));
  }
  
}
