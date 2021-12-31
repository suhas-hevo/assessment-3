package com.rest.dao.contact;

import java.util.List;
import org.skife.jdbi.v2.Handle;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rest.dao.DbConnectionHandle;
import com.rest.dao.mappers.ContactMapper;
import com.rest.modules.DbConnectionModule;
import com.rest.representations.Contact;

public class ContactDao {

	Injector injector = Guice.createInjector(new DbConnectionModule());
	DbConnectionHandle dbConenctionHandle = injector.getInstance(DbConnectionHandle.class);
	Handle handle = dbConenctionHandle.getHandle();

	public List<Contact> getAllContacts(Integer userId) {

		String getAllContactsQuery = "SELECT * FROM contacts WHERE uid= :uid";

		List<Contact> allContactRecords = handle.createQuery(getAllContactsQuery).map(new ContactMapper())
				.bind("uid", userId).list();

		return allContactRecords;

	}

	public Contact getContactById(Integer userId, Integer contactId) {

		String getContactByIdQuery = "SELECT * FROM contacts WHERE uid= :uid and cid = :cid";

		Contact contactRecord = handle.createQuery(getContactByIdQuery).map(new ContactMapper()).bind("uid", userId)
				.bind("cid", contactId).first();
		return contactRecord;
	}

	public List<Contact> getContactByFirstName(Integer userId, String firstName) {

		String getContactByFisrtNameQuery = "SELECT * FROM contacts WHERE uid= :uid and firstName= :firstName";

		List<Contact> contactsWithGivenFirstName = handle.createQuery(getContactByFisrtNameQuery)
				.map(new ContactMapper()).bind("uid", userId).bind("firstName", firstName).list();

		return contactsWithGivenFirstName;
	}

	public List<Contact> getContactByLastName(Integer userId, String lastName) {

		String getContactByLastNameQuery = "SELECT * FROM contacts WHERE uid= :uid and lastName= :lastName";

		List<Contact> contactsWithGivenLastName = handle.createQuery(getContactByLastNameQuery).map(new ContactMapper())
				.bind("uid", userId).bind("lastName", lastName).list();

		return contactsWithGivenLastName;
	}

	public List<Contact> getContactByName(Integer userId, String firstName, String lastName) {

		String getContactByNameQuery = "SELECT * FROM contacts WHERE uid= :uid and firstName= :firstName AND lastName = :lastName";

		List<Contact> contactsWithGivenName = handle.createQuery(getContactByNameQuery).map(new ContactMapper())
				.bind("uid", userId).bind("firstName", firstName).bind("lastName", lastName).list();

		return contactsWithGivenName;
	}

	public boolean updateContact(Integer userId, Integer contactId, Contact contact) {
		String updateContactQuery = "UPDATE contacts SET firstName= :firstname, lastName = :lastname, email = :email where uid = :uid AND cid = :cid";

		return handle.createStatement(updateContactQuery).bind("uid", userId).bind("cid", contactId)
				.bind("firstname", contact.getFirstName()).bind("lastname", contact.getLastName())
				.bind("email", contact.getEmail()).execute() == 1;
	}

	public boolean insertContact(Integer userId, Integer contactId, Contact contact) {
		String insertContactQuery = "INSERT INTO contacts (uid,cid,firstName,lastName,email) VALUES (:uid,:cid,:firstname,:lastname,:email)";

		return handle.createStatement(insertContactQuery).bind("uid", userId).bind("cid", contactId)
				.bind("firstname", contact.getFirstName()).bind("lastname", contact.getLastName())
				.bind("email", contact.getEmail()).execute() == 1;

	}

	public void removeContact(Integer userId, Integer cid) {

		String removeContactQuery = "DELETE FROM contacts WHERE uid=? AND cid =?";

		handle.execute(removeContactQuery, userId, cid);

	}
}