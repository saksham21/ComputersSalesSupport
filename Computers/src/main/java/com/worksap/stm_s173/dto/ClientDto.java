package com.worksap.stm_s173.dto;

import org.springframework.expression.spel.CodeFlow.ClinitAdder;

import com.worksap.stm_s173.entity.ClientsCreationEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientDto {

	private int id;
	private String name;
	private String contact;
	private String address;
	private String status;
	private String domain;
	private int employeeid;
	private String employeename;
	private int officeid;
	private String placeid;
	private int enabled;
	private String description;
	private double lat;
	private double lng;

	public ClientDto(ClientsCreationEntity clientsCreationEntity) {
		this.name = clientsCreationEntity.getName();
		this.contact = clientsCreationEntity.getContact();
		this.address = clientsCreationEntity.getAddress();
		this.status = clientsCreationEntity.getStatus();
		this.domain = clientsCreationEntity.getDomain();
		this.employeeid = clientsCreationEntity.getEmployeeid();
		this.employeename = clientsCreationEntity.getEmployeename();
		this.placeid = clientsCreationEntity.getPlaceid();
		this.enabled = clientsCreationEntity.getEnabled();
		this.lat = clientsCreationEntity.getLat();
		this.lng = clientsCreationEntity.getLng();
	}

}
