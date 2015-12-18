package com.worksap.stm_s173.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.worksap.stm_s173.dao.spec.EmployeeDao;
import com.worksap.stm_s173.dto.EmployeeDto;
import com.worksap.stm_s173.dto.OwnIntPairDto;


public class EmployeeDaoImpl implements EmployeeDao {
	
	private static final String FETCH_BY_ID ="SELECT * FROM user_account WHERE id = ?";
	private static final String FETCH_BY_NAME ="SELECT * FROM user_account WHERE username  = ?";
	private static final String INSERT_USER = "INSERT INTO user_account "
			+ " (firstname, lastname, email, role, officename, username, password, enabled)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ? ,1)";
	private static final String DELETE_USER = "UPDATE user_account SET enabled=0 WHERE id=?";
	private static final String USERACCOUNT_TABLE_SIZE = "SELECT COUNT(*) FROM user_account WHERE enabled=1";
	private static final String USERACCOUNT_TABLE_SIZE_ID = "SELECT COUNT(*) FROM user_account WHERE enabled=1 AND officename=?";
	private static final String FETCH = "SELECT * FROM user_account WHERE enabled=1 LIMIT ? OFFSET ?";
	private static final String FETCH_BY_OFFICE = "SELECT * FROM user_account WHERE enabled=1 AND officename=? LIMIT ? OFFSET ?";
	private static final String FETCH_REP_BY_ID = "SELECT * FROM user_account WHERE enabled=1 AND officename=? AND role=?";
	private static final String UPDATE_CLIENTS_TO_MEET = "UPDATE user_account SET clients_to_meet=? WHERE id=?";
	
	private int i=0;
	private JdbcTemplate jdbcTemplate;	
	private DataSource dataSource;
	public void setDataSource(DataSource dataSource) {
	       this.dataSource = dataSource;
	   }

	public EmployeeDto getEmployee(int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			return jdbcTemplate.queryForObject(
					FETCH_BY_ID, (rs, rownum) -> {
						return new EmployeeDto(rs.getInt("id"),
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("email"),
								rs.getString("role"),
								rs.getInt("officename"),
								rs.getString("username"),
								rs.getString("password"),
								rs.getInt("enabled"),
								rs.getInt("clients_to_meet"),
								rs.getInt("converted_clients"));
						}, id);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	

	@Override
	public EmployeeDto getEmployee(String name) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			return jdbcTemplate.queryForObject(
					FETCH_BY_NAME, (rs, rownum) -> {
						return new EmployeeDto(rs.getInt("id"),
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("email"),
								rs.getString("role"),
								rs.getInt("officename"),
								rs.getString("username"),
								rs.getString("password"),
								rs.getInt("enabled"),
								rs.getInt("clients_to_meet"),
								rs.getInt("converted_clients"));
						}, name);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	public void addEmployee(EmployeeDto employee) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
		jdbcTemplate.update(
				INSERT_USER, (ps) -> {
					ps.setString(1, employee.getFirstname());
					ps.setString(2, employee.getLastname());
					ps.setString(3, employee.getEmail());
					ps.setString(4, employee.getRole());
					ps.setInt(5, employee.getOfficename());
					ps.setString(6, employee.getUsername());
					ps.setString(7, employee.getPassword());
				});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	public void updateEmployee(EmployeeDto employee) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE user_account SET "
				+ " firstname = ?, lastname = ?, email = ?, role = ?, officename = ?,  username = ?, password = ?, enabled = ?, clients_to_meet = ?, converted_clients = ? WHERE id = ?";
		jdbcTemplate.update(
				sql, (ps) -> {
					ps.setString(1, employee.getFirstname());
					ps.setString(2, employee.getLastname());
					ps.setString(3, employee.getEmail());
					ps.setString(4, employee.getRole());
					ps.setInt(5, employee.getOfficename());
					ps.setString(6, employee.getUsername());
					ps.setString(7, employee.getPassword());
					ps.setInt(8, employee.getEnabled());
					ps.setInt(9, employee.getClients_to_meet());
					ps.setInt(10, employee.getConverted_clients());
					ps.setInt(11, employee.getId());
				});
	}

