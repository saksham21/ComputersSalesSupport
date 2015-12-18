package com.worksap.stm_s173.service.spec;

import com.worksap.stm_s173.entity.OfficeEntity;
import com.worksap.stm_s173.entity.OfficeFetchEntity;
import com.worksap.stm_s173.exception.ServiceException;

public interface OfficeService {

	String getBy(int officeId) throws ServiceException;

	void insert(String name) throws ServiceException;

	void deleteBy(int id) throws ServiceException;

	OfficeEntity getBy(OfficeFetchEntity entity) throws ServiceException;
}
