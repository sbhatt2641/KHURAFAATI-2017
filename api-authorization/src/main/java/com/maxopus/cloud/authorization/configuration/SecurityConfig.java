package com.maxopus.cloud.authorization.configuration;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxopus.cloud.authorization.mongo.user.MongoUserDetailsManager;

@Configuration
public class SecurityConfig {

	@Autowired 
	private MongoUserDetailsManager mongoUserDetailsManager;
	
    @Configuration
    @Order(1)
    public static class Context1SecurityConfig extends WebSecurityConfigurerAdapter {

    	/*@Override
        @Bean
        protected UserDetailsService userDetailsService() {
            return new MongoUserDetailsManager();
        }*/
    	
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //@formatter:off
            http
                .antMatcher("/admin/**")
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/admin/dashboardLogin")
                    .loginProcessingUrl("/admin/login.do")
                    .defaultSuccessUrl("/admin/dashboard")
                    .permitAll()
                    .and()
                .logout()
                    //.logoutUrl("/admin/logout.do")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout.do")) // AntPathRequestMatcher for GET request
                    .permitAll();
            //@formatter:on
        }
        
        /*@Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
        	auth.userDetailsService(userDetailsService());
        }*/
    }
    
    @Configuration
    @Order(2)
    public static class Context2SecurityConfig extends WebSecurityConfigurerAdapter {

    	@Override
        protected void configure(HttpSecurity http) throws Exception {
            //@formatter:off
            http.antMatcher("/sso/**")
                .authorizeRequests()
                    .antMatchers("/", "/js/**", "/css/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/sso/ssoLogin")
                    .loginProcessingUrl("/sso/login.do")
                    .defaultSuccessUrl("/sso/home")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/sso/logout.do")
                    .permitAll();
            //@formatter:on
        }
    }

    @Configuration
    @RestController
    public static class AuthSSOSecurityConfig extends WebSecurityConfigurerAdapter {

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
    	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(mongoUserDetailsManager);
    }    
}
