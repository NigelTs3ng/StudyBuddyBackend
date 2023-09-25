package com.studybuddy.backend.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Gson gson() {
		return new GsonBuilder()
				.disableHtmlEscaping()
				.setPrettyPrinting()
				.create();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	}

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("test1234"));
	}
}
