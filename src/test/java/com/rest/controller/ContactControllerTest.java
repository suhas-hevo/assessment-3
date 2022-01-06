package com.rest.controller;

import io.dropwizard.auth.*;
import io.dropwizard.auth.basic.*;
import io.dropwizard.testing.junit5.*;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import com.rest.auth.UserAuthenticator;
import com.rest.auth.UserAuthorizer;
import com.rest.dao.contact.ContactDao;
import com.rest.filter.AccessCheckFeature;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import com.rest.representations.*;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import com.rest.service.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class ContactControllerTest {
	
	private static ContactService contactService = mock(ContactService.class);
	private Contact contact;
	static ContactController contactController = new ContactController(contactService);

	private static BasicCredentialAuthFilter<User> BASIC_AUTH_HANDLER = new BasicCredentialAuthFilter.Builder<User>()
			.setAuthenticator(new UserAuthenticator())
			.setAuthorizer(new UserAuthorizer()).setRealm("BASIC-TEST-REALM")
			.setPrefix("Basic").buildAuthFilter();

	public static ResourceExtension resources = ResourceExtension.builder()
			.addProvider(RolesAllowedDynamicFeature.class)
			.addProvider(new AuthDynamicFeature(BASIC_AUTH_HANDLER))
			.addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
			.addProvider(AccessCheckFeature.class)
			.setTestContainerFactory(new GrizzlyWebTestContainerFactory())
			.addProvider(contactController).build();

	@BeforeEach
	public void setUp() {
		contact = new Contact();
		contact.setId(11);
		contact.setFirstName("hello");
		contact.setLastName("test");
		contact.setEmail("test@new.com");

	}

	@AfterEach
	void tearDown() {
		reset(contactService);
	}

	@Test
	public void testGetAllContacts() {
		final List<Contact> contactList = Collections.singletonList(contact);
		when(contactService.getAllContacts(1)).thenReturn(contactList);
		List<Contact> result = resources.target("/user/1/contacts/").request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.get(new GenericType<List<Contact>>() {
				});

		Assert.assertNotNull(result);
	}

	@Test
	public void testGetContactByName() {
		Optional<String> firstname = Optional.of(contact.getFirstName());
		Optional<String> lastname = Optional.of(contact.getLastName());
		final List<Contact> contactList = Collections.singletonList(contact);

		when(contactService.getContactByName(1, firstname, lastname)).thenReturn(contactList);
		Response resultName = resources.target("/user/1/contact/")
				.queryParam("firstname", contact.getFirstName())
				.queryParam("lastname", contact.getLastName()).request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.get();

		verify(contactService, times(1)).getContactByName(1, firstname, lastname);
		Assert.assertEquals(200, resultName.getStatus());

	}
	
	@Test
	public void testGetContactByNameNoParams() {
		Optional<String> firstname = Optional.of(contact.getFirstName());
		Optional<String> lastname = Optional.of(contact.getLastName());
		final List<Contact> contactList = Collections.singletonList(contact);

		when(contactService.getContactByName(1, firstname, lastname)).thenReturn(contactList);
		
		Response resultName = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.get();
		
		Assert.assertEquals(400, resultName.getStatus());

	}
	
	@Test
	public void testGetContactByNameNoMatchingRecords() {
		Optional<String> firstname = Optional.of(contact.getFirstName());
		Optional<String> lastname = Optional.of(contact.getLastName());
		final List<Contact> contactList = Collections.emptyList();

		when(contactService.getContactByName(1, firstname, lastname)).thenReturn(contactList);
		
		Response resultName = resources.target("/user/1/contact/")
				.queryParam("firstname", contact.getFirstName())
				.queryParam("lastname", contact.getLastName())
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.get();

		verify(contactService, times(1)).getContactByName(1, firstname, lastname);
		
		Assert.assertEquals(404, resultName.getStatus());

	}
	
	@Test
	public void testGetContactFirstByName() {
		Optional<String> firstname = Optional.of(contact.getFirstName());
		Optional<String> lastname = Optional.empty();
		final List<Contact> contactList = Collections.singletonList(contact);

		when(contactService.getContactByName(1, firstname, lastname)).thenReturn(contactList);
		Response resultName = resources.target("/user/1/contact/")
				.queryParam("firstname", contact.getFirstName())
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.get();

		verify(contactService, times(1)).getContactByName(1, firstname, lastname);
		Assert.assertEquals(200, resultName.getStatus());

	}
	
	@Test
	public void testGetContactLastByName() {
		Optional<String> firstname = Optional.empty();
		Optional<String> lastname = Optional.of(contact.getLastName());
		final List<Contact> contactList = Collections.singletonList(contact);

		when(contactService.getContactByName(1, firstname, lastname)).thenReturn(contactList);
		Response resultName = resources.target("/user/1/contact/")
				.queryParam("lastname", contact.getLastName())
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.get();

		verify(contactService, times(1)).getContactByName(1, firstname, lastname);
		Assert.assertEquals(200, resultName.getStatus());

	}

	@Test
	public void testCreateContact() {
		when(contactService.insertContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response createResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.post(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));
		
		//verify(contactService).insertContact(1, contact.getId() ,contact);
		verify(contactService).insertContact(anyInt(), anyInt() ,any(Contact.class));
		Assert.assertEquals(201, createResponse.getStatus());
	}
	
	
	@Test
	public void testCreateContactWithExistingId() {
		when(contactService.insertContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(false);
		Response createResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.post(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));
		
		verify(contactService).insertContact(anyInt(), anyInt() ,any(Contact.class));
		Assert.assertEquals(400, createResponse.getStatus());
	}
	
	@Test
	public void testCreateContactNullId() {
		contact.setId(null);
		when(contactService.insertContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response createResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.post(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));
		
		verify(contactService,never()).insertContact(anyInt(), anyInt() ,any(Contact.class));
		Assert.assertEquals(422, createResponse.getStatus());
	}

	@Test
	public void testCreateContactInvalidFirstName() {
		contact.setFirstName("l");
		when(contactService.insertContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response createResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.post(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));
		
		verify(contactService,never()).insertContact(anyInt(), anyInt() ,any(Contact.class));
		Assert.assertEquals(422, createResponse.getStatus());
	}
	
	@Test
	public void testCreateContactInvalidLastName() {
		contact.setLastName("l");
		when(contactService.insertContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response createResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.post(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));
		
		verify(contactService,never()).insertContact(anyInt(), anyInt() ,any(Contact.class));
		Assert.assertEquals(422, createResponse.getStatus());
	}
	
	@Test
	public void testCreateContactInvalidEmail() {
		contact.setEmail("email.com");
		when(contactService.insertContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response createResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.post(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));
		
		verify(contactService,never()).insertContact(anyInt(), anyInt() ,any(Contact.class));
		Assert.assertEquals(422, createResponse.getStatus());
	}

	@Test
	public void testUpdateContact() {
		when(contactService.updateContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response updateResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.put(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));

		verify(contactService).updateContact(anyInt(), anyInt() ,any(Contact.class));
		
		Assert.assertEquals(200, updateResponse.getStatus());
	}
	
	@Test
	public void testUpdateNonExistentContact() {
		when(contactService.updateContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(false);
		Response updateResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.put(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));

		verify(contactService).updateContact(anyInt(), anyInt() ,any(Contact.class));
		
		Assert.assertEquals(404, updateResponse.getStatus());
	}
	
	@Test
	public void testUpdateContactNullId() {
		contact.setId(null);
		when(contactService.updateContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response updateResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.put(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));

		verify(contactService,never()).updateContact(anyInt(), anyInt() ,any(Contact.class));
		
		Assert.assertEquals(422, updateResponse.getStatus());
	}
	
	@Test
	public void testUpdateContactInvalidFirstName() {
		contact.setFirstName("s");
		when(contactService.updateContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response updateResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.put(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));

		verify(contactService,never()).updateContact(anyInt(), anyInt() ,any(Contact.class));
		
		Assert.assertEquals(422, updateResponse.getStatus());
	}
	
	@Test
	public void testUpdateContactInvalidLastName() {
		contact.setLastName("s");
		when(contactService.updateContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response updateResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.put(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));

		verify(contactService,never()).updateContact(anyInt(), anyInt() ,any(Contact.class));
		
		Assert.assertEquals(422, updateResponse.getStatus());
	}
	
	@Test
	public void testUpdateContactInvalidEmail() {
		contact.setEmail("s@e");
		when(contactService.updateContact(anyInt(), anyInt() ,any(Contact.class))).thenReturn(true);
		Response updateResponse = resources.target("/user/1/contact/")
				.request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.put(Entity.entity(contact, MediaType.APPLICATION_JSON_TYPE));

		verify(contactService,never()).updateContact(anyInt(), anyInt() ,any(Contact.class));
		
		Assert.assertEquals(422, updateResponse.getStatus());
	}
	
	@Test
	public void testDeleteContact() {

		when(contactService.removeContact(1, contact.getId())).thenReturn(true);
		Response resultDelete = resources.target("/user/1/contact/")
				.queryParam("contactId", contact.getId()).request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.delete();

		Assert.assertEquals(200, resultDelete.getStatus());
	}
	
	

	@Test
	public void testDeleteNonExistentContact() {

		when(contactService.removeContact(1, contact.getId())).thenReturn(false);
		Response resultDelete = resources.target("/user/1/contact/")
				.queryParam("contactId", contact.getId()).request()
				.header(HttpHeaders.AUTHORIZATION, "Basic c3VoYXMxOnN1aGFzMTIz")
				.delete();

		Assert.assertEquals(404, resultDelete.getStatus());
	}

}