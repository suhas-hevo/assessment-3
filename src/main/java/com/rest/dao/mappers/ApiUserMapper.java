package com.rest.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.rest.representations.ApiUser;

public class ApiUserMapper implements ResultSetMapper<ApiUser> {

	@Override
	public ApiUser map(int idx, ResultSet rs, StatementContext ctx) throws SQLException {
		return new ApiUser(rs.getInt("uid"), rs.getString("username"), rs.getString("password"), rs.getInt("level"));
	}
}
