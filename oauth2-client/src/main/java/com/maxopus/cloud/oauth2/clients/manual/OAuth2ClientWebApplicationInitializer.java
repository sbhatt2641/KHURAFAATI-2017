package com.maxopus.cloud.oauth2.clients.manual;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

@Configuration
@EnableOAuth2Client
@RestController
@Profile("oauth2-client")
public class OAuth2ClientWebApplicationInitializer extends WebSecurityConfigurerAdapter {
 	
	/*@Value("${netiq.client.clientId}")
	private String clientId;

    @Value("${netiq.client.clientSecret}")
    private String clientSecret;
    
    @Value("${netiq.client.accessTokenUri}")
    private String accessTokenUri;
    
    @Value("${netiq.client.userAuthorizationUri}")
    private String userAuthUri;
    
    private String baseUrlOptions = "?scope=sAMAccountName%20openid&response_type=code&client_id=";*/
    
    @Value("${oauth2.grantType}")
	private String oauth2GrantType;
    
	@Autowired
	private OAuth2ClientContext oauth2ClientContext;

	@Autowired
    private CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;
	
	/*@Autowired
	private RestTemplate restTemplateNetIqClient;*/
		
	/*@RequestMapping("")
	public String home() {
		return "redirect:/home.html";
	}
	
	@RequestMapping("/")
	public String home(Principal user) {
		return "Hi " + user.getName();
	}*/
	
	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}
		
	/*@RequestMapping (value = "/localization", method = RequestMethod.GET)
	public ResponseEntity<Object> localization(@RequestParam(value = "code", required = false) String authorizationCode,
			  								   @RequestParam(value = "state", required = false) String clientState,
			  								   @RequestParam(value = "scope", required = false) String clientScope, HttpServletRequest request) throws URISyntaxException {
		ResponseEntity<Object> responseEntity = null;
		String redirectUrl = getBaseUrl(request);
		if(StringUtils.isBlank(authorizationCode)) {
			
			String essoAuthorizationUrl = userAuthUri + baseUrlOptions + clientId + "&redirect_uri=" + redirectUrl;
			URI uri = new URI(essoAuthorizationUrl);
		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setLocation(uri);
		    responseEntity = new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		} else {			
			HttpEntity<MultiValueMap<String, String>> oAuthEntity = createEssoRequestForAuthetication(authorizationCode, redirectUrl);
			responseEntity = restTemplateNetIqClient.exchange(accessTokenUri, HttpMethod.POST, oAuthEntity, Object.class);			
		}
		return responseEntity;
	}
	
	private String getBaseUrl(HttpServletRequest req) {
	    return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + req.getRequestURI();
	}*/
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
			http.antMatcher("/**").authorizeRequests()
				.antMatchers("/", "/login**", "/webjars/**").permitAll().anyRequest().authenticated()
				.and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
				.and().logout().logoutSuccessUrl("/").permitAll()
				.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
		// @formatter:on		
	}
			
	private Filter ssoFilter() {

		CompositeFilter filter = new CompositeFilter();
		List<Filter> filters = new ArrayList<>();

		OAuth2ClientAuthenticationProcessingFilter netIqFilter = new OAuth2ClientAuthenticationProcessingFilter("/localization");
		OAuth2RestTemplate netIqTemplate = new OAuth2RestTemplate(netIq(), oauth2ClientContext);
		netIqFilter.setRestTemplate(netIqTemplate);
		UserInfoTokenServices tokenServices = new UserInfoTokenServices(netIqResource().getUserInfoUri(), netIq().getClientId());
		tokenServices.setRestTemplate(netIqTemplate);
		netIqFilter.setTokenServices(tokenServices);
		netIqFilter.setAuthenticationSuccessHandler(customizeAuthenticationSuccessHandler);
		
		OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/google");
		OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oauth2ClientContext);
		googleFilter.setRestTemplate(googleTemplate);
		tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
		tokenServices.setRestTemplate(googleTemplate);
		googleFilter.setTokenServices(tokenServices);
				
		OAuth2ClientAuthenticationProcessingFilter maxopusFilter = new OAuth2ClientAuthenticationProcessingFilter("/maxopus");
		OAuth2RestTemplate maxopusTemplate = new OAuth2RestTemplate(maxopus(), oauth2ClientContext);
		if(!"authorization_code".equals(oauth2GrantType)){
			maxopusTemplate.setAccessTokenProvider(userAccessTokenProvider());			
		}
		maxopusFilter.setRestTemplate(maxopusTemplate);
		tokenServices = new UserInfoTokenServices(maxopusResource().getUserInfoUri(), maxopus().getClientId());
		tokenServices.setRestTemplate(maxopusTemplate);
		maxopusFilter.setTokenServices(tokenServices);
		maxopusFilter.setAuthenticationSuccessHandler(customizeAuthenticationSuccessHandler);

		filters.add(netIqFilter);
		filters.add(googleFilter);
		filters.add(maxopusFilter);	
		
		filter.setFilters(filters);

		return filter;
	}
	
	@Bean
    public AccessTokenProvider userAccessTokenProvider() {
        ResourceOwnerPasswordAccessTokenProvider accessTokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
        return accessTokenProvider;
    }
	
	@Bean
    @Primary
    public OAuth2ClientContextFilter oauth2ClientContextFilterWithPath() {
        return new Oauth2ClientContextFilterWithPath();
    }
	
	/*@Bean
	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext clientContext) {
		final OAuth2RestTemplate template = new OAuth2RestTemplate(reddit(), clientContext);
		final List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
		list.add(new UserAgentInterceptor());
		template.setInterceptors(list);
		final AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(
				Arrays.<AccessTokenProvider> asList(new MyAuthorizationCodeAccessTokenProvider(),
						new ImplicitAccessTokenProvider(), new ResourceOwnerPasswordAccessTokenProvider(),
						new ClientCredentialsAccessTokenProvider()));
		template.setAccessTokenProvider(accessTokenProvider);
		return template;
	}*/

	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}
	
	/*@Bean
    public OAuth2RestOperations myRestTemplate(@Value("${oauth.token}") String tokenUrl) {

        OAuth2RestTemplate template = new OAuth2RestTemplate(fullAccessresourceDetails(tokenUrl), new DefaultOAuth2ClientContext(
                new DefaultAccessTokenRequest()));
        template.setAccessTokenProvider(userAccessTokenProvider());
        return template;
    }*/
	
	@Bean
	@ConfigurationProperties("maxopus.oauth2.client")
	public OAuth2ProtectedResourceDetails maxopus() {
		
		OAuth2ProtectedResourceDetails protectedResourceDetails = null;
		if("authorization_code".equals(oauth2GrantType)){
			protectedResourceDetails = new AuthorizationCodeResourceDetails();			
		} else {
			ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
	        resource.setUsername("user");
	        resource.setPassword("user");
	        protectedResourceDetails = resource;
		}
		return protectedResourceDetails;
	}
	
	@Bean
	@ConfigurationProperties("maxopus.oauth2.resource")
	public ResourceServerProperties maxopusResource() {
		return new ResourceServerProperties();
	}

	@Bean
	@ConfigurationProperties("netiq.oauth2.client")
	public OAuth2ProtectedResourceDetails netIq() {		
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("netiq.oauth2.resource")
	public ResourceServerProperties netIqResource() {
		return new ResourceServerProperties();
	}

	@Bean
	@ConfigurationProperties("google.client")
	public OAuth2ProtectedResourceDetails google() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("google.resource")
	public ResourceServerProperties googleResource() {
		return new ResourceServerProperties();
	}
	
	/*@Bean(name = "restTemplateNetIqClient")
    public RestTemplate restTemplateNetIq() {
        return new RestTemplate(clientHttpRequestFactory(2000, 5000));
    }
	
    private ClientHttpRequestFactory clientHttpRequestFactory(int connectTimeout, int readTimeout) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }
	
	private HttpEntity<MultiValueMap<String, String>> createEssoRequestForAuthetication(String authorizationCode, String redirectUrl) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", authorizationCode);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", redirectUrl);
        return new HttpEntity<>(map, new HttpHeaders());
    }*/
}
