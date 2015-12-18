package com.worksap.stm_s173.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.worksap.stm_s173.dto.ClientDto;
import com.worksap.stm_s173.dto.TimelineDto;
import com.worksap.stm_s173.entity.ClientEntity;
import com.worksap.stm_s173.entity.ClientExistingEntity;
import com.worksap.stm_s173.entity.ClientsCreationEntity;
import com.worksap.stm_s173.entity.ClientsFetchEntity;
import com.worksap.stm_s173.entity.MapClientEntity;
import com.worksap.stm_s173.service.spec.ClientService;

@Controller
public class ClientController {

	@Autowired
	private ClientService clientService;

	@RequestMapping(value = "/clientmanagement", method = RequestMethod.GET)
	public String client() {
		return "client";
	}

	@RequestMapping(value = "/clientmanagement/addClients", method = RequestMethod.POST)
	@ResponseBody
	public void addClients(
			@RequestBody ClientsCreationEntity employeeAccountCreationEntity,
			Principal p) {
		clientService.insert(employeeAccountCreationEntity, p.getName());
	}

	@RequestMapping(value = "/clientmanagement/findUserByClientId", method = RequestMethod.POST)
	@ResponseBody
	public ClientEntity findUserByClientId(
			@RequestBody ClientsFetchEntity entity, Principal p) {
		return clientService.getBy(entity, p.getName());
	}

	@RequestMapping(value = "/clientmanagement/findUserByClientIdToAssignAll", method = RequestMethod.POST)
	@ResponseBody
	public void findUserByClientIdToAssignAll(
			@RequestBody ClientsFetchEntity entity, Principal p) {
		System.out.println("In findUserByClientIdToAssignAll");
		clientService.assignAll(entity, p.getName());
	}

	@RequestMapping(value = "/clientmanagement/findUserByClientId1", method = RequestMethod.POST)
	@ResponseBody
	public ClientEntity findUserByClientId1(
			@RequestBody ClientsFetchEntity entity, Principal p) {
		return clientService.getBy1(entity, p.getName());
	}

	@RequestMapping(value = "/clientmanagement/findUserByClientId3", method = RequestMethod.POST)
	@ResponseBody
	public ClientExistingEntity findUserByClientId3(
			@RequestBody ClientsFetchEntity entity, Principal p,
			HttpServletRequest request) {
		return clientService.getBy3(entity, p.getName(), request);
	}

	@RequestMapping(value = "/clientmanagement/assignClients", method = RequestMethod.POST)
	@ResponseBody
	public void assignClients(@RequestBody ClientDto entity, Principal p) {
		clientService.assign(entity, p.getName());
	}

	@RequestMapping(value = "/checkmapclient", method = RequestMethod.POST)
	@ResponseBody
	public List<Integer> checkClients(@RequestBody MapClientEntity entity) {
		return clientService.checkClients(entity);
	}

	@RequestMapping(value = "/clientmanagement/sendStatus", method = RequestMethod.POST)
	@ResponseBody
	public void sendStatus(@RequestBody ClientDto entity, Principal p) {
		clientService.sendStatus(entity, p.getName());
	}

	@RequestMapping(value = "/clientmanagement/createOrderTimeline")
	@ResponseBody
	public List<TimelineDto> createOrderTimeline(@RequestParam int id) {
		return clientService.createOrderTimeline(id);
	}

	@RequestMapping(value = "/clientmanagement/createPastOrderTimeline")
	@ResponseBody
	public List<TimelineDto> createPastOrderTimeline(@RequestParam int id) {
		return clientService.createPastOrderTimeline(id);
	}

	@RequestMapping(value = "/clientmanagement/findAllClientsByOfficeId")
	@ResponseBody
	public List<ClientDto> findAllClientsByOfficeId(Principal p) {
		return clientService.findAllClientsByOfficeId(p.getName());
	}
}
