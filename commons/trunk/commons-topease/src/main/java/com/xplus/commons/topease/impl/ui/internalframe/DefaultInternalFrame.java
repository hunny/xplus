package com.xplus.commons.topease.impl.ui.internalframe;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplus.commons.topease.api.MethodInitable;

public class DefaultInternalFrame extends JInternalFrame implements MethodInitable {

	private static final long serialVersionUID = -3575447608313039708L;

	private static final Logger logger = LoggerFactory.getLogger(DefaultInternalFrame.class);

	private int width = 400;
	private int height = 300;
	private JLabel message = new JLabel();
	
	@Override
	public void init() {
		logger.debug("Init DefaultInternalFrame.");
		this.setResizable(true);
		this.setMaximizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(getPanel(), BorderLayout.CENTER);
		message.setBorder(BorderFactory.createEtchedBorder());
		container.add(message, BorderLayout.SOUTH);
		this.setBounds(0, 0, width, height);
		this.setPreferredSize(new Dimension(width, height));
	}
	
	public void setMessage(String msg) {
		this.message.setText(msg);
	}
	
	public JPanel getPanel() {
		return new JPanel();
	}
	
	public JInternalFrame current() {
		return this;
	}

}
