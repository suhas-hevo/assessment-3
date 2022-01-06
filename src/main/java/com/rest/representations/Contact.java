package com.rest.representations;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact {

	@NotNull
	private Integer contactId;
	@Length(min = 2, max = 255)
	private String firstName;
	@Length(min = 2, max = 255)
	private String lastName;
	@Pattern(regexp = ".+@.+\\.[a-z]+")
	private String email;

	public Contact() {
	}

	public Contact(Integer contactId, String firstName, String lastName, String email) {
		this.contactId = contactId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	@JsonProperty("id")
	public Integer getId() {
		return contactId;
	}

	@JsonProperty("id")
	public void setId(Integer contactId) {
		this.contactId = contactId;
	}

	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Contact [id=" + contactId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ "]";
	}
}