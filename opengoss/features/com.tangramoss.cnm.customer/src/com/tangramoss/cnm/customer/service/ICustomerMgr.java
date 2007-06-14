package com.tangramoss.cnm.customer.service;

import java.util.List;

import org.opengoss.dao.core.DaoException;

import com.tangramoss.cnm.customer.domain.Customer;
import com.tangramoss.cnm.customer.domain.QuestData;


public interface ICustomerMgr {
	
	public Customer create(Customer customer) ; 
	
	public Customer update(Customer customer);
	
	public void deleteCustomer(long id);
	
	public void delCustomers (Long[] ids);
	
	public List<Customer> findCustomer(QuestData data);
	
	public Customer findCustomerByName(String name);
	
	public Customer load(Long id);
	
	public boolean save(Customer customer) ;
	
	public List<Customer> loadAllCustomers() throws DaoException;
	
	public String hello2();
}