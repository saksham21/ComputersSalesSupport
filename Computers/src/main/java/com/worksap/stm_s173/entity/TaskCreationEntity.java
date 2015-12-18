package com.worksap.stm_s173.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskCreationEntity {

	private String title;
	private String creator;
	private String description;
	private String reminder;
	private long start;
	private long end;

}
