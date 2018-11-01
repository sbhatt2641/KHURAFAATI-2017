package com.maxopus.cloud.authorization.configuration;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/admin/dashboard").setViewName("admin/dashboardPage");
        registry.addViewController("/sso/home").setViewName("sso/ssoHomePage");
        registry.addViewController("/admin/dashboardLogin").setViewName("admin/dashboardLoginPage");
        registry.addViewController("/sso/ssoLogin").setViewName("sso/ssoLoginPage");
    }
}
