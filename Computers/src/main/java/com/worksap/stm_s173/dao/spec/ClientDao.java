package com.worksap.stm_s173.dao.spec;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.worksap.stm_s173.dto.ClientDto;
import com.worksap.stm_s173.dto.ClientExistingDto;

public interface ClientDao {
	
	void addClient(ClientDto client, int id) throws IOException;
	public int getCount(String map_id);

	int getAssignedCount(String status, int id, String role, int emp_id);
	int getAssignedCount1(String status, int id, String role, int emp_id);

	List<ClientDto> getBy(String status, int start, int length, int id, String role, int emp_id) throws IOException;
	List<ClientDto> getBy1(String status, int start, int length, int id, String role, int emp_id) throws IOException;
	
	
	//GET client details by his id;
	ClientDto getBy(int client_id) throws IOException;

	void updateToAssigned(int emp_id, ClientDto entity) throws IOException;
	
	void sendStatus(ClientDto entity) throws IOException;
	
	int getAssignedClientsCount(int id) throws IOException;
	
	int getExistingClientCount(String status, int id, HttpServletRequest request, String name) throws IOException;
	
	List<ClientExistingDto> getExistingClients(String status, int id, HttpServletRequest request, String name) throws IOException;
	
	// update client status to existing (till now) through his ID
	void updateStatusByClientID(int id, String string) throws IOException;
	
	List<ClientDto> getExistingClients1(String status, int id) throws IOException;
	
	List<ClientDto> findAllClientsByOfficeId(int id) throws IOException;

	
}
	