package org.opengoss.cnm.security.internal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opengoss.cnm.log.core.LogAction;
import org.opengoss.cnm.log.core.LogLevel;
import org.opengoss.cnm.log.core.LogResult;
import org.opengoss.cnm.log.core.service.ICnmLog;
import org.opengoss.cnm.security.core.AclEntry;
import org.opengoss.cnm.security.core.CnmSecurityException;
import org.opengoss.cnm.security.core.Permission;
import org.opengoss.cnm.security.core.Resource;
import org.opengoss.cnm.security.core.User;
import org.opengoss.cnm.security.core.UserData;
import org.opengoss.cnm.security.core.Role;
import org.opengoss.cnm.security.core.UserSearchData;
import org.opengoss.cnm.security.core.service.IAclEntryService;
import org.opengoss.cnm.security.core.service.IUserDataService;
import org.opengoss.cnm.security.core.service.IUserService;
import org.opengoss.core.IStartable;
import org.opengoss.dao.core.DaoException;

/**
 * this class implements SimpleUserService interface. 
 * In addition , this dclass mainly provides converting TransObject which be communicated by browser and 
 * server  to  user object.
 * @author zouxc
 * @date 2006-12-14
 */
public class UserDataService implements IUserDataService,IStartable{

	private IUserService userService;
	
	private IAclEntryService aclEntryService;

	private ICnmLog cnmLogger;
	
	private Logger log = Logger.getLogger("SimpleUserServiceImpl");
	
	private static final String FROM = "from";
	
	private static final String BLANK = " ";
	
	private static final String ALIAS = "o";
	
	private static final String WHERE = "where";
	
