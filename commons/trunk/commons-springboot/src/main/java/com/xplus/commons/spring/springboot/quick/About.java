package com.xplus.commons.spring.springboot.quick;

import java.io.Serializable;

public class About implements Serializable {

	private static final long serialVersionUID = -2651861710445073442L;
	
	private String name;
	private String version;
	private String content;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	

}
