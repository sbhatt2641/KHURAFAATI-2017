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
		return "loginPage";
	}

	@RequestMapping("/admin/dashboard")
	public String dashboard() {
		return "admin/dashboardPage";
	}

	@RequestMapping("/admin/dashboardLogin")
	public String dashboardLoginPage() {
		return "admin/dashboardLoginPage";
	}
	
	@RequestMapping("/sso/ssoLogin")
	public String ssoLoginPage() {
		return "sso/ssoLoginPage";
	}
	
	@RequestMapping("/sso/home")
	public String ssoHome() {
		return "sso/ssoHomePage";
	}
}
