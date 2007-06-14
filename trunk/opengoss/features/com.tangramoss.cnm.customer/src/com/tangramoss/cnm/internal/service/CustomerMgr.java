package com.tangramoss.cnm.internal.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.cnm.security.core.Resource;
import org.opengoss.cnm.security.core.dao.ISemDao;
import org.opengoss.core.IStartable;
import org.opengoss.dao.core.DaoException;

import com.tangramoss.cnm.customer.dao.ICustomerDao;
import com.tangramoss.cnm.customer.domain.Customer;
import com.tangramoss.cnm.customer.domain.QuestData;
import com.tangramoss.cnm.customer.service.ICustomerMgr;

public class CustomerMgr implements ICustomerMgr,IStartable{

	private Log log =  LogFactory.getLog(CustomerMgr.class);
	
	private ICustomerDao  hibernateCustomerDao ;
	
	private ISemDao semDao;
	
	private static final String FROM = "from";
	
	private static final String BLANK = " ";
	
	private static final String ALIAS = "o";
	
	private static final String WHERE = "where";
	
	private static final String AND = "and";
	
	public ICustomerDao getSysDao() {
		return hibernateCustomerDao;
	}

	public void setSysDao(ICustomerDao sysDao) {
		this.hibernateCustomerDao = hibernateCustomerDao;
	}

	public Customer create(Customer customer)  {
		log.info("enter create method");
		try {
			hibernateCustomerDao.save(customer);
			Resource resource = new Resource(customer.getId(),customer.getName());
			semDao.save(resource);
			return customer;
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteCustomer(long id) {
		try {
			Resource resc = semDao.findResourceByRescId(Long.valueOf(id));
			semDao.deleteResource(resc);
			hibernateCustomerDao.delete(Customer.class ,Long.valueOf(id));
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}
	public void delCustomers(Long [] ids) {
//		log.info("enter del customers method");
		System.out.println("enter del customers method");
		for (int i = 0; i < ids.length; i++) {
			deleteCustomer(Long.valueOf(ids[i]));
		}
		System.out.println("exit del customers method");
//		log.info("exit del customers method");
	}
	public List<Customer> findCustomer(QuestData data) {
		String hql = parse2Hql(Customer.class, data);
		try {
			return hibernateCustomerDao.findCustomerByHql(hql);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Customer findCustomerByName(String name) {
		return null;
	}

	public Customer load(Long id) {
		Customer customer = null;
		try {
			customer = hibernateCustomerDao.get(Customer.class ,id);
		} catch (DaoException e) {
			e.printStackTrace();
		} 
		return  customer;
	}
	
	public List<Customer> loadAllCustomers() throws DaoException {
		return hibernateCustomerDao.listAll(Customer.class);
	}

	public boolean save(Customer customer)  {
		return false;
	}

	public Customer update(Customer customer) {
		log.info("enter updata method:");
		Customer oldCustomer = null;
		try {
			oldCustomer = hibernateCustomerDao.get(Customer.class ,customer.getId());
		} catch (DaoException e) {
			e.printStackTrace();
		}
		
		if (oldCustomer != null){
			modifyInfo(oldCustomer, customer);
			try {
				return hibernateCustomerDao.update(oldCustomer);
			} catch (DaoException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void modifyInfo(Customer used,Customer recently){
//		used = recently; 不能updata, 内存地址改变
		used.setName(recently.getName());
		used.setFullName(recently.getFullName());
		used.setLevel(recently.getLevel());
		used.setTime(recently.getTime());
		used.setMobile(recently.getMobile());
		used.setPhone(recently.getPhone());
		used.setInformed(recently.getInformed());
		used.setMessage(recently.getMessage());
		used.setEmail(recently.getEmail());
		used.setCustomerClass(recently.getCustomerClass());
		used.setCorperator(recently.getCorperator());
		used.setAddress(recently.getAddress());
		used.setRegion(recently.getRegion());
		used.setFax(recently.getFax());
		used.setDescription(recently.getDescription());
		used.setCircuitNo(recently.getCircuitNo());
		used.setCircuitInfo(recently.getCircuitInfo());
		used.setLinkMan(recently.getLinkMan());
	}
	private String parse2Hql(Class clazz, QuestData data ){
		String sql = FROM;
		sql = sql +BLANK+ clazz.getName() + BLANK + ALIAS +BLANK + WHERE +BLANK ;
			sql = sql+ALIAS +".name = " +"\'"+ data.getCusName()+"\'" + BLANK;
		if(!(data.getCusLevel() == null)){
			sql = sql +AND +BLANK  +ALIAS + ".userName = "+"\'"+data.getCusLevel()+"\'" + BLANK;
		}
		if (!(data.getCusMail() == null )){
			sql = sql +AND  +BLANK +ALIAS + ".mail= "+"\'"+ data.getCusMail()+"\'" + BLANK;
		}
		if (!(data.getCusPhone() == null)){
			sql = sql +AND  +BLANK +ALIAS + ".phone = "+"\'"+ data.getCusPhone()+"\'"+ BLANK;
		}
		if (!(data.getCusMobile() == null)){
			sql = sql +AND  +BLANK +ALIAS + ".mobile = "+"\'"+ data.getCusMobile()+"\'" + BLANK;;
		}
		
		return sql;
	}

	public String hello2() {
		return "acho hell2";
	}

	public void start() throws Exception {
	/*	Customer c = new Customer();
		c.setId(Long.valueOf(8));
		c.setAddress("modify");
		c.setCircuitInfo("idmofiydfy");
		c.setName("modify");
		c.setInformed(true);
//		create(c);
		update(c);*/
		//1 不行,new Long(1) 可以
//		DomainObject customer = hibernateCustomerDao.get(Customer.class, 1);
//		int i = 0;
//		log.info(customer.get);
//		deleteCustomer(new Long(7));
		/*List<DomainObject> customers = hibernateCustomerDao.listAll(Customer.class);
		for (int i = 0; i < customers.size(); i++) {
			if (customers.get(i).getId() == 7){
			}
		}*/
//		log.info("size"+customer.size());
//		create(c);
	}

	public void stop() throws Exception {
		
	}

	public ISemDao getSemDao() {
		return semDao;
	}

	public void setSemDao(ISemDao semDao) {
		this.semDao = semDao;
	}

	public ICustomerDao getHibernateCustomerDao() {
		return hibernateCustomerDao;
	}

	public void setHibernateCustomerDao(ICustomerDao hibernateCustomerDao) {
		this.hibernateCustomerDao = hibernateCustomerDao;
	}

	
	
}
