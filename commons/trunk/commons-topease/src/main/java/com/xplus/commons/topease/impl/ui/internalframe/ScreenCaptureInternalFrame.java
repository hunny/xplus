package com.xplus.commons.topease.impl.ui.internalframe;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.auth0.jwt.internal.com.fasterxml.jackson.core.type.TypeReference;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.SerializationFeature;
import com.xplus.commons.topease.api.service.Cookies;
import com.xplus.commons.topease.api.service.phantomjs.Phantomjs;
import com.xplus.commons.topease.impl.service.phantomjs.PhantomjsServiceImpl;

public class ScreenCaptureInternalFrame extends DefaultInternalFrame {

	private static final long serialVersionUID = -7384072168690506273L;
	private static final Logger logger = LoggerFactory.getLogger(CleanMavenInternalFrame.class);

	@Autowired
	private PhantomjsServiceImpl phantomjsServiceImpl;

	private JTextField txtPath = new JTextField();
	private JTextField urlPath = new JTextField();
	private JTextArea areaCookie = new JTextArea(3, 3);
	private JTextField txtCmd = new JTextField();
	private JButton btnOk = new JButton("确定截图");
	private JButton btnCmd = new JButton("命令");
	private JButton btnSelector = new JButton("...");

	public void init() {
		super.init();
		txtPath.setPreferredSize(new Dimension(180, 30));
		urlPath.setPreferredSize(new Dimension(180, 30));
		txtCmd.setPreferredSize(new Dimension(180, 30));
		areaCookie.setPreferredSize(new Dimension(200, 300));
		areaCookie.setBorder(BorderFactory.createEtchedBorder());
		urlPath.setText("http://127.0.0.1:8085/");
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
				if (StringUtils.isBlank(urlPath.getText()) || StringUtils.isBlank(txtPath.getText())) {
					JOptionPane.showMessageDialog(current(), "请输入地址并选择目录。");
					return;
				}
				setMessage(String.format("截图选择存放位置[%s]", txtPath.getText()));
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							Phantomjs phantomjs = new Phantomjs();
							if (StringUtils.isNotBlank(areaCookie.getText())) {
								List<Cookies> cookie = new ObjectMapper().readValue(areaCookie.getText(), new TypeReference<List<Cookies>>() {});
								phantomjs.setCookies(cookie);
							}
							phantomjs.setUrl(urlPath.getText());
							phantomjs.setScreenCapturePath(txtPath.getText());
							phantomjsServiceImpl.screenCapture(phantomjs);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(current(), e.getMessage());
							return;
						}
						logger.info("执行完毕。");
						setMessage(String.format("截图完毕，存放位置[%s]", txtPath.getText()));
					}
				});
			}
		});
		btnCmd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String arg = txtCmd.getText();
					setMessage(phantomjsServiceImpl.phantomjsCommandLine(arg.split("\\s+")));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void select() {
		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		fileChooser.setDialogTitle("选择文件存储位置.");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileHidingEnabled(true);
		fileChooser.setSelectedFile(new File("abc.png"));
		fileChooser.setFileFilter(new PngFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
		if (StringUtils.isNotBlank(txtPath.getText())) {
			File dir = new File(txtPath.getText());
			if (dir.exists()) {
				fileChooser.setCurrentDirectory(dir);
			}
		}
		int returnValue = fileChooser.showSaveDialog(current());
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			txtPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
			setMessage(txtPath.getText());
		}
	}

	public JPanel getPanel() {
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEtchedBorder());
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10); // top padding

		buildUrl(0, 0, pane, c);
		buildPath(0, 1, pane, c);
		buildCookie(0, 2, pane, c);
		buildUiBtn(0, 3, pane, c);
		JPanel parent = super.getPanel();
		parent.add(pane);

		JPanel cmdPanel = new JPanel();
		cmdPanel.setBorder(BorderFactory.createEtchedBorder());
		buildUiCmd(0, 0, cmdPanel, c);
		parent.add(cmdPanel);

		return parent;
	}

	private void buildUrl(int x, int y, JPanel pane, GridBagConstraints c) {
		c.gridx = x;
		c.gridy = y;
		c.weightx = 0.2;
		JLabel label = new JLabel("地址路径");
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setPreferredSize(new Dimension(80, 30));
		pane.add(label, c);
		c.gridx = x + 1;
		c.weightx = 0.8;
		pane.add(urlPath, c);
	}

	private void buildPath(int x, int y, JPanel pane, GridBagConstraints c) {
		c.gridx = x;
		c.gridy = y;
		c.weightx = 0.2;
		JLabel label = new JLabel("截图路径");
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setPreferredSize(new Dimension(80, 30));
		pane.add(label, c);
		c.gridx = x + 1;
		c.weightx = 0.6;
		pane.add(txtPath, c);
		c.gridx = x + 2;
		c.weightx = 0.2;
		pane.add(btnSelector, c);
	}

	private void buildCookie(int x, int y, JPanel pane, GridBagConstraints c) {
		c.gridx = x;
		c.gridy = y;
		c.weightx = 0.2;
		c.gridwidth = 1;
		JLabel label = new JLabel("cookie");
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setPreferredSize(new Dimension(80, 30));
		pane.add(label, c);
		c.gridx = x + 1;
		c.weightx = 0.8;
		c.gridwidth = 2;
		JScrollPane scrollPane = new JScrollPane(areaCookie); 
		scrollPane.setPreferredSize(new Dimension(300, 200));
		pane.add(scrollPane, c);

		try {
			Cookies cookie = new Cookies();
			cookie.setValue("string");
			cookie.setName("string");
			cookie.setDomain("string");
			cookie.setExpires(new Date().getTime() + 1000 * 60 * 60);
			cookie.setPath("string");
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			areaCookie.setText(mapper.writeValueAsString(Collections.singletonList(cookie)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildUiBtn(int x, int y, JPanel pane, GridBagConstraints c) {
		c.weightx = 1.0;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 3;
		pane.add(btnOk, c);
	}

	private void buildUiCmd(int x, int y, JPanel pane, GridBagConstraints c) {
		c.weightx = 1.0;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 1;
		pane.add(txtCmd, c);
		c.gridx = x + 1;
		c.gridwidth = 1;
		pane.add(btnCmd, c);
	}

	class PngFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
		}

		@Override
		public String getDescription() {
			return "PNG files (*.png)";
		}
	}

}
