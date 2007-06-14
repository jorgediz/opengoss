package org.opengoss.alarm.manager.core;

import java.util.List;

import org.opengoss.alarm.core.Alarm;
import org.opengoss.alarm.core.AlarmRule;
import org.opengoss.alarm.core.BizAlarm;
import org.opengoss.alarm.manager.internal.core.CustomersWithAlarm;
import org.opengoss.alarm.manager.internal.core.ForwardRuleDialogDomain;
import org.opengoss.dao.core.DaoException;

import com.tangramoss.cnm.customer.domain.Customer;

public interface IManagerService {

	// ==========================比较特殊，专为大客户告警页面所设计的操作==========取=============================
	/**
	 * @针对展现有告警的大客户功能而提供的接口
	 * @数据协议: id;name;level;critical,major,minor,warning;
	 * @param pageNo
	 *            页数;
	 * @return List<CustomersWithAlarm> ,返回的是有告警信息的大客户集合中的第pageNo页的数据
	 */
	public List<CustomersWithAlarm> getAlarmsCustomerList(Integer pageNo) throws DaoException;

	/**
	 * @针对展现所有的大客户功能而提供的接口
	 * @数据协议: id;name;level;critical,major,minor,warning;
	 * @param pageNo
	 *            页数;
	 * @return List<CustomersWithAlarm> ,返回的是所有大客户信息集合中的第pageNo页的数据
	 */
	public List<CustomersWithAlarm> getAllCustomerList(Integer pageNo) throws DaoException ;

	// ==========================为customer信息提供的接口==========取=============================
	/**
	 * @根据用户ID取得某个大客户的详细信息
	 * @param id
	 * @return CustermorInfo
	 */
	public Customer getCustomerInfo(Long id) throws DaoException;

	/**
	 * @默认在后台将数据已经分页，根据前台请求的页数返回用户数据
	 * @param pageNo
	 *            页数;
	 * @return List<CustomersWithAlarm> ,返回的是所有的大客户集合中的第pageNo页的数据
	 */
	public List<Customer> getAllCustomerInfo(Integer pageNo) throws DaoException;

	/**
	 * @针对搜索大客户信息所提供的接口
	 * @param condition
	 *            搜索条件
	 * @param pageNo
	 *            页数
	 * @return List<Vip> ,返回更具条件搜索到的大客户信息
	 */
	// public List<Customer> getCustomerList(SearchStruct condition , Integer
	// pageNo);
	// ==========================为customer信息提供的接口==========无增=============================

	// ==========================为customer信息提供的接口==========删=============================
	/**
	 * @针对删除操作所提供的接口
	 * @param id
	 *            要删除的大客户的ID
	 * @return 返回是否删除成功 true=成功;
	 */
	public void deleteCustomer(Long id) throws DaoException;

	// ==========================为customer信息提供的接口==========改=============================
	/**
	 * @针对修改大客户备注所提供的接口
	 * @param customer
	 *            被修改过的大客户信息
	 * @return 返回是否修改成功 true=成功;
	 */
	public boolean modifyCustomerInfo(Long id, String desc) throws DaoException;

	// ==========================为Alarm信息提供的接口==========取=============================
	/**
	 * @针对业务告警展现所提供的接口
	 * @param pageNo
	 *            页数
	 * @return List<Alert> 返回业务告警数据
	 */
	public List<BizAlarm> getAllBizAlrams(Integer pageNo) throws DaoException ;

	/**
	 * @根据alarm的id取得某条告警信息
	 * @param customerID
	 * @return Map<Map<String, Integer>, Map<String, BussinessAlarm>>
	 */
	public List<BizAlarm> getAlarm(Long customerID) throws DaoException;

	/**
	 * @根据告警ID取某条告警信息
	 * @param alarmID
	 * @return BussinessAlarm
	 */
	public BizAlarm getAlarmByAlarmID(Long alarmID) throws DaoException;
	
