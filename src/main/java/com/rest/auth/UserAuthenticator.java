package com.rest.auth;

import com.rest.representations.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import com.rest.dao.apiuser.ApiUserDao;
import com.rest.representations.User;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {

	private static final Map<String, Set<String>> VALID_USERS = ImmutableMap.of("guest", ImmutableSet.of(), "user",
			ImmutableSet.of("USER"), "admin", ImmutableSet.of("ADMIN", "USER"));

	private ApiUserDao apiUserDao = new ApiUserDao();

	@Override
	public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {

		int userPrivilageLevel = apiUserDao.checkApiUser(credentials.getUsername(), credentials.getPassword());

		if (userPrivilageLevel == 1) {
			return Optional.of(new User(credentials.getUsername(), VALID_USERS.get("user")));
		} else if (userPrivilageLevel == 0) {
			return Optional.of(new User(credentials.getUsername(), VALID_USERS.get("admin")));
		}

		return Optional.empty();

	}
}