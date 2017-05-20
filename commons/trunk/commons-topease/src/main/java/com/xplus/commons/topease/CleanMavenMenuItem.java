package com.xplus.commons.topease;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CleanMavenMenuItem extends JMenuItem {

	private static final long serialVersionUID = 684495393643486898L;
	
	private static final Logger logger = LoggerFactory.getLogger(CleanMavenMenuItem.class);
	
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

	public void init() {
		this.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        logger.info("点击菜单。");
        desktopPane.add(getInternalFrame());
        getInternalFrame().setVisible(true);
        MainFrame.changeLookAndFeel(frame);
      }
    });
	}

}
