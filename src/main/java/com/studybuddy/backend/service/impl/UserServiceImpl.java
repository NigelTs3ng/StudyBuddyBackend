package com.studybuddy.backend.service.impl;

import com.studybuddy.backend.entity.User;
import com.studybuddy.backend.repository.UserRepository;
import com.studybuddy.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@Override
	public User login(String username, String password) {
		try {
			log.info("Login with user: " + username);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (userDetails == null) {
				return null;
			}
			if (!passwordEncoder.matches(password, userDetails.getPassword())) {
				return null;
			}
			User user = findByEmail(username);
			user.setPassword("******");
			return user;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	@Transactional
	public boolean register(User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.saveAndFlush(user);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkExistByEmail(String mail) {
		try {
			return userRepository.findByEmail(mail).isPresent();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		try {
			Optional<User> optUser = userRepository.findByEmail(email);
			if (optUser.isPresent()) {
				return optUser.get();
			}
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findTutors() {
		List<User> tutors = new ArrayList<>();
		try {
			return userRepository.findAllByRole("tutor");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return tutors;
	}

	@Override
	@Transactional(readOnly = true)
	public String findTutorNameById(int tutorId) {
		String tutorName = "";
		try {
			List<User> tutors =  findTutors();
			User tutor = tutors.stream().filter(t -> t.getId() == tutorId).findFirst().orElse(null);
			tutorName =  tutor == null ? "" : tutor.getFirstName() + tutor.getLastName();
			return tutorName;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return tutorName;
	}
}
