package com.xplus.commons.topease.impl.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExitAction implements ActionListener {

	private final Logger logger = LoggerFactory.getLogger(ExitAction.class);
	
	@Override
	public void actionPerformed(ActionEvent e) {
		logger.debug("系统退出。");
		System.exit(0);
	}

}
