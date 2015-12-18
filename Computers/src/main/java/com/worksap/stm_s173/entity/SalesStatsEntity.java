package com.worksap.stm_s173.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalesStatsEntity {

	private int id;
	private String name;
	private int monthly_sales;
	private int annual_sales;
	private int assigned_monthly_sales;
	private int assigned_annual_sales;
}
