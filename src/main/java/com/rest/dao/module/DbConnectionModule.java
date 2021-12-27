package com.rest.dao.module;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import java.util.Properties;

import com.rest.dao.module.PropertyGetter;

public class DbConnectionModule extends AbstractModule {
	
	private PropertyGetter propertyGetter = new PropertyGetter();
	private Properties properties = propertyGetter.getProperties();
	
	  @Override
	  protected void configure() {
		  
	      bind(String.class)
	         .annotatedWith(Names.named("url"))
	         .toInstance(properties.getProperty("datasource.url"));
	      
	      bind(String.class)
	         .annotatedWith(Names.named("userName"))
	         .toInstance(properties.getProperty("datasource.userName"));
	      
	      bind(String.class)
	         .annotatedWith(Names.named("password")).toInstance("suhas789");
	         //.toInstance(prop.getProperty("datasource.password"));
	   } 
}
