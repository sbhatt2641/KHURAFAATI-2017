package com.maxopus.cloud.authorization.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Order(1)
public class AdminContextSecurityConfig extends WebSecurityConfigurerAdapter {

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
