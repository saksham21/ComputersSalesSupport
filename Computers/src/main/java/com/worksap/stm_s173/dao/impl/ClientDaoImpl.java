package com.worksap.stm_s173.dao.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.worksap.stm_s173.dao.spec.ClientDao;
import com.worksap.stm_s173.dto.ClientDto;
import com.worksap.stm_s173.dto.ClientExistingDto;

public class ClientDaoImpl implements ClientDao {

	private static final String COUNT_CLIENT_BY_MAPID = "SELECT COUNT(*) FROM map_clients WHERE placeid=?";
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void addClient(ClientDto client, int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO map_clients "
				+ " (name, contact, address	, status, domain, employeeid, employeename, officeid, placeid, enabled, lat, lng)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			jdbcTemplate.update(sql, (ps) -> {
				ps.setString(1, client.getName());
				ps.setString(2, client.getContact());
				ps.setString(3, client.getAddress());
				ps.setString(4, client.getStatus());
				ps.setString(5, client.getDomain());
				ps.setInt(6, client.getEmployeeid());
				ps.setString(7, client.getEmployeename());
				ps.setInt(8, id);
				ps.setString(9, client.getPlaceid());
				ps.setInt(10, client.getEnabled());
				ps.setDouble(11, client.getLat());
				ps.setDouble(12, client.getLng());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	public int getCount(String map_id) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForObject(COUNT_CLIENT_BY_MAPID,
				(rs, rownum) -> {
					return rs.getInt(1);
				}, map_id);
	}

