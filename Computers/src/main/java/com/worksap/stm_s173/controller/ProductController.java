package com.worksap.stm_s173.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.worksap.stm_s173.dto.ProductDto;
import com.worksap.stm_s173.entity.ProductEntity;
import com.worksap.stm_s173.entity.ProductFetchEntity;
import com.worksap.stm_s173.service.spec.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/product/addProduct", method = RequestMethod.POST)
	@ResponseBody
	public void addNewProduct(@RequestBody ProductDto productCreationEntity) {
		productService.insert(productCreationEntity);
	}
	
	@RequestMapping(value = "/product/displayProducts", method = RequestMethod.POST)
	@ResponseBody
	public ProductEntity displayProduct(@RequestBody ProductFetchEntity productFetchEntity) {
		return productService.getAll(productFetchEntity);
	}
	
	@RequestMapping(value = "/product/getAllProducts", method = RequestMethod.POST)
	@ResponseBody
	public ProductEntity getAllProducts() {
		return productService.getAll();
	}
	
	@RequestMapping(value = "/product/updateProduct", method = RequestMethod.POST)
	@ResponseBody
	public void updateProduct(@RequestBody ProductDto productCreationEntity) {
		productService.update(productCreationEntity);
	}
	
	@RequestMapping(value = "/product/deleteProduct", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteProduct(@RequestParam int id) {
		productService.deleteById(id);
	}
	
}
