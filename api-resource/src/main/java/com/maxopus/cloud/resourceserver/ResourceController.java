package com.maxopus.cloud.resourceserver;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ROLE_OAUTH_USER')")
public class ResourceController {

    @RequestMapping("/")
    public Principal resource(Principal principal) {
        return principal;
    }
    
    @RequestMapping({"/userInfo" })
	public Principal user(Principal principal) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("name", principal.getName());
		return principal;
	}
}
