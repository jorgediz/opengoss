package org.opengoss.alarm.manager.internal.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opengoss.alarm.core.Alarm;
import org.opengoss.alarm.core.AlarmRule;
import org.opengoss.alarm.core.BizAlarm;
import org.opengoss.alarm.core.IAlarmDao;
import org.opengoss.alarm.core.IAlarmRuleDao;
import org.opengoss.alarm.manager.core.IManagerService;
import org.opengoss.dao.core.DaoException;

import com.tangramoss.cnm.customer.dao.ICustomerDao;
import com.tangramoss.cnm.customer.domain.Customer;

public class ManagerService implements IManagerService {

	private IAlarmRuleDao alarmRuleDao;

	public void setAlarmRuleDao(IAlarmRuleDao rule) {
		this.alarmRuleDao = rule;
	}

	private ICustomerDao customerDao;

	private IAlarmDao alarmDao;

	public void setCustomerDao(ICustomerDao rule) {
		this.customerDao = rule;
	}

	public void setAlarmDao(IAlarmDao iAlarmDao) {
		this.alarmDao = iAlarmDao;
	}

	/** ******************全局变量区*********************************************** */
	private Integer pageRow = 20;

	/* 用于存储剩下的alarms list */
	List<BizAlarm> buAListLeft = new ArrayList<BizAlarm>();

	/* 第三张页面，存储剩余的业务告警信息（分页操作） */
	List<BizAlarm> busAlarmListLeft = new ArrayList<BizAlarm>();

