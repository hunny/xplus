package com.example.bootweb.kotlin.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
	@GetMapping("/hello")
	fun helloKotlin(): String {
		return "hello world"
	}
}