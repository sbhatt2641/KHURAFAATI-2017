package com.maxopus.cloud.oauth2.clients;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@Profile("oauth2-client")
public class ClientController {

	@Autowired
	private OAuth2RestOperations restTemplate;

	@Value("${maxopus.oauth2.resource.apiUrl}")
	private String resourceURI;
	    
    @RequestMapping("/resource")
    public Principal resource(Principal principal) {
        return principal;
    }
    
    @RequestMapping({"/userInfo" })
	public Principal userInfo(Principal principal) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("name", principal.getName());
		return principal;
	}
    	
	@RequestMapping("/homeAction")
	public JsonNode home() {
		return restTemplate.getForObject(resourceURI, JsonNode.class);		
	}
	
	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}
}
