package com.xplus.commons.topease;

import javax.swing.SwingUtilities;

/**
 * @author huzexiong
 *
 */
public class SwingApp {

  private Runnable runnable;

  public Runnable getRunnable() {
    return runnable;
  }

  public void setRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  public void run(String... args) throws Exception {
    SwingUtilities.invokeLater(runnable);
  }

}
