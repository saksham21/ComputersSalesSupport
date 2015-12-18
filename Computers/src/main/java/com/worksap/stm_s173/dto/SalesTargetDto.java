package com.worksap.stm_s173.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalesTargetDto {

	private int id;
	private String name;
	private int past_emp;
	private int current_emp;
	private int past_sales;

}
