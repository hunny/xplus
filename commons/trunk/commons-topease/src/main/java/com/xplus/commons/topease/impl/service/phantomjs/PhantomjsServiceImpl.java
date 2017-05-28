package com.xplus.commons.topease.impl.service.phantomjs;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import com.xplus.commons.topease.api.service.phantomjs.Phantomjs;
import com.xplus.commons.topease.api.service.phantomjs.PhantomjsExecutor;
import com.xplus.commons.tpl.api.TemplateMaker;

@Service
public class PhantomjsServiceImpl {

	private final Logger logger = LoggerFactory.getLogger(PhantomjsServiceImpl.class);

	@Resource(name = "commons-tpl.freeMarkerTemplateMaker")
	private TemplateMaker templateMaker;
	
	@Autowired
	private PhantomjsExecutor phantomjsExecutor;

	@Value("${commons-topease.tpl.phantomjs.path:/META-INF/phantomjs}")
	private String path;
	
	public void screenCaptureWithFtl(String url, File dest) throws Exception {

		File tpl = new File(String.format("%s/%s", path, "capture.ftl"));
		File out = new File(
				String.format("%s/%s/%s%s", dest.getParent(), "tmp", String.valueOf(System.currentTimeMillis()), ".js"));
		Map<String, Object> value = new HashMap<String, Object>();
		value.put("url", url);
		value.put("path", dest.getPath());
		templateMaker.setForceOverWrite(true);
		templateMaker.make(value, tpl.getPath(), out.getPath());
		
		phantomjsExecutor.run(new String[] {out.getAbsolutePath()});
	}
	
	public String phantomjsCommandLine(String [] args) throws Exception {
		return phantomjsExecutor.run(args);
	}

	public void screenCapture(Phantomjs phantomjs) throws Exception {
		URL uRL = PhantomjsServiceImpl.class.getResource("/");
		String url = phantomjs.getUrl();
		File js = new File(String.format("%s/%s/%s", uRL.getFile(), path, "capture.js"));
		File dest = new File(phantomjs.getScreenCapturePath());
		ObjectMapper objectMapper = new ObjectMapper();
		String cookies = objectMapper.writeValueAsString(phantomjs.getCookies());
		logger.info(cookies);
		String[] exec = new String[] {
				js.getAbsolutePath(), //
				url, //
				dest.getAbsolutePath(), //
				cookies //
				};
		logger.info(objectMapper.writeValueAsString(exec));
		logger.info(phantomjsExecutor.run(exec));
	}

}
