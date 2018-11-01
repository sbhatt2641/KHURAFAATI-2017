package com.maxopus.cloud.authorization.configuration;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
public class AuthSSOSecurityConfig extends WebSecurityConfigurerAdapter {

	/*@Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new MongoUserDetailsManager();
    }*/
	
	@RequestMapping({"/me" })
	public Principal user(Principal principal) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("name", principal.getName());
		return principal;
	}        
    
    @Override
    protected void configure(HttpSecurity http) throws Exception { 
		// @formatter:off
        http.requestMatchers()
            .antMatchers("/login", "/login.do", "/oauth/authorize", "/oauth/token")
            .and()
            .csrf().disable()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .usernameParameter("name")
            .loginProcessingUrl("/login.do").permitAll();
     // @formatter:on
    }
    
    @Configuration
	@EnableResourceServer
	protected static class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
			// @formatter:on
		}
	}
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    /*@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userDetailsService());
    }*/
}
