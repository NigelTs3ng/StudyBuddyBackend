package com.studybuddy.backend.controller;


import com.google.common.base.Strings;
import com.studybuddy.backend.entity.Question;
import com.studybuddy.backend.entity.Subject;
import com.studybuddy.backend.entity.User;
import com.studybuddy.backend.request.LoginRequest;
import com.studybuddy.backend.request.RegisterUserRequest;
import com.studybuddy.backend.response.FindTutorResponse;
import com.studybuddy.backend.response.LoginResponse;
import com.studybuddy.backend.response.RegisterUserResponse;
import com.studybuddy.backend.service.SubjectService;
import com.studybuddy.backend.service.UserService;
import com.studybuddy.backend.service.impl.AuthenticationService;
import com.studybuddy.backend.utils.ValidtionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/user")
@Slf4j
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private SubjectService subjectService;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest rq) throws Exception {
		LoginResponse res = LoginResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		if (Strings.isNullOrEmpty(rq.getEmail())) {
			res.setCode("01");
			res.setDesc("Please enter your email");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		} else if (!ValidtionUtils.validEmail(rq.getEmail())) {
			res.setCode("01");
			res.setDesc("Invalid format email");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		if (Strings.isNullOrEmpty(rq.getPassword())) {
			res.setCode("01");
			res.setDesc("Please enter your password");
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}
		try {
			User user = userService.login(rq.getEmail(), rq.getPassword());
			if (user == null) {
				res.setCode("02");
				res.setDesc("Wrong email or password");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			if ("tutor".equalsIgnoreCase(user.getRole())) {
				List<Subject> subjects = subjectService.findAll();
				user.setSubjects(subjects);
			}
			user.getSubjects().forEach(subject -> subject.getQuestions().forEach(question -> {
				question.setTutorName(userService.findTutorNameById(question.getTutorId()));
			}));

			User newUser = user.toBuilder().build();
			newUser.setSubjects(new ArrayList<>());
			final String token = authenticationService.generateToken(newUser);

			res.setUser(user);
			res.setAccessToken(token);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			res.setCode("99");
			res.setDesc("System error. Please try again");
		}
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisterUserResponse> regis(@RequestBody RegisterUserRequest rq) throws Exception {
		RegisterUserResponse res = RegisterUserResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		try {
			if (!ValidtionUtils.checkEmptyOrNull(rq.getEmail())) {
				if (!ValidtionUtils.validEmail(rq.getEmail())) {
					res.setCode("01");
					res.setDesc("Invalid format email");
					return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
				}
			} else {
				res.setCode("01");
				res.setDesc("Please enter your email");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			if (ValidtionUtils.checkEmptyOrNull(rq.getPassword())) {
				res.setCode("01");
				res.setDesc("Please enter your password");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			if (ValidtionUtils.checkEmptyOrNull(rq.getFirstName(), rq.getLastName())) {
				res.setCode("01");
				res.setDesc("Please enter your name");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			if (ValidtionUtils.checkEmptyOrNull(rq.getRole())) {
				res.setCode("01");
				res.setDesc("Please enter your role");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}

			boolean isExist = userService.checkExistByEmail(rq.getEmail());
			if (isExist) {
				res.setCode("02");
				res.setDesc("Email have existed");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			User user = User.builder()
					.email(rq.getEmail())
					.firstName(rq.getFirstName())
					.lastName(rq.getLastName())
					.role(rq.getRole())
					.password(rq.getPassword())
					.build();
			boolean register = userService.register(user);
			if (!register) {
				res.setCode("03");
				res.setDesc("Register failed. Please try again");
				return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
			}
			user.setPassword("******");
			res.setUser(user);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			res.setCode("99");
			res.setDesc("System error. Please try again");
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/tutors", method = RequestMethod.GET)
	public ResponseEntity<FindTutorResponse> findTutors() {
		FindTutorResponse res = FindTutorResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		try {
			List<User> tutors = userService.findTutors();
			res.setTutors(tutors);
			return ResponseEntity.ok(res);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			res.setCode("99");
			res.setDesc("System error. Please try again");
			return ResponseEntity.internalServerError().body(res);
		}
	}
}
