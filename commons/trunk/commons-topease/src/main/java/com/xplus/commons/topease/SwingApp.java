package com.xplus.commons.topease;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.annotation.Resource;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.springframework.stereotype.Component;

/**
 * @author huzexiong
 *
 */
@Component
public class SwingApp {
	
	@Resource(name = MainFrame.BEAN_ID)
	private JFrame frame;

	public void run(String... args) throws Exception {

		MainFrame.changeLookAndFeel(frame);
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JPanel panel = new JPanel(new GridLayout(3, 5));
				panel.setBorder(BorderFactory.createEmptyBorder(10, // top
						10, // left
						10, // bottom
						10) // right
				);
				JSpinner spnDemo = new JSpinner();
				JComboBox<String> cmbDemo = new JComboBox<String>();
				cmbDemo.addItem("One");
				cmbDemo.addItem("Two");
				cmbDemo.addItem("Three");

				JMenu menuLookAndFeel = new JMenu("LAF");
				menuLookAndFeel.setToolTipText("Application Look And Feel.");
				frame.getJMenuBar().add(menuLookAndFeel);

				DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] { "First", "Second" });
				model.addRow(new Object[] { "Some text 1", "Another text 1" });
				model.addRow(new Object[] { "Some text 2", "Another text 2" });
				model.addRow(new Object[] { "Some text 3", "Another text 3" });
				model.addRow(new Object[] { "Some text 4", "Another text 4" });
				JTable table = new JTable(model);
				panel.add(spnDemo);
				panel.add(cmbDemo);
				frame.add(panel, BorderLayout.NORTH);
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setBorder(BorderFactory.createEtchedBorder());
				frame.add(scrollPane, BorderLayout.CENTER);
				frame.pack();
				frame.setVisible(true);
			}

		});
	}

}
