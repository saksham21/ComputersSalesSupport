package com.worksap.stm_s173.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCreationEntity {

	private int id;
	private String name;
	private String company;
	private String types;
	private double price;
	private String description;

}
