package com.worksap.stm_s173.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.worksap.stm_s173.dao.spec.ClientDao;
import com.worksap.stm_s173.dao.spec.EmployeeDao;
import com.worksap.stm_s173.dao.spec.OfficeDao;
import com.worksap.stm_s173.dao.spec.SalesDao;
import com.worksap.stm_s173.dao.spec.TaskDao;
import com.worksap.stm_s173.dao.spec.TimelineDao;
import com.worksap.stm_s173.dto.CartProductDto;
import com.worksap.stm_s173.dto.EmployeeDto;
import com.worksap.stm_s173.dto.NotificationDto;
import com.worksap.stm_s173.dto.OfficeDto;
import com.worksap.stm_s173.dto.OfficeSalesRecordDto;
import com.worksap.stm_s173.dto.OwnIntPairDto;
import com.worksap.stm_s173.dto.ProductDto;
import com.worksap.stm_s173.dto.SalesTargetDto;
import com.worksap.stm_s173.dto.TimelineDto;
import com.worksap.stm_s173.entity.SalesCreationEntity;
import com.worksap.stm_s173.entity.SalesRecommendationSearchEntity;
import com.worksap.stm_s173.entity.SalesStatsEntity;
import com.worksap.stm_s173.exception.ServiceException;
import com.worksap.stm_s173.service.spec.EmployeeService;
import com.worksap.stm_s173.service.spec.SalesService;

public class SalesServiceImpl implements SalesService {

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private SalesDao salesDao;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private TimelineDao timelineDao;

	@Autowired
	private OfficeDao officeDao;

	private int i = 0;

