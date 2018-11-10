package com.maxopus.cloud.authorization.configuration;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.maxopus.cloud.authorization.mongo.oauth.MongoApprovalStore;
import com.maxopus.cloud.authorization.mongo.oauth.MongoClientDetailsService;

@Configuration
@EnableAuthorizationServer
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter{

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuthConfiguration.class);
	
	@Autowired 
	private MongoClientDetailsService mongoClientDetailsService;
	
	@Autowired 
	private TokenStore tokenStore;
	
	@Autowired
	private TokenEnhancer tokenEnhancer;
	
	@Autowired(required = false) 
	private JwtAccessTokenConverter accessTokenConverter;
	
	@Autowired 
	private MongoApprovalStore mongoApprovalStore;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		LOGGER.info("Entering AuthorizationServerEndpointsConfigurer ..." + tokenEnhancer);
		endpoints/*.approvalStore(mongoApprovalStore)
				.authorizationCodeServices(authorizationCodeServices())*/
				.tokenServices(tokenServices())
				/*.tokenStore(tokenStore)
				.tokenEnhancer(tokenEnhancerChain)*/
				.authenticationManager(authenticationManager);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// only for RemoteTokenService !!
		LOGGER.info("Entering AuthorizationServerSecurityConfigurer ...");
		security.tokenKeyAccess("isAnonymous() || permitAll()").checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)throws Exception {
		LOGGER.info("Entering ClientDetailsServiceConfigurer ...");
		clients.withClientDetails(mongoClientDetailsService);
	}
	
	@Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setSupportRefreshToken(true);
        List<TokenEnhancer> enhancers = new ArrayList<>();
        if (accessTokenConverter != null) {
            enhancers.add(accessTokenConverter);
        }

        //Some custom enhancer
        enhancers.add(tokenEnhancer);

        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(enhancers);
        defaultTokenServices.setTokenEnhancer(enhancerChain);

        return defaultTokenServices;
    }
	
	/*@Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new MongoAuthorizationCodeServices();
    }*/	
}
