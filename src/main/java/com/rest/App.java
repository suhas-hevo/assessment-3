package com.rest;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import com.rest.controller.ContactController;
import com.rest.dao.contact.ContactDao;
import com.rest.representations.*;
import com.rest.auth.*;
import com.rest.filter.*;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import com.google.inject.Guice;
import com.google.inject.Injector;


public class App extends Application<Configuration> {
	Injector injector = Guice.createInjector();
	ContactController contactController = injector.getInstance(ContactController.class);
    
	@Override
	public void run(Configuration configuration, Environment environment) throws Exception {

		environment.jersey().register(contactController);
		environment.jersey()
				.register(new AuthDynamicFeature(
						new BasicCredentialAuthFilter.Builder<User>().setAuthenticator(new UserAuthenticator())
								.setAuthorizer(new UserAuthorizer()).setRealm("BASIC-AUTH-REALM").buildAuthFilter()));
		environment.jersey().register(RolesAllowedDynamicFeature.class);
		environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
		environment.jersey().register(AccessCheckFeature.class);

	}

	public static void main(String[] args) throws Exception {
		new App().run(args);
	}
}