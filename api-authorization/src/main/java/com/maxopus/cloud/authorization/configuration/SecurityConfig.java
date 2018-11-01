package com.maxopus.cloud.authorization.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import com.maxopus.cloud.authorization.mongo.user.MongoUserDetailsManager;

@Configuration
public class SecurityConfig {

	@Autowired 
	private MongoUserDetailsManager mongoUserDetailsManager;    

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(mongoUserDetailsManager);
    }    
}
