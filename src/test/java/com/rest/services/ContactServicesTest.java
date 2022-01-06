package com.rest.services;

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
import com.rest.dao.contact.ContactDao;
import java.util.Optional;
import static org.mockito.Mockito.*;
import org.mockito.InOrder;
import org.mockito.Mock;
import com.rest.representations.*;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import com.rest.service.*;

public class ContactServicesTest{

	private static ContactDao contactDao = mock(ContactDao.class);
	ContactService contactService = new ContactService(contactDao);
	private Contact contact;
	
	@BeforeEach
	void setUp() {
		contact = new Contact();
		contact.setId(11);
		contact.setFirstName("hello");
		contact.setLastName("test");
		contact.setEmail("test@new.com");

	}

	@AfterEach
	void tearDown() {
		reset(contactDao);
	}
	
	@Test
	public void testGetContactByName() {
		Optional<String> firstname = Optional.of(contact.getFirstName());
		Optional<String> lastname = Optional.of(contact.getLastName());
		final List<Contact> contactList = Collections.singletonList(contact);

		when(contactDao.getContactByName(1, firstname.get(), lastname.get())).thenReturn(contactList);
		List<Contact> contacts = contactService.getContactByName(1, firstname, lastname);

		verify(contactDao).getContactByName(1, firstname.get(), lastname.get());
		verify(contactDao, never()).getContactByFirstName(1, firstname.get());
		verify(contactDao, never()).getContactByLastName(1, lastname.get());
		Assert.assertNotNull(contacts);

	}
	
	@Test
	public void testGetContactByFirstName() {
		Optional<String> firstname = Optional.of(contact.getFirstName());
		Optional<String> lastname = Optional.empty();
		final List<Contact> contactList = Collections.singletonList(contact);

		when(contactDao.getContactByName(1, contact.getFirstName(), contact.getLastName())).thenReturn(contactList);
		when(contactDao.getContactByFirstName(1, contact.getFirstName())).thenReturn(contactList);
		when(contactDao.getContactByLastName(1, contact.getLastName())).thenReturn(contactList);
		
		List<Contact> contacts = contactService.getContactByName(1, firstname, lastname);

		
		verify(contactDao).getContactByFirstName(1,firstname.get());
		verify(contactDao,never()).getContactByName(1, contact.getFirstName(), contact.getLastName());
		verify(contactDao, never()).getContactByLastName(1, contact.getLastName());
		
		Assert.assertNotNull(contacts);

	}
	
	@Test
	public void testGetContactByLastName() {
		Optional<String> firstname = Optional.empty();
		Optional<String> lastname = Optional.of(contact.getLastName());
		final List<Contact> contactList = Collections.singletonList(contact);

		when(contactDao.getContactByName(1, contact.getFirstName(), contact.getLastName())).thenReturn(contactList);
		when(contactDao.getContactByFirstName(1, contact.getFirstName())).thenReturn(contactList);
		when(contactDao.getContactByLastName(1, contact.getLastName())).thenReturn(contactList);
		
		List<Contact> contacts = contactService.getContactByName(1, firstname, lastname);
		
		verify(contactDao).getContactByLastName(1, contact.getLastName());
		verify(contactDao,never()).getContactByName(1, contact.getFirstName(), contact.getLastName());
		verify(contactDao,never()).getContactByFirstName(1, contact.getFirstName());
		
		Assert.assertNotNull(contacts);

	}
	
	@Test
	public void testCreateContact() {
		when(contactDao.getContactById(1, contact.getId())).thenReturn(null);
		when(contactDao.insertContact(1, contact.getId(), contact)).thenReturn(true);
		
		boolean create = contactService.insertContact(1, contact.getId(), contact);
		
		InOrder inOrder = inOrder(contactDao);
		inOrder.verify(contactDao).getContactById(1, contact.getId());
		inOrder.verify(contactDao).insertContact(1, contact.getId(), contact);
		Assert.assertTrue(create);
	}
	
