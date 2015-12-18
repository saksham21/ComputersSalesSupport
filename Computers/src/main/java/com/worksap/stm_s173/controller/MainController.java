package com.worksap.stm_s173.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping(value = "/dashboard")
	public String loginPage(HttpServletRequest request) {
		if (request.isUserInRole("Representative")) {
			return "client";
		}
		else{
			return "dashboard";
		}
	}

	@RequestMapping(value = "/employeemanagement")
	public String employeeManagement() {
		return "employee";
	}

	@RequestMapping(value = "/maps")
	public String maps() {
		return "maps";
	}

	@RequestMapping(value = "/product")
	public String product() {
		return "product";
	}

}