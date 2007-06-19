package org.opengoss.petstore.dao.ibatis;

import org.opengoss.petstore.dao.DaoFacade;

import com.ibatis.dao.client.DaoManager;

public class SqlMapDaoFacade implements DaoFacade {
	
	private DaoManager daoManager;

	public SqlMapDaoFacade(DaoManager daoManager) {
		this.daoManager = daoManager;
	}

	public <T> T getDao(Class<T> daoClass) {
		return (T)daoManager.getDao(daoClass);
	}

}
