package com.rest.dao.apiuser;

import org.skife.jdbi.v2.Handle;
//import org.jdbi.v3.core.*;
import com.rest.dao.DbConnectionHandle;
import com.rest.dao.mappers.ApiUserMapper;
import com.rest.representations.ApiUser;
import com.google.inject.Injector;
import com.google.inject.Guice;
import com.rest.dao.module.DbConnectionModule;

public class ApiUserDao{
	
 
	Injector injector = Guice.createInjector(new DbConnectionModule());
	DbConnectionHandle dbConenctionHandle = injector.getInstance(DbConnectionHandle.class);
	Handle handle =  dbConenctionHandle.getHandle();
	
	public int checkApiUser(String username,String password){
    	
    	String query = "SELECT * FROM apiuser WHERE username = :username and password = :password";
    	
    	ApiUser apiUser = handle.createQuery(query)
    							.map(new ApiUserMapper())
    							.bind("username", username)
    							.bind("password", password)
    							.first();
        
    	if (apiUser == null) {
    		return 2;
    	}
    	else if (apiUser.getPrivilageLevel() == 0){
    		return 0;
    	}
    	else {
    		return 1;
    	}
    	
    }
	
	
	public boolean checkAccess(int userId,String username){
    	
    	String query = "SELECT * FROM apiuser WHERE username = :username";
    	ApiUser apiUser = handle.createQuery(query)
    							.map(new ApiUserMapper())
    							.bind("username", username)
    							.first();
        
    	if (apiUser != null && (apiUser.getUserId() == userId || apiUser.getPrivilageLevel() == 0)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }



}