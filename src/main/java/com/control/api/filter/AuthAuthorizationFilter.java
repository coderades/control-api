package com.control.api.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if ("/api/login".equals(request.getServletPath())) {
			filterChain.doFilter(request, response);
		} else {
			final var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					final var token = authorizationHeader.substring("Bearer ".length());
					final var algorithm = Algorithm.HMAC256("secret".getBytes());
					final var jwtVerifier = JWT.require(algorithm).build();
					final var decodedJWT = jwtVerifier.verify(token);
					final var username = decodedJWT.getSubject();
					final var roles = decodedJWT.getClaim("roles").asArray(String.class);

					final java.util.Collection<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = new ArrayList<>();
					java.util.Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

					final var authenticationToken = new UsernamePasswordAuthenticationToken(username, null,
							authorities);

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				} catch (Exception e) {
					log.error("Error Logging in: {}", e.getMessage());		
					
					response.setHeader("error", e.getMessage());
					response.setStatus(org.springframework.http.HttpStatus.FORBIDDEN.value());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);

					final Map<String, String> error = new HashMap<>();
					new ObjectMapper().writeValue(response.getOutputStream(), error);
					error.put("error_message", e.getMessage());
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}

}
