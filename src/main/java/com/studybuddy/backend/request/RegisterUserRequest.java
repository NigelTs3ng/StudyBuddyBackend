package com.studybuddy.backend.request;

import lombok.Getter;

@Getter
public class RegisterUserRequest {
	private String firstName;
	private String lastName;
	private String role;
	private String email;
	private String password;
}
