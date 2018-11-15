package com.maxopus.cloud.oauth2.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
//@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Value("${oauth2.clientType}")
	private String oauth2ClientType;
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	if("SERVER_CLIENT".equals(oauth2ClientType)) {
    		registry.addViewController("/").setViewName("redirect:/welcome");
    	} else {
    		registry.addRedirectViewController("/", "index");
    	}
        registry.addViewController("/welcome").setViewName("/welcomePage");
    }
}
