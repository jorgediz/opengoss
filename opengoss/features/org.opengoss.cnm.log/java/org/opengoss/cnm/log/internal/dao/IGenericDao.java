package org.opengoss.cnm.log.internal.dao;

import java.util.List;

import org.opengoss.cnm.log.core.LoggerItemList;

/**
 * this interface mainly provides load and save informaiiton of LoggerItemList class
 * @author zouxc
 * @date 2006-12-14
 * deprecated
 */
public interface IGenericDao {

	/**
	 * load object of specifid class from db 
	 * @param clazz
	 * @return
	 */
	public LoggerItemList load(Class clazz);
	
	/**
	 * save object of specified class to db
	 * @param object
	 */
	public void save (LoggerItemList object);
}
