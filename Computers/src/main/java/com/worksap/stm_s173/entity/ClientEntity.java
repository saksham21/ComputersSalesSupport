package com.worksap.stm_s173.entity;

import java.util.List;

import com.worksap.stm_s173.dto.ClientDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientEntity {
	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private List<ClientDto> clientEntities;
}
