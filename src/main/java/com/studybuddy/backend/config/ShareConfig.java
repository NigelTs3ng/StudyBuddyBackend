package com.studybuddy.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ShareConfig {
	@Value("${jwt-token-validity-time:18000}")
	private Integer validityTokenTime;
	@Value("${jwt.secret.key:kanban}")
	private String secretKey;
	@Value("${field.sensitive.list:password}")
	private String fieldSensitiveList;
}
