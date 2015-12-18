package com.worksap.stm_s173.entity;

import java.util.List;

import com.worksap.stm_s173.dto.EmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAccountEntity {
	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private List<EmployeeDto> employeeAccountEntities;
	private List<EmployeeDto> recommendedEmp;
}