package com.xplus.commons.topease;

import javax.annotation.Resource;
import javax.swing.SwingUtilities;

import org.springframework.stereotype.Component;

/**
 * @author huzexiong
 *
 */
@Component
public class SwingApp {
	
	@Resource(name = SwingAppRunnable.BEAN_ID)
	private Runnable runnable;

	public void run(String... args) throws Exception {
		SwingUtilities.invokeLater(runnable);
	}

}
