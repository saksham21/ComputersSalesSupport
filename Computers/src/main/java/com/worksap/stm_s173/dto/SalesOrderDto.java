package com.worksap.stm_s173.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalesOrderDto {

	private int id;
	private int order_id;
	private int product_id;
	private Double price;
	private int quantity;
	private int emp_id;
	private int client_id;
	private int enabled;
	private String status;

}
