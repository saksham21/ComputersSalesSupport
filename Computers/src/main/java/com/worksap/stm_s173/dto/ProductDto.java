package com.worksap.stm_s173.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {

	private int id;
	private String company;
	private double price;
	private String name;
	private String description;
	private int enabled;

}
