package com.xplus.commons.topease.impl.service.phantomjs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import com.xplus.commons.topease.api.service.phantomjs.PhantomjsExecutor;
import com.xplus.commons.util.operatorsystem.OSUtil;

@Component
public class PhantomjsExecutorImpl implements PhantomjsExecutor {

	private final Logger logger = LoggerFactory.getLogger(PhantomjsExecutorImpl.class);

	@Value("${commons-topease.tpl.phantomjs.path:/META-INF/phantomjs}")
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String run(String[] args) throws Exception {
		URL uRL = PhantomjsExecutorImpl.class.getResource("/");
		File phantomjsFile = new File(String.format("%s%s/%s", uRL.getFile(), path, getPhantomjs()));
		phantomjsFile.setExecutable(true);
		ObjectMapper objectMapper = new ObjectMapper();
		Runtime runtime = Runtime.getRuntime();
		List<String> cmds = new ArrayList<String>();
		cmds.add(phantomjsFile.getPath());
		cmds.addAll(Arrays.asList(args));
		logger.debug(objectMapper.writeValueAsString(cmds));
		Process process = runtime.exec(cmds.toArray(new String[] {}));
		process.waitFor();
		InputStream is = process.getInputStream();
		StringBuilder message = new StringBuilder();
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		while ((line = br.readLine()) != null) {
			message.append(line);
		}
		String out = message.toString();
		logger.debug(out);
		return out;
	}

	private String getPhantomjs() {
		if (OSUtil.isWindowsLike()) {
			return "phantomjs.exe";
		}
		return "phantomjs";
	}

}
