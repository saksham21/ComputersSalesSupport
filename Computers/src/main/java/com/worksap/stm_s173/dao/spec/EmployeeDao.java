package com.worksap.stm_s173.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm_s173.dto.EmployeeDto;


public interface EmployeeDao {

	List<EmployeeDto> getBy(int id, int start, int size) throws IOException;
	
	List<EmployeeDto> getRep(int id) throws IOException;
	
	EmployeeDto getEmployee(int id) throws IOException; 
	
	EmployeeDto getEmployee(String name) throws IOException; 
	
	int getTotalCount(int id) throws IOException;
	
	void addEmployee(EmployeeDto employee) throws IOException ;
	
	void updateEmployee(EmployeeDto employee) throws IOException;
	
	void updateEmployeeList(List<EmployeeDto> employee) throws IOException;
	
	void deleteEmployee(int id) throws IOException;

	EmployeeDto getManager(int officeId)  throws IOException;

	void updateConvertedClientsCount(int id) throws IOException;

	void updateClientsToMeetCount(int id) throws IOException;

	int getTotalCount(int id, String string) throws IOException;

	List<EmployeeDto> getUnderEmployees(String role, int id) throws IOException;

}
