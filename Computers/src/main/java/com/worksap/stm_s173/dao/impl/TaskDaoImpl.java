package com.worksap.stm_s173.dao.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.worksap.stm_s173.dao.spec.TaskDao;
import com.worksap.stm_s173.dto.NotificationDto;
import com.worksap.stm_s173.dto.TaskDto;

public class TaskDaoImpl implements TaskDao {

	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void addEvent(TaskDto task) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO task_account "
				+ " (title , creator, description, start, end, enabled, reminder)"
				+ " VALUES (? , ? , ? , ? , ? , ? , ?)";
		try {
			jdbcTemplate.update(sql, (ps) -> {
				ps.setString(1, task.getTitle());
				ps.setString(2, task.getCreator());
				ps.setString(3, task.getDescription());
				ps.setLong(4, task.getStart());
				ps.setLong(5, task.getEnd());
				ps.setInt(6, 1);
				ps.setLong(7, task.getReminder());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<TaskDto> getAll(long start, long end, String username)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		// System.out.println(start + " , " + end + " , " + username);
		String sql = "SELECT * FROM task_account WHERE enabled=1 AND creator=? AND start>=? AND start<=? ";
		return jdbcTemplate.query(
				sql,
				(ps) -> {
					ps.setString(1, username);
					ps.setDouble(2, start);
					ps.setDouble(3, end);
				},
				(rs, rownum) -> {
					return new TaskDto(rs.getInt("id"), rs.getInt("client_id"),
							rs.getString("title"), rs.getString("creator"), rs
									.getString("description"), rs
									.getLong("reminder"), rs.getLong("start"),
							rs.getLong("end"), rs.getInt("enabled"));
				});
	}

	@Override
	public List<TaskDto> getCalReminders(long date) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM task_account WHERE enabled=1 AND reminder>=?";
		return jdbcTemplate.query(
				sql,
				(ps) -> {
					ps.setLong(1, date);
				},
				(rs, rownum) -> {
					return new TaskDto(rs.getInt("id"), rs.getInt(""), rs
							.getString("title"), rs.getString("creator"), rs
							.getString("description"), rs.getLong("reminder"),
							rs.getLong("start"), rs.getLong("end"), rs
									.getInt("enabled"));
				});
	}

	@Override
	public void addNotification(NotificationDto notif) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO notification_table "
				+ " (title , description , reminder_time , to_who , from_who, status, enabled, value)"
				+ " VALUES (? , ? , ? , ? , ? , ? , ?, ?)";
		try {
			jdbcTemplate.update(sql, (ps) -> {
				ps.setString(1, notif.getTitle());
				ps.setString(2, notif.getDescription());
				ps.setDouble(3, notif.getReminder_time());
				ps.setString(4, notif.getTo_who());
				ps.setString(5, notif.getFrom_who());
				ps.setString(6, notif.getStatus());
				ps.setInt(7, 1);
				ps.setInt(8, notif.getValue());
			});
		} catch (DataAccessException e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<NotificationDto> getAllNotifications(long timestamp, String name)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "SELECT * FROM notification_table WHERE enabled=1 AND to_who=? AND reminder_time<=? ";
		return jdbcTemplate.query(
				sql,
				(ps) -> {
					ps.setString(1, name);
					ps.setDouble(2, timestamp);
				},
				(rs, rownum) -> {
					return new NotificationDto(rs.getInt("id"), rs
							.getString("title"), rs.getString("description"),
							rs.getLong("reminder_time"),
							rs.getString("to_who"), rs.getString("from_who"),
							rs.getString("status"), rs.getInt("enabled"), rs
									.getInt("value"));
				});
	}

	@Override
	public void deleteNotificationById(int id) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE notification_table SET enabled=0 WHERE id=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, id);
		});
	}

	@Override
	public void updateEvent(TaskDto taskCreationEntity) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE task_account SET title=?, creator=?, description=?, start=?, end=?, reminder=? WHERE id=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setString(1, taskCreationEntity.getTitle());
			ps.setString(2, taskCreationEntity.getCreator());
			ps.setString(3, taskCreationEntity.getDescription());
			ps.setDouble(4, taskCreationEntity.getStart());
			ps.setDouble(5, taskCreationEntity.getEnd());
			ps.setDouble(6, taskCreationEntity.getReminder());
			ps.setInt(7, taskCreationEntity.getId());
		});
	}

	@Override
	public void deleteEvent(TaskDto taskCreationEntity) throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE task_account SET enabled=0 WHERE id=?";
		jdbcTemplate.update(sql, (ps) -> {
			ps.setInt(1, taskCreationEntity.getId());
		});
	}

	@Override
	public void updateNotification(TaskDto taskCreationEntity)
			throws IOException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE notification_table SET reminder_time=? WHERE to_who=? and description=?";
		String str = taskCreationEntity.getTitle() + "@ "
				+ new Date(taskCreationEntity.getStart() * 1000);
		jdbcTemplate.update(sql, (ps) -> {
			ps.setLong(1, taskCreationEntity.getReminder());
			ps.setString(2, taskCreationEntity.getCreator());
			ps.setString(3, str);
		});
	}
}
