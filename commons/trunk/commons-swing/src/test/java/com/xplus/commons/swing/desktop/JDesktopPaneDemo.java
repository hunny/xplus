package com.xplus.commons.swing.desktop;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/**
 * @author huzexiong
 *
 */
public class JDesktopPaneDemo extends JFrame {

  private static final long serialVersionUID = 5171440298375569685L;

  public JDesktopPaneDemo() {
    CustomDesktopPane desktopPane = new CustomDesktopPane();
    Container contentPane = getContentPane();
    contentPane.add(desktopPane, BorderLayout.CENTER);
    desktopPane.display(desktopPane);

    setTitle("Learning MDI");
    setSize(300, 500);
  }

  public static void main(String args[]) {
    new JDesktopPaneDemo().setVisible(true);;
  }
}

class CustomDesktopPane extends JDesktopPane {
  
  private static final long serialVersionUID = 8819235801256662284L;
  
  int numFrames = 5, x = 30, y = 30;

  public void display(CustomDesktopPane dp) {
    for (int i = 0; i < numFrames; ++i) {
      JInternalFrame jif = new JInternalFrame("Internal Frame " + i, true, true, true, true);

      jif.setBounds(x, y, 250, 85);
      Container c1 = jif.getContentPane();
      c1.add(new JLabel("S. Nageswra Rao, Corporate Trainer."));
      dp.add(jif);
      jif.setVisible(true);
      y += 85;
    }
  }
}
