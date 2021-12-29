package com.rest.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.rest.representations.Contact;

public class ContactMapper implements ResultSetMapper<Contact> {

	@Override
	public Contact map(int idx, ResultSet rs, StatementContext ctx) throws SQLException {
		return new Contact(rs.getInt("cid"), rs.getString("firstName"), rs.getString("lastName"),
				rs.getString("email"));
	}
}
