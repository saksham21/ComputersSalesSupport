package com.worksap.stm_s173.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardEntity {

	private int monthly_sales;
	private int annual_sales;
	private int assigned_monthly_sales;
	private int assigned_annual_sales;
	private int average_sale_order;
	private int leads_generated;
	private int active_leads;
	private int representatives;
}
