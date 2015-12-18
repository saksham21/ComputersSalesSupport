package com.worksap.stm_s173.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.worksap.stm_s173.dao.spec.ClientDao;
import com.worksap.stm_s173.dao.spec.EmployeeDao;
import com.worksap.stm_s173.dao.spec.TaskDao;
import com.worksap.stm_s173.dao.spec.TimelineDao;
import com.worksap.stm_s173.dto.NotificationDto;
import com.worksap.stm_s173.dto.TaskDto;
import com.worksap.stm_s173.dto.TimelineDto;
import com.worksap.stm_s173.entity.TaskEntity;
import com.worksap.stm_s173.entity.TaskFetchEntity;
import com.worksap.stm_s173.exception.ServiceException;
import com.worksap.stm_s173.service.spec.TaskService;

public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private TimelineDao timelineDao;

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Override
	public void insert(TaskDto taskCreationEntity, HttpServletRequest request)
			throws ServiceException {

		NotificationDto notif = new NotificationDto();
		notif.setTitle("Meeting Reminder");
		notif.setDescription(taskCreationEntity.getTitle() + "@ "
				+ new Date((long) taskCreationEntity.getStart() * 1000));
		notif.setTo_who(taskCreationEntity.getCreator());
		notif.setFrom_who(taskCreationEntity.getCreator());
		notif.setStatus("NEW");
		notif.setValue(2);
		if (taskCreationEntity.getReminder() == 0) {
			taskCreationEntity
					.setReminder(taskCreationEntity.getStart() - 3600); // setting
																		// reminder
																		// one
																		// hour
																		// back
																		// if it
																		// is
																		// not
																		// selected
																		// manually,
																		// BY
																		// DEFAULT
			notif.setReminder_time(taskCreationEntity.getStart() - 3600);
		} else {
			notif.setReminder_time(taskCreationEntity.getReminder());
		}

		try {
			taskDao.addEvent(taskCreationEntity);
			taskDao.addNotification(notif);
		} catch (IOException e) {
			throw new ServiceException("Cannot add event for Title: "
					+ taskCreationEntity.getTitle(), e);
		}
	}

	@Override
	public void insertClientEvent(TaskDto taskCreationEntity,
			HttpServletRequest request) throws ServiceException {

		NotificationDto notif = new NotificationDto();
		notif.setTitle("Meeting Reminder");
		notif.setDescription(taskCreationEntity.getTitle() + "@ "
				+ new Date((long) taskCreationEntity.getStart() * 1000));
		notif.setTo_who(taskCreationEntity.getCreator());
		notif.setFrom_who(taskCreationEntity.getCreator());
		notif.setStatus("NEW");
		notif.setValue(2);
		TimelineDto timelineDto = eventTimeline(taskCreationEntity);
		if (taskCreationEntity.getReminder() == 0) {
			taskCreationEntity
					.setReminder(taskCreationEntity.getStart() - 3600);
			notif.setReminder_time(taskCreationEntity.getStart() - 3600);
		} else {
			notif.setReminder_time(taskCreationEntity.getReminder());
		}

		try {
			taskDao.addEvent(taskCreationEntity);
			taskDao.addNotification(notif);
			timelineDao.addEvent(timelineDto);
		} catch (IOException e) {
			throw new ServiceException("Cannot add event for Title: "
					+ taskCreationEntity.getTitle(), e);
		}
	}

	private TimelineDto eventTimeline(TaskDto taskCreationEntity) {
		TimelineDto timelineDto = new TimelineDto();
		timelineDto.setClient_id(taskCreationEntity.getClient_id());
		timelineDto.setCreation_time((new Date()).getTime() / 1000);
		timelineDto.setCreator(taskCreationEntity.getCreator());
		Date date = new Date(taskCreationEntity.getStart() * 1000);
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		StringBuilder sb = new StringBuilder();
		sb.append(date.getHours());
		sb.append(":");
		sb.append(date.getMinutes());
		sb.append(" ");
		sb.append(months[date.getMonth()]);
		sb.append(" ");
		sb.append(date.getDate());
		sb.append(",");
		sb.append(date.getYear());
		String str = sb.toString();
		// System.out.println("date in string format in eventTimeline: " +str);
		timelineDto.setTitle("Reminder Set for " + taskCreationEntity.getTitle());
		timelineDto.setDescription("Reminder@" + str + "<br>" + taskCreationEntity.getDescription());
		return timelineDto;
	}

	@Override
	public TaskEntity getBy(TaskFetchEntity entity) throws ServiceException {

		List<TaskDto> tasks = null;

		try {
			tasks = taskDao.getAll(entity.getStart(), entity.getEnd(),
					entity.getUsername());
		} catch (IOException e) {
			throw new ServiceException("Cannot get all events " + e);
		}

		return new TaskEntity(tasks);
	}

	@Override
	public List<TaskDto> notifyEveryone(long date) throws ServiceException {
		// System.out.println("Int notify");
		List<TaskDto> accounts = null;// = new ArrayList<TaskDto>();
		try {
			accounts = taskDao.getCalReminders(date);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public List<NotificationDto> getAllNotifications(long timestamp, String name)
			throws ServiceException {
		List<NotificationDto> accounts = new ArrayList<NotificationDto>();
		try {
			accounts = taskDao.getAllNotifications(timestamp, name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public void deleteNotificationById(int id) throws ServiceException {

		try {
			taskDao.deleteNotificationById(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public TaskEntity getAllEventsOfEmployees(TaskFetchEntity entity,
			String name) throws ServiceException {
		List<TaskDto> tasks = new ArrayList<TaskDto>();
		List<TaskDto> tasks_id = new ArrayList<TaskDto>();
		List<Integer> emp_id = new ArrayList<Integer>();
		if (!(entity.getUsername()).isEmpty()) {
			for (String retval : entity.getUsername().split(",")) {
				emp_id.add(Integer.parseInt(retval));
			}
		}

		try {
			for (int i = 0; i < emp_id.size(); i++) {
				tasks_id = taskDao.getAll(entity.getStart(), entity.getEnd(),
						employeeDao.getEmployee(emp_id.get(i)).getUsername());
				tasks.addAll(tasks_id);

			}
			tasks_id = taskDao.getAll(entity.getStart(), entity.getEnd(), name);
			tasks.addAll(tasks_id);

		} catch (IOException e) {
			throw new ServiceException("Cannot get all events " + e);
		}

		return new TaskEntity(tasks);
	}

	@Override
	public void createInHouseMeeting(TaskFetchEntity entity, String name)
			throws ServiceException {

		List<Integer> emp_id = new ArrayList<Integer>();
		if (!(entity.getUsername()).isEmpty()) {
			for (String retval : entity.getUsername().split(",")) {
				emp_id.add(Integer.parseInt(retval));
			}
		}

		try {
			for (int i = 0; i < emp_id.size(); i++) {
				TaskDto taskDto = new TaskDto();
				taskDto.setTitle("In-House Meeting");
				taskDto.setCreator(employeeDao.getEmployee(emp_id.get(i))
						.getUsername());
				taskDto.setDescription(entity.getDescription());
				taskDto.setEnabled(1);
				taskDto.setStart(entity.getStart());
				taskDto.setEnd(entity.getEnd());
				taskDto.setReminder(entity.getStart() - 3600);
				taskDao.addEvent(taskDto);

				NotificationDto notif = new NotificationDto();
				notif.setTitle("Meeting Reminder");
				notif.setDescription(taskDto.getTitle() + "@ "
						+ new Date((long) taskDto.getStart() * 1000));
				notif.setTo_who(taskDto.getCreator());
				notif.setFrom_who(taskDto.getCreator());
				notif.setStatus("NEW");
				notif.setReminder_time(taskDto.getReminder());
				notif.setValue(2);
				taskDao.addNotification(notif);

			}

			TaskDto taskDto = new TaskDto();
			taskDto.setTitle("In-House Meeting");
			taskDto.setCreator(name);
			taskDto.setDescription(entity.getDescription());
			taskDto.setEnabled(1);
			taskDto.setStart(entity.getStart());
			taskDto.setEnd(entity.getEnd());
			taskDto.setReminder(entity.getStart() - 3600);
			taskDao.addEvent(taskDto);

			NotificationDto notif = new NotificationDto();
			notif.setTitle("Meeting Reminder");
			notif.setDescription(taskDto.getTitle() + "@ "
					+ new Date((long) taskDto.getStart() * 1000));
			notif.setTo_who(taskDto.getCreator());
			notif.setFrom_who(taskDto.getCreator());
			notif.setStatus("NEW");
			notif.setReminder_time(taskDto.getReminder());
			notif.setValue(2);
			taskDao.addNotification(notif);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void editEvent(TaskDto taskCreationEntity) throws ServiceException {
		try {
			taskDao.updateEvent(taskCreationEntity);
			taskDao.updateNotification(taskCreationEntity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deleteEvent(TaskDto taskCreationEntity) throws ServiceException {
		try {
			taskDao.deleteEvent(taskCreationEntity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
