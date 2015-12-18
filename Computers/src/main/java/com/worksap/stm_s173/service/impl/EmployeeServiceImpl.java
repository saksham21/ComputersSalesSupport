package com.worksap.stm_s173.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worksap.stm_s173.dao.spec.EmployeeDao;
import com.worksap.stm_s173.dto.EmployeeDto;
import com.worksap.stm_s173.entity.EmployeeAccountCreationEntity;
import com.worksap.stm_s173.entity.EmployeeAccountFetchEntity;
import com.worksap.stm_s173.entity.EmployeeAccountEntity;
import com.worksap.stm_s173.exception.ServiceException;
import com.worksap.stm_s173.service.spec.ClientService;
import com.worksap.stm_s173.service.spec.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private int i = 0;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private ClientService clientService;

	@Transactional
	public void insert(
			EmployeeAccountCreationEntity employeeAccountCreationEntity)
			throws ServiceException {

		EmployeeDto employeeAccount = new EmployeeDto(
				employeeAccountCreationEntity);
		try {
			employeeDao.addEmployee(employeeAccount);
		} catch (IOException e) {
			throw new ServiceException("Cannot add user account for userName: "
					+ employeeAccountCreationEntity.getUsername(), e);
		}

	}

	@Override
	public void update(
			EmployeeAccountCreationEntity employeeAccountCreationEntity) {
		EmployeeDto emp = new EmployeeDto(employeeAccountCreationEntity);
		try {
			employeeDao.updateEmployee(emp);

		} catch (IOException e) {
			throw new ServiceException(
					"Cannot update user account for userName: "
							+ employeeAccountCreationEntity.getUsername(), e);
		}
	}

	public EmployeeAccountEntity getBy(EmployeeAccountFetchEntity entity)
			throws ServiceException {
		List<EmployeeDto> accounts = null;
		int employeeCountById;
		try {
			employeeCountById = employeeDao.getTotalCount(entity.getOfficeId());
			accounts = employeeDao.getBy(entity.getOfficeId(),
					entity.getStart(), entity.getLength());
		} catch (IOException e) {
			throw new ServiceException("Cannot find user for Id: "
					+ entity.getOfficeId(), e);
		}

		return new EmployeeAccountEntity(entity.getDraw(), employeeCountById,
				employeeCountById, accounts, null);
	}

	@Override
	public int getId(String name) throws ServiceException {
		int id = 0;
		try {
			id = (employeeDao.getEmployee(name)).getOfficename();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public List<EmployeeDto> getRep(int id) throws ServiceException {
		List<EmployeeDto> accounts = null;
		try {
			accounts = employeeDao.getRep(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public void updateEmpList(List<EmployeeDto> emp) throws ServiceException {
		try {
			employeeDao.updateEmployeeList(emp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String findRole(String name) throws ServiceException {
		try {
			return (employeeDao.getEmployee(name)).getRole();
		} catch (IOException e) {
			throw new ServiceException("Cannot add user account for name: "
					+ name, e);
		}
	}

	@Override
	public int findId(String name) throws ServiceException {
		try {
			return (employeeDao.getEmployee(name)).getId();
		} catch (IOException e) {
			throw new ServiceException("Cannot add user account for name: "
					+ name, e);
		}
	}

	@Override
	public void deleteBy(int id) throws ServiceException {
		try {
			employeeDao.deleteEmployee(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot add user account for id: " + id,
					e);
		}

	}

	private List<EmployeeDto> getRecEmp(int officename) {
		List<EmployeeDto> accounts = null;
		List<EmployeeDto> account = new ArrayList<EmployeeDto>();
		try {
			accounts = employeeDao.getRep(officename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int len = accounts.size();
		int[] a = new int[len];

		for (int i = 0; i < len; i++) {
			a[i] = -1;
		}
		for (int i = 0; i < 3; i++) {
			int min = 9999999;
			int index = -1;
			for (int j = 0; j < len; j++) {
				if (a[j] == -1 && accounts.get(j).getClients_to_meet() < 8
						&& -accounts.get(j).getConverted_clients() <= min) {
					min = -accounts.get(j).getConverted_clients();
					index = j;
				}
			}
			if (index != -1) {
				a[index] = 1;
				account.add(accounts.get(index));
			}
		}
		return account;
	}

	@Override
	public EmployeeAccountEntity getByEmpId(EmployeeAccountFetchEntity entity,
			String name) throws ServiceException {
		int employeeCountById = 0;
		List<EmployeeDto> officeAccounts = new ArrayList<EmployeeDto>();
		List<EmployeeDto> accounts = new ArrayList<EmployeeDto>();
		EmployeeDto emp = new EmployeeDto();
		try {
			emp = employeeDao.getEmployee(name);
			entity.setOfficeId(emp.getOfficename());
			officeAccounts = employeeDao.getRep(entity.getOfficeId());
			for (i = 0; i < officeAccounts.size(); i++) {
				employeeDao.updateConvertedClientsCount(officeAccounts.get(i)
						.getId());
				employeeDao.updateClientsToMeetCount(officeAccounts.get(i)
						.getId());
			}
			employeeCountById = employeeDao.getTotalCount(entity.getOfficeId());
			accounts = employeeDao.getBy(entity.getOfficeId(), 0,
					employeeCountById);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<EmployeeDto> recEmp = getRecEmp(emp.getOfficename());
		return new EmployeeAccountEntity(0, employeeCountById,
				employeeCountById, accounts, recEmp);

	}

	@Override
	public EmployeeDto getEmp(String employeename) throws ServiceException {
		EmployeeDto emp = null;
		try {
			emp = employeeDao.getEmployee(employeename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return emp;
	}

	@Override
	public void updateEmp(EmployeeDto emp) throws ServiceException {
		try {
			employeeDao.updateEmployee(emp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<EmployeeDto> getUnderEmployees(String name)
			throws ServiceException {
		EmployeeDto employeeDto = new EmployeeDto();
		String role = "";
		int id = 0;
		List<EmployeeDto> accounts = new ArrayList<EmployeeDto>();
		try {
			employeeDto = employeeDao.getEmployee(name);
			id = employeeDto.getOfficename();
			role = employeeDto.getRole();
			accounts = employeeDao.getUnderEmployees(role, id);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return accounts;
	}

}
