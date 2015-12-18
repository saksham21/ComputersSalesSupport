package com.worksap.stm_s173.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientExistingDto {

	private int id;
	private String name;
	private String contact;
	private String address;
	private String domain;
	private long last_order_time;
	private int num_orders;
}
