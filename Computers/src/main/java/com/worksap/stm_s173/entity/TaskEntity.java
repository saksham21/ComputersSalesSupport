package com.worksap.stm_s173.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.worksap.stm_s173.dto.TaskDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskEntity {
	private List<TaskDto> taskEntities;
}
