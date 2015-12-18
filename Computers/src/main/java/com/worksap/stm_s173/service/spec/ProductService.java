package com.worksap.stm_s173.service.spec;

import com.worksap.stm_s173.dto.ProductDto;
import com.worksap.stm_s173.entity.ProductEntity;
import com.worksap.stm_s173.entity.ProductFetchEntity;
import com.worksap.stm_s173.exception.ServiceException;

public interface ProductService {

	void insert(ProductDto productCreationEntity) throws ServiceException;

	ProductEntity getAll(ProductFetchEntity productFetchEntity)
			throws ServiceException;

	void update(ProductDto productCreationEntity) throws ServiceException;

	void deleteById(int id) throws ServiceException;

	ProductEntity getAll() throws ServiceException;
}
