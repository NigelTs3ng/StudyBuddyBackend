package com.studybuddy.backend.response;

import com.studybuddy.backend.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class LoginResponse extends BaseResponse {
	private String accessToken;
	private User user;
}