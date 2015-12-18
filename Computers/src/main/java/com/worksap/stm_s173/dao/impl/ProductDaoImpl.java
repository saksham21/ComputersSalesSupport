package com.worksap.stm_s173.dao.impl;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.worksap.stm_s173.dao.spec.ProductDao;
import com.worksap.stm_s173.dto.ProductDto;

public class ProductDaoImpl implements ProductDao {

	private JdbcTemplate jdbcTemplate;	
	private DataSource dataSource;
	public void setDataSource(DataSource dataSource) {
	       this.dataSource = dataSource;
	   }

	@Override
	public void addProduct(ProductDto product)
			throws IOException {
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql =  "INSERT INTO product_details "
				+ " ( company, price, name, description, enabled)"
				+ " VALUES (?, ?, ?, ?, 1)";
		jdbcTemplate.update(sql,(ps) -> {
			ps.setString(1, product.getCompany());			
			ps.setDouble(2, product.getPrice());
			ps.setString(3, product.getName());
			ps.setString(4, product.getDescription());
			});
		}

	@Override
	public int getAllCount()
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT COUNT(*) FROM product_details WHERE enabled=1";
		return jdbcTemplate.queryForObject(sql, (rs, rownum) -> {
			return rs.getInt(1);
		});
	}

	@Override
	public List<ProductDto> getBy(int start, int size) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM product_details WHERE enabled=1";
		return jdbcTemplate.query(
				sql,
				ps -> {
//					ps.setInt(1, size);
//					ps.setInt(2, start);
				},
				(rs, rownum) -> {
					return new ProductDto(rs.getInt("id"),
							rs.getString("company"),
							rs.getDouble("price"),
							rs.getString("name"),
							rs.getString("description"),
							rs.getInt("enabled"));
					
				});
	}

	@Override
	public void deleteEmployee(int id)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE product_details SET enabled=0 WHERE id=?";
		try {
			jdbcTemplate.update(sql, (ps) -> {
				ps.setInt(1, id);
			});
		}
		catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void updateEmployee(ProductDto product)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE product_details SET "
				+ " company = ?, price = ?, name = ?, description = ? WHERE id = ?";
		jdbcTemplate.update(
				sql, (ps) -> {
					ps.setString(1, product.getCompany());			
					ps.setDouble(2, product.getPrice());
					ps.setString(3, product.getName());
					ps.setString(4, product.getDescription());
					ps.setInt(5,  product.getId());
					});
	}

	@Override
	public List<ProductDto> getBy() throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM product_details WHERE enabled=1";
		return jdbcTemplate.query(
				sql,
				(rs, rownum) -> {
					return new ProductDto(rs.getInt("id"),
							rs.getString("company"),
							rs.getDouble("price"),
							rs.getString("name"),
							rs.getString("description"),
							rs.getInt("enabled"));
					
				});
	}

}
