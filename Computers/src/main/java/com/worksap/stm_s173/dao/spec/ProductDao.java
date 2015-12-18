package com.worksap.stm_s173.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm_s173.dto.ProductDto;

public interface ProductDao {
	
	void addProduct(ProductDto product) throws IOException;

	int getAllCount() throws IOException;

	List<ProductDto> getBy(int start, int length) throws IOException;

	void deleteEmployee(int id) throws IOException;

	void updateEmployee(ProductDto product) throws IOException;

	List<ProductDto> getBy() throws IOException;
}
