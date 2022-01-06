package com.rest.service;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rest.dao.apiuser.*;
import com.rest.dao.contact.ContactDao;
import com.rest.dao.mappers.ApiUserMapper;
import com.rest.representations.*;

public class CheckUserService {
	
	Injector injector = Guice.createInjector();
    ApiUserDao apiUserDao = injector.getInstance(ApiUserDao.class);
    
    public CheckUserService() {}
    
    public CheckUserService(ApiUserDao apiUserDao){
    	this.apiUserDao = apiUserDao;
    }
    
    public int checkCredentials(String username, String password) {
    	ApiUser apiUser = apiUserDao.checkApiUser(username, password);
    	
    	if (apiUser == null) {
			return 2;
		} else if (apiUser.getPrivilageLevel() == 0) {
			return 0;
		} else {
			return 1;
		}
    }
    
    public boolean checkAccess(int userId, String username) {
    	
    	ApiUser apiUser = apiUserDao.checkAccess(userId, username);

		if (apiUser != null && (apiUser.getUserId() == userId || apiUser.getPrivilageLevel() == 0)) {
			return true;
		} else {
			return false;
		}
    }

}
