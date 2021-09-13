package com.org.citius.pms.user.config.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.org.citius.pms.user.config.security.exceptionhandler.CustomBasicAuthenticationEntryPoint;
import com.org.citius.pms.user.config.security.service.JwtUserDetailsService;
import com.org.citius.pms.user.util.Hmac512PasswordEncoder;
import com.org.citius.pms.user.util.constants.AppConstants;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	private static final String SSHA_512 = "SSHA-512";

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(SSHA_512, new Hmac512PasswordEncoder());
		return new DelegatingPasswordEncoder(SSHA_512, encoders);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable().and().authorizeRequests()
				.antMatchers("/v2**", "/status/**", "/swagger**", "/authenticate/**", "/masterlist/**", "/login",
						"/register", "/forgotPassword")
				.permitAll().antMatchers("/user/**")
				.hasAnyAuthority(AppConstants.PATIENT, AppConstants.ADMIN, AppConstants.PHYSICIAN, AppConstants.NURSE)
				.antMatchers("/admin/**").hasAuthority(AppConstants.ADMIN).anyRequest().authenticated().and()
				.httpBasic().authenticationEntryPoint(customBasicAuthenticationEntryPoint()).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable().cors().disable();

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public AuthenticationEntryPoint customBasicAuthenticationEntryPoint() {
		CustomBasicAuthenticationEntryPoint obj = new CustomBasicAuthenticationEntryPoint();
		obj.setRealmName("PMS User and Admin Realm...");
		return obj;
	}

}