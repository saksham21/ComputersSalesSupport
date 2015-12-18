package com.worksap.stm_s173.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worksap.stm_s173.dao.spec.OfficeDao;
import com.worksap.stm_s173.dto.OfficeDto;
import com.worksap.stm_s173.entity.OfficeEntity;
import com.worksap.stm_s173.entity.OfficeFetchEntity;
import com.worksap.stm_s173.exception.ServiceException;
import com.worksap.stm_s173.service.spec.OfficeService;

@Service
public class OfficeServiceImpl implements OfficeService {

	@Autowired
	private OfficeDao officeDao;

	public String getBy(int officeId) throws ServiceException {
		OfficeDto office = null;
		try {
			office = officeDao.getBy(officeId);
		} catch (IOException e) {
			throw new ServiceException("Cannot find office for office Id: "
					+ officeId, e);
		}
		String name = null;
		if (office != null) {
			name = office.getName();
		}
		return name;
	}

	public void insert(String name) throws ServiceException {
		try {
			/* System.out.println("In OfficeServiceImpl, here name: " + name); */
			officeDao.insert(name);
		} catch (IOException e) {
			throw new ServiceException("Could not add office with name: "
					+ name, e);
		}
	}

	public void deleteBy(int id) throws ServiceException {
		try {
			officeDao.deleteBy(id);
		} catch (IOException e) {
			throw new ServiceException(
					"Could not delete office with id: " + id, e);
		}
	}

	public OfficeEntity getBy(OfficeFetchEntity entity) throws ServiceException {
		try {
			int officeSize = officeDao.getTotalCount();
			/* System.out.println("size: " + officeSize); */
			List<OfficeDto> officeData = officeDao.getAll(entity.getStart(),
					entity.getLength());
			/* System.out.println(officeData.size()); */
			return new OfficeEntity(entity.getDraw(), officeSize, officeSize,
					officeData);
		} catch (IOException e) {
			/* System.out.println("gchcdsdvfsdg"); */
			throw new ServiceException("Could not search offices", e);
		}
	}

}