	public void deleteEmployee(int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			jdbcTemplate.update(DELETE_USER, (ps) -> {
				ps.setInt(1, id);
			});
		}
		catch (DataAccessException e) {
			throw new IOException(e);
		}
	}
	
	//Get all representatives for an office
	@Override
	public List<EmployeeDto> getRep(int id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(
				FETCH_REP_BY_ID,
				ps -> {
					ps.setInt(1, id);
					ps.setString(2, "Representative");
					
				},
				(rs, rownum) -> {
					return new EmployeeDto(rs.getInt("id"),
							rs.getString("firstname"),
							rs.getString("lastname"),
							rs.getString("email"),
							rs.getString("role"),
							rs.getInt("officename"),
							rs.getString("username"),
							rs.getString("password"),
							rs.getInt("enabled"),
							rs.getInt("clients_to_meet"),
							rs.getInt("converted_clients"));
					
				});
	}


	@Override
	public List<EmployeeDto> getBy(int id, int start, int size)
			throws IOException {
		System.out.println("officeId: "+id);
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			if(id <=0 ) {
				return jdbcTemplate.query(
						FETCH,
						ps -> {
							ps.setInt(1, size);
							ps.setInt(2, start);
						},
						(rs, rownum) -> {
							return new EmployeeDto(rs.getInt("id"),
									rs.getString("firstname"),
									rs.getString("lastname"),
									rs.getString("email"),
									rs.getString("role"),
									rs.getInt("officename"),
									rs.getString("username"),
									rs.getString("password"),
									rs.getInt("enabled"),
									rs.getInt("clients_to_meet"),
									rs.getInt("converted_clients"));
							
						});
			}
			else {
				return jdbcTemplate.query(
						FETCH_BY_OFFICE,
						ps -> {
							ps.setInt(1, id);
							ps.setInt(2, size);
							ps.setInt(3, start);
						},
						(rs, rownum) -> {
							return new EmployeeDto(rs.getInt("id"),
									rs.getString("firstname"),
									rs.getString("lastname"),
									rs.getString("email"),
									rs.getString("role"),
									rs.getInt("officename"),
									rs.getString("username"),
									rs.getString("password"),
									rs.getInt("enabled"),
									rs.getInt("clients_to_meet"),
									rs.getInt("converted_clients"));
						});
			}
			
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public int getTotalCount(int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		if(id<=0){
		return jdbcTemplate.queryForObject(USERACCOUNT_TABLE_SIZE, (rs, rownum) -> {
			return rs.getInt(1);
		});
		}
		else{
			return jdbcTemplate.queryForObject(USERACCOUNT_TABLE_SIZE_ID, (rs, rownum) -> {
				return rs.getInt(1);
			}, id);
		}
	}

	@Override
	public void updateEmployeeList(List<EmployeeDto> employee)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		for(i=0;i<employee.size();i++){
		jdbcTemplate.update(UPDATE_CLIENTS_TO_MEET, (ps) -> {
			ps.setInt(1,employee.get(i).getClients_to_meet());
			ps.setInt(2,employee.get(i).getId());
		});
		}
	}
	
	@Override
	public EmployeeDto getManager(int officeId)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM user_account WHERE enabled=1 and officename=? and role='Manager'";
		return jdbcTemplate.queryForObject(
				sql, (rs, rownum) -> {
					return new EmployeeDto(rs.getInt("id"),
							rs.getString("firstname"),
							rs.getString("lastname"),
							rs.getString("email"),
							rs.getString("role"),
							rs.getInt("officename"),
							rs.getString("username"),
							rs.getString("password"),
							rs.getInt("enabled"),
							rs.getInt("clients_to_meet"),
							rs.getInt("converted_clients"));
					}, officeId);
	}

	
	@Override
	public void updateConvertedClientsCount(int id)
			throws IOException {

		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(distinct order_id) FROM product_sales_detail where emp_id=? and enabled=1;";
		int a =  jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt(1);
		},id);
		
		sql = "UPDATE user_account SET converted_clients=? where id=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, a);
			ps.setInt(2, id);
		});
	}

	
	@Override
	public void updateClientsToMeetCount(int id)
			throws IOException {

		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(*) FROM map_clients where employeeid=? and status='ASSIGNED' and enabled=1";
		int a = jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt(1);
		},id);
		
		sql = "UPDATE user_account SET clients_to_meet=? where id=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, a);
			ps.setInt(2, id);
		});
	}

	@Override
	public int getTotalCount(int id, String string)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(*) FROM user_account where officename=? and role=? and enabled=1";		
		return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt(1);
		},id, string);
	}

	@Override
	public List<EmployeeDto> getUnderEmployees(String role, int id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		List<EmployeeDto> accounts = new ArrayList<EmployeeDto>();
		if(new String("Director").equals(role)){
			sql = "SELECT * FROM user_account where enabled=1 and role in ('Manager')";
			return jdbcTemplate.query(
					sql, ps -> {},
					(rs, rownum) -> {
						return new EmployeeDto(rs.getInt("id"),
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("email"),
								rs.getString("role"),
								rs.getInt("officename"),
								rs.getString("username"),
								rs.getString("password"),
								rs.getInt("enabled"),
								rs.getInt("clients_to_meet"),
								rs.getInt("converted_clients"));
						
					});
		}
		else if(new String("Manager").equals(role)){
			sql = "SELECT * FROM user_account where enabled=1 and role in ('Representative') and officename=?";
			return jdbcTemplate.query(
					sql, ps -> {
						ps.setInt(1,id);
					},
					(rs, rownum) -> {
						return new EmployeeDto(rs.getInt("id"),
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("email"),
								rs.getString("role"),
								rs.getInt("officename"),
								rs.getString("username"),
								rs.getString("password"),
								rs.getInt("enabled"),
								rs.getInt("clients_to_meet"),
								rs.getInt("converted_clients"));
						
					});
		}
		return accounts;
		
	}

}
