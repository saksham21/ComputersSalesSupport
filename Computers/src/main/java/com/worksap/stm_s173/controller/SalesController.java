package com.worksap.stm_s173.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.worksap.stm_s173.dto.CartProductDto;
import com.worksap.stm_s173.dto.ProductDto;
import com.worksap.stm_s173.dto.SalesTargetDto;
import com.worksap.stm_s173.entity.DashboardEntity;
import com.worksap.stm_s173.entity.SalesCreationEntity;
import com.worksap.stm_s173.entity.SalesRecommendationSearchEntity;
import com.worksap.stm_s173.entity.SalesStatsEntity;
import com.worksap.stm_s173.service.spec.SalesService;

@Controller
public class SalesController {
	
	@Autowired
	private SalesService salesService;
	
	@RequestMapping(value = "/salesmanagement", method = RequestMethod.GET)
	public String sales() {
		return "sales";
	}
	
	@RequestMapping(value = "/salesmanagement/addNewOrder", method = RequestMethod.POST)
	@ResponseBody
	public void addNewOrder(@RequestBody SalesCreationEntity salesCreationEntity, Principal p) {
		salesService.insertProductDetails(salesCreationEntity, p.getName());
	}
	
	@RequestMapping(value = "/salesmanagement/getRecDomainBased")
	@ResponseBody
	public List<ProductDto> getRecBasedOnDomain(@RequestParam String domain){
		return salesService.getRecBasedOnDomain(domain);
	}
	
	@RequestMapping(value = "/salesmanagement/getRecSearchBased", method = RequestMethod.POST)
	@ResponseBody
	public List<ProductDto> getRecBasedOnSearch(@RequestBody SalesRecommendationSearchEntity salesRecommendationSearchEntity){
		return salesService.getRecBasedOnSearch(salesRecommendationSearchEntity);
	}
	
	@RequestMapping(value = "/salesmanagement/addProductInCart", method = RequestMethod.POST)
	@ResponseBody
	public void addProductInCart(@RequestBody CartProductDto cartProductDto){
		salesService.addProductInCart(cartProductDto);
	}
	
	@RequestMapping(value = "/salesmanagement/deleteProductFromCart", method = RequestMethod.POST)
	@ResponseBody
	public void deleteProductFromCart(@RequestBody CartProductDto cartProductDto){
		salesService.deleteProductFromCart(cartProductDto);
	}
	
	@RequestMapping(value = "/salesmanagement/updateQuantity", method = RequestMethod.POST)
	@ResponseBody
	public void updateQuantity(@RequestBody CartProductDto cartProductDto){
		salesService.updateQuantity(cartProductDto);
	}
		
	@RequestMapping(value = "/salesmanagement/getAllProductsFromCart", method = RequestMethod.POST)
	@ResponseBody
	public List<CartProductDto> getAllProductsFromCart(@RequestBody CartProductDto cartProductDto){
		return salesService.getAllProductsFromCart(cartProductDto);
	}
	
	
	@RequestMapping(value = "/addSalesTarget", method = RequestMethod.POST)
	@ResponseBody
	public List <SalesTargetDto> addSalesTarget(){
		return salesService.addSalesTarget();
	}
	
	
	@RequestMapping(value = "/getAllRequiredValues", method = RequestMethod.POST)
	@ResponseBody
	public DashboardEntity getAllRequiredValues(HttpServletRequest request, Principal p){
		DashboardEntity entity = new DashboardEntity();
		entity.setMonthly_sales(salesService.getMonthlySales(request, p.getName()));
		entity.setAnnual_sales(salesService.getAnnualSales(request, p.getName()));
		entity.setAssigned_monthly_sales(salesService.getAssignedMonthlySales(request, p.getName()));
		entity.setAssigned_annual_sales(salesService.getAssignedAnnualSales(request, p.getName()));
		entity.setAverage_sale_order(salesService.getAverageSaleOrder(request, p.getName()));
		entity.setActive_leads(salesService.getActiveLeads(request,p.getName()));
		entity.setRepresentatives(salesService.getRepresentatives(request,p.getName()));
		entity.setLeads_generated(salesService.getLeads_generated(request,p.getName()));
		return entity;
	}
	
	@RequestMapping(value = "/setSalesTarget", method = RequestMethod.POST)
	@ResponseBody
	public void setSalesTarget(@RequestBody List<SalesTargetDto> salesTargetDto ){
		salesService.setSalesTarget(salesTargetDto);
	}
	
	@RequestMapping(value = "/getAllOfficeStats", method = RequestMethod.POST)
	@ResponseBody
	public List<SalesStatsEntity> getAllOfficeStats(){
		return salesService.getAllOfficeStats();
	}
	
	
}
