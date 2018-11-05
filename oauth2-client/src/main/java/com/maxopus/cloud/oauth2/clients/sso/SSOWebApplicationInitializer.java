package com.maxopus.cloud.oauth2.clients.sso;

import java.security.Principal;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@EnableOAuth2Sso
@RestController
@Profile("sso-client")
public class SSOWebApplicationInitializer extends WebSecurityConfigurerAdapter {
 
	@RequestMapping("/")
	public String home(Principal user) {
		return "Hello " + user.getName();
	}	
}
