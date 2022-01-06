package com.rest.filter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rest.dao.apiuser.ApiUserDao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import com.rest.service.*;

@Provider
public class AccessAllowFilter implements ContainerRequestFilter {

	Injector injector = Guice.createInjector();
    CheckUserService checkUserService = injector.getInstance(CheckUserService.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		GetURLInfo requestInfo = new GetURLInfo(requestContext);

		Integer userId = requestInfo.getUserId();

		String userName = requestInfo.getUserName();

		if (!checkUserService.checkAccess(userId, userName)) {
			throw new WebApplicationException(new IllegalArgumentException("Access Not Allowed"),
					Response.Status.UNAUTHORIZED);
		}
	}
}

class GetURLInfo {
	private ContainerRequestContext requestContext;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetURLInfo.class);

	GetURLInfo(ContainerRequestContext requestContext) {
		this.requestContext = requestContext;
	}

	public int getUserId() {

		String[] requestUrlSplits = {};

		try {
			URL requestUrl = requestContext.getUriInfo().getRequestUri().toURL();
			requestUrlSplits = requestUrl.getPath().split("/");
		} catch (MalformedURLException ex) {
			LOGGER.error("malformed URL");
		}

		Integer userId = Integer.parseInt(requestUrlSplits[2]);

		return userId;
	}

	public String getUserName() {

		String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		String[] userNamePasswordSplits = authHeader.split(" ");
		Base64.Decoder decoder = Base64.getDecoder();
		String usernameAndPass = new String(decoder.decode(userNamePasswordSplits[1]));

		String userName = usernameAndPass.split(":")[0];

		return userName;

	}
}