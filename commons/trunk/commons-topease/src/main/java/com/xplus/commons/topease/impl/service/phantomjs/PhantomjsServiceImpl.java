package com.xplus.commons.topease.impl.service.phantomjs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xplus.commons.tpl.api.TemplateMaker;
import com.xplus.commons.util.operatorsystem.OSUtil;

@Service
public class PhantomjsServiceImpl {

	private final Logger logger = LoggerFactory.getLogger(PhantomjsServiceImpl.class);

	@Resource(name = "commons-tpl.freeMarkerTemplateMaker")
	private TemplateMaker templateMaker;

	@Value("${commons-topease.tpl.phantomjs.path:/META-INF/phantomjs}")
	private String phantomjsPath;

	public void screenCaptureWithFtl(String url, File dest) throws Exception {
		URL uRL = PhantomjsServiceImpl.class.getResource("/");
		File phantomjs = new File(uRL.getFile() + phantomjsPath + "/phantomjs");
		phantomjs.setExecutable(true);

		File tpl = new File(String.format("%s/%s", phantomjsPath, "capture.ftl"));
		File out = new File(
				String.format("%s/%s/%s%s", dest.getParent(), "tmp", String.valueOf(System.currentTimeMillis()), ".js"));
		Map<String, Object> value = new HashMap<String, Object>();
		value.put("url", url);
		value.put("path", dest.getPath());
		templateMaker.setForceOverWrite(true);
		templateMaker.make(value, tpl.getPath(), out.getPath());

		Runtime runtime = Runtime.getRuntime();
		String exec = MessageFormat.format("{0} {1}", phantomjs, out.getAbsolutePath());
		runtime.exec(exec);
	}
	
	public String getPhantomjs() {
	  if (OSUtil.isWindowsLike()) {
	    return "phantomjs.exe";
	  }
	  return "phantomjs";
	}

	public void screenCapture(String url, File dest) throws Exception {
		URL uRL = PhantomjsServiceImpl.class.getResource("/");
		File phantomjs = new File(String.format("%s%s/%s", uRL.getFile(), phantomjsPath, getPhantomjs()));
		phantomjs.setExecutable(true);
		File js = new File(String.format("%s/%s/%s", uRL.getFile(), phantomjsPath, "capture.js"));
		Runtime runtime = Runtime.getRuntime();
		String[] exec = new String[] { phantomjs.getPath(), js.getAbsolutePath(), url, dest.getAbsolutePath() };
		logger.info(exec.toString());
		Process process = runtime.exec(exec);
		process.waitFor();
		InputStream is = process.getInputStream();
		StringBuilder message = new StringBuilder();
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		while ((line = br.readLine()) != null) {
			message.append(line);
		}
	}

}
