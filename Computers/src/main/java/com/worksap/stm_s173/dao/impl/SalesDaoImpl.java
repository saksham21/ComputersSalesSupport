package com.worksap.stm_s173.dao.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.worksap.stm_s173.dao.spec.SalesDao;
import com.worksap.stm_s173.dto.CartProductDto;
import com.worksap.stm_s173.dto.OfficeSalesRecordDto;
import com.worksap.stm_s173.dto.OwnIntPairDto;
import com.worksap.stm_s173.dto.ProductDto;
import com.worksap.stm_s173.dto.SalesTargetDto;
import com.worksap.stm_s173.entity.SalesCreationEntity;

public class SalesDaoImpl implements SalesDao {

	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private int i = 0;

	@Override
	public void insertAllProducts(SalesCreationEntity salesCreationEntity,
			int order_id, int emp_id, long ts) throws IOException {
		String sql = "INSERT INTO product_sales_detail "
				+ " (order_id, product_id, price, quantity, client_id, emp_id, enabled, timestamp)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ? , ?)";
		jdbcTemplate = new JdbcTemplate(dataSource);
		int len = salesCreationEntity.getPrice().size();
		for (i = 0; i < len; i++) {
			jdbcTemplate.update(sql, (ps) -> {
				ps.setInt(1, order_id);
				ps.setInt(2, salesCreationEntity.getName().get(i));
				ps.setDouble(3, salesCreationEntity.getPrice().get(i));
				ps.setInt(4, salesCreationEntity.getQuantity().get(i));
				ps.setInt(5, salesCreationEntity.getId());
				ps.setInt(6, emp_id);
				ps.setInt(7, 1);
				ps.setDouble(8, ts);
			});
		}
	}

	@Override
	public int getOrderId() throws IOException {
		String sql = "SELECT MAX(order_id) FROM product_sales_detail";
		jdbcTemplate = new JdbcTemplate(dataSource);
		@SuppressWarnings("deprecation")
		int id = (int) jdbcTemplate.queryForLong(sql);
		id = id + 1;
		return id;
	}

