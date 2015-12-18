package com.worksap.stm_s173.service.spec;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.worksap.stm_s173.dto.ClientDto;
import com.worksap.stm_s173.dto.TimelineDto;
import com.worksap.stm_s173.entity.ClientEntity;
import com.worksap.stm_s173.entity.ClientExistingEntity;
import com.worksap.stm_s173.entity.ClientsCreationEntity;
import com.worksap.stm_s173.entity.ClientsFetchEntity;
import com.worksap.stm_s173.entity.MapClientEntity;
import com.worksap.stm_s173.exception.ServiceException;

public interface ClientService {

	void insert(ClientsCreationEntity clientsCreationEntity, String name);

	ClientEntity getBy(ClientsFetchEntity entity, String name)
			throws ServiceException;

	ClientEntity getBy1(ClientsFetchEntity entity, String name)
			throws ServiceException;

	ClientExistingEntity getBy3(ClientsFetchEntity entity, String name,
			HttpServletRequest request) throws ServiceException;

	int getOfficeId(int client_id) throws ServiceException;

	List<Integer> checkClients(MapClientEntity entity) throws ServiceException;

	void assign(ClientDto entity, String name) throws ServiceException;

	void sendStatus(ClientDto entity, String name) throws ServiceException;

	List<TimelineDto> createOrderTimeline(int id) throws ServiceException;

	List<TimelineDto> createPastOrderTimeline(int id) throws ServiceException;

	List<ClientDto> findAllClientsByOfficeId(String name)
			throws ServiceException;

	void assignAll(ClientsFetchEntity entity, String name)
			throws ServiceException;
}
