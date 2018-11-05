package com.maxopus.cloud.authorization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String root() {
		return "index";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "ssoLoginPage";
	}

	@RequestMapping("/admin/dashboard")
	public String dashboard() {
		return "admin/dashboardPage";
	}

	@RequestMapping("/admin/dashboardLogin")
	public String dashboardLoginPage() {
		return "admin/dashboardLoginPage";
	}
	
	@RequestMapping("/context2/context2Login")
	public String context2LoginPage() {
		return "context2/login";
	}
	
	@RequestMapping("/context2/home")
	public String context2Home() {
		return "context2/context2HomePage";
	}
}
