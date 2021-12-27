package com.rest.dao;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

public class DbConnectionHandle {
	
	private Handle handle = null;
	private String dbConnectionUrl;
	private String dbConnectionUserName;
	private String dbConnectionPassword;
	
	@Inject
	public DbConnectionHandle(@Named("url") String dbConnectionUrl,@Named("userName") String dbConnectionUserName,@Named("password") String dbConnectionPassword) {
		this.dbConnectionUrl = dbConnectionUrl;
		this.dbConnectionUserName = dbConnectionUserName;
		this.dbConnectionPassword = dbConnectionPassword;
	}
	
	public Handle getHandle() {

        
        DBI dbi = new DBI(dbConnectionUrl,dbConnectionUserName, dbConnectionPassword);
        
        try {
            handle = dbi.open();  
    	}
        catch(Exception e) {
        	e.printStackTrace();
        }
        
        return handle;
   }
	
	
}
