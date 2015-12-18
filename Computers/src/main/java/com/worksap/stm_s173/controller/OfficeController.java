package com.worksap.stm_s173.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.worksap.stm_s173.entity.OfficeCreationEntity;
import com.worksap.stm_s173.entity.OfficeEntity;
import com.worksap.stm_s173.entity.OfficeFetchEntity;
import com.worksap.stm_s173.service.spec.OfficeService;


@Controller
public class OfficeController {
	
	@Autowired
	private OfficeService officeService;
	
	@RequestMapping(value = "/officemanagement")
	public String officemanagement() {
		return "office";
	}
	
	@RequestMapping(value = "/office", method = RequestMethod.POST)
	@ResponseBody
	public void addOffice(@RequestBody OfficeCreationEntity office) {
		officeService.insert(office.getName());
	}
	
	@RequestMapping(value = "/office", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteOffice(@RequestParam int id) {
		// return false if any user in this office or id does not exist
		officeService.deleteBy(id);
	}
	
	@RequestMapping(value = "/offices", method = RequestMethod.POST)
	@ResponseBody
	public OfficeEntity getOffices(
			@RequestBody OfficeFetchEntity officeFetchEntity) {
		return officeService.getBy(officeFetchEntity);
		/*System.out.println(entity);*/
	}
	
}
