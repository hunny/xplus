package com.xplus.commons.topease;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * @author huzexiong
 *
 */
public class TopeaseMenu extends JMenu {

  private static final long serialVersionUID = 655099742116411600L;

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
