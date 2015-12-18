package com.worksap.stm_s173.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAccountCreationEntity {

	private String password;
	private String firstname;
	private String lastname;
	private String username;
	private String role;
	private int officename;
	private String email;
	private int id;

}
