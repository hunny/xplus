package com.xplus.commons.topease.impl.service;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CleanMavenServiceImpl {

	private final Logger logger = LoggerFactory.getLogger(CleanMavenServiceImpl.class);
	
	public void check(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File tmp : files) {
				check(tmp);
			}
		} else {
			if (file.getName().endsWith("lastUpdated") //
					|| file.getName().endsWith("-lastUpdated.properties") //
					|| file.getName().endsWith("-not-available")) {
				logger.info(file.getAbsolutePath());
				file.delete();
				File fileParent = new File(file.getParent());
				File[] files = fileParent.listFiles();
				boolean hasJarFile = false;
				for (File f : files) {
					if (!f.isDirectory() && f.getName().endsWith(".jar")) {
						hasJarFile = true;
						break;
					}
				}
				if (!hasJarFile) {
					logger.info("delete : " + fileParent.getAbsolutePath() + " ");
					fileParent.deleteOnExit();
				}
			}
		}
	}

	public void execute(final File path) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<Void> future = executorService.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				check(path);
				return null;
			}
		});
		try {
			// print the return value of Future, notice the output delay in console
			// because Future.get() waits for task to get completed
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		// shut down the executor service now
		executorService.shutdown();
	}
	
}
