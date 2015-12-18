package com.worksap.stm_s173.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ClientsFetchEntity {

	private String status;
	private int draw;
	private int start;
	private int length;
}
