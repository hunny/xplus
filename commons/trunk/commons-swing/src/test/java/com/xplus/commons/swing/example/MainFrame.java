package com.xplus.commons.swing.example;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.xplus.commons.swing.api.Frame;
import com.xplus.commons.swing.common.SpringUtilities;

/**
 * @author huzexiong
 *
 */
public class MainFrame extends JFrame implements Frame {

  private static final long serialVersionUID = -446154006759346932L;
  
  @Override
  public JFrame createAndShowGUI() {
    JFrame frame = init();
    return frame;
  }
  
  protected JFrame init() {
    this.setTitle("名称");
    // Add the ubiquitous "Hello World" label.
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(400, 300));
    SpringLayout layout = new SpringLayout();
    panel.setLayout(layout);
    
    JLabel label = new JLabel("Label: ");
    JTextField textField = new JTextField("Text field", 15);
    
//    layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, panel);
//    layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, panel);
//    layout.putConstraint(SpringLayout.WEST, textField, 5, SpringLayout.EAST, label);
//    layout.putConstraint(SpringLayout.NORTH, textField, 5, SpringLayout.NORTH, panel);
    
    panel.add(label);
    panel.add(textField);
    
    int rows = 1;
    int cols = 1;
    SpringUtilities.makeGrid(panel, //parent
        rows, cols,
        3, 3,  //initX, initY
        3, 3); //xPad, yPad
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(panel, BorderLayout.CENTER);
    // Display the window.
    this.pack();
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    return this;
  }

}