	private static final String AND = "and";
	
	
	public synchronized UserData create(UserData transObject) throws CnmSecurityException {
		log.log(Level.INFO,"enter SimpleUserServiceImpl create method");
		User newUser = transformBasicInfo(transObject);
		try {
			newUser = userService.create(newUser);
		
			AclEntry aclEntry = trans2AclEntry(transObject, newUser);
		
			aclEntryService.create(aclEntry);
		} catch (DaoException e) {
			try {
				cnmLogger.operation(LogAction.CreateUser, LogResult.FAILED, "create user failed caused by daoexception");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		transObject.setId(newUser.getId());
		log.log(Level.INFO,"exit SimpleUserServiceImpl create method");
		try {
			cnmLogger.operation(LogAction.CreateUser, LogResult.SUCCEED, "create user details");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transObject;
	}

	private AclEntry trans2AclEntry(UserData transObject, User newUser) throws CnmSecurityException {
		log.log(Level.INFO,"enter SimpleUserServiceImpl transformACLEntry method");
		
		AclEntry aclEntry = new AclEntry();
		aclEntry.setUser(newUser);
		//生成userGroup,Permission和Resource信息
		
		List<String> selCustomerNames = transObject.getSelResc();
		if(transObject.getRoleInfo().equals("manager")){ //选择了大客户经理的角色
			List<Resource> resources = aclEntryService.getResources();
			getRoleInfo(aclEntry, Role.Type.MANAGER_ROLE);
			Permission permission = new Permission();
			List<Resource> selResources = getResource(resources, selCustomerNames);
			permission.setPermissionList(selResources);
			aclEntry.setPermission(permission);
		}else{
			getRoleInfo(aclEntry, Role.Type.OPERATOR_ROLE);
			aclEntry.setPermission(null); //运维人员对客户操作的权限
		}
		log.log(Level.INFO,"exit SimpleUserServiceImpl transformACLEntry method");
		return aclEntry ;
	}

	/**
	 * 选择的customerName对应到customer
	 * @param customers:所有的customer
	 * @param selCustomerNames
	 * @return
	 */
	private List<Resource> getResource(List<Resource> resources, List<String> selCustomerNames) {
		ArrayList selResource = new ArrayList();
		for (String name : selCustomerNames) {
			for (Resource resource: resources) {
				if(resource.getRescName().equals(name)){
					selResource.add(resource);
				}
			}
		}
		return selResource;
	}


	private User transformBasicInfo(UserData transObject) {
		User newUser = new User();
		newUser.setName(transObject.getName());
		data2User(transObject, newUser);
		return newUser;
	}

	private void data2User(UserData userData, User newUser) {
//		
		newUser.setUserName(userData.getUserName());
		newUser.setPassword(userData.getPassword());
		newUser.setMail(userData.getMail()); //修改界面是否需要修改邮箱????
		newUser.setAddress(userData.getAddress());
		newUser.setMobile(userData.getMobile());
		newUser.setPhone(userData.getPhone());
		newUser.setCompany(userData.getCompany());
		newUser.setDepartment(userData.getDepartment());
	}

	
	
	public synchronized UserData modify(UserData userData ) throws CnmSecurityException {
		log.log(Level.INFO,"enter SimpleUserServiceImpl modify method");
		
		AclEntry oldAclEntry = null;
		try {
			oldAclEntry = userService.loadAclEntryByUserId(userData.getId());
		} catch (DaoException e) {
			e.printStackTrace();
		}
		User oldUser = oldAclEntry.getUser();
		if (oldAclEntry.getRole().getType().equals("operator")){ //改变角色，变成operator
			try {
				aclEntryService.deletePermissionByAclEntry(oldAclEntry);
			} catch (DaoException e) {
				e.printStackTrace();
			}
			getRoleInfo(oldAclEntry,Role.Type.OPERATOR_ROLE);
		}else{
			List<Resource> resources  = aclEntryService.getResources();
			List<String> selCustomerNames = userData.getSelResc();
			Permission permission = oldAclEntry.getPermission();
			if (permission != null){ //原来和改变的都是manager角色
				try {
					aclEntryService.deletePermissionByAclEntry(oldAclEntry);
				} catch (DaoException e) {
					e.printStackTrace();
				} //刷新aclEntry
			}
			 permission = new Permission ();
			 List<Resource> selResources = getResource(resources, selCustomerNames);
			 permission.setPermissionList(selResources);
			 oldAclEntry.setPermission(permission);
		}
		userData.setName(oldUser.getName()); //不需要修改的部分,但是需要显示
		userData.setMail(oldUser.getMail());//不需要修改的部分,但是需要显示
		data2User(userData, oldUser);
		try {
			aclEntryService.modify(oldAclEntry);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		log.log(Level.INFO,"exit SimpleUserServiceImpl modify method");
		try {
			cnmLogger.operation(LogAction.ModifyUser, LogResult.SUCCEED, "modify user details");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userData;
	}

	private void getRoleInfo(AclEntry oldAclEntry,String roleType) {
		List<Role> roles  = aclEntryService.getUserGroups();
		for (Role role : roles) {
			if (role.getType().equals(roleType)){
				oldAclEntry.setRole(role);
				break;
			}
		};
	}

	public void delUsers(Long [] ids) {
		for (int i = 0; i < ids.length; i++) {
			deleteUser(ids[i]);
		}
	}
	
	private synchronized void deleteUser(Long id)  {
		log.log(Level.INFO,"enter SimpleUserServiceImpl deleteUser method");
		AclEntry aclEntry = null;
		try {
			aclEntry = aclEntryService.findAclEntryByUserId(id);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		if (aclEntry == null){
			return  ;
		}
		User user = aclEntry.getUser();
		if (user == null) {
			return ;
		}
		
		try {
//			置User的Status状态为DELETED
			userService.moidfyUserStatus(user);
			aclEntryService.delete(aclEntry);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		
		log.log(Level.INFO,"exit UserDataServiceImpl deleteUser method");
		
		try {
			cnmLogger.operation(LogAction.DeleteUser, LogResult.SUCCEED, "delete user "+user.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<UserData> findUser(UserSearchData data)
			throws CnmSecurityException {
		log.info("enter findUser method");
		String hql = parse2Sql(User.class, data);
		List<User> users = userService.findUserByHql(hql); //查询后的结果
		List<UserData> resultList = new ArrayList<UserData>();
		for (int i = 0; i < users.size(); i++) {
			UserData object = new UserData();
			user2data(object, users.get(i));
			resultList.add(object);
		}
		log.info("exit findUser method");
		try {
			cnmLogger.operation(LogAction.FindUsers, LogResult.SUCCEED, "find user details");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
//		return userDataMock();
	}
	
	
	/**
	 *传输数据转换成sql语句
	 *sql = from org.opengoss.cnm.log.core.OperationLog o where o.type.name = 'operation'
	 *and o.userName = 'user'  and o.userIP = 'aa' and o.action = 'bb' and o.result = 'a' and o.desc = 'd'
	 * @param s
	 * @return
	 *
	 */
	private String parse2Sql(Class clazz, UserSearchData data ){
		String hql = FROM;
		hql = hql +BLANK+ clazz.getName() + BLANK + ALIAS +BLANK + WHERE +BLANK ;
			hql = hql+ALIAS +".name = " +"\'"+ data.getName()+"\'" + BLANK;
			try{
		if(!(data.getUserName()== null)){
			hql = hql +AND +BLANK  +ALIAS + ".userName = "+"\'"+data.getUserName()+"\'" + BLANK;
		}}catch(Exception e){
			e.printStackTrace();
		}
		if (!(data.getMail() == null)){
			hql = hql +AND  +BLANK +ALIAS + ".mail= "+"\'"+ data.getMail()+"\'" + BLANK;
		}
		if (!(data.getPhone() == null)){
			hql = hql +AND  +BLANK +ALIAS + ".phone = "+"\'"+ data.getPhone()+"\'"+ BLANK;
		}
		if (!(data.getTel()== null)){
			hql = hql +AND  +BLANK +ALIAS + ".mobile = "+"\'"+ data.getTel()+"\'" + BLANK;;
		}
		if (!(data.getDepartment()== null)){
			hql = hql +AND  +BLANK +ALIAS + ".department = "+"\'"+ data.getDepartment()+"\'"+ BLANK; ;
		}
		if (!(data.getCompany()== null)){
			hql = hql +AND  +BLANK +ALIAS + ".company = "+"\'"+ data.getCompany()+"\'"+ BLANK; ;
		}
		if (!(data.getAddress()== null)){
			hql = hql +AND  +BLANK +ALIAS + ".address = "+"\'"+ data.getAddress()+"\'" + BLANK;;
		}
		return hql;
	}
	
	/**
	 * 查询到的user转换成data
	 * @param object
	 * @param user
	 */
	private void user2data(UserData object,User user){
		
		object.setId(user.getId());
		object.setName(user.getName());
		object.setMail(user.getMail());
		object.setMobile(user.getMobile());
		object.setCompany(user.getCompany());
		object.setUserName(user.getUserName());
		object.setAddress(user.getAddress());
		object.setPhone(user.getPhone());
		object.setDepartment(user.getDepartment());
	}
	private void appendRole(UserData data,String role){
		data.setRoleInfo(role);
	}
	private String [] parseString(String s){
		String [] equalString = s.split("&");
		for (int i = 0; i < equalString.length; i++) {
			String [] subString = equalString[i].split("=");
			equalString[i] = subString[1];
		}
		return equalString;
	}

	public UserData findUserByName(String name) throws CnmSecurityException {
		return null;
	}

	public UserData load(Long id) {
		User user = userService.load(id);
		UserData data = new UserData();
		try {
			AclEntry aclEntry = aclEntryService.findAclEntryByUserId(id);
			if (aclEntry.getRole().getType().equals(Role.Type.MANAGER_ROLE)){
				List<Resource> allResc = aclEntryService.getResources();
				List<Resource> selResc = null;
				try {
					selResc = aclEntryService.findRescsByUserId(id);
				} catch (DaoException e) {
					e.printStackTrace();
				}
				if (selResc != null){
					user2data(data, user);
					appendRole(data, Role.Type.MANAGER_ROLE);
					List<String> allRescName = resc2Name(allResc);
					List<String> selRescName = resc2Name(selResc);
					for (int i = 0; i < selRescName.size(); i++) {
						allRescName.remove(selRescName.get(i));
					}
					
					data.setAllResc(allRescName);
					data.setSelResc(selRescName);
				}
				return data;
			}
			if (aclEntry.getRole().getType().equals(Role.Type.OPERATOR_ROLE)){
				user2data(data, user);
				appendRole(data, Role.Type.OPERATOR_ROLE);
				return data;
			}
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private List<String> resc2Name(List<Resource> resc) {
		List<String> rescName = new ArrayList<String>();
		for (int i = 0; i < resc.size(); i++) {
			rescName.add(resc.get(i).getRescName());
		}
		return rescName;
	}

	


	public IAclEntryService getAclEntryService() {
		return aclEntryService;
	}

	public void setAclEntryService(IAclEntryService aclEntryService) {
		this.aclEntryService = aclEntryService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String hello(){
		try {
			log.info("before cnmLogger");
//			cnmLogger.operation(LogAction.CreateUser, LogResult.SUCCEED, "in security hello");
			cnmLogger.sysInfo(LogLevel.INFO, "log level");
			log.info("after cnmLoger");
			return "hello";
		} catch (Exception e) {
			e.printStackTrace();
			return "handler eception";
		}
		
	}

	public ICnmLog getCnmLogger() {
		return cnmLogger;
	}

	public void setCnmLogger(ICnmLog cnmLogger) {
		this.cnmLogger = cnmLogger;
	}

	public void start() throws Exception {
		aclEntryService.loadInitData();
		
	}
	//for test 
	private void addUser() throws CnmSecurityException {
		UserData data= new UserData();
		data.setAddress("6");
		data.setCompany("6");
		data.setDepartment("6");
		data.setMail("6");
		data.setMobile("6");
		data.setName("6");
		data.setPassword("6");
		data.setPhone("6");
		List<String > sels = new ArrayList<String>();
		sels.add("a");
		sels.add("b");
		data.setSelResc(sels);
		data.setRoleInfo("manager");
		data.setUserName("5");
		create(data);
	}

	public void stop() throws Exception {
		
	}

	public List<String> loadResources() {
		List<Resource> resources = aclEntryService.getResources();
		List<String> rescNames = new ArrayList<String>();
		
		for (Resource resource : resources) {
			rescNames.add(resource.getRescName());
		}
		return rescNames;
	}

	public List<UserData> loadAllUser() {
		List<UserData> userData = new ArrayList<UserData>();
		try {
			List<User> users = userService.LoadAllUsers();
			for (User user : users) {
				UserData data = new UserData();
				user2data(data,user);
				userData.add(data);
			}
			return userData;
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}
}
