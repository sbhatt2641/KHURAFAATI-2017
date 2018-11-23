package com.maxopus.cloud.resource.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class OAuth2ResourceUserConfig extends ResourceServerConfigurerAdapter {

   /* @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests().anyRequest().permitAll();
        // @formatter:on                
    }*/
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.requestMatchers().antMatchers("/", "/**")
            .and()
            .authorizeRequests().anyRequest().access("#oauth2.hasScope('read')");
        // @formatter:on
    }
    
    /*
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
         resources.resourceId("/");
    }
	
    //RemoteTokenServices will not work while using private/public key pair.
    @Primary
    @Bean
    public RemoteTokenServices tokenServices() {
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl("https://directsales-ub-local.ncw.webapps.rr.com:8081/oauth/check_token");
        tokenService.setClientId("fooClientId");
        tokenService.setClientSecret("fooSecret");
        return tokenService;
    }    
    
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }
    
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
         converter.setSigningKey("123");

//        Resource resource = new ClassPathResource("publicKey.txt");
//        String publicKey = null;
//
//        try {
//            publicKey = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
//        } catch (final IOException e) {
//            throw new RuntimeException(e);
//        }
//        converter.setVerifierKey(publicKey);
        return converter;
    }*/
}
