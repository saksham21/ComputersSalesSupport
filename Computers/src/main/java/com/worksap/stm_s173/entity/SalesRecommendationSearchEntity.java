package com.worksap.stm_s173.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalesRecommendationSearchEntity {

	private double start;
	private double end;
	private String ram;
	private String processor;
	private String storage;
	private String brand;

}
