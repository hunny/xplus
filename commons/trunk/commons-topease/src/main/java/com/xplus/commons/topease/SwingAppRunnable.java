package com.xplus.commons.topease;

import javax.swing.JFrame;

import com.xplus.commons.topease.impl.ui.main.MainFrame;

/**
 * @author huzexiong
 *
 */
public class SwingAppRunnable implements Runnable {

	private JFrame frame;

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	@Override
	public void run() {
		MainFrame.changeLookAndFeel(frame);
		frame.pack();
		frame.setVisible(true);
	}

}
