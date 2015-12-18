package com.worksap.stm_s173.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientsCreationEntity {

	private String name;
	private String contact;
	private String address;
	private String status;
	private String domain;
	private int employeeid;
	private String employeename;
	private String placeid;
	private int enabled;
	private double lat;
	private double lng;

}
