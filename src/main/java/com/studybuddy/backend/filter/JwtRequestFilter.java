package com.studybuddy.backend.filter;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.studybuddy.backend.entity.User;
import com.studybuddy.backend.service.impl.AuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private AuthenticationService jwtTokenUtil;
	@Autowired
	private UserDetailsService jwtUserDetailsService;
	@Autowired
	private Gson gson;

	@SneakyThrows
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		User user = null;
		String jwtToken = jwtTokenUtil.extractTokenFromRequest(request);
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		if (!Strings.isNullOrEmpty(jwtToken)) {
			try {
				user = jwtTokenUtil.getUserFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				log.info("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				log.info("JWT Token has expired");
			}
		} else {
			log.info("JWT Token does not begin with Bearer String");
		}

		// Once we get the token validate it.
		if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(user.getEmail());

			 // if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				log.info("JWT validate token success");
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

}