	@Override
	public int getAssignedCount(String status, int id, String role, int emp_id) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		if (new String("Director").equals(role)) {
			sql = "SELECT COUNT(*) FROM map_clients WHERE enabled=1 AND status=?";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, status);
		} else if (new String("Manager").equals(role)) {
			sql = "SELECT COUNT(*) FROM map_clients WHERE enabled=1 AND status=? AND officeid=?";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, status, id);
		} else {
			sql = "SELECT COUNT(*) FROM map_clients WHERE enabled=1 AND status=? AND employeeid=?";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, status, emp_id);
		}
	}

	@Override
	public int getAssignedCount1(String status, int id, String role, int emp_id) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		if (new String("Director").equals(role)) {
			sql = "SELECT COUNT(*) FROM map_clients WHERE enabled=1 AND status in ('ASSIGNED','INTERESTED','NOT_INTERESTED')";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			});
		} else if (new String("Manager").equals(role)) {
			sql = "SELECT COUNT(*) FROM map_clients WHERE enabled=1 AND status in ('ASSIGNED','INTERESTED','NOT_INTERESTED') AND officeid=?";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, id);
		} else {
			sql = "SELECT COUNT(*) FROM map_clients WHERE enabled=1 AND status in ('ASSIGNED','INTERESTED','NOT_INTERESTED') AND employeeid=?";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, emp_id);
		}
	}

	@Override
	public List<ClientDto> getBy(String status, int start, int length, int id,
			String role, int emp_id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		if (new String("Director").equals(role)) {
			sql = "SELECT * FROM map_clients WHERE enabled=1 AND status=?";
			return jdbcTemplate
					.query(sql,
							ps -> {
								ps.setString(1, status);
							},
							(rs, rownum) -> {
								return new ClientDto(rs.getInt("id"), rs
										.getString("name"), rs
										.getString("contact"), rs
										.getString("address"), rs
										.getString("status"), rs
										.getString("domain"), rs
										.getInt("employeeid"), rs
										.getString("employeename"), rs
										.getInt("officeid"), rs
										.getString("placeid"), rs
										.getInt("enabled"), rs
										.getString("description"), rs
										.getDouble("lat"), rs.getDouble("lng"));
							});

		} else if (new String("Manager").equals(role)) {
			sql = "SELECT * FROM map_clients WHERE enabled=1 AND status=? AND officeid=?";
			return jdbcTemplate
					.query(sql,
							ps -> {
								ps.setString(1, status);
								ps.setInt(2, id);
							},
							(rs, rownum) -> {
								return new ClientDto(rs.getInt("id"), rs
										.getString("name"), rs
										.getString("contact"), rs
										.getString("address"), rs
										.getString("status"), rs
										.getString("domain"), rs
										.getInt("employeeid"), rs
										.getString("employeename"), rs
										.getInt("officeid"), rs
										.getString("placeid"), rs
										.getInt("enabled"), rs
										.getString("description"), rs
										.getDouble("lat"), rs.getDouble("lng"));
							});

		} else {
			sql = "SELECT * FROM map_clients WHERE enabled=1 AND status=? AND employeeid=?";
			return jdbcTemplate
					.query(sql,
							ps -> {
								ps.setString(1, status);
								ps.setInt(2, emp_id);
							},
							(rs, rownum) -> {
								return new ClientDto(rs.getInt("id"), rs
										.getString("name"), rs
										.getString("contact"), rs
										.getString("address"), rs
										.getString("status"), rs
										.getString("domain"), rs
										.getInt("employeeid"), rs
										.getString("employeename"), rs
										.getInt("officeid"), rs
										.getString("placeid"), rs
										.getInt("enabled"), rs
										.getString("description"), rs
										.getDouble("lat"), rs.getDouble("lng"));
							});

		}

	}

	@Override
	public List<ClientDto> getBy1(String status, int start, int length, int id,
			String role, int emp_id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		if (new String("Director").equals(role)) {
			sql = "SELECT * FROM map_clients WHERE enabled=1 AND status in ('ASSIGNED','INTERESTED','NOT_INTERESTED')";
			return jdbcTemplate
					.query(sql,
							ps -> {
							},
							(rs, rownum) -> {
								return new ClientDto(rs.getInt("id"), rs
										.getString("name"), rs
										.getString("contact"), rs
										.getString("address"), rs
										.getString("status"), rs
										.getString("domain"), rs
										.getInt("employeeid"), rs
										.getString("employeename"), rs
										.getInt("officeid"), rs
										.getString("placeid"), rs
										.getInt("enabled"), rs
										.getString("description"), rs
										.getDouble("lat"), rs.getDouble("lng"));
							});

		} else if (new String("Manager").equals(role)) {
			sql = "SELECT * FROM map_clients WHERE enabled=1 AND status in ('ASSIGNED','INTERESTED','NOT_INTERESTED') AND officeid=?";
			return jdbcTemplate
					.query(sql,
							ps -> {
								ps.setInt(1, id);
							},
							(rs, rownum) -> {
								return new ClientDto(rs.getInt("id"), rs
										.getString("name"), rs
										.getString("contact"), rs
										.getString("address"), rs
										.getString("status"), rs
										.getString("domain"), rs
										.getInt("employeeid"), rs
										.getString("employeename"), rs
										.getInt("officeid"), rs
										.getString("placeid"), rs
										.getInt("enabled"), rs
										.getString("description"), rs
										.getDouble("lat"), rs.getDouble("lng"));
							});

		} else {
			sql = "SELECT * FROM map_clients WHERE enabled=1 AND status in ('ASSIGNED','INTERESTED','NOT_INTERESTED') AND employeeid=?";
			return jdbcTemplate
					.query(sql,
							ps -> {
								ps.setInt(1, emp_id);
							},
							(rs, rownum) -> {
								return new ClientDto(rs.getInt("id"), rs
										.getString("name"), rs
										.getString("contact"), rs
										.getString("address"), rs
										.getString("status"), rs
										.getString("domain"), rs
										.getInt("employeeid"), rs
										.getString("employeename"), rs
										.getInt("officeid"), rs
										.getString("placeid"), rs
										.getInt("enabled"), rs
										.getString("description"), rs
										.getDouble("lat"), rs.getDouble("lng"));
							});

		}

	}

	// GET client details by his id;
	@Override
	public ClientDto getBy(int client_id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM map_clients WHERE id=? and enabled=1";
		return jdbcTemplate.queryForObject(
				sql,
				(rs, rownum) -> {
					return new ClientDto(rs.getInt("id"), rs.getString("name"),
							rs.getString("contact"), rs.getString("address"),
							rs.getString("status"), rs.getString("domain"), rs
									.getInt("employeeid"), rs
									.getString("employeename"), rs
									.getInt("officeid"), rs
									.getString("placeid"),
							rs.getInt("enabled"), rs.getString("description"),
							rs.getDouble("lat"), rs.getDouble("lng"));
				}, client_id);
	}

	@Override
	public void updateToAssigned(int emp_id, ClientDto entity)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE map_clients SET "
				+ " status = ?, employeeid = ?, employeename = ? WHERE id = ?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setString(1, entity.getStatus());
			ps.setInt(2, emp_id);
			ps.setString(3, entity.getEmployeename());
			ps.setInt(4, entity.getId());
		});
	}

	@Override
	public void sendStatus(ClientDto entity) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE map_clients SET "
				+ " status = ? , description = ? WHERE id = ? ";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setString(1, entity.getStatus());
			ps.setString(2, entity.getDescription());
			ps.setInt(3, entity.getId());
		});
	}

	@Override
	public int getAssignedClientsCount(int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(*) FROM map_clients WHERE enabled=1 AND status in ('ASSIGNED' , 'INTERESTED') AND employeeid=?";
		return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt(1);
		}, id);
	}

	@Override
	public int getExistingClientCount(String status, int id,
			HttpServletRequest request, String name) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		if (request.isUserInRole("Manager")) {
			sql = "SELECT COUNT(*) FROM map_clients WHERE enabled=1 AND status=? AND officeid=?";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, status, id);
		} else if (request.isUserInRole("Representative")) {
			sql = "SELECT COUNT(*) FROM map_clients WHERE enabled=1 AND status=? AND officeid=? and employeename=?";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, status, id, name);
		} else {
			return 0;
		}
	}

	@Override
	public List<ClientExistingDto> getExistingClients(String status, int id,
			HttpServletRequest request, String name) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		if (request.isUserInRole("Manager")) {
			sql = "SELECT map_clients.id , map_clients.name, map_clients.contact, map_clients.address, map_clients.domain, "
					+ " MAX(product_sales_detail.timestamp), COUNT(distinct product_sales_detail.order_id) from product_sales_detail "
					+ " join map_clients on product_sales_detail.client_id = map_clients.id where map_clients.status=? and "
					+ " map_clients.enabled='1' and product_sales_detail.enabled='1' and map_clients.officeid=? group by map_clients.id";
			return jdbcTemplate
					.query(sql,
							ps -> {
								ps.setString(1, status);
								ps.setInt(2, id);
							},
							(rs, rownum) -> {
								return new ClientExistingDto(
										rs.getInt("id"),
										rs.getString("name"),
										rs.getString("contact"),
										rs.getString("address"),
										rs.getString("domain"),
										rs.getLong("MAX(product_sales_detail.timestamp)"),
										rs.getInt("COUNT(distinct product_sales_detail.order_id)"));
							});
		} else if (request.isUserInRole("Representative")) {
			sql = "SELECT map_clients.id , map_clients.name, map_clients.contact, map_clients.address, map_clients.domain, "
					+ " MAX(product_sales_detail.timestamp), COUNT(distinct product_sales_detail.order_id) from product_sales_detail "
					+ " join map_clients on product_sales_detail.client_id = map_clients.id where map_clients.status=? and map_clients.employeename=? "
					+ " and map_clients.enabled='1' and product_sales_detail.enabled='1' and map_clients.officeid=? group by map_clients.id";
			return jdbcTemplate
					.query(sql,
							ps -> {
								ps.setString(1, status);
								ps.setString(2, name);
								ps.setInt(3, id);
							},
							(rs, rownum) -> {
								return new ClientExistingDto(
										rs.getInt("id"),
										rs.getString("name"),
										rs.getString("contact"),
										rs.getString("address"),
										rs.getString("domain"),
										rs.getLong("MAX(product_sales_detail.timestamp)"),
										rs.getInt("COUNT(distinct product_sales_detail.order_id)"));
							});
		} else {
			return null;
		}

	}

	@Override
	public void updateStatusByClientID(int id, String string)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE map_clients SET status=? WHERE id=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setString(1, string);
			ps.setInt(2, id);
		});
	}

	@Override
	public List<ClientDto> getExistingClients1(String status, int id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM map_clients WHERE enabled=1 AND status=? AND officeid=?";
		return jdbcTemplate.query(
				sql,
				ps -> {
					ps.setString(1, status);
					ps.setInt(2, id);
				},
				(rs, rownum) -> {
					return new ClientDto(rs.getInt("id"), rs.getString("name"),
							rs.getString("contact"), rs.getString("address"),
							rs.getString("status"), rs.getString("domain"), rs
									.getInt("employeeid"), rs
									.getString("employeename"), rs
									.getInt("officeid"), rs
									.getString("placeid"),
							rs.getInt("enabled"), rs.getString("description"),
							rs.getDouble("lat"), rs.getDouble("lng"));
				});

	}

	@Override
	public List<ClientDto> findAllClientsByOfficeId(int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM map_clients where officeid = ?";
		return jdbcTemplate.query(
				sql,
				ps -> {
					ps.setInt(1, id);
				},
				(rs, rownum) -> {
					return new ClientDto(rs.getInt("id"), rs.getString("name"),
							rs.getString("contact"), rs.getString("address"),
							rs.getString("status"), rs.getString("domain"), rs
									.getInt("employeeid"), rs
									.getString("employeename"), rs
									.getInt("officeid"), rs
									.getString("placeid"),
							rs.getInt("enabled"), rs.getString("description"),
							rs.getDouble("lat"), rs.getDouble("lng"));
				});
	}

}
