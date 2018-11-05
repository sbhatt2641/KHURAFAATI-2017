package com.maxopus.cloud.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@SpringBootApplication
@EnableResourceServer
public class ResourceServerApplication extends /*WebSecurityConfigurerAdapter*/ ResourceServerConfigurerAdapter {

	static {
		System.setProperty("javax.net.ssl.trustStore", "C:/certs/trust-store.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "Charter817");
	}
	
    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
    
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }
    
    @Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
		// @formatter:on
	}
	*/
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .requestMatchers().antMatchers("/", "/**")
                .and()
                .authorizeRequests().anyRequest().access("#oauth2.hasScope('read')");
        // @formatter:on
    }
    
    /*
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
         resources.resourceId("/");
    }*/
}
