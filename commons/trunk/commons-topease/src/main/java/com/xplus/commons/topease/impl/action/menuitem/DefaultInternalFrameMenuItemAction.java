package com.xplus.commons.topease.impl.action.menuitem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xplus.commons.topease.impl.ui.main.MainFrame;

public class DefaultInternalFrameMenuItemAction implements ActionListener {

	private static final Logger logger = LoggerFactory.getLogger(DefaultInternalFrameMenuItemAction.class);
	
	@Autowired
	private JFrame frame = null;
	@Autowired
	private JDesktopPane desktopPane = null;
	private JInternalFrame internalFrame;

	public JInternalFrame getInternalFrame() {
		return internalFrame;
	}

	public void setInternalFrame(JInternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		logger.info("点击菜单。");
    desktopPane.add(getInternalFrame());
    getInternalFrame().setVisible(true);
    MainFrame.changeLookAndFeel(frame);
	}

}
