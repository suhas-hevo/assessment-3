package com.rest.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import io.dropwizard.auth.Auth;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rest.dao.contact.ContactDao;
import com.rest.representations.Contact;
import com.rest.representations.*;
import com.rest.filter.*;
import java.util.Optional;
import java.util.List;
import java.util.Collections;
import javax.validation.Valid;


public class ContactService{
	
	Injector injector = Guice.createInjector();
    ContactDao contactDao = injector.getInstance(ContactDao.class);
    
    public ContactService() {}
    
    public ContactService(ContactDao contactDao){
    	this.contactDao = contactDao;
    }
    
    public List<Contact> getAllContacts(Integer userId) {
    	return contactDao.getAllContacts(userId);
    }
	
	public List<Contact> getContactByName(Integer userId, Optional<String> firstname, Optional<String> lastname){
		List<Contact> resultContacts = Collections.<Contact>emptyList();
		
		if (firstname.isPresent() && lastname.isPresent()) {

			resultContacts = contactDao.getContactByName(userId, firstname.get(), lastname.get());

		} else if (firstname.isPresent() && !lastname.isPresent()) {

			resultContacts = contactDao.getContactByFirstName(userId, firstname.get());

		} else if (!firstname.isPresent() && lastname.isPresent()) {

			resultContacts= contactDao.getContactByLastName(userId, lastname.get());

		}
		
		return resultContacts;
	}
	
	
	public boolean insertContact(Integer userId, Integer contactId, Contact contact) {
		if (contactDao.getContactById(userId, contact.getId()) == null && contactDao.insertContact(userId, contact.getId(), contact)) {
			return true;
		}
		
		return false;
	}
	
	public boolean updateContact(Integer userId, Integer contactId, Contact contact) {
		
		if (contactDao.getContactById(userId, contact.getId()) != null && contactDao.updateContact(userId, contact.getId(), contact)) {
			return true;
		}
		
		return false;
		
	}
	
	public boolean removeContact(Integer userId, Integer contactId) {
		
		if ( contactDao.getContactById(userId, contactId) == null) {
			return false;
		}
		contactDao.removeContact(userId, contactId);
		return true;
	}
}