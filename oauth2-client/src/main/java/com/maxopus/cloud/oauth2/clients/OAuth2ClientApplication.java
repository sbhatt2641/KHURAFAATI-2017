package com.maxopus.cloud.oauth2.clients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@PropertySource("classpath:${spring.profiles.active}.properties")
public class OAuth2ClientApplication {
	
	static {
		System.setProperty("javax.net.ssl.trustStore", "C:/certs/trust-store.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "Charter817");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(OAuth2ClientApplication.class, args);
	}
	
	/*public static void main(String[] args) {
		new SpringApplicationBuilder(OAuth2SSOClientApplication.class).properties("spring.config.name=sso-client").run(args);
	}*/
}