package com.xplus.server.service.hystrix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class HelloService {

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "hystrix")
	public String hiService(String name) {
		return restTemplate.getForObject("http://SERVICE-HI/hi?name=" + name, String.class);
	}
	
	public String hystrix(String name) {
		return "Sorry," + name + " error.";
	}

}
