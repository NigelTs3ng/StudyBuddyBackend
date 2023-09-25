package com.studybuddy.backend.service.impl;

import com.studybuddy.backend.entity.User;
import com.studybuddy.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Optional<User> optUser = userRepository.findByEmail(username);
			if (!optUser.isPresent()) {
				return null;
			}
			User user = optUser.get();

			SimpleGrantedAuthority role = new SimpleGrantedAuthority(user.getRole());
			Collection<SimpleGrantedAuthority> roleList = new ArrayList<>();
			roleList.add(role);
			return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()), user.getPassword(), roleList);
		} catch (Exception e) {
			log.error("Failure in autoLogin", e);
		}
		return null;
	}
}
