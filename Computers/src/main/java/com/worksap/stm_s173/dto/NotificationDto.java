package com.worksap.stm_s173.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDto {

	private int id;
	private String title;
	private String description;
	private long reminder_time;
	private String to_who;
	private String from_who;
	private String status;
	private int enabled;
	private int value;

}

// value:
// 0 for assigned, interested, not interested, reassign
// 1 for new order
// 2 for meetings (calendar)