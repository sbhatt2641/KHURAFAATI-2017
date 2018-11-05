package com.maxopus.cloud.authorization.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(2)
public class Context2SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http.csrf().disable().antMatcher("/context2/**")
            .authorizeRequests()
                .antMatchers("/", "/js/**", "/css/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/context2/context2Login")
                .loginProcessingUrl("/context2/login.do")
                .defaultSuccessUrl("/context2/home")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/context2/logout.do")
                .permitAll();
        //@formatter:on
    }
}
