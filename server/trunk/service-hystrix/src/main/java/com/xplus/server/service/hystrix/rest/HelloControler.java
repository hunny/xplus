package com.xplus.server.service.hystrix.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xplus.server.service.hystrix.service.HelloService;

@RestController
public class HelloControler {

	@Autowired
	private HelloService helloService;

	@RequestMapping(value = "/hi")
	public String hi(@RequestParam String name) {
		return helloService.hiService(name);
	}

}