	@Override
	public List<ProductDto> getRecBasedOnDomain(String domain)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT product_details.* FROM product_sales_detail JOIN map_clients "
				+ " on product_sales_detail.client_id = map_clients.id JOIN  product_details "
				+ " on product_sales_detail.product_id = product_details.id where map_clients.domain=? and product_details.enabled=1 "
				+ " group by product_sales_detail.product_id ORDER BY count(product_sales_detail.product_id) desc LIMIT 3";
		return jdbcTemplate.query(
				sql,
				(rs, rownum) -> {
					return new ProductDto(rs.getInt("id"), rs
							.getString("company"), rs.getDouble("price"), rs
							.getString("name"), rs.getString("description"), rs
							.getInt("enabled"));
				}, domain);
	}

	@Override
	public List<ProductDto> getRecBasedOnSearch(List<String> a, double start,
			double end, String brand) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT product_details.* FROM product_sales_detail JOIN  product_details "
				+ " on product_sales_detail.product_id = product_details.id where ";
		for (i = 0; i < a.size(); i++) {
			sql += " product_details.description like'%" + a.get(i) + "%' and";
		}
		if (brand.equals("")) {
			sql += " product_details.enabled=1 and product_details.price>=? and product_details.price<=? "
					+ " group by product_sales_detail.product_id ORDER BY "
					+ " count(product_sales_detail.product_id) desc LIMIT 10";
			System.out.println("SQL(empty): " + sql);
			return jdbcTemplate.query(
					sql,
					(rs, rownum) -> {
						return new ProductDto(rs.getInt("id"), rs
								.getString("company"), rs.getDouble("price"),
								rs.getString("name"), rs
										.getString("description"), rs
										.getInt("enabled"));
					}, start, end);
		} else {
			sql += " product_details.enabled=1 and product_details.company=? and product_details.price>=? "
					+ " and product_details.price<=? group by product_sales_detail.product_id ORDER BY "
					+ " count(product_sales_detail.product_id) desc LIMIT 10";
			System.out.println("SQL(not empty): " + sql);
			return jdbcTemplate.query(
					sql,
					(rs, rownum) -> {
						return new ProductDto(rs.getInt("id"), rs
								.getString("company"), rs.getDouble("price"),
								rs.getString("name"), rs
										.getString("description"), rs
										.getInt("enabled"));
					}, brand, start, end);
		}
	}

	@Override
	public void addNewOrder(int order_id, int emp_id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO order_table (id, emp_id) VALUES (?, ?)";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, order_id);
			ps.setInt(2, emp_id);
		});
	}

	@Override
	public int checkOfficeTargetSet(int id, int mnth, int year)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(*) FROM sales_target_table WHERE officeid=? and month=? and year=?";
		return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt(1);
		}, id, mnth, year);
	}

	@Override
	public void insertSalesTarget(int id, int mnth, int year, int emp_count)
			throws IOException {
		System.out.println("In insertSalesTarget, " + mnth + " , " + year
				+ " , " + id + " , " + emp_count);
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO sales_target_table "
				+ " (month, year, officeid, emp_count) "
				+ " VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, mnth);
			ps.setInt(2, year);
			ps.setDouble(3, id);
			ps.setInt(4, emp_count);
		});
	}

	@Override
	public OfficeSalesRecordDto getRecord(int id, int mnth, int i)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM sales_target_table WHERE officeid=? and month=? and year=?";
		return jdbcTemplate.queryForObject(
				sql,
				(rs, rownum) -> {
					return new OfficeSalesRecordDto(rs.getInt("id"), rs
							.getInt("month"), rs.getInt("year"), rs
							.getInt("target_set"),
							rs.getInt("target_achieved"),
							rs.getInt("officeid"), rs.getInt("emp_count"));
				}, id, mnth, i);
	}

	@Override
	public int getMonthlySales(int mnth, int year, HttpServletRequest request,
			int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		int a = 0;
		if (request.isUserInRole("Director")) {
			sql = "SELECT SUM(target_achieved) FROM sales_target_table WHERE month=? and year=?";
			a = jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt("SUM(target_achieved)");
			}, mnth, year);
		} else if (request.isUserInRole("Manager")) {
			sql = "SELECT target_achieved FROM sales_target_table WHERE officeid=? and month=? and year=?";
			a = jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt("target_achieved");
			}, id, mnth, year);
		}
		return a;
	}

	@Override
	public int getAnnualSales(int month, int year, HttpServletRequest request,
			int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		int a = 0;
		if (request.isUserInRole("Director")) {
			sql = "SELECT SUM(target_achieved) FROM sales_target_table WHERE month<=? and year=?";
			a = jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt("SUM(target_achieved)");
			}, month, year);
		} else if (request.isUserInRole("Manager")) {
			sql = "SELECT SUM(target_achieved) FROM sales_target_table WHERE officeid=? and month<=? and year=?";
			a = jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt("SUM(target_achieved)");
			}, id, month, year);
		}
		return a;
	}

	@Override
	public int getAssignedAnnualSales(int mnth, int year,
			HttpServletRequest request, int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		int a = 0;
		if (request.isUserInRole("Director")) {
			sql = "SELECT SUM(target_set) FROM sales_target_table WHERE month<=? and year=?";
			a = jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt("SUM(target_set)");
			}, mnth, year);
		} else if (request.isUserInRole("Manager")) {
			sql = "SELECT SUM(target_set) FROM sales_target_table WHERE officeid=? and month<=? and year=?";
			a = jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt("SUM(target_set)");
			}, id, mnth, year);
		}
		return a;
	}

	@Override
	public int getAssignedMonthlySales(int mnth, int year,
			HttpServletRequest request, int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "";
		int a = 0;
		if (request.isUserInRole("Director")) {
			sql = "SELECT SUM(target_set) FROM sales_target_table WHERE month=? and year=?";
			a = jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt("SUM(target_set)");
			}, mnth, year);
		} else if (request.isUserInRole("Manager")) {
			sql = "SELECT target_set FROM sales_target_table WHERE officeid=? and month=? and year=?";
			a = jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt("target_set");
			}, id, mnth, year);
		}
		return a;
	}

	@Override
	public void setSalesTarget(List<SalesTargetDto> salesTargetDto, int mnth,
			int year) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE sales_target_table SET target_set = ? WHERE officeid=? and month=? and year=?";
		for (i = 0; i < salesTargetDto.size(); i++) {
			jdbcTemplate.update(sql, (ps) -> {
				ps.setInt(1, salesTargetDto.get(i).getPast_sales());
				ps.setInt(2, salesTargetDto.get(i).getId());
				ps.setInt(3, mnth);
				ps.setInt(4, year);
			});
		}
	}

	@Override
	public int getMonthlySalesDirectorById(int month, int year, int id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT target_achieved FROM sales_target_table WHERE officeid=? and month=? and year=?";
		return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt("target_achieved");
		}, id, month, year);
	}

	@Override
	public int getAnnualSalesDirectorById(int mnth, int year, int id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT SUM(target_achieved) FROM sales_target_table WHERE officeid=? and month<=? and year=?";
		return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt("SUM(target_achieved)");
		}, id, mnth, year);
	}

	@Override
	public int getAssignedMonthlySalesDirectorById(int month, int year, int id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT target_set FROM sales_target_table WHERE officeid=? and month=? and year=?";
		return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt("target_set");
		}, id, month, year);
	}

	@Override
	public int getAssignedAnnualSalesDirectorById(int month, int year, int id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT SUM(target_set) FROM sales_target_table WHERE officeid=? and month<=? and year=?";
		return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt("SUM(target_set)");
		}, id, month, year);
	}

	@Override
	public List<OwnIntPairDto> getAverageSaleOrderByOffice(int id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT SUM(product_sales_detail.price*product_sales_detail.quantity) as value, product_sales_detail.order_id FROM product_sales_detail join user_account on product_sales_detail.emp_id = user_account.id WHERE user_account.officename=? group by product_sales_detail.order_id";
		return jdbcTemplate.query(
				sql,
				(rs, rownum) -> {
					return new OwnIntPairDto(rs.getInt("value"), rs
							.getInt("order_id"));
				}, id);
	}

	@Override
	public void updateEmployeeNumber(int id, int mnth, int year, int emp_count)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE sales_target_table SET emp_count=? where officeid=? and month=? and year=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, emp_count);
			ps.setInt(2, id);
			ps.setInt(3, mnth);
			ps.setInt(4, year);
		});
	}

	@Override
	public void updateMonthlySales(int value, int id, int month, int year)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE sales_target_table SET target_achieved = target_achieved + ? where officeid=? and month=? and year=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, value);
			ps.setInt(2, id);
			ps.setInt(3, month);
			ps.setInt(4, year);
		});
	}

	@Override
	public void addProductInCart(CartProductDto cartProductDto)
			throws IOException {
		String sql = "INSERT INTO order_save_table "
				+ " (client_id, product_id, quantity)" + " VALUES (?, ?, ?)";
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, cartProductDto.getClient_id());
			ps.setInt(2, cartProductDto.getProduct_id());
			ps.setInt(3, cartProductDto.getQuantity());
		});
	}

	@Override
	public List<CartProductDto> getAllProductsFromCart(
			CartProductDto cartProductDto) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM order_save_table WHERE client_id=? ";
		return jdbcTemplate.query(
				sql,
				(rs, rownum) -> {
					return new CartProductDto(rs.getInt("client_id"), rs
							.getInt("product_id"), rs.getInt("quantity"));
				}, cartProductDto.getClient_id());
	}

	@Override
	public void deleteProductFromCart(CartProductDto cartProductDto)
			throws IOException {
		String sql = "DELETE FROM order_save_table WHERE client_id=? and product_id=?";
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, cartProductDto.getClient_id());
			ps.setInt(2, cartProductDto.getProduct_id());
		});
	}

	@Override
	public void updateQuantity(CartProductDto cartProductDto)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE order_save_table SET quantity=? where client_id=? and product_id=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, cartProductDto.getQuantity());
			ps.setInt(2, cartProductDto.getClient_id());
			ps.setInt(3, cartProductDto.getProduct_id());
		});
	}

	@Override
	public void emptyCartUsingClientId(int id) throws IOException {
		String sql = "DELETE FROM order_save_table WHERE client_id=?";
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, id);
		});
	}

	@Override
	public int getActiveLeads(HttpServletRequest request, int office_id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		if (request.isUserInRole("Manager")) {
			String sql = "SELECT COUNT(*) FROM map_clients WHERE status='ASSIGNED' and officeid=? and enabled=1";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, office_id);
		} else {
			String sql = "SELECT COUNT(*) FROM map_clients WHERE status='ASSIGNED' and enabled=1";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			});
		}
	}

	@Override
	public int getRepresentatives(HttpServletRequest request, int officeid)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		if (request.isUserInRole("Manager")) {
			String sql = "SELECT COUNT(*) FROM user_account WHERE role='Representative' and enabled=1 and officename=?";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, officeid);
		} else {
			String sql = "SELECT COUNT(*) FROM user_account WHERE role='Representative' and enabled=1";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			});
		}
	}

	@Override
	public int getLeads_generated(HttpServletRequest request, int office_id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		if (request.isUserInRole("Manager")) {
			String sql = "SELECT COUNT(*) FROM map_clients WHERE status='UNASSIGNED' and enabled=1 and officeid=?";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			}, office_id);
		} else {
			String sql = "SELECT COUNT(*) FROM map_clients WHERE status='UNASSIGNED' and enabled=1";
			return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
				return rs.getInt(1);
			});
		}
	}

}
