package com.maxopus.cloud.resource.user.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.EurekaClient;

@RestController
@RequestMapping("/users/v1")
@PreAuthorize("hasRole('ROLE_OAUTH_USER')")
public class UserController {

	@Autowired
    @Lazy
    private EurekaClient eurekaClient;
	
	@Value("${spring.application.name}")
    private String appName;
    
    @Value("${server.port}")
    private String portNumber;
	
    @RequestMapping("/me")
    public Principal me(Principal principal) {
        return principal;
    }
        
    @RequestMapping({"/userInfo" })
	public Principal user(Principal principal) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("name", principal.getName());
		return principal;
	}
    
    @RequestMapping(method=RequestMethod.GET, value="/test")
    public String resource() {
    	String str = String.format("Welcome To User Service at '%s with Port number %s' !", eurekaClient.getApplication(appName).getName(), portNumber);        
		return "{\"message\": \"" + str + "\"}";
    }
}
