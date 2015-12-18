package com.worksap.stm_s173.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalesCreationEntity {

	private int id; // clientid
	private String status;
	private List<Integer> name;
	private List<Integer> quantity;
	private List<Double> price;

}
