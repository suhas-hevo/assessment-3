package com.rest.services;

import io.dropwizard.auth.*;
import io.dropwizard.auth.basic.*;
import io.dropwizard.testing.junit5.*;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import com.rest.dao.contact.ContactDao;
import java.util.Optional;
import static org.mockito.Mockito.*;
import org.mockito.InOrder;
import org.mockito.Mock;
import com.rest.representations.*;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import com.rest.service.*;
import com.rest.dao.apiuser.*;
import com.rest.representations.*;


public class CheckUserServiceTest {
	
	private static ApiUserDao apiUserDao = mock(ApiUserDao.class);
	
	CheckUserService checkUserService = new CheckUserService(apiUserDao);
	private ApiUser apiUser;
	
	public CheckUserServiceTest() {}
	
	@BeforeEach
	public void setUp() {
		apiUser = new ApiUser();
		apiUser.setUserId(1);
		apiUser.setUserName("suhas");
		apiUser.setPassword("suhas123");
		apiUser.setPrivilageLevel(1);
	}

	@AfterEach
	void tearDown() {
		reset(apiUserDao);
	}
	
	@Test
	public void testCheckCredentialsUser() {
		
		when(apiUserDao.checkApiUser(apiUser.getUserName(), apiUser.getPassword())).thenReturn(apiUser);
		
		int privilage = checkUserService.checkCredentials(apiUser.getUserName(), apiUser.getPassword());
		
		Assert.assertEquals(1, privilage);
	}
	
	@Test
	public void testCheckCredentialsGuest() {
		when(apiUserDao.checkApiUser(apiUser.getUserName(), apiUser.getPassword())).thenReturn(null);
		
		int privilage = checkUserService.checkCredentials(apiUser.getUserName(), apiUser.getPassword());
		
		Assert.assertEquals(2, privilage);
	}
	
	@Test
	public void testCheckCredentialsAdmin() {
		apiUser.setPrivilageLevel(0);
		when(apiUserDao.checkApiUser(apiUser.getUserName(), apiUser.getPassword())).thenReturn(apiUser);
		
		int privilage = checkUserService.checkCredentials(apiUser.getUserName(), apiUser.getPassword());
		
		Assert.assertEquals(0, privilage);
	}
	
	@Test
	public void testCheckAccess() {
		when(apiUserDao.checkAccess(1,apiUser.getUserName())).thenReturn(apiUser);
		
		boolean access = checkUserService.checkAccess(1,apiUser.getUserName());
		
		Assert.assertTrue(access);
	}
	
	@Test
	public void testCheckAccessIncorrectUserName() {
		when(apiUserDao.checkAccess(1,apiUser.getUserName())).thenReturn(null);
		
		boolean access = checkUserService.checkAccess(1,apiUser.getUserName());
		
		Assert.assertFalse(access);
	}
	
	@Test
	public void testCheckAccessUnauthorized() {
		when(apiUserDao.checkAccess(1,apiUser.getUserName())).thenReturn(apiUser);
		
		boolean access = checkUserService.checkAccess(2,apiUser.getUserName());
		
		Assert.assertFalse(access);
	}

}
