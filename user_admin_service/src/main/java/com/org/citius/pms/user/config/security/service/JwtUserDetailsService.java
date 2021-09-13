package com.org.citius.pms.user.config.security.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.org.citius.pms.user.service.UserService;
import com.org.citius.pms.user.util.Hmac512PasswordEncoder;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUserDetailsService.class);

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			com.org.citius.pms.user.service.dao.User user = this.userService.queryUserName(username);
			if (Objects.nonNull(user)) {
				Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
				grantedAuthorities.add(new SimpleGrantedAuthority(
						Objects.nonNull(user.getUserRole().getRoleName()) ? user.getUserRole().getRoleName() : null));
				return new User(Objects.nonNull(user.getEmailId()) ? user.getEmailId() : null,
						Objects.nonNull(user.getPasswrd()) ? Hmac512PasswordEncoder.SSHA512_PREFIX + user.getPasswrd()
								: null,
						Objects.nonNull(grantedAuthorities) ? grantedAuthorities : null);
			}
		} catch (Exception e) {
			LOGGER.error("User not found with username: " + username);
		}
		throw new UsernameNotFoundException("User not found with username: " + username);
	}
}