package com.maxopus.cloud.authorization.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import com.maxopus.cloud.authorization.mongo.oauth.MongoApprovalStore;
import com.maxopus.cloud.authorization.mongo.oauth.MongoClientDetailsService;
import com.maxopus.cloud.authorization.mongo.oauth.MongoTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter{

	@Autowired 
	private MongoClientDetailsService mongoClientDetailsService;
	
	@Autowired 
	private MongoTokenStore mongoTokenStore;
	
	@Autowired 
	private MongoApprovalStore mongoApprovalStore;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints/*.approvalStore(mongoApprovalStore)*/.tokenStore(mongoTokenStore).authenticationManager(authenticationManager);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// only for RemoteTokenService !!
		security.tokenKeyAccess("isAnonymous() || permitAll()").checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)throws Exception {
		clients.withClientDetails(mongoClientDetailsService);
	}
}
