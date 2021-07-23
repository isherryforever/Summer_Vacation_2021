package com.hyh.springboot.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/toPage")
public class PageController {
	@RequestMapping("/{page}")
	public String toPage(@PathVariable String page)
	{
		return page;
	}
}
