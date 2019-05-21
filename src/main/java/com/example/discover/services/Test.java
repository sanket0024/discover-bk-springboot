package com.example.discover.services;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date : Apr 14, 2019
 *
 * @Author: Divyavijay Sahay
 */

@RestController
public class Test {

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello World";
	}

}
