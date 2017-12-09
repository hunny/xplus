package com.example.bootweb.security.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.security.profile.HttpBasicAngularJSDemo;

@RestController
@HttpBasicAngularJSDemo
public class HttpBasicAngularJSController {
    private static final String template = "Hello World!";

    @RequestMapping("/greeting")
    public HttpBasicAngularJS greeting() {
        return new HttpBasicAngularJS(String.format(template));
    }
}
