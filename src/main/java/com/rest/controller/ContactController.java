package com.rest.controller;

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
import com.rest.service.*;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class ContactController {
	
	Injector injector = Guice.createInjector();
    ContactDao contactDao = injector.getInstance(ContactDao.class);
    ContactService contactService = injector.getInstance(ContactService.class);
    
    public ContactController() {}
    
    public ContactController(ContactService contactService){
    	this.contactService = contactService;
    }

	@GET
	@Path("/{uid}/contacts/")
	@AccessCheck
	@RolesAllowed({ "USER" })
	public Response getAllContacts(@PathParam("uid") Integer userId, @Auth User user) {

		return Response.ok(contactService.getAllContacts(userId)).build();

	}

	@GET
	@Path("/{uid}/contact/")
	@AccessCheck
	@RolesAllowed({ "USER" })
	public Response getContactByName(@PathParam("uid") Integer userId,
			@QueryParam("firstname") Optional<String> firstname, @QueryParam("lastname") Optional<String> lastname,
			@Auth User user) {
		
		if(!firstname.isPresent() && !lastname.isPresent()) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		List<Contact> resultContacts = contactService.getContactByName(userId, firstname, lastname);

		if (!resultContacts.isEmpty()) {
			return Response.ok(resultContacts).build();
		} else
			return Response.status(Status.NOT_FOUND).build();

	}

	@POST
	@Path("/{uid}/contact/")
	@AccessCheck
	@RolesAllowed({ "USER" })
	public Response createContact(@PathParam("uid") Integer userId,@Valid Contact contact, @Auth User user)
			throws URISyntaxException {

		if (contactService.insertContact(userId, contact.getId(), contact)) {

			return Response.created(new URI(contact.getId()+"/contact/")).build();
		}

		else
			return Response.status(Status.BAD_REQUEST).build();

	}

	@PUT
	@Path("/{uid}/contact/")
	@AccessCheck
	@RolesAllowed({ "USER" })
	public Response updateContactById(@PathParam("uid") Integer userId,@Valid Contact contact, @Auth User user) {
		
		if (contactService.updateContact(userId, contact.getId(), contact)) {

			return Response.ok().build();

		} else

			return Response.status(Status.NOT_FOUND).build();
	}

	@DELETE
	@Path("/{uid}/contact/")
	@AccessCheck
	@RolesAllowed({ "USER" })
	public Response removeContactById(@PathParam("uid") Integer userId, @QueryParam("contactId") Integer contactId,
			@Auth User user) {

		if (contactService.removeContact(userId, contactId)) {
			return Response.ok().build();
		}

		else
			return Response.status(Status.NOT_FOUND).build();
	}
	
}