package com.idosinchuk.handlemovies.microservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	private String readProperty(String param) {
		return env.getProperty(param);
	}

	private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(readProperty("spring.security.user.name"))
				.password(encoder.encode(readProperty("spring.security.user.password"))).roles("ADMIN");

	}
}
