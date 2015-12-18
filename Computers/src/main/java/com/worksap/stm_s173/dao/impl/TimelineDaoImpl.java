package com.worksap.stm_s173.dao.impl;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.worksap.stm_s173.dao.spec.TimelineDao;
import com.worksap.stm_s173.dto.TimelineDto;

public class TimelineDaoImpl implements TimelineDao {

	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void addEvent(TimelineDto timelineDto) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO timeline_table "
				+ " (client_id, creator, creation_time, title, description, enabled)"
				+ " VALUES (?, ?, ?, ?, ?, 0)";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, timelineDto.getClient_id());
			ps.setString(2, timelineDto.getCreator());
			ps.setDouble(3, timelineDto.getCreation_time());
			ps.setString(4, timelineDto.getTitle());
			ps.setString(5, timelineDto.getDescription());
		});

	}

	@Override
	public List<TimelineDto> createOrderTimeline(int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM timeline_table WHERE client_id = ? and enabled = 0 ";
		return jdbcTemplate.query(
				sql,
				ps -> {
					ps.setInt(1, id);
				},
				(rs, rownum) -> {
					return new TimelineDto(rs.getInt("id"), rs
							.getInt("client_id"), rs.getString("creator"), rs
							.getLong("creation_time"), rs.getString("title"),
							rs.getString("description"), rs.getInt("enabled"),
							rs.getInt("order_id"));
				});
	}

	@Override
	public void moveThisTimelineToOld(int client_id, int order_id)
			throws IOException {
		System.out.println("In moveThisTimelineToOld , " + client_id + " , "
				+ order_id);
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE timeline_table SET "
				+ " enabled = 1 WHERE order_id=? and client_id=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, order_id);
			ps.setInt(2, client_id);
		});
	}

	@Override
	public void setOrderId(int client_id, int order_id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE timeline_table SET "
				+ " order_id=? where enabled=0 and client_id=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, order_id);
			ps.setInt(2, client_id);
		});
	}

	@Override
	public List<TimelineDto> createPastOrderTimeline(int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM timeline_table WHERE client_id = ? and enabled = 1 ";
		return jdbcTemplate.query(
				sql,
				ps -> {
					ps.setInt(1, id);
				},
				(rs, rownum) -> {
					return new TimelineDto(rs.getInt("id"), rs
							.getInt("client_id"), rs.getString("creator"), rs
							.getLong("creation_time"), rs.getString("title"),
							rs.getString("description"), rs.getInt("enabled"),
							rs.getInt("order_id"));
				});
	}

}
