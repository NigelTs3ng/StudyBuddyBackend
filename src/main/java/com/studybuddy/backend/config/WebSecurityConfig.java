package com.studybuddy.backend.config;

import com.studybuddy.backend.exception.JwtAuthenticationException;
import com.studybuddy.backend.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Autowired
	private JwtAuthenticationException jwtAuthenticationEntryPoint;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// Enable CORS and disable CSRF
		http.csrf(csrf -> csrf.disable());

//		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));

		http.authorizeHttpRequests(request ->
				request.requestMatchers("/app/user/login", "/app/user/register", "/ws-chat/**").permitAll()
				.requestMatchers(HttpMethod.OPTIONS).permitAll()
				.anyRequest().authenticated());

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
