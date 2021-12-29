package com.rest.auth;

import io.dropwizard.auth.Authorizer;
import com.rest.representations.*;

public class UserAuthorizer implements Authorizer<User> {
	@Override
	public boolean authorize(User user, String role) {
		return user.getRoles() != null && user.getRoles().contains(role);
	}

}