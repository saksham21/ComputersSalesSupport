package com.worksap.stm_s173.dao.spec;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.worksap.stm_s173.dto.CartProductDto;
import com.worksap.stm_s173.dto.OfficeSalesRecordDto;
import com.worksap.stm_s173.dto.OwnIntPairDto;
import com.worksap.stm_s173.dto.ProductDto;
import com.worksap.stm_s173.dto.SalesTargetDto;
import com.worksap.stm_s173.entity.SalesCreationEntity;

public interface SalesDao {

	void insertAllProducts(SalesCreationEntity salesCreationEntity,
			int order_id, int emp_id, long ts) throws IOException;

	int getOrderId() throws IOException;

	List<ProductDto> getRecBasedOnDomain(String domain) throws IOException;

	List<ProductDto> getRecBasedOnSearch(List<String> a, double start,
			double end, String brand) throws IOException;

	void addNewOrder(int order_id, int emp_id) throws IOException;

	int checkOfficeTargetSet(int id, int mnth, int year) throws IOException;

	void insertSalesTarget(int id, int mnth, int year, int emp_count)
			throws IOException;

	OfficeSalesRecordDto getRecord(int id, int mnth, int i) throws IOException;

	int getMonthlySales(int month, int year, HttpServletRequest request, int id)
			throws IOException;

	int getAnnualSales(int month, int year, HttpServletRequest request, int id)
			throws IOException;

	int getAssignedAnnualSales(int month, int year, HttpServletRequest request,
			int id) throws IOException;

	int getAssignedMonthlySales(int mnth, int year, HttpServletRequest request,
			int id) throws IOException;

	void setSalesTarget(List<SalesTargetDto> salesTargetDto, int mnth, int year)
			throws IOException;

	int getMonthlySalesDirectorById(int month, int year, int id)
			throws IOException;

	int getAnnualSalesDirectorById(int month, int year, int id)
			throws IOException;

	int getAssignedMonthlySalesDirectorById(int month, int year, int id)
			throws IOException;

	int getAssignedAnnualSalesDirectorById(int month, int year, int id)
			throws IOException;

	List<OwnIntPairDto> getAverageSaleOrderByOffice(int id) throws IOException;

	void updateEmployeeNumber(int id, int mnth, int year, int emp_count)
			throws IOException;

	void updateMonthlySales(int value, int a, int b, int c) throws IOException;

	void addProductInCart(CartProductDto cartProductDto) throws IOException;

	List<CartProductDto> getAllProductsFromCart(CartProductDto cartProductDto)
			throws IOException;

	void deleteProductFromCart(CartProductDto cartProductDto)
			throws IOException;

	void updateQuantity(CartProductDto cartProductDto) throws IOException;

	void emptyCartUsingClientId(int id) throws IOException;

	int getActiveLeads(HttpServletRequest request, int office_id)
			throws IOException;

	int getRepresentatives(HttpServletRequest request, int officeid)
			throws IOException;

	int getLeads_generated(HttpServletRequest request, int office_id)
			throws IOException;

}
