package com.xplus.commons.topease.impl.ui.internalframe;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanMavenInternalFrame extends DefaultInternalFrame {

	private static final long serialVersionUID = -8980360742741568325L;
	private static final Logger logger = LoggerFactory.getLogger(CleanMavenInternalFrame.class);

	private JTextField txtPath = new JTextField();
	private JButton btnOk = new JButton("确定");
	private JButton btnSelector = new JButton("选择...");

	public void init() {
		super.init();
		txtPath.setPreferredSize(new Dimension(120, 30));
		btnSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setMessage("选择目录");
				select();
			}
		});
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!StringUtils.isEmpty(txtPath.getText())) {
					setMessage(String.format("准备清理目录[%s]", txtPath.getText()));
					SwingUtilities.invokeLater(new Runnable() {
			      public void run() {
							execute(new File(txtPath.getText()));
			      }
			    });
				} else {
					JOptionPane.showMessageDialog(current(), "请选择目录。");
				}
			}
		});
	}

	private void select() {
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		fileChooser.setDialogTitle("Choose maven directory.");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setFileHidingEnabled(false);
		if (StringUtils.isNotBlank(txtPath.getText())) {
			File dir = new File(txtPath.getText());
			if (dir.exists()) {
				fileChooser.setCurrentDirectory(dir);
			}
		}
		int returnValue = fileChooser.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (fileChooser.getSelectedFile().isDirectory()) {
				txtPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
				setMessage(txtPath.getText());
			}
		}
	}

	public JPanel getPanel() {
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEtchedBorder());
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10); // top padding

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		JLabel label = new JLabel("maven路径");
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setPreferredSize(new Dimension(80, 30));
		pane.add(label, c);
		c.gridx = 1;
		c.weightx = 0.8;
		pane.add(txtPath, c);
		c.gridx = 2;
		c.weightx = 0.1;
		pane.add(btnSelector, c);

		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		pane.add(btnOk, c);

		// btnOk = new JButton("5");
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.ipady = 0; // reset to default
		// c.weighty = 1.0; // request any extra vertical space
		// c.anchor = GridBagConstraints.PAGE_END; // bottom of space
		// c.insets = new Insets(10, 0, 0, 0); // top padding
		// c.gridx = 1; // aligned with button 2
		// c.gridwidth = 2; // 2 columns wide
		// c.gridy = 2; // third row
		// pane.add(btnOk, c);
		return pane;
	}

	public static void check(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File tmp : files) {
				check(tmp);
			}
		} else {
			if (file.getName().endsWith("lastUpdated") || file.getName().endsWith("-lastUpdated.properties")) {
				logger.info(file.getAbsolutePath());
				file.delete();
				File fileParent = new File(file.getParent());
				File[] files = fileParent.listFiles();
				boolean hasJarFile = false;
				for (File f : files) {
					if (!f.isDirectory() && f.getName().endsWith(".jar")) {
						hasJarFile = true;
						break;
					}
				}
				if (!hasJarFile) {
					logger.info("delete : " + fileParent.getAbsolutePath() + " ");
					fileParent.deleteOnExit();
				}
			}
		}
	}

	private void execute(final File path) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<Void> future = executorService.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				Thread.sleep(5000);
				check(path);
				return null;
			}
		});
		try {
			// print the return value of Future, notice the output delay in console
			// because Future.get() waits for task to get completed
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		logger.info("执行完毕。");
		setMessage("执行完毕。");
		// shut down the executor service now
		executorService.shutdown();
	}

}
