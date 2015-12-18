package com.worksap.stm_s173.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OfficeSalesRecordDto {

	private int id;
	private int month;
	private int year;
	private int target_set;
	private int target_achieved;
	private int officeid;
	private int emp_count;

}
