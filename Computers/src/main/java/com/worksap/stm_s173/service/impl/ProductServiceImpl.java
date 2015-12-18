package com.worksap.stm_s173.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.worksap.stm_s173.dao.spec.ProductDao;
import com.worksap.stm_s173.dto.ProductDto;
import com.worksap.stm_s173.entity.ProductCreationEntity;
import com.worksap.stm_s173.entity.ProductEntity;
import com.worksap.stm_s173.entity.ProductFetchEntity;
import com.worksap.stm_s173.exception.ServiceException;
import com.worksap.stm_s173.service.spec.ProductService;

public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public void insert(ProductDto productCreationEntity)
			throws ServiceException {

		// ProductDto product = new ProductDto(productCreationEntity);
		try {
			productDao.addProduct(productCreationEntity);
		} catch (IOException e) {
			throw new ServiceException("Cannot add new Product with name: "
					+ productCreationEntity.getName(), e);
		}
	}

	@Override
	public ProductEntity getAll(ProductFetchEntity entity)
			throws ServiceException {

		List<ProductDto> products = null;
		int no_products = 0;
		try {
			no_products = productDao.getAllCount();
			products = productDao.getBy(entity.getStart(), entity.getLength());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ProductEntity(entity.getDraw(), no_products, no_products,
				products);
	}

	@Override
	public void update(ProductDto productCreationEntity)
			throws ServiceException {
		// ProductDto product = new ProductDto(productCreationEntity);
		try {
			productDao.updateEmployee(productCreationEntity);

		} catch (IOException e) {
			throw new ServiceException("Cannot update product of Id: "
					+ productCreationEntity.getId(), e);
		}
	}

	@Override
	public void deleteById(int id) throws ServiceException {
		try {
			productDao.deleteEmployee(id);
		} catch (IOException e) {
			throw new ServiceException("Cannot add user account for id: " + id,
					e);
		}

	}

	@Override
	public ProductEntity getAll() throws ServiceException {
		List<ProductDto> products = null;
		int no_products = 0;
		try {
			no_products = productDao.getAllCount();
			products = productDao.getBy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ProductEntity(0, no_products, no_products, products);
	}

}
