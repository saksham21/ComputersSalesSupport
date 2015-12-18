package com.worksap.stm_s173.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm_s173.dto.NotificationDto;
import com.worksap.stm_s173.dto.TaskDto;

public interface TaskDao {
	
	void addEvent(TaskDto task) throws IOException;

	List<TaskDto> getAll(long start, long end, String username) throws IOException;

	List<TaskDto> getCalReminders(long date) throws IOException;

	void addNotification(NotificationDto notif) throws IOException;

	List<NotificationDto> getAllNotifications(long timestamp, String name) throws IOException;

	void deleteNotificationById(int id) throws IOException;

	void updateEvent(TaskDto taskCreationEntity) throws IOException;

	void deleteEvent(TaskDto taskCreationEntity) throws IOException;

	void updateNotification(TaskDto taskCreationEntity) throws IOException;
	
}
