package com.rest.filter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rest.dao.apiuser.ApiUserDao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

@Provider
public class AccessAllowFilter implements ContainerRequestFilter {
	
	private ApiUserDao apiUserDao = new ApiUserDao();
	
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	
    	GetURLInfo requestInfo = new GetURLInfo(requestContext);
     	
    	Integer userId = requestInfo.getUserId();
    	String userName = requestInfo.getUserName();
    	
        if (!apiUserDao.checkAccess(userId,userName)) {
            throw new WebApplicationException(new IllegalArgumentException("Access Not Allowed"),Response.Status.UNAUTHORIZED);
        }
    }
}






class GetURLInfo{
	private ContainerRequestContext requestContext;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetURLInfo.class);
	
	GetURLInfo(ContainerRequestContext requestContext){
		this.requestContext = requestContext;
	}
	
	public int getUserId() {
		
		String[] strArr1 = {};
    	
    	try {
    	    URL requestUrl = requestContext.getUriInfo().getRequestUri().toURL();
    	    strArr1  = requestUrl.getPath().split("/");
    	  } 
    	catch (MalformedURLException ex) {
    		LOGGER.info("malformed URL");
    	  }
    	
    	Integer userId = Integer.parseInt(strArr1[2]);
    	
    	return userId;
	}
	
	
	public String getUserName() {
		
		String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
        String[] strArr2  = authHeader.split(" ");
        Base64.Decoder decoder = Base64.getDecoder(); 
        String usernameAndPass = new String(decoder.decode(strArr2[1]));
        
        String userName = usernameAndPass.split(":")[0];
        
        return userName;
    	
    }
}