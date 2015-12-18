package com.worksap.stm_s173.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartProductDto {

	private int client_id;
	private int product_id;
	private int quantity;

}
