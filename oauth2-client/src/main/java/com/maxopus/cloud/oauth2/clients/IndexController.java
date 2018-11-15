package com.maxopus.cloud.oauth2.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

	@Value("${oauth2.grantType}")
	private String oauth2GrantType;
	
	@RequestMapping("/home")
	public String home() {
		return "homePage";
	}
	
	@RequestMapping("/index")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("indexPage");
		modelAndView.addObject("oauth2GrantType", oauth2GrantType);
		return modelAndView;
	}
}
