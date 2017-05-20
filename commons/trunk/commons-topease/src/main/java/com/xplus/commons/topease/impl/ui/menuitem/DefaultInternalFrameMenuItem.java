package com.xplus.commons.topease.impl.ui.menuitem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplus.commons.topease.api.MethodInitable;

public class DefaultInternalFrameMenuItem extends JMenuItem implements MethodInitable {

	private static final Logger logger = LoggerFactory.getLogger(DefaultInternalFrameMenuItem.class);
	
	private static final long serialVersionUID = -7978793266781696950L;

	private List<ActionListener> actions = new ArrayList<ActionListener>();
	
	public List<ActionListener> getActions() {
		return actions;
	}

	public void setActions(List<ActionListener> actions) {
		this.actions = actions;
	}

	@Override
	public void init() {
		if (null == actions || actions.isEmpty()) {
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					logger.debug("没有定义菜单操作行为。");
				}
			});
			return;
		}
		for (ActionListener action : actions) {
			this.addActionListener(action);
		}
	}

}
