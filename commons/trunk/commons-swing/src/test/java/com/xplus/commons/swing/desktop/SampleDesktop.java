package com.xplus.commons.swing.desktop;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.AbstractAction;
import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class SampleDesktop extends JFrame {

  private static final long serialVersionUID = -4544167026338939414L;

  private JDesktopPane desk;

  private IconPolice iconPolice = new IconPolice();

  public SampleDesktop(String title) {
    super(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // Create a desktop and set it as the content pane. Don't set the
    // layered
    // pane, since it needs to hold the menu bar too.
    desk = new JDesktopPane();
    setContentPane(desk);

    // Install our custom desktop manager.
    desk.setDesktopManager(new SampleDesktopMgr());

    createMenuBar();
    loadBackgroundImage();
  }

  // Create a menu bar to show off a few things.
  protected void createMenuBar() {
    JMenuBar mb = new JMenuBar();
    JMenu menu = new JMenu("Frames");

    menu.add(new AddFrameAction(true)); // add "upper" frame
    menu.add(new AddFrameAction(false)); // add "lower" frame
    menu.add(new TileAction(desk)); // add tiling capability

    setJMenuBar(mb);
    mb.add(menu);
  }

  // Here we load a background image for our desktop.
  protected void loadBackgroundImage() {
    ImageIcon icon = new ImageIcon("images/sky.png");
    JLabel l = new JLabel(icon);
    l.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());

    // Place the image in the lowest possible layer so nothing
    // can ever be painted under it.
    desk.add(l, new Integer(Integer.MIN_VALUE));
  }

  // This class adds a new JInternalFrame when requested.
  class AddFrameAction extends AbstractAction {

    private static final long serialVersionUID = -7233402035640037399L;

    private Integer layer;
    private String name;

    public AddFrameAction(boolean upper) {
      super(upper ? "Add Upper Frame" : "Add Lower Frame");
      if (upper) {
        this.layer = new Integer(2);
        this.name = "Up";
      } else {
        this.layer = new Integer(1);
        this.name = "Lo";
      }
    }

    public void actionPerformed(ActionEvent ev) {
      JInternalFrame f = new JInternalFrame(name, true, true, true, true);
      f.addVetoableChangeListener(iconPolice);

      f.setBounds(0, 0, 120, 60);
      desk.add(f, layer);
      f.setVisible(true); // Needed since 1.3
    }
  }

  // A simple vetoable change listener that insists that there is always at
  // least one noniconified frame (just as an example of the vetoable
  // properties).
  class IconPolice implements VetoableChangeListener {
    public void vetoableChange(PropertyChangeEvent ev) throws PropertyVetoException {
      String name = ev.getPropertyName();
      if (name.equals(JInternalFrame.IS_ICON_PROPERTY) && (ev.getNewValue() == Boolean.TRUE)) {
        JInternalFrame[] frames = desk.getAllFrames();
        int count = frames.length;
        int nonicons = 0; // how many are not icons?
        for (int i = 0; i < count; i++) {
          if (!frames[i].isIcon()) {
            nonicons++;
          }
        }
        if (nonicons <= 1) {
          throw new PropertyVetoException("Invalid Iconification!", ev);
        }
      }
    }
  }

  // A simple test program.
  public static void main(String[] args) {
    SampleDesktop td = new SampleDesktop("Sample Desktop");

    td.setSize(300, 220);
    td.setVisible(true);
  }
}

// TileAction.java
// An action that tiles all internal frames when requested.
//

class TileAction extends AbstractAction {
  private static final long serialVersionUID = -680275357053091563L;
  private JDesktopPane desk; // the desktop to work with

  public TileAction(JDesktopPane desk) {
    super("Tile Frames");
    this.desk = desk;
  }

  public void actionPerformed(ActionEvent ev) {

    // How many frames do we have?
    JInternalFrame[] allframes = desk.getAllFrames();
    int count = allframes.length;
    if (count == 0)
      return;

    // Determine the necessary grid size
    int sqrt = (int) Math.sqrt(count);
    int rows = sqrt;
    int cols = sqrt;
    if (rows * cols < count) {
      cols++;
      if (rows * cols < count) {
        rows++;
      }
    }

    // Define some initial values for size & location.
    Dimension size = desk.getSize();

    int w = size.width / cols;
    int h = size.height / rows;
    int x = 0;
    int y = 0;

    // Iterate over the frames, deiconifying any iconified frames and then
    // relocating & resizing each.
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols && ((i * cols) + j < count); j++) {
        JInternalFrame f = allframes[(i * cols) + j];

        if (!f.isClosed() && f.isIcon()) {
          try {
            f.setIcon(false);
          } catch (PropertyVetoException ignored) {
          }
        }

        desk.getDesktopManager().resizeFrame(f, x, y, w, h);
        x += w;
      }
      y += h; // start the next row
      x = 0;
    }
  }
}

// SampleDesktopMgr.java
// A DesktopManager that keeps its frames inside the desktop.

class SampleDesktopMgr extends DefaultDesktopManager {

  private static final long serialVersionUID = -6240780705370639205L;

  // This is called anytime a frame is moved. This
  // implementation keeps the frame from leaving the desktop.
  public void dragFrame(JComponent f, int x, int y) {
    if (f instanceof JInternalFrame) { // Deal only w/internal frames
      JInternalFrame frame = (JInternalFrame) f;
      JDesktopPane desk = frame.getDesktopPane();
      Dimension d = desk.getSize();

      // Nothing all that fancy below, just figuring out how to adjust
      // to keep the frame on the desktop.
      if (x < 0) { // too far left?
        x = 0; // flush against the left side
      } else {
        if (x + frame.getWidth() > d.width) { // too far right?
          x = d.width - frame.getWidth(); // flush against right side
        }
      }
      if (y < 0) { // too high?
        y = 0; // flush against the top
      } else {
        if (y + frame.getHeight() > d.height) { // too low?
          y = d.height - frame.getHeight(); // flush against the
          // bottom
        }
      }
    }

    // Pass along the (possibly cropped) values to the normal drag handler.
    super.dragFrame(f, x, y);
  }
}
