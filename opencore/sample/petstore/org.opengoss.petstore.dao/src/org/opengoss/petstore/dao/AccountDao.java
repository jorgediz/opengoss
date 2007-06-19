package org.opengoss.petstore.dao;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.domain.Account;

public interface AccountDao {

	Account getAccount(String username) throws DaoException;

	Account getAccount(String username, String password) throws DaoException;

	void insertAccount(Account account) throws DaoException;

	void updateAccount(Account account) throws DaoException;

	List getUsernameList() throws DaoException;

}
