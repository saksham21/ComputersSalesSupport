package com.worksap.stm_s173.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm_s173.dto.OfficeDto;


public interface OfficeDao {

	void insert(String name) throws IOException;
	
	void deleteBy(int id) throws IOException;
	
	int getTotalCount() throws IOException;
	
	OfficeDto getBy(int officeId) throws IOException;
	
	List<OfficeDto> getAll(int start, int size) throws IOException;
	
	List<OfficeDto> getAll() throws IOException;
	
}
