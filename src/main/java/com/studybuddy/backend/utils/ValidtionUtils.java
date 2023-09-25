package com.studybuddy.backend.utils;


import com.google.common.base.Strings;

import java.util.regex.Pattern;

public class ValidtionUtils {

	public static boolean checkEmptyOrNull(String... strings) {
		for (String s : strings) {
			if (Strings.isNullOrEmpty(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean validEmail(String email) {
		String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		return Pattern.compile(regexPattern).matcher(email).matches();
	}
}
