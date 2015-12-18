package com.worksap.stm_s173.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDto {

	private int id;
	private int client_id;
	private String title;
	private String creator;
	private String description;
	private long reminder;
	private long start;
	private long end;
	private int enabled;

}
