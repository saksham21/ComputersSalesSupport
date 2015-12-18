package com.worksap.stm_s173.dao.impl;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.worksap.stm_s173.dao.spec.OfficeDao;
import com.worksap.stm_s173.dto.OfficeDto;

public class OfficeDaoImpl implements OfficeDao {
	
	private static final String INSERT_OFFICE = "INSERT INTO office (name, enabled) VALUES(?, 1)";
	private static final String OFFICE_TABLE_SIZE = "SELECT COUNT(*) FROM office WHERE enabled=1";
	private static final String FETCH_BY_ID = "SELECT * FROM office WHERE id = ?";
	private static final String FETCH_OFFICES = "SELECT * FROM office WHERE enabled=1 LIMIT ? OFFSET ? ";
	private static final String DELETE_OFFICE = "UPDATE office SET enabled=0 WHERE id=?";
	
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	public void setDataSource(DataSource dataSource) {
	       this.dataSource = dataSource;
	   }


	@Override
	public void insert(String name) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		/*System.out.println(name);*/
		try {
			jdbcTemplate.update(INSERT_OFFICE , ps -> {
				ps.setString(1, name);
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
		
	}

	
	// Implement later
	
	@Override
	public void deleteBy(int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			jdbcTemplate.update(DELETE_OFFICE , ps -> {
				ps.setInt(1,id);
			});
		}
		catch (DataAccessException e) {
			throw new IOException(e);
			}
	}
/*		try {
			int numOfficeEmployees = template.queryForObject(
					CHECK_OFFICE_EMPLOYEES, (rs, rowNum) -> {
						return rs.getInt(1);
					}, id);
			if (numOfficeEmployees > 0) {
				return false;
			}
			int numDelete = jdbcTemplate.update(DELETE_OFFICE,
					ps -> ps.setInt(1, id));
			if (numDelete == 0) {
				return false;
			}
			return true;
		} catch (DataAccessException e) {
			throw new IOException(e);
		}*/

	@Override
	public int getTotalCount() throws IOException {
		/*System.out.println("gchc");*/
		jdbcTemplate = new JdbcTemplate(dataSource);
		/*System.out.println("gcdsfsdfhc");*/
		return jdbcTemplate.queryForObject(OFFICE_TABLE_SIZE, (rs, rownum) -> {
			return rs.getInt(1);
		});
	}

	@Override
	public OfficeDto getBy(int officeId) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			return jdbcTemplate.queryForObject(FETCH_BY_ID, (rs, rownum) -> {
				return new OfficeDto(rs.getInt("id"), rs.getString("name"), rs.getInt("enabled"));
			}, officeId);
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<OfficeDto> getAll(int start, int size) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			/*System.out.println("sam: " + start + " " + size);*/
			return jdbcTemplate.query(FETCH_OFFICES, ps -> {
				ps.setInt(1, size);
				ps.setInt(2, start);
			}, (rs, rownum) -> {
				return new OfficeDto(rs.getInt("id"), rs.getString("name"), rs.getInt("enabled"));
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}


	@Override
	public List<OfficeDto> getAll() throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM office where enabled=1";
		return jdbcTemplate.query(sql, ps -> {
		}, (rs, rownum) -> {
			return new OfficeDto(rs.getInt("id"), rs.getString("name"), rs.getInt("enabled"));
		});
	}

}
