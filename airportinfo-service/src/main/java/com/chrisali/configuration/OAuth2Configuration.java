package com.chrisali.configuration;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/* @Configuration
@EnableResourceServer
@EnableAuthorizationServer */
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
	
	private static final Logger log = LogManager.getLogger(OAuth2Configuration.class);
	private static final String APPLICATION_NAME = "airportinfo";
	
	@Autowired
	AuthenticationManagerBuilder authenticationManager;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		log.info("==================================");
		log.info("Setting up Authentication Server");
		log.info("==================================");
		
		endpoints.authenticationManager(new AuthenticationManager() {

			@Override
			public Authentication authenticate(Authentication authenticate) throws AuthenticationException {
				return authenticationManager.getOrBuild().authenticate(authenticate);
			}
		});
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
			.inMemory()
			.withClient(APPLICATION_NAME)
			.authorizedGrantTypes("password", "authorization_code", "refresh_token")
			.authorities("ROLE_FREE", "ROLE_PREMIUM", "ROLE_ADMIN")
			.scopes("read", "write")
			.resourceIds(APPLICATION_NAME)
			.secret("test123$%^");
	}
}
