package com.worksap.stm_s173.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.worksap.stm_s173.dao.spec.ClientDao;
import com.worksap.stm_s173.dao.spec.EmployeeDao;
import com.worksap.stm_s173.dao.spec.TaskDao;
import com.worksap.stm_s173.dao.spec.TimelineDao;
import com.worksap.stm_s173.dto.ClientDto;
import com.worksap.stm_s173.dto.ClientExistingDto;
import com.worksap.stm_s173.dto.EmployeeDto;
import com.worksap.stm_s173.dto.NotificationDto;
import com.worksap.stm_s173.dto.TimelineDto;
import com.worksap.stm_s173.entity.ClientEntity;
import com.worksap.stm_s173.entity.ClientExistingEntity;
import com.worksap.stm_s173.entity.ClientsCreationEntity;
import com.worksap.stm_s173.entity.ClientsFetchEntity;
import com.worksap.stm_s173.entity.MapClientEntity;
import com.worksap.stm_s173.exception.ServiceException;
import com.worksap.stm_s173.service.spec.ClientService;
import com.worksap.stm_s173.service.spec.EmployeeService;

public class ClientServiceImpl implements ClientService {

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private TimelineDao timelineDao;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeDao employeeDao;

	private int i = 0;

	@Override
	public void insert(ClientsCreationEntity clientsCreationEntity, String name)
			throws ServiceException {
		ClientDto newClient = new ClientDto(clientsCreationEntity);
		int id = employeeService.getId(name);
		// System.out.println("Id: "+id);
		try {
			clientDao.addClient(newClient, id);
		} catch (IOException e) {
			throw new ServiceException("Cannot add client for name: "
					+ clientsCreationEntity.getName(), e);
		}
	}

	@Override
	public ClientEntity getBy(ClientsFetchEntity entity, String name)
			throws ServiceException {
		List<ClientDto> accounts = null;
		int id = employeeService.getId(name); // officeid
		String role = employeeService.findRole(name);
		int emp_id = employeeService.findId(name);
		int clientCountByAssigned;
		try {
			clientCountByAssigned = clientDao.getAssignedCount(
					entity.getStatus(), id, role, emp_id);
			accounts = clientDao.getBy(entity.getStatus(), entity.getStart(),
					entity.getLength(), id, role, emp_id);

		} catch (IOException e) {
			throw new ServiceException("Cannot find user for Id: " + id, e);
		}
		return new ClientEntity(entity.getDraw(), clientCountByAssigned,
				clientCountByAssigned, accounts);
	}

	@Override
	public ClientEntity getBy1(ClientsFetchEntity entity, String name)
			throws ServiceException {
		List<ClientDto> accounts = null;
		int id = employeeService.getId(name); // officeid
		String role = employeeService.findRole(name);
		int emp_id = employeeService.findId(name);
		int clientCountByAssigned = 0;
		try {
			clientCountByAssigned = clientDao.getAssignedCount1(
					entity.getStatus(), id, role, emp_id);
			accounts = clientDao.getBy1(entity.getStatus(), entity.getStart(),
					entity.getLength(), id, role, emp_id);

		} catch (IOException e) {
			throw new ServiceException("Cannot find user for Id: " + id, e);
		}
		return new ClientEntity(entity.getDraw(), clientCountByAssigned,
				clientCountByAssigned, accounts);
	}

