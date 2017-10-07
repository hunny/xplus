package com.xplus.server.service.feign.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xplus.server.service.feign.hystric.SchedualServiceHiHystrix;

/**
 * 定义一个feign接口，通过@ FeignClient（“服务名”），来指定调用哪个服务。
 * 
 * @since 0.1
 */
@FeignClient(value = "service-hi", fallback = SchedualServiceHiHystrix.class)
public interface SchedualServiceHi {
	
	@RequestMapping(value = "/hi", method = RequestMethod.GET)
	String sayHiFromClientOne(@RequestParam(value = "name") String name);

}
