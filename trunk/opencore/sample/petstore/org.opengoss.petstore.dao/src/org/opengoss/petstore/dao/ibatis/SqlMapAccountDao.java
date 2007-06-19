package org.opengoss.petstore.dao.ibatis;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.AccountDao;
import org.opengoss.petstore.dao.domain.Account;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

/**
 * In this and other DAOs in this package, a DataSource property is inherited
 * from the SqlMapClientDaoSupport convenience superclass supplied by Spring.
 * DAOs don't need to extend such superclasses, but it saves coding in many
 * cases. There are analogous superclasses for JDBC (JdbcDaoSupport), Hibernate
 * (HibernateDaoSupport), JDO (JdoDaoSupport) etc.
 * 
 * <p>
 * This and other DAOs are configured using Dependency Injection. This means,
 * for example, that Spring can source the DataSource from a local class, such
 * as the Commons DBCP BasicDataSource, or from JNDI, concealing the JNDI lookup
 * from application code.
 * 
 * @author Juergen Hoeller
 * @author Colin Sampaleanu
 */
public class SqlMapAccountDao extends SqlMapDaoTemplate implements
		AccountDao {

	public SqlMapAccountDao(DaoManager daoManager) {
		super(daoManager);
	}

	public Account getAccount(String username) throws DaoException {
		return (Account) queryForObject(
				"getAccountByUsername", username);
	}

	public Account getAccount(String username, String password)
			throws DaoException {
		Account account = new Account();
		account.setUsername(username);
		account.setPassword(password);
		return (Account) queryForObject(
				"getAccountByUsernameAndPassword", account);
	}

	public void insertAccount(Account account) throws DaoException {
		insert("insertAccount", account);
		insert("insertProfile", account);
		insert("insertSignon", account);
	}

	public void updateAccount(Account account) throws DaoException {
		update("updateAccount", account);
		update("updateProfile", account);
		if (account.getPassword() != null && account.getPassword().length() > 0) {
			update("updateSignon", account);
		}
	}

	public List getUsernameList() throws DaoException {
		return queryForList("getUsernameList", null);
	}

}
