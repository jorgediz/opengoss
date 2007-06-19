package org.opengoss.petstore.dao;

public interface DaoFacade {

	<T> T getDao(Class<T> daoClass);
	
}