	// ==========================比较特殊，专为大客户告警页面所设计的操作==========取=============================
	/**
	 * @针对展现有告警的大客户功能而提供的接口
	 * @数据协议: id;name;level;critical,major,minor,warning;
	 * @param pageNo
	 *            页数;
	 * @return List<CustomersWithAlarm> ,返回的是有告警信息的大客户集合中的第pageNo页的数据
	 * @throws DaoException
	 */
	public List<CustomersWithAlarm> getAlarmsCustomerList(Integer pageNo)
			throws DaoException {
		// 等待返回的用户数据
		List<CustomersWithAlarm> customerList = new ArrayList<CustomersWithAlarm>();

		List<BizAlarm> dbList = null;
		try {
			dbList = alarmDao.getBizAlarms(BizAlarm.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Iterator i = dbList.iterator();
		while (i.hasNext()) {

			BizAlarm alarm = (BizAlarm) i.next();
			// @取得此条告警关联的大客户ID链

			Long id = ((BizAlarm) alarmDao.getById(alarm.getId()))
					.getCustomerId();

			Integer criticalNo = 0, majorNo = 0, minorNo = 0, warningNo = 0;
			// while (ite.hasNext()) {
			CustomersWithAlarm customersWithA;
			Customer infoTemp = null;

			try{
				infoTemp = customerDao.get(Customer.class, id);
				
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Exception cause is "+e.toString());
			}

			if (infoTemp != null) {
				String name = infoTemp.getName();
				String level = infoTemp.getLevel();

				/* 如果list中已经有此客户的信息 */
				if (getCuInList(customerList, id) != null) {
					/* 取完再放进去 */
					customersWithA = getCuInList(customerList, id);
					Integer perceivedSeverity = alarm.getPerceivedSeverity();

					if (perceivedSeverity == 5) {
						Integer number = customersWithA.getCritical();
						customersWithA.setCritical(number + 1);
					} else if (perceivedSeverity == 4) {
						Integer number = customersWithA.getMajor();
						customersWithA.setMajor(number + 1);
					} else if (perceivedSeverity == 3) {
						Integer number = customersWithA.getMinor();
						customersWithA.setMinor(number + 1);
					} else if (perceivedSeverity == 2) {
						Integer number = customersWithA.getWarning();
						customersWithA.setWarning(number + 1);
					}

					/* 放进去 */
					// customerList.add(customersWithA);
				} else {/* 如果是第一次出现这个客户信息 */
					customersWithA = new CustomersWithAlarm();
					/* 先将一个顾客的告警信息结构装配起来 */
					customersWithA.setID(id);
					customersWithA.setName(name);
					customersWithA.setLevel(level);

					customersWithA.setCritical(criticalNo);
					customersWithA.setMajor(majorNo);
					customersWithA.setMinor(minorNo);
					customersWithA.setWarning(warningNo);

					Integer perceivedSeverity = alarm.getPerceivedSeverity();
					if (perceivedSeverity == 5) {
						Integer number = customersWithA.getCritical();
						customersWithA.setCritical(number + 1);
					} else if (perceivedSeverity == 4) {
						Integer number = customersWithA.getMajor();
						customersWithA.setMajor(number + 1);
					} else if (perceivedSeverity == 3) {
						Integer number = customersWithA.getMinor();
						customersWithA.setMinor(number + 1);
					} else if (perceivedSeverity == 2) {
						Integer number = customersWithA.getWarning();
						customersWithA.setWarning(number + 1);
					}
					customerList.add(customersWithA);
				}
			}
			// }
		}
		System.out.println(this.toString() + " : " + customerList.size());

		return customerList;
	}

	private CustomersWithAlarm getCuInList(
			List<CustomersWithAlarm> customerList, Long id) {
		Iterator i = customerList.iterator();
		while (i.hasNext()) {
			CustomersWithAlarm alarm = (CustomersWithAlarm) i.next();
			if (alarm.getID().equals(id)) {
				return alarm;
			}
		}
		return null;
	}

	/**
	 * @针对展现所有的大客户功能而提供的接口
	 * @数据协议: id;name;level;critical,major,minor,warning;
	 * @param pageNo
	 *            页数;
	 * @return List<CustomersWithAlarm> ,返回的是所有大客户信息集合中的第pageNo页的数据
	 * @throws DaoException
	 */
	public List<CustomersWithAlarm> getAllCustomerList(Integer pageNo)
			throws DaoException {
		List<Customer> list = getAllCustomerInfo(pageNo);

		// 将所有的用户ID搜集起来
		List<Long> IDList_1 = new ArrayList<Long>();

		Iterator ite = list.iterator();
		while (ite.hasNext()) {
			Customer cusInfo = (Customer) ite.next();
			Long id = cusInfo.getId();
			IDList_1.add(id);
		}

		// 将有告警信息的用户ID搜集起来
		List<Long> IDList_2 = new ArrayList<Long>();
		List<CustomersWithAlarm> listCusWithAlarms = getAlarmsCustomerList(pageNo);
		Iterator iteAlarms = listCusWithAlarms.iterator();
		while (iteAlarms.hasNext()) {
			CustomersWithAlarm customersWithAlarm = (CustomersWithAlarm) iteAlarms
					.next();
			Long id = customersWithAlarm.getID();
			IDList_2.add(id);
		}

		List<CustomersWithAlarm> listCusWithAlarmsTemp = new ArrayList<CustomersWithAlarm>();

		Iterator ii = IDList_1.iterator();
		while (ii.hasNext()) {
			Long id_1 = (Long) ii.next();
			if (!IDList_2.contains(id_1)) {
				CustomersWithAlarm customersWithAlarmTemp = new CustomersWithAlarm();
				String level = getCustomerInfo(id_1).getLevel();
				String name = getCustomerInfo(id_1).getName();
				customersWithAlarmTemp.setName(name);
				customersWithAlarmTemp.setID(id_1);
				customersWithAlarmTemp.setLevel(level);
				customersWithAlarmTemp.setCritical(0);
				customersWithAlarmTemp.setMajor(0);
				customersWithAlarmTemp.setMinor(0);
				customersWithAlarmTemp.setWarning(0);
				listCusWithAlarmsTemp.add(customersWithAlarmTemp);
			}
		}

		Iterator i_3 = listCusWithAlarmsTemp.iterator();
		while (i_3.hasNext()) {
			CustomersWithAlarm alarm = (CustomersWithAlarm) i_3.next();
			System.out.println(this.toString() + " : " + alarm.getID());
		}
		return listCusWithAlarmsTemp;
	}

	// ==========================为customer信息提供的接口==========取=============
	/**
	 * @默认在后台将数据已经分页，根据前台请求的页数返回用户数据
	 * @param pageNo
	 *            页数;
	 * @return List<CustomersWithAlarm> ,返回的是所有的大客户集合中的第pageNo页的数据
	 * @throws DaoException
	 */
	public List<Customer> getAllCustomerInfo(Integer pageNo)
			throws DaoException {
		// ItemDAOImpl impl = new ItemDAOImpl();
		// List<CustomerInfo> list = impl.findAllCustomers();
		List<Customer> cusList = null;
		try{
			cusList = customerDao.listAll(Customer.class);			
		}catch(Exception e){
			e.printStackTrace();
		}

		System.out.println("$" + cusList.size());
		return cusList;
	}

	/**
	 * @取得某个大客户的信息
	 * @param id
	 * @return CustermorInfo
	 * @throws DaoException
	 */
	public Customer getCustomerInfo(Long id) throws DaoException {
		System.out.println(this.toString() + " - The Id is : " + id);
		Customer customer_1 = null;

		customer_1 = customerDao.get(Customer.class, id);

		System.out.println(this.toString() + " - " + customer_1.getName());
		return customer_1;
	}

	// ==========================为customer信息提供的接口==========改============
	/**
	 * @针对修改大客户备注所提供的接口
	 * @param id=大客户的id；desc=大客户的备注信息；
	 * @return 返回是否修改成功 true=成功;
	 * @throws DaoException
	 */
	public boolean modifyCustomerInfo(Long id, String desc) throws DaoException {
		Customer customerInfo = null;

		customerInfo = customerDao.get(Customer.class, id);

		customerInfo.setDescription(desc);

		customerDao.update(customerInfo);
		return true;
	}

	// ==========================为customer信息提供的接口==========删============
	/**
	 * @针对删除操作所提供的接口
	 * @param id
	 *            要删除的大客户的ID
	 * @return 返回是否删除成功 true=成功;
	 * @throws DaoException
	 */
	public void deleteCustomer(Long id) throws DaoException {

		customerDao.delete(Customer.class, id);

	}

	// ==========================为Bussiness Alarm信息提供的接口==========取==========
	// /**
	// * @针对业务告警展现所提供的接口
	// * @param pageNo
	// * 页数
	// * @return List<Alert> 返回业务告警数据
	// * @throws DaoException
	// */
	// public List<BizAlarm> getAllAlrams(Long pageNo) throws DaoException {
	// /* 用于存储第一页数据 */
	// // List<BizAlarm> bAListTemp = new ArrayList<BizAlarm>();
	//
	// List<BizAlarm> bAllList = iAlarmDao.getBizAlarms(BizAlarm.class);
	//		
	// // Integer size = pageRow > bAllList.size() ? bAllList.size() : pageRow;
	// // int count = 0;
	// // while (count < size) {
	// // BizAlarm alarmTemp = bAllList.remove(0);
	// // bAListTemp.add(alarmTemp);
	// // count++;
	// // }
	// //
	// // busAlarmListLeft = bAllList;
	// return bAllList;
	// }

	/**
	 * @根据alarm的id取得某条告警信息
	 * @param customerID
	 * @return Object[]的第一个参数是有几条告警数据与此大客户相关;BussinessAlarm是其中的第一条数据,用于前台的展现
	 * @throws DaoException
	 */
	public List<BizAlarm> getAlarm(Long customerID) throws DaoException {
		List<BizAlarm> alarmList = null;

		alarmList = alarmDao.getBizAlarms(BizAlarm.class);

		return alarmList;
	}

	/**
	 * @根据告警ID取某条告警信息
	 * @param alarmID
	 * @return BussinessAlarm
	 * @throws DaoException
	 */
	public BizAlarm getAlarmByAlarmID(Long alarmID) throws DaoException {
		BizAlarm result = null;

		result = (BizAlarm) alarmDao.getById(alarmID);

		return result;
	}

	// ==========================为Bussiness Alarm信息提供的接口==========修改====
	public boolean modifyAlarmInfo(Long id, String memo) throws DaoException {
		Alarm bizAlarm = null;

		bizAlarm = alarmDao.getById(id);

		bizAlarm.setMemo(memo);

		alarmDao.update(bizAlarm);

		return true;

	}

	// ==========================为Bussiness Alarm信息提供的接口==========修改====
	public boolean delBussinessAlarm(Long alarmId) throws DaoException {
		// ItemDAO impl = new ItemDAOImpl();
		Alarm alarm = null;

		alarm = alarmDao.getById(alarmId);
		alarmDao.delete(alarm);

		return true;
	}

	// ==========================为自动确认提供的接口=================================
	/**
	 * @针对自动确认操作所提供的接口;
	 * @param equipment
	 * @param customer
	 * @param alarmSeveirty
	 * @return boolean
	 * @throws DaoException
	 * @添加不成功的异常没有添加
	 */
	public AlarmRule setAutoAcknowledgeAlarms(Long id, String name,
			String customerName, String alarmSeveirty) throws DaoException {
		String type = "自动确认规则";// 总共有3种类型：“自动确认规则”“短信前转规则”“Email前转规则”
		StringBuilder condition = new StringBuilder();
		condition.append("from " + BizAlarm.class.getName() + " as bizAlarm, "
				+ Customer.class.getName() + " as customer");

		condition.append(" where" + " customer.name=" + customerName
				+ " and bizAlarm.alarmSeveirty=" + alarmSeveirty);
		System.out.println("condition is " + condition);
		String context = "";// 默认为空
		String desc = "自动确认规则";

		boolean isRuleHas = true; // 初始数据库中有这条数据
		AlarmRule alarmRule = null;
		if (id != null) {// id不为空,表示是在修改某条规则
			try {
				alarmRule = alarmRuleDao.getById(id);
			} catch (DaoException e) {
				e.printStackTrace();
			}
			if (alarmRule == null) {
				System.out.println("新建一条规则");
				alarmRule = new AlarmRule();
				isRuleHas = false;
			}
		} else {
			alarmRule = new AlarmRule();
			isRuleHas = false;
		}

		alarmRule.setName(name);
		alarmRule.setType(type);
		alarmRule.setRuleCondition(condition.toString());
		alarmRule.setContext(context);
		alarmRule.setDescription(desc);

		// AlarmRule rule = iAlarmRuleDao.add(alarmRule);
		AlarmRule rule = null;
		try {
			if (isRuleHas) {
				alarmRuleDao.update(alarmRule);
				rule = alarmRuleDao.getById(id);
			} else {
				rule = alarmRuleDao.save(alarmRule);

			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return rule;
	}

	// ==================================为告警列表中的搜索提供的接口======================
	public List<CustomersWithAlarm> getSearchResult(String name, String level,
			String isAll) throws DaoException {
		List<CustomersWithAlarm> reslutList = new ArrayList<CustomersWithAlarm>();
		// 先取到第一页的告警信息
		List<CustomersWithAlarm> listAlarms = getAlarmsCustomerList(100000);// 注意：此处的100000无意义
		Iterator ite = listAlarms.iterator();
		while (ite.hasNext()) {
			CustomersWithAlarm alarmTemp = (CustomersWithAlarm) ite.next();
			if(!name.equals("")&&!level.equals("")){//如果两个查询条件均不为空，则
				if (alarmTemp.getName().equals(name)
						&& alarmTemp.getLevel().equals(level)) {
					reslutList.add(alarmTemp);
				}
			}
			else{//如果两个查询条件有一空或者全空
				if (alarmTemp.getName().equals(name)
						|| alarmTemp.getLevel().equals(level)) {
					reslutList.add(alarmTemp);
				}				
			}
		}

		if (isAll.equals("0")) {// 如果展现的是由告警信息的大客户列表，则...
			return reslutList;
		} else {
			List<CustomersWithAlarm> allInfo = getAllCustomerList(1000000);
			Iterator iTemp = allInfo.iterator();
			while (iTemp.hasNext()) {
				CustomersWithAlarm cusTemp = (CustomersWithAlarm) iTemp.next();
				if(!name.equals("")
						&& !cusTemp.getLevel().equals("")){
					if (cusTemp.getName().equals(name)
							&& level.equals(level)) {
						reslutList.add(cusTemp);
					}					
				}
				else{
					if (cusTemp.getName().equals(name)
							|| cusTemp.getLevel().equals(level)) {
						reslutList.add(cusTemp);
					}
				}
			}

			return reslutList;
		}

	}

	// ==================页面二================为大客户信息的搜索提供的接口==================
	public List<Customer> getSearchResultOfCusInfo(String name, String level)
			throws DaoException {
		List<Customer> reslutList = null;

		StringBuilder condition = new StringBuilder();
		condition.append("from " + Customer.class.getName() + " as customer");// FROM

		if (name.equals("") && level.equals("")) {

		} else if (level.equals("")) {
			condition.append(" where customer.name='" + name + "'");
		} else if (name.equals("")) {
			condition.append(" where" + " customer.level='" + level + "'");
		} else {
			condition.append(" where customer.name='" + name
					+ "' and customer.level='" + level + "'");
		}

		System.out.println("condition : " + condition.toString());
		reslutList = customerDao.findCustomerByHql(condition.toString());

		if (reslutList == null) {// 如果没有取到任何数据
			reslutList = new ArrayList<Customer>();
		}
		return reslutList;
	}

	public boolean acknowledgeAlarm(Long alarmId) throws DaoException {

		Alarm bizAlarm = null;
		bizAlarm = alarmDao.getById(alarmId);
		bizAlarm.setAckUserId(88888888);// 88888888默认为用户ID
		alarmDao.update(bizAlarm);
		System.out.println("告警 " + alarmId + " 已经确认");

		return true;

	}

	public boolean unacknowledgeAlarm(Long alarmId) throws DaoException {
		Alarm bizAlarm = null;
		bizAlarm = alarmDao.getById(alarmId);
		bizAlarm.setAckUserId(00000000);// 00000000默认为用户ID
		alarmDao.update(bizAlarm);
		System.out.println("告警 " + alarmId + " 已经反确认");

		return true;
	}

	/**
	 * @获取所有的业务告警数据
	 */
	public List<BizAlarm> getAllBizAlrams(Integer pageNo) throws DaoException {
		List<BizAlarm> list = alarmDao.getBizAlarms(BizAlarm.class);

		return list;
	}

	// ---------------------------------------------为取到某条自动告警规则而提供的接口---------------------------
	/**
	 * @根据自动确认规则的id到数据库中取到某条记录
	 * @param id
	 * @return AlarmRule
	 * @throws DaoException
	 */
	public AlarmRule getAutoAlarmRule(Long id) throws DaoException {
		AlarmRule alarmRule = alarmRuleDao.getById(id);

		return alarmRule;
	}

	/**
	 * 取到数据库中所有type类型是‘自动确认规则’类型的数据
	 * 
	 * @throws DaoException
	 */
	public List<AlarmRule> getAllAutoRules(Integer pageNo) throws DaoException {
		List<AlarmRule> list = alarmRuleDao.getAlarmRules(AlarmRule.class);

		List<AlarmRule> autoRulesList = new ArrayList<AlarmRule>();
		Iterator ite = list.iterator();
		while (ite.hasNext()) {
			AlarmRule rule = (AlarmRule) ite.next();
			if (rule.getType().equals("自动确认规则")) {
				autoRulesList.add(rule);
			}
		}
		return autoRulesList;
	}

	/**
	 * @删除某条告警规则
	 * @param id
	 * @throws DaoException
	 */
	public void deleteAutoAlarmRule(Long id) throws DaoException {
		AlarmRule alarmRule = null;

		alarmRule = alarmRuleDao.getById(id);
		alarmRuleDao.delete(alarmRule);

	}

	/**
	 * @throws DaoException
	 * @取到数据库中所有type类型是‘前转规则’的数据
	 */
	public List<AlarmRule> getAllForwordRules(Integer pageNo)
			throws DaoException {
		List<AlarmRule> list = null;

		list = alarmRuleDao.getAlarmRules(AlarmRule.class);

		List<AlarmRule> forwardRulesList = new ArrayList<AlarmRule>();
		Iterator ite = list.iterator();
		while (ite.hasNext()) {
			AlarmRule rule = (AlarmRule) ite.next();
			if (rule.getType().equals("mail前转规则")
					|| rule.getType().equals("短信前转规则")) {
				forwardRulesList.add(rule);
				System.out.println("打印出的rule ：id:" + rule.getId() + " ownerId:"
						+ rule.getOwnerId() + " name:" + rule.getName()
						+ " type:" + rule.getType() + " ruleCondition:"
						+ rule.getRuleCondition() + " context:"
						+ rule.getContext() + " description:"
						+ rule.getDescription());
			}
		}
		return forwardRulesList;
	}

	/**
	 * @取到某条前转规则,根据id
	 * @param id
	 * @return AlarmRule
	 * @throws DaoException
	 */
	public AlarmRule getForwardRuleById(Long id) throws DaoException {
		AlarmRule alarmRule = null;
		alarmRule = alarmRuleDao.getById(id);

		return alarmRule;
	}

	/**
	 * @在数据库中增加一条前转规则 objectArray["id"] = ruleId;//id objectArray["ruleName"] =
	 *                ruleNameStr;//规则名称 objectArray["customerName"] =
	 *                customerNameStr;//客户名称
	 *                objectArray["alarmSerceivedSeverity"] =
	 *                alarmSeverityStr;//告警级别 objectArray["type"] =
	 *                ruleTypeStr;//Email前转规则 objectArray["context"] =
	 *                ruleContextStr;//短信号码
	 */
	public AlarmRule addForwardRule(ForwardRuleDialogDomain obj)
			throws DaoException {
		System.out.println("obj:" + obj.getId() + ";" + obj.getRuleName() + ";"
				+ obj.getCustomerName() + ";" + obj.getAlarmSerceivedSeverity()
				+ ";" + obj.getRuleType() + ";" + obj.getRuleContext() + ";");

		Long ownerId = Long.valueOf(88888888);// 默认user id 是88888888
		String type = null;// 总共有: mail前转规则、短信前转规则
		if (obj.getRuleType().equals("Email")) {
			type = "mail前转规则";
		} else {
			type = "短信前转规则";
		}
		StringBuilder condition = new StringBuilder();
		// **
		condition.append("from " + BizAlarm.class.getName() + " as bizAlarm, "
				+ Customer.class.getName() + " as customer");

		condition.append(" where" + " customer.name=" + obj.getCustomerName()
				+ " and bizAlarm.perceivedSeverity="
				+ obj.getAlarmSerceivedSeverity());
		System.out.println("condition is " + condition);

		String context = obj.getRuleContext();
		String desc = "告警前转规则";
		Long id = obj.getId();

		boolean isRuleHas = true; // 初始数据库中有这条数据
		AlarmRule alarmRule;
		if (id != 0) {// id为空
			alarmRule = alarmRuleDao.getById(id);// ==============>同样
			// 的问题，数据库中有数据，id正确，但却取不出来
			if (alarmRule == null) {
				System.out.println("新建一条规则");
				alarmRule = new AlarmRule();
				isRuleHas = false;
			}
		} else {
			alarmRule = new AlarmRule();
			isRuleHas = false;
		}

		alarmRule.setOwnerId(ownerId);
		alarmRule.setId(id);
		alarmRule.setName(obj.getRuleName());
		alarmRule.setType(type);
		alarmRule.setRuleCondition(condition.toString());
		alarmRule.setContext(context);
		alarmRule.setDescription(desc);

		AlarmRule rule = null;

		if (isRuleHas) {
			alarmRuleDao.update(alarmRule);
			rule = alarmRuleDao.getById(id);
		} else {
			rule = alarmRuleDao.save(alarmRule);

		}

		return rule;
	}

	/**
	 * @throws DaoException
	 * @根据id取得某条前转规则
	 */
	public AlarmRule getForwordRuleById(Long id) throws DaoException {
		AlarmRule alarmRule = alarmRuleDao.getById(id);
		System.out.println("[]=1." + alarmRule.getName() + ";2."
				+ alarmRule.getContext() + ";3." + alarmRule.getDescription()
				+ ";4." + alarmRule.getRuleCondition()+";5."+alarmRule.getType()+";6."+alarmRule.getId());
		return alarmRule;
	}

	public void deleteForwordRuleById(Long id) throws DaoException {
		AlarmRule alarmRule = alarmRuleDao.getById(id);
		alarmRuleDao.delete(alarmRule);
	}

	/**
	 * 
	 * @param String
	 *            name , String type
	 * @return List<Alarm>
	 */
	public List<Alarm> getSearchResultOfBizAlarm(String name, String type) {
		StringBuilder condition = new StringBuilder();
		condition.append("from " + BizAlarm.class.getName() + " as bizAlarm");// FROM

		if (name.equals("") && type.equals("")) {

		} else if (type.equals("")) {
			condition.append(" where bizAlarm.name='" + name + "'");
		} else if (name.equals("")) {
			condition.append(" where" + " bizAlarm.alarmType='" + type + "'");
		} else {
			condition.append(" where bizAlarm.name='" + name
					+ "' and bizAlarm.alarmType='" + type + "'");
		}

		System.out.println("Condition " + condition.toString());
		List<Alarm> bizAlarmList = null;
		try {
			bizAlarmList = alarmDao.query(condition.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bizAlarmList;
	}
}
