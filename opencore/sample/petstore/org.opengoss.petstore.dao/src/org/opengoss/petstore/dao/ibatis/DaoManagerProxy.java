package org.opengoss.petstore.dao.ibatis;

import java.io.IOException;
import java.io.InputStreamReader;

import org.opengoss.core.IServiceProxy;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.DaoManagerBuilder;

public class DaoManagerProxy implements IServiceProxy {

	DaoManager daoManager = null;

	public synchronized Object getService() {
		if (daoManager == null) {
			daoManager = buildDaoManager();
		}
		return daoManager;
	}

	private DaoManager buildDaoManager() {
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(DaoManagerProxy.class.getClassLoader()
					.getResourceAsStream("dao.xml"));
			return DaoManagerBuilder.buildDaoManager(reader, null);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