	@Override
	public void insertProductDetails(SalesCreationEntity salesCreationEntity,
			String name) throws ServiceException {
		int order_id = 0, emp_id = 0;
		EmployeeDto emp = employeeService.getEmp(name);
		emp_id = emp.getId();
		employeeService.updateEmp(emp);
		NotificationDto notif = abc(salesCreationEntity, name);
		NotificationDto notif1 = abc(salesCreationEntity, name);
		notif1.setTo_who(name);
		TimelineDto timelineDto = productAddTimeline(salesCreationEntity, name);
		int office_id = 0;
		try {
			office_id = employeeDao.getEmployee(name).getOfficename();
			order_id = salesDao.getOrderId();
			salesDao.addNewOrder(order_id, emp_id);
			timelineDto.setTitle("Order Placed. OrderID:" + order_id);
			salesDao.insertAllProducts(salesCreationEntity, order_id, emp_id,
					(new Date()).getTime() / 1000);
			notif.setDescription("Client: " + salesCreationEntity.getId()
					+ " with orderID:" + order_id);
			notif1.setDescription("Client: " + salesCreationEntity.getId()
					+ " with orderID:" + order_id);
			int value = 0;
			for (int i = 0; i < salesCreationEntity.getPrice().size(); i++) {
				value += (salesCreationEntity.getQuantity().get(i) * salesCreationEntity
						.getPrice().get(i));
			}
			Date date = new Date();
			salesDao.updateMonthlySales(value, office_id, date.getMonth(),
					date.getYear());
			taskDao.addNotification(notif);
			taskDao.addNotification(notif1);
			clientDao.updateStatusByClientID(salesCreationEntity.getId(),
					"EXISTING");
			/*
			 * employeeDao.updateConvertedClientsCount();
			 * employeeDao.updateClientsToMeetCount();
			 */
			timelineDao.addEvent(timelineDto);
			timelineDao.setOrderId(timelineDto.getClient_id(), order_id);
			timelineDao.moveThisTimelineToOld(timelineDto.getClient_id(),
					order_id);
			emptyCartUsingClientId(salesCreationEntity.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void emptyCartUsingClientId(int id) {
		try {
			salesDao.emptyCartUsingClientId(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private TimelineDto productAddTimeline(
			SalesCreationEntity salesCreationEntity, String name) {
		TimelineDto timelineDto = new TimelineDto();
		timelineDto.setClient_id(salesCreationEntity.getId());
		timelineDto.setCreator(name);
		timelineDto.setCreation_time((new Date()).getTime() / 1000);
		// timelineDto.setTitle("Order Placed");
		String str = "";
		for (i = 0; i < salesCreationEntity.getQuantity().size(); i++) {
			str = str + "Product:" + salesCreationEntity.getName().get(i)
					+ " , Quantity: "
					+ salesCreationEntity.getQuantity().get(i) + " , Price: "
					+ salesCreationEntity.getPrice().get(i) + "<br>";
		}
		timelineDto.setDescription(str);
		return timelineDto;
	}

	private NotificationDto abc(SalesCreationEntity salesCreationEntity,
			String name) {
		NotificationDto notif = new NotificationDto();
		notif.setTitle("New Order Placed By " + name + "!!");
		// notif.setDescription("Client was " + salesCreationEntity.getId());
		notif.setReminder_time((new Date()).getTime() / 1000);
		notif.setTo_who(findMyManager(name));
		notif.setFrom_who(name);
		notif.setStatus("NEW");
		notif.setValue(1);
		return notif;
	}

	private String findMyManager(String name) {
		int officeId;
		EmployeeDto emp = new EmployeeDto();
		try {
			emp = employeeDao.getEmployee(name);
			officeId = emp.getOfficename();
			emp = employeeDao.getManager(officeId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return emp.getUsername();
	}

	@Override
	public List<ProductDto> getRecBasedOnDomain(String domain)
			throws ServiceException {

		List<ProductDto> bestProductList = new ArrayList<ProductDto>();
		try {
			bestProductList = salesDao.getRecBasedOnDomain(domain);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bestProductList;

	}

	@Override
	public List<ProductDto> getRecBasedOnSearch(
			SalesRecommendationSearchEntity entity) throws ServiceException {
		List<String> a = new ArrayList<String>();
		List<ProductDto> bestProductList = new ArrayList<ProductDto>();
		if (!entity.getProcessor().equals("")) {
			a.add(entity.getProcessor());
		}
		if (!entity.getRam().equals("")) {
			a.add(entity.getRam());
		}
		if (!entity.getStorage().equals("")) {
			a.add(entity.getStorage());
		}
		try {
			bestProductList = salesDao.getRecBasedOnSearch(a,
					entity.getStart(), entity.getEnd(), entity.getBrand());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bestProductList;
	}

	@Override
	public List<SalesTargetDto> addSalesTarget() throws ServiceException {
		List<OfficeDto> office = new ArrayList<OfficeDto>();
		try {
			office = officeDao.getAll();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Date date = new Date();
		int mnth = date.getMonth();
		int year = date.getYear();
		if (mnth == 11) {
			year++;
			mnth = 0;
		} else {
			mnth++;
		}
		List<SalesTargetDto> office_sales_history = new ArrayList<SalesTargetDto>();

		for (i = 0; i < office.size(); i++) {
			try {
				if (salesDao.checkOfficeTargetSet(office.get(i).getId(), mnth,
						year) == 0) {
					System.out
							.println("In if condition of checkOfficeTargetSet");
					int emp_count = employeeDao.getTotalCount(office.get(i)
							.getId(), "Representative");
					salesDao.insertSalesTarget(office.get(i).getId(), mnth,
							year, emp_count);
				} else {
					int emp_count = employeeDao.getTotalCount(office.get(i)
							.getId(), "Representative");
					salesDao.updateEmployeeNumber(office.get(i).getId(), mnth,
							year, emp_count);
				}

				SalesTargetDto acc = new SalesTargetDto();
				acc.setId(office.get(i).getId());
				acc.setName(office.get(i).getName());

				OfficeSalesRecordDto ofc = salesDao.getRecord(office.get(i)
						.getId(), mnth, year - 1);

				acc.setPast_emp(ofc.getEmp_count());
				acc.setCurrent_emp((salesDao.getRecord(office.get(i).getId(),
						mnth, year)).getEmp_count());
				acc.setPast_sales(ofc.getTarget_achieved());

				office_sales_history.add(acc);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return office_sales_history;
	}

	@Override
	public int getMonthlySales(HttpServletRequest request, String string)
			throws ServiceException {

		Date date = new Date();
		int mnth = date.getMonth();
		int year = date.getYear();
		int id = 0;
		int a = 0;
		try {
			id = (employeeDao.getEmployee(string)).getOfficename();
			a = salesDao.getMonthlySales(mnth, year, request, id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public int getAssignedAnnualSales(HttpServletRequest request, String string)
			throws ServiceException {
		Date date = new Date();
		int year = date.getYear();
		int mnth = date.getMonth();
		int id = 0;
		int a = 0;
		try {
			id = (employeeDao.getEmployee(string)).getOfficename();
			a = salesDao.getAssignedAnnualSales(mnth, year, request, id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public int getAssignedMonthlySales(HttpServletRequest request, String string)
			throws ServiceException {
		Date date = new Date();
		int mnth = date.getMonth();
		int year = date.getYear();
		int id = 0;
		int a = 0;
		try {
			id = (employeeDao.getEmployee(string)).getOfficename();
			a = salesDao.getAssignedMonthlySales(mnth, year, request, id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public int getAnnualSales(HttpServletRequest request, String string)
			throws ServiceException {
		Date date = new Date();
		int year = date.getYear();
		int mnth = date.getMonth();
		int id = 0;
		int a = 0;
		try {
			id = (employeeDao.getEmployee(string)).getOfficename();
			a = salesDao.getAnnualSales(mnth, year, request, id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public void setSalesTarget(List<SalesTargetDto> salesTargetDto)
			throws ServiceException {

		Date date = new Date();
		int year = date.getYear();
		int mnth = date.getMonth();
		if (mnth == 11) {
			mnth = 0;
			year++;
		} else {
			mnth++;
		}

		try {
			salesDao.setSalesTarget(salesTargetDto, mnth, year);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<SalesStatsEntity> getAllOfficeStats() throws ServiceException {

		List<SalesStatsEntity> office_stats = new ArrayList<SalesStatsEntity>();

		List<OfficeDto> office_all = new ArrayList<OfficeDto>();
		try {
			office_all = officeDao.getAll();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Date date = new Date();
		try {
			for (int i = 0; i < office_all.size(); i++) {
				SalesStatsEntity salesStats = new SalesStatsEntity();
				salesStats.setId(office_all.get(i).getId());
				salesStats.setName(office_all.get(i).getName());
				salesStats.setMonthly_sales(salesDao
						.getMonthlySalesDirectorById(date.getMonth(),
								date.getYear(), office_all.get(i).getId()));
				salesStats.setAnnual_sales(salesDao.getAnnualSalesDirectorById(
						date.getMonth(), date.getYear(), office_all.get(i)
								.getId()));
				salesStats.setAssigned_monthly_sales(salesDao
						.getAssignedMonthlySalesDirectorById(date.getMonth(),
								date.getYear(), office_all.get(i).getId()));
				salesStats.setAssigned_annual_sales(salesDao
						.getAssignedAnnualSalesDirectorById(date.getMonth(),
								date.getYear(), office_all.get(i).getId()));
				office_stats.add(salesStats);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return office_stats;
	}

	@Override
	public int getAverageSaleOrder(HttpServletRequest request, String name)
			throws ServiceException {
		int id = 0;
		List<OwnIntPairDto> id1 = new ArrayList<OwnIntPairDto>();
		try {
			id = employeeDao.getEmployee(name).getOfficename();
			id1 = salesDao.getAverageSaleOrderByOffice(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long sum = 0;
		for (int i = 0; i < id1.size(); i++) {
			sum += id1.get(i).getA();
		}
		if (id1.size() > 0)
			sum = sum / (id1.size());
		return (int) sum;
	}

	@Override
	public void addProductInCart(CartProductDto cartProductDto)
			throws ServiceException {

		try {
			salesDao.addProductInCart(cartProductDto);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<CartProductDto> getAllProductsFromCart(
			CartProductDto cartProductDto) throws ServiceException {
		List<CartProductDto> products = new ArrayList<CartProductDto>();
		try {
			products = salesDao.getAllProductsFromCart(cartProductDto);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public void deleteProductFromCart(CartProductDto cartProductDto)
			throws ServiceException {
		try {
			salesDao.deleteProductFromCart(cartProductDto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateQuantity(CartProductDto cartProductDto)
			throws ServiceException {
		try {
			salesDao.updateQuantity(cartProductDto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getActiveLeads(HttpServletRequest request, String name)
			throws ServiceException {
		int office_id = 0;
		int active_leads = 0;
		try {
			office_id = employeeDao.getEmployee(name).getOfficename();
			active_leads = salesDao.getActiveLeads(request, office_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return active_leads;
	}

	@Override
	public int getRepresentatives(HttpServletRequest request, String name)
			throws ServiceException {
		int officeid =0;
		int rep_total = 0;
		try {
			officeid = employeeDao.getEmployee(name).getOfficename();
			rep_total = salesDao.getRepresentatives(request, officeid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rep_total;
	}

	@Override
	public int getLeads_generated(HttpServletRequest request, String name)
			throws ServiceException {
		int office_id = 0;
		int active_leads = 0;
		try {
			office_id = employeeDao.getEmployee(name).getOfficename();
			active_leads = salesDao.getLeads_generated(request, office_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return active_leads;

	}

}
