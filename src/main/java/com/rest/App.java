package com.rest;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import com.rest.controller.ContactRESTController;
import com.rest.representations.*;
import com.rest.auth.*;
import com.rest.filter.*;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;


public class App extends Application<Configuration> {


	@Override
	public void run(Configuration c, Environment e) throws Exception {
		
		
		e.jersey().register(new ContactRESTController(e.getValidator()));
		e.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator())
                .setAuthorizer(new UserAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()));
		e.jersey().register(RolesAllowedDynamicFeature.class);
        e.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        e.jersey().register(AccessCheckFeature.class);
        
	}
	


	public static void main(String[] args) throws Exception {
		new App().run(args);
	}
}