package com.worksap.stm_s173.service.spec;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.worksap.stm_s173.dto.CartProductDto;
import com.worksap.stm_s173.dto.ProductDto;
import com.worksap.stm_s173.dto.SalesTargetDto;
import com.worksap.stm_s173.entity.SalesCreationEntity;
import com.worksap.stm_s173.entity.SalesRecommendationSearchEntity;
import com.worksap.stm_s173.entity.SalesStatsEntity;
import com.worksap.stm_s173.exception.ServiceException;

public interface SalesService {

	void insertProductDetails(SalesCreationEntity salesCreationEntity,
			String name) throws ServiceException;

	List<ProductDto> getRecBasedOnDomain(String domain) throws ServiceException;

	List<ProductDto> getRecBasedOnSearch(SalesRecommendationSearchEntity entity)
			throws ServiceException;

	List<SalesTargetDto> addSalesTarget() throws ServiceException;

	int getMonthlySales(HttpServletRequest request, String string)
			throws ServiceException;

	int getAssignedAnnualSales(HttpServletRequest request, String string)
			throws ServiceException;

	int getAssignedMonthlySales(HttpServletRequest request, String string)
			throws ServiceException;

	int getAnnualSales(HttpServletRequest request, String string)
			throws ServiceException;

	void setSalesTarget(List<SalesTargetDto> salesTargetDto)
			throws ServiceException;

	List<SalesStatsEntity> getAllOfficeStats() throws ServiceException;

	int getAverageSaleOrder(HttpServletRequest request, String name)
			throws ServiceException;

	void addProductInCart(CartProductDto cartProductDto)
			throws ServiceException;

	List<CartProductDto> getAllProductsFromCart(CartProductDto cartProductDto)
			throws ServiceException;

	void deleteProductFromCart(CartProductDto cartProductDto)
			throws ServiceException;

	void updateQuantity(CartProductDto cartProductDto) throws ServiceException;

	int getActiveLeads(HttpServletRequest request, String name)
			throws ServiceException;

	int getRepresentatives(HttpServletRequest request, String name)
			throws ServiceException;

	int getLeads_generated(HttpServletRequest request, String name)
			throws ServiceException;

}
