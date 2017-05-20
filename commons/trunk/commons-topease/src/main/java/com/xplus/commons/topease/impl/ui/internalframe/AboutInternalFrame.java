package com.xplus.commons.topease.impl.ui.internalframe;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutInternalFrame extends DefaultInternalFrame {

	private static final long serialVersionUID = -7384072168690506273L;

	public JPanel getPanel() {
		JPanel parent = super.getPanel();
		JPanel panel = new JPanel();
		JLabel text = new JLabel("使用Spring Boot，实现配置化快速开发UI。");
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.add(text);
		parent.add(panel);
		return parent;
	}
}
