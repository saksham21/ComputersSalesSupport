package com.worksap.stm_s173.dto;

import java.util.Comparator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimelineDto implements Comparator<TimelineDto> {

	private int id;
	private int client_id;
	private String creator;
	private long creation_time;
	private String title;
	private String description;
	private int enabled;
	private int order_id;

	@Override
	public int compare(TimelineDto arg0, TimelineDto arg1) {
		return (int) (arg0.creation_time - arg1.creation_time);
	}

}
