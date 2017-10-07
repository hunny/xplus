package com.xplus.server.service.feign.hystric;

import org.springframework.stereotype.Component;

import com.xplus.server.service.feign.api.SchedualServiceHi;

@Component
public class SchedualServiceHiHystrix implements SchedualServiceHi {

	@Override
	public String sayHiFromClientOne(String name) {
		return "Sorry, " + name + " hystrix.";
	}

}