	// ==========================为Bussiness Alarm信息提供的接口==========取==========
//	/**
//	 * @针对业务告警展现所提供的接口
//	 * @param pageNo
//	 *            页数
//	 * @return List<Alert> 返回业务告警数据
//	 * @throws DaoException 
//	 */
//	
//	public List<BizAlarm> getAllAlrams(Long pageNo) throws DaoException;

	// ==========================为Alarm信息提供的接口==========改=========================
	/**
	 * 根据告警的id修改告警的备注信息
	 * 
	 * @param id
	 * @param memo
	 * @return boolean 修改成功返回true；
	 */
	public boolean modifyAlarmInfo(Long id, String memo) throws DaoException;

	// ==========================================告警确认与凡确认=========================================
	/**
	 * 第三张页面的告警确认
	 * 
	 * @param alarmId
	 * @return boolean
	 */
	public boolean acknowledgeAlarm(Long alarmId) throws DaoException;

	/**
	 * 第三张页面的告警反确认
	 * 
	 * @param alarmId
	 * @return boolean
	 */
	public boolean unacknowledgeAlarm(Long alarmId) throws DaoException;

	// ==========================为自动确认提供的接口=======================================
	/**
	 * @针对自动确认操作所提供的接口;
	 * @param name
	 * @param equipment
	 * @param customer
	 * @param alarmSeveirty
	 * @return
	 */
	public AlarmRule setAutoAcknowledgeAlarms(Long id , String name, String customer, String alarmSeveirty) throws DaoException;

	/**
	 * @根据自动确认规则的id到数据库中取到某条记录
	 * @param id
	 * @return AlarmRule
	 */
	public AlarmRule getAutoAlarmRule(Long id) throws DaoException;

	/**
	 * 取得的数据库中的第pageNo页的所有数据
	 * 
	 * @param pageNo
	 * @return List<AlarmRule>
	 */
	public List<AlarmRule> getAllAutoRules(Integer pageNo) throws DaoException;

	/**
	 * @根据某条告警规则id删除某条告警规则记录
	 * @param id
	 */
	public void deleteAutoAlarmRule(Long id) throws DaoException;
	
	//==========================为Bussiness Alarm信息提供的接口==========修改====
	public boolean delBussinessAlarm(Long alarmId) throws DaoException;

	// ==========================为前转提供的接口==========取=============================
	/**
	 * @根据前转条件将消息发送到某处
	 * @param message=需要发送的消息;condition=前转条件
	 * @return boolean 返回是否前转成功;true=成功;
	 */
	public List<AlarmRule> getAllForwordRules(Integer pageNo) throws DaoException;

	/**
	 * @取到某条前转规则,根据id
	 * @param id
	 * @return AlarmRule
	 */
	public AlarmRule getForwardRuleById(Long id) throws DaoException;

	/**
	 * @在数据库中增加一条前转规则
	 * @param id
	 * @param name
	 * @param equipment
	 * @param alarm
	 * @param type
	 * @param parameter
	 * @return AlarmRule
	 */
	public AlarmRule addForwardRule(ForwardRuleDialogDomain domain) throws DaoException;
	
	/**
	 * @根据id取得某条前转规则
	 * @param id 
	 * @return AlarmRule
	 */
	public AlarmRule getForwordRuleById(Long id) throws DaoException;
	
	/**
	 * 根据某个id删除数据库中的数据
	 * @param id
	 */
	public void deleteForwordRuleById(Long id) throws DaoException;
	
	//=====================各种页面的搜索操作=========================
	public List<Alarm> getSearchResultOfBizAlarm(String name , String type);
	
	//================================================================
	public List<Customer> getSearchResultOfCusInfo(String name, String level) throws DaoException;
	
	//==================================为告警列表中的搜索提供的接口======================
	public List<CustomersWithAlarm> getSearchResult(String name, String level,
			String isAll) throws DaoException;
}
