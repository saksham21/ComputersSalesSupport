package com.worksap.stm_s173.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;









import com.worksap.stm_s173.dto.EmployeeDto;
import com.worksap.stm_s173.entity.EmployeeAccountCreationEntity;
import com.worksap.stm_s173.entity.EmployeeAccountFetchEntity;
import com.worksap.stm_s173.entity.EmployeeAccountEntity;
import com.worksap.stm_s173.service.spec.EmployeeService;

@Controller
public class EmployeeManagementController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "/employeemanagement/addUserAccount", method = RequestMethod.POST)
	@ResponseBody
	public void addUserAccount(@RequestBody EmployeeAccountCreationEntity employeeAccountCreationEntity) {
		employeeService.insert(employeeAccountCreationEntity);
	}

	@RequestMapping(value = "/employeemanagement/updateUserAccount", method = RequestMethod.POST)
	@ResponseBody
	public void updateUserAccount(@RequestBody EmployeeAccountCreationEntity employeeAccountCreationEntity) {
		employeeService.update(employeeAccountCreationEntity);
	}

	
	@RequestMapping(value = "/employeemanagement/findUserByOfficeId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public EmployeeAccountEntity findUserByOfficeId(@RequestBody EmployeeAccountFetchEntity entity) {
	//	System.out.println(entity);
		return employeeService.getBy(entity);
	}
	
	@RequestMapping(value = "/employeemanagement/findUserByEmpId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public EmployeeAccountEntity findUserByClientId(@RequestBody EmployeeAccountFetchEntity entity, Principal p) {
		//System.out.println(p.getName());
		return employeeService.getByEmpId(entity,p.getName());
	}
	
	
	@RequestMapping(value = "/employeemanagement/deleteUserAccount", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUserAccount(@RequestParam int id) {
		employeeService.deleteBy(id);
	}
	
	@RequestMapping(value = "/employeemanagement/getUnderEmployees", method = RequestMethod.POST)
	@ResponseBody
	public List<EmployeeDto> getUnderEmployees(Principal p) {
		return employeeService.getUnderEmployees(p.getName());
	}
	
}
