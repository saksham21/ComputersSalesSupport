package com.worksap.stm_s173.dto;

import com.worksap.stm_s173.entity.EmployeeAccountCreationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDto {

	private int id;
	private String firstname;
	private String lastname;
	private String email;
	private String role;
	private int officename;
	private String username;
	private String password;
	private int enabled;
	private int clients_to_meet;
	private int converted_clients;

	public EmployeeDto(
			EmployeeAccountCreationEntity employeeAccountCreationEntity) {
		this.firstname = employeeAccountCreationEntity.getFirstname();
		this.lastname = employeeAccountCreationEntity.getLastname();
		this.role = employeeAccountCreationEntity.getRole();
		this.username = employeeAccountCreationEntity.getUsername();
		this.password = employeeAccountCreationEntity.getPassword();
		this.officename = employeeAccountCreationEntity.getOfficename();
		this.email = employeeAccountCreationEntity.getEmail();
		this.id = employeeAccountCreationEntity.getId();
	}

}
