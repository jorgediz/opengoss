package org.opengoss.petstore.dao.internal;

import org.opengoss.core.IPluginContext;
import org.opengoss.core.PluginActivator;
import org.opengoss.petstore.dao.AccountDao;
import org.opengoss.petstore.dao.DaoFacade;

public class Activator extends PluginActivator {

	@Override
	protected void startPlugin(IPluginContext pluginContext) throws Exception {
		System.out.println(ClassLoader.getSystemClassLoader());
		DaoFacade daoFacade = (DaoFacade)pluginContext.getServiceRegistry().getService("DaoFacade");
		System.out.println(daoFacade.getDao(AccountDao.class).getUsernameList());
	}

	@Override
	protected void stopPlugin(IPluginContext pluginContext) throws Exception {
		// TODO Auto-generated method stub
	}

}
