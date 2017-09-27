package com.xplus.commons.spring.springboot.quick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hunnyhu
 * @since 0.1
 */
@RestController // 等同于同时加上了@Controller和@ResponseBody
public class HelloController {

	@Autowired
	private AboutConfig aboutConfig;
	
	// 访问/hello或者/hi任何一个地址，都会返回一样的结果
	@RequestMapping(value = { "/hello", "/hi" }, method = RequestMethod.GET)
	public String say() {
		return "Hello, Quick Spring Boot.";
	}
	
	// 访问/about或者/help任何一个地址，都会返回一样的结果
	@RequestMapping(value = { "/about", "/help" }, method = RequestMethod.GET)
	public About about() {
		About about = new About();
		about.setName("Quick Spring Boot.");
		about.setVersion(aboutConfig.getVersion());
		about.setContent("这是一个快速入门的SpringBoot程序示例。");
		return about;
	}

}
