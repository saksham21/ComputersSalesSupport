package com.worksap.stm_s173.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.worksap.stm_s173.dto.EmployeeDto;

@SuppressWarnings("rawtypes")
public class EmployeeRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		EmployeeDto employee = new EmployeeDto();
		employee.setFirstname(rs.getString("FirstName"));
		employee.setLastname(rs.getString("LastName"));
		employee.setRole(rs.getString("Role"));
		employee.setId(rs.getInt("CompanyId"));
		employee.setEnabled(rs.getInt("Enabled"));
		employee.setPassword(rs.getString("PassWord"));
		return employee;
	}

}
