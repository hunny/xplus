package com.xplus.commons.topease;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.annotation.Resource;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.springframework.stereotype.Component;

/**
 * @author huzexiong
 *
 */
@Component(SwingAppRunnable.BEAN_ID)
public class SwingAppRunnable implements Runnable {
  
  public static final String BEAN_ID = "commons-topease.swing.runnable.SwingAppRunnable";
  
  @Resource(name = MainFrame.BEAN_ID)
  private JFrame frame;

  @Override
  public void run() {
    MainFrame.changeLookAndFeel(frame);
    JPanel panel = new JPanel(new GridLayout(3, 5));
    panel.setBorder(BorderFactory.createEmptyBorder(10, // top
        10, // left
        10, // bottom
        10) // right
    );

    DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] {
        "First", "Second" });
    model.addRow(new Object[] {
        "Some text 1", "Another text 1" });
    model.addRow(new Object[] {
        "Some text 2", "Another text 2" });
    model.addRow(new Object[] {
        "Some text 3", "Another text 3" });
    model.addRow(new Object[] {
        "Some text 4", "Another text 4" });
    JTable table = new JTable(model);
    frame.add(panel, BorderLayout.NORTH);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEtchedBorder());
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }

}
