package com.rest.dao.apiuser;

import org.skife.jdbi.v2.Handle;
//import org.jdbi.v3.core.*;
import com.rest.dao.DbConnectionHandle;
import com.rest.dao.mappers.ApiUserMapper;
import com.rest.modules.DbConnectionModule;
import com.rest.representations.ApiUser;
import com.google.inject.Injector;
import com.google.inject.Guice;

public class ApiUserDao {

	Injector injector = Guice.createInjector(new DbConnectionModule());
	DbConnectionHandle dbConenctionHandle = injector.getInstance(DbConnectionHandle.class);
	Handle handle = dbConenctionHandle.getHandle();

	public ApiUser checkApiUser(String username, String password) {

		String checkCredentialsQuery = "SELECT * FROM apiuser WHERE username = :username and password = :password";

		ApiUser apiUser = handle.createQuery(checkCredentialsQuery).map(new ApiUserMapper()).bind("username", username)
				.bind("password", password).first();

		return apiUser;

	}

	public ApiUser checkAccess(int userId, String username) {

		String checkAccessQuery = "SELECT * FROM apiuser WHERE username = :username";
		ApiUser apiUser = handle.createQuery(checkAccessQuery).map(new ApiUserMapper()).bind("username", username).first();

		return apiUser;
	}

}