package com.worksap.stm_s173.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.worksap.stm_s173.dto.NotificationDto;
import com.worksap.stm_s173.dto.TaskDto;
import com.worksap.stm_s173.entity.TaskEntity;
import com.worksap.stm_s173.entity.TaskFetchEntity;
import com.worksap.stm_s173.service.spec.TaskService;

@Controller
public class TaskController {

	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/taskmanagement", method = RequestMethod.GET)
	public String task() {
		return "calendar";
	}

	@RequestMapping(value = "/taskmanagement/addEvent", method = RequestMethod.POST)
	@ResponseBody
	public void addEvent(@RequestBody TaskDto taskCreationEntity,
			HttpServletRequest request) {
		taskService.insert(taskCreationEntity, request);
	}

	@RequestMapping(value = "/taskmanagement/editEvent", method = RequestMethod.POST)
	@ResponseBody
	public void editEvent(@RequestBody TaskDto taskCreationEntity) {
		System.out.println(taskCreationEntity);
		taskService.editEvent(taskCreationEntity);
	}

	@RequestMapping(value = "/taskmanagement/deleteEvent", method = RequestMethod.POST)
	@ResponseBody
	public void deleteEvent(@RequestBody TaskDto taskCreationEntity) {
		// System.out.println(taskCreationEntity);
		taskService.deleteEvent(taskCreationEntity);
	}

	@RequestMapping(value = "/taskmanagement/addClientEvent", method = RequestMethod.POST)
	@ResponseBody
	public void addlientEvent(@RequestBody TaskDto taskCreationEntity,
			HttpServletRequest request) {
		taskService.insertClientEvent(taskCreationEntity, request);
	}

	@RequestMapping(value = "/taskmanagement/getAllEvents", method = RequestMethod.POST)
	@ResponseBody
	public TaskEntity getEvents(@RequestBody TaskFetchEntity entity) {
		return taskService.getBy(entity);
	}

	@RequestMapping(value = "/taskmanagement/getAllNotifications", method = RequestMethod.POST)
	@ResponseBody
	public List<NotificationDto> getAllNotifications(Principal p) {
		Date date = new Date();
		long timestamp = date.getTime() / 1000;
		return taskService.getAllNotifications(timestamp, p.getName());
	}

	@RequestMapping(value = "/taskmanagement/deleteNotificationById", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteNotificationById(@RequestParam int id) {
		taskService.deleteNotificationById(id);
	}

	@RequestMapping(value = "/taskmanagement/getAllEventsOfEmployees", method = RequestMethod.POST)
	@ResponseBody
	public TaskEntity getAllEventsOfEmployees(
			@RequestBody TaskFetchEntity entity, Principal p) {
		return taskService.getAllEventsOfEmployees(entity, p.getName());
	}

	@RequestMapping(value = "/taskmanagement/createInHouseMeeting", method = RequestMethod.POST)
	@ResponseBody
	public void createInHouseMeeting(@RequestBody TaskFetchEntity entity,
			Principal p) {
		taskService.createInHouseMeeting(entity, p.getName());
	}

}
