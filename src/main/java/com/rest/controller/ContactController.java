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

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class ContactController {
	
	Injector injector = Guice.createInjector();
    final ContactDao contactDao = injector.getInstance(ContactDao.class);

	@GET
	@Path("/{uid}/contacts/")
	@AccessCheck
	@RolesAllowed({ "USER" })
	public Response getAllContacts(@PathParam("uid") Integer userId, @Auth User user) {

		return Response.ok(contactDao.getAllContacts(userId)).build();

	}

	@GET
	@Path("/{uid}/contact/")
	@AccessCheck
	@RolesAllowed({ "USER" })
	public Response getContactByName(@PathParam("uid") Integer userId,
			@QueryParam("firstname") Optional<String> firstname, @QueryParam("lastname") Optional<String> lastname,
			@Auth User user) {
		List<Contact> resultContacts = Collections.<Contact>emptyList();

		if (firstname.isPresent() && lastname.isPresent()) {

			resultContacts = contactDao.getContactByName(userId, firstname.get(), lastname.get());

		} else if (firstname.isPresent() && !lastname.isPresent()) {

			resultContacts = contactDao.getContactByFirstName(userId, firstname.get());

		} else if (!firstname.isPresent() && lastname.isPresent()) {

			resultContacts= contactDao.getContactByLastName(userId, lastname.get());

		}

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
		
		Contact contactRecord = contactDao.getContactById(userId, contact.getId());

		if (contactRecord == null && contactDao.insertContact(userId, contact.getId(), contact)) {

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
		
		Contact contactRecord = contactDao.getContactById(userId, contact.getId());

		if (contactRecord != null && contactDao.updateContact(userId, contact.getId(), contact)) {

			return Response.ok(contact).build();

		} else

			return Response.status(Status.NOT_FOUND).build();
	}

	@DELETE
	@Path("/{uid}/contact/")
	@AccessCheck
	@RolesAllowed({ "USER" })
	public Response removeContactById(@PathParam("uid") Integer userId, @QueryParam("contactId") Integer contactId,
			@Auth User user) {

		Contact contact = contactDao.getContactById(userId, contactId);

		if (contact != null) {
			contactDao.removeContact(userId, contactId);
			return Response.ok().build();
		}

		else
			return Response.status(Status.NOT_FOUND).build();
	}
	
}