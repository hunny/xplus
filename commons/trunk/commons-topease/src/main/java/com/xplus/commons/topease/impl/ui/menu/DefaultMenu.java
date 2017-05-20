package com.xplus.commons.topease.impl.ui.menu;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.xplus.commons.topease.api.MethodInitable;

public class DefaultMenu extends JMenu implements MethodInitable {

	private static final long serialVersionUID = 7317022638056694724L;

  private List<JMenuItem> menuItems = new ArrayList<JMenuItem>();

  public List<JMenuItem> getMenuItems() {
    return menuItems;
  }

  public void setMenuItems(List<JMenuItem> menuItems) {
    this.menuItems.clear();
    if (null != menuItems && !menuItems.isEmpty()) {
      this.menuItems.addAll(menuItems);
    }
  }

  public void init() {
    for (JMenuItem menuItem : menuItems) {
      super.add(menuItem);
    }
  }
	
}