	@Override
	public ClientExistingEntity getBy3(ClientsFetchEntity entity, String name,
			HttpServletRequest request) throws ServiceException {
		List<ClientExistingDto> accounts = new ArrayList<ClientExistingDto>();
		int existingClientCount = 0;
		int id = employeeService.getId(name);
		try {
			existingClientCount = clientDao.getExistingClientCount(
					entity.getStatus(), id, request, name);
			accounts = clientDao.getExistingClients(entity.getStatus(), id,
					request, name);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ClientExistingEntity(entity.getDraw(), existingClientCount,
				existingClientCount, accounts);
	}

	@Override
	public List<Integer> checkClients(MapClientEntity entity)
			throws ServiceException {
		List<Integer> client = new ArrayList<Integer>();
		List<String> s = entity.getClients();
		for (i = 0; i < s.size(); i++) {
			if (clientDao.getCount(s.get(i)) == 0) {
				client.add(1);
			} else {
				client.add(0);
			}
		}
		return client;
	}

	@Override
	public int getOfficeId(int client_id) throws ServiceException {
		int id = 0;
		try {
			id = (clientDao.getBy(client_id)).getOfficeid();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}

	/* To set TimelineDto description */
	@Override
	public void assign(ClientDto entity, String name) throws ServiceException {
		// id, name, employeename(Rep), status(Assigned/Interested) available in
		// ClientDto
		NotificationDto notif = setNotificationAssign(entity, name);
		EmployeeDto emp = employeeService.getEmp(entity.getEmployeename());
		int emp_id = emp.getId();
		TimelineDto timelineDto = assignTimeline(entity, name);
		timelineDto.setDescription("");
		if (new String("ASSIGNED").equals(entity.getStatus())) {
			notif.setTitle("Client:" + entity.getId() + " Assigned (New)");
			timelineDto.setTitle("Representative " + entity.getEmployeename()
					+ " ASSIGNED");
		} else {
			notif.setTitle("Client:" + entity.getId()
					+ " Assigned (Interested)");
			timelineDto.setTitle("Client " + entity.getName()
					+ " is INTERESTED in product, Representative "
					+ entity.getEmployeename() + " to meet him");
		}

		try {
			clientDao.updateToAssigned(emp_id, entity);
			taskDao.addNotification(notif);
			timelineDao.addEvent(timelineDto);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void assignAll(ClientsFetchEntity entity, String name)
			throws ServiceException {
		List<ClientDto> accounts = new ArrayList<ClientDto>();
		List<EmployeeDto> rep_accounts = new ArrayList<EmployeeDto>();
		int id = employeeService.getId(name); // officeid
		String role = employeeService.findRole(name);
		int emp_id = employeeService.findId(name);
		try {
			accounts = clientDao.getBy(entity.getStatus(), entity.getStart(),
					entity.getLength(), id, role, emp_id);
			rep_accounts = employeeDao.getRep(id);
			int total = 0;
			for (int i = 0; i < rep_accounts.size(); i++) {
				total += (rep_accounts.get(i).getConverted_clients());
			}
			int j = 0;
			for (int i = 0; i < rep_accounts.size(); i++) {
				int rep_value = (int) Math
						.ceil(rep_accounts.get(i).getConverted_clients()
								* accounts.size() * 1.0 / total);
				int counter = 0;
				int emp_id1 = rep_accounts.get(i).getId();
				while (j < accounts.size()
						&& rep_accounts.get(i).getClients_to_meet() < 8
						&& counter < rep_value) {
					System.out.println(rep_accounts.get(i));
					accounts.get(j).setStatus("ASSIGNED");
					accounts.get(j).setEmployeeid(rep_accounts.get(i).getId());
					accounts.get(j).setEmployeename(
							rep_accounts.get(i).getUsername());
					NotificationDto notif = setNotificationAssign(
							accounts.get(j), name);
					TimelineDto timelineDto = assignTimeline(accounts.get(j),
							name);
					notif.setTitle("Client:" + accounts.get(j).getId()
							+ " Assigned (New)");
					timelineDto.setTitle("Representative "
							+ accounts.get(j).getEmployeename()
							+ " ASSIGNED to Client "
							+ accounts.get(j).getName());

					clientDao.updateToAssigned(emp_id1, accounts.get(j));
					taskDao.addNotification(notif);
					timelineDao.addEvent(timelineDto);

					j++;
					counter++;
					int meet_value = rep_accounts.get(i).getClients_to_meet() + 1;
					rep_accounts.get(i).setClients_to_meet(meet_value);
				}
			}

			for (int i = 0; i < rep_accounts.size(); i++) {
				int emp_id1 = rep_accounts.get(i).getId();
				while (j < accounts.size()
						&& rep_accounts.get(i).getClients_to_meet() < 8) {
					System.out.println(rep_accounts.get(i));
					accounts.get(j).setStatus("ASSIGNED");
					accounts.get(j).setEmployeeid(rep_accounts.get(i).getId());
					accounts.get(j).setEmployeename(
							rep_accounts.get(i).getUsername());
					NotificationDto notif = setNotificationAssign(
							accounts.get(j), name);
					TimelineDto timelineDto = assignTimeline(accounts.get(j),
							name);
					notif.setTitle("Client:" + accounts.get(j).getId()
							+ " Assigned (New)");
					timelineDto.setTitle("Representative "
							+ accounts.get(j).getEmployeename()
							+ " ASSIGNED to Client "
							+ accounts.get(j).getName());

					clientDao.updateToAssigned(emp_id1, accounts.get(j));
					taskDao.addNotification(notif);
					timelineDao.addEvent(timelineDto);

					j++;
					rep_accounts.get(i).setClients_to_meet(
							rep_accounts.get(i).getClients_to_meet() + 1);
				}
			}

		} catch (IOException e) {
			throw new ServiceException("Cannot find user for Id: " + id, e);
		}
	}

	private NotificationDto setNotificationAssign(ClientDto entity, String name) {
		NotificationDto notif = new NotificationDto();
		notif.setDescription("Name: " + entity.getName() + " , Domain: "
				+ entity.getDomain());
		notif.setReminder_time(((new Date()).getTime()) / 1000);
		notif.setTo_who(entity.getEmployeename());
		notif.setFrom_who("Manager(" + name + ")");
		notif.setStatus("NEW");
		notif.setValue(0);
		return notif;
	}

	private TimelineDto assignTimeline(ClientDto entity, String name) {
		TimelineDto timelineDto = new TimelineDto();
		timelineDto.setCreation_time(((new Date()).getTime()) / 1000);
		timelineDto.setClient_id(entity.getId());
		timelineDto.setCreator(name);
		return timelineDto;

	}

	@Override
	public void sendStatus(ClientDto entity, String name)
			throws ServiceException {
		NotificationDto notif = setNotificationStatus(entity, name);
		TimelineDto timelineDto = statusTimeline(entity, name);
		try {
			clientDao.sendStatus(entity);
			taskDao.addNotification(notif);
			timelineDao.addEvent(timelineDto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private TimelineDto statusTimeline(ClientDto entity, String name) {
		TimelineDto timelineDto = new TimelineDto();
		timelineDto.setCreation_time(((new Date()).getTime()) / 1000);
		timelineDto.setCreator(name);
		timelineDto.setClient_id(entity.getId());
		timelineDto.setDescription(entity.getDescription());
		timelineDto.setTitle("Client is " + entity.getStatus());
		return timelineDto;
	}

	private NotificationDto setNotificationStatus(ClientDto entity, String name) {
		NotificationDto notif = new NotificationDto();
		notif.setDescription(entity.getDescription());
		notif.setReminder_time(((new Date()).getTime()) / 1000);
		notif.setFrom_who("Rep: " + name);
		notif.setStatus("NEW");
		notif.setTo_who(findMyManager(name));
		notif.setTitle("Client:" + entity.getId() + " " + entity.getStatus());
		notif.setValue(0);
		return notif;
	}

	private String findMyManager(String name) {
		int officeId;
		EmployeeDto emp = new EmployeeDto();
		try {
			emp = employeeDao.getEmployee(name);
			officeId = emp.getOfficename();
			emp = employeeDao.getManager(officeId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return emp.getUsername();
	}

	@Override
	public List<TimelineDto> createOrderTimeline(int id)
			throws ServiceException {
		List<TimelineDto> accounts = new ArrayList<TimelineDto>();
		try {
			accounts = timelineDao.createOrderTimeline(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public List<TimelineDto> createPastOrderTimeline(int id)
			throws ServiceException {
		List<TimelineDto> accounts = new ArrayList<TimelineDto>();
		try {
			accounts = timelineDao.createPastOrderTimeline(id);
			Collections.sort(accounts, new TimelineDto());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public List<ClientDto> findAllClientsByOfficeId(String name)
			throws ServiceException {

		List<ClientDto> clients = new ArrayList<ClientDto>();
		int id = 0;
		try {
			id = employeeDao.getEmployee(name).getOfficename();
			clients = clientDao.findAllClientsByOfficeId(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clients;
	}

}
