package com.maxopus.cloud.resource.user.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/v1")
@PreAuthorize("hasRole('ROLE_OAUTH_USER')")
public class UserController {

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
		return "{\"message\": \"Hello\"}";
    }
}
