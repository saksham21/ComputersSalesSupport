package com.worksap.stm_s173.service.spec;

import java.util.List;

import com.worksap.stm_s173.dto.EmployeeDto;
import com.worksap.stm_s173.entity.EmployeeAccountCreationEntity;
import com.worksap.stm_s173.entity.EmployeeAccountFetchEntity;
import com.worksap.stm_s173.entity.EmployeeAccountEntity;
import com.worksap.stm_s173.exception.ServiceException;

public interface EmployeeService {

	void insert(EmployeeAccountCreationEntity employeeAccountCreationEntity)
			throws ServiceException;

	EmployeeAccountEntity getBy(EmployeeAccountFetchEntity entity)
			throws ServiceException;

	int getId(String name) throws ServiceException;

	List<EmployeeDto> getRep(int id) throws ServiceException;

	void updateEmpList(List<EmployeeDto> emp) throws ServiceException;

	String findRole(String name) throws ServiceException;

	int findId(String name) throws ServiceException;

	void deleteBy(int id) throws ServiceException;

	void update(EmployeeAccountCreationEntity employeeAccountCreationEntity)
			throws ServiceException;

	EmployeeAccountEntity getByEmpId(EmployeeAccountFetchEntity entity,
			String name) throws ServiceException;

	EmployeeDto getEmp(String employeename) throws ServiceException;

	void updateEmp(EmployeeDto emp) throws ServiceException;

	List<EmployeeDto> getUnderEmployees(String name) throws ServiceException;

}
