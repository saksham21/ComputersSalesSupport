package com.worksap.stm_s173.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

	@RequestMapping(value = { "welcome" , ""}, method = RequestMethod.GET)
	public String welcomePage(Model model, HttpServletRequest request) {
		if (request.isUserInRole("Director") || request.isUserInRole("Manager")) {
			return "redirect:dashboard";
		}
		else if(request.isUserInRole("Representative")) {
			return "redirect:clientmanagement";
		}
		else {
			return "redirect:login";
		}
	}
}
