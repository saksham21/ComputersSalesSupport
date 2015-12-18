package com.worksap.stm_s173.service.spec;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.worksap.stm_s173.dto.NotificationDto;
import com.worksap.stm_s173.dto.TaskDto;
import com.worksap.stm_s173.entity.TaskEntity;
import com.worksap.stm_s173.entity.TaskFetchEntity;
import com.worksap.stm_s173.exception.ServiceException;

public interface TaskService {

	void insert(TaskDto taskCreationEntity, HttpServletRequest request)
			throws ServiceException;

	void insertClientEvent(TaskDto taskCreationEntity,
			HttpServletRequest request) throws ServiceException;

	TaskEntity getBy(TaskFetchEntity entity) throws ServiceException;

	List<TaskDto> notifyEveryone(long date) throws ServiceException;

	List<NotificationDto> getAllNotifications(long timestamp, String name)
			throws ServiceException;

	void deleteNotificationById(int id) throws ServiceException;

	TaskEntity getAllEventsOfEmployees(TaskFetchEntity entity, String string)
			throws ServiceException;

	void createInHouseMeeting(TaskFetchEntity entity, String name)
			throws ServiceException;

	void editEvent(TaskDto taskCreationEntity) throws ServiceException;

	void deleteEvent(TaskDto taskCreationEntity) throws ServiceException;

}
