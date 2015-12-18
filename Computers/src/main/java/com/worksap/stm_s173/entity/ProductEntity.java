package com.worksap.stm_s173.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.worksap.stm_s173.dto.ProductDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductEntity {

	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	List<ProductDto> productEntities;
}