	@Test
	public void testCreateContactWithExistingId() {
		when(contactDao.getContactById(1, contact.getId())).thenReturn(contact);
		when(contactDao.insertContact(1, contact.getId(), contact)).thenReturn(true);
		
		boolean create = contactService.insertContact(1, contact.getId(), contact);
		
		verify(contactDao).getContactById(1, contact.getId());
		verify(contactDao,never()).insertContact(1, contact.getId(), contact);
		Assert.assertFalse(create);
	}
	
	@Test
	public void testCreateContactInsertFails() {
		when(contactDao.getContactById(1, contact.getId())).thenReturn(null);
		when(contactDao.insertContact(1, contact.getId(), contact)).thenReturn(false);
		
		boolean create = contactService.insertContact(1, contact.getId(), contact);
		
		verify(contactDao).getContactById(1, contact.getId());
		verify(contactDao).insertContact(1, contact.getId(), contact);
		Assert.assertFalse(create);
	}
	
	@Test
	public void testCreateContactWithExistingIdAndInsertFail() {
		when(contactDao.getContactById(1, contact.getId())).thenReturn(contact);
		when(contactDao.insertContact(1, contact.getId(), contact)).thenReturn(false);
		
		boolean create = contactService.insertContact(1, contact.getId(), contact);
		
		verify(contactDao).getContactById(1, contact.getId());
		verify(contactDao,never()).insertContact(1, contact.getId(), contact);
		Assert.assertFalse(create);
	}
	
	@Test
	public void testUpdateContact() {
		when(contactDao.getContactById(1, contact.getId())).thenReturn(contact);
		when(contactDao.updateContact(1, contact.getId(), contact)).thenReturn(true);
		
		boolean update = contactService.updateContact(1, contact.getId(), contact);

		InOrder inOrder = inOrder(contactDao);
		inOrder.verify(contactDao).getContactById(1, contact.getId());
		inOrder.verify(contactDao).updateContact(1, contact.getId(), contact);
		
		Assert.assertTrue(update);
	}
	
	@Test
	public void testUpdateNonExistentContact() {
		when(contactDao.getContactById(1, contact.getId())).thenReturn(null);
		when(contactDao.updateContact(1, contact.getId(), contact)).thenReturn(true);
		
		boolean update = contactService.updateContact(1, contact.getId(), contact);

		verify(contactDao).getContactById(1, contact.getId());
		verify(contactDao, never()).updateContact(1, contact.getId(), contact);
		
		Assert.assertFalse(update);
	}
	
	@Test
	public void testUpdateContactUpdateFails() {
		when(contactDao.getContactById(1, contact.getId())).thenReturn(contact);
		when(contactDao.updateContact(1, contact.getId(), contact)).thenReturn(false);
		
		boolean update = contactService.updateContact(1, contact.getId(), contact);

		verify(contactDao).getContactById(1, contact.getId());
		verify(contactDao).updateContact(1, contact.getId(), contact);
		
		Assert.assertFalse(update);
	}
	
	@Test
	public void testUpdateNonExistentContactAndUpdateFails() {
		when(contactDao.getContactById(1, contact.getId())).thenReturn(null);
		when(contactDao.updateContact(1, contact.getId(), contact)).thenReturn(false);
		
		boolean update = contactService.updateContact(1, contact.getId(), contact);

		verify(contactDao).getContactById(1, contact.getId());
		verify(contactDao, never()).updateContact(1, contact.getId(), contact);
		
		Assert.assertFalse(update);
	}
	
	@Test
	public void testDeleteContact() {

		when(contactDao.getContactById(1, contact.getId())).thenReturn(contact);
		doNothing().when(contactDao).removeContact(1, contact.getId());
		
		boolean delete = contactService.removeContact(1, contact.getId());

		Assert.assertTrue(delete);
	}

	@Test
	public void testDeleteNonExistentContact() {

		when(contactDao.getContactById(1, contact.getId())).thenReturn(null);
		doNothing().when(contactDao).removeContact(1, contact.getId());
		
		boolean delete = contactService.removeContact(1, contact.getId());

		verify(contactDao, never()).removeContact(1, contact.getId());
		Assert.assertFalse(delete);
	}
	
}