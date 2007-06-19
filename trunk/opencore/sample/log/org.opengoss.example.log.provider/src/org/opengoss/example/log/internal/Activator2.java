package org.opengoss.example.log.internal;

import org.opengoss.core.IPluginContext;
import org.opengoss.core.IPluginServiceRegistry;
import org.opengoss.core.PluginActivator;
import org.opengoss.example.log.IMyLogService;

public class Activator2 extends PluginActivator {

	@Override
	protected void startPlugin(IPluginContext context) throws Exception {
		IPluginServiceRegistry registry = context.getServiceRegistry();

		IMyLogService myLogService = (IMyLogService)registry.getService("MyLogService");
		myLogService.log("Start 1...");
		
		myLogService = registry.getService(IMyLogService.class);
		myLogService.log("Start 2...");
	}

	@Override
	protected void stopPlugin(IPluginContext context) throws Exception {
		IPluginServiceRegistry registry = context.getServiceRegistry();

		IMyLogService myLogService = (IMyLogService)registry.getService("MyLogService");
		myLogService.log("Stop...");
	}

}
