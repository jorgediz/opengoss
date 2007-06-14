package org.opengoss.alarm.manager.internal.core;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.opengoss.alarm.manager.core.IAlarmInfoService;
import org.opengoss.alarm.manager.core.IManagerService;
import org.opengoss.alarm.manager.core.IOperatorRuleService;
import org.opengoss.alarm.manager.core.IPersonalInfoService;
import org.opengoss.core.IPluginContext;
import org.opengoss.web.core.IWebPlugin;
import org.opengoss.web.service.IWSContainer;

import com.metaparadigm.jsonrpc.JSONRPCBridge;

public class ExportServices implements ServletContextListener,
		HttpSessionListener {
	private IManagerService managerService;
	
	private IPersonalInfoService personalService;
	
	private IAlarmInfoService alarmInfoService;

	private IOperatorRuleService operatorRuleService;

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		IWebPlugin webPlugin = (IWebPlugin) context.getAttribute("WebPlugin");
		IPluginContext pluginContext = webPlugin.getContext();
		IWSContainer wsContainer = (IWSContainer) pluginContext
				.getServiceRegistry().getService("WebCore:WSContainer");
		context.setAttribute("WSContainer", wsContainer);

		managerService = (IManagerService) pluginContext.getServiceRegistry()
				.getService("ManagerService");
		personalService = (IPersonalInfoService) pluginContext.getServiceRegistry()
		.getService("PersonalInfoService");

		/** hrr */
		alarmInfoService = (IAlarmInfoService) pluginContext
				.getServiceRegistry().getService("AlarmService");
		operatorRuleService = (IOperatorRuleService) pluginContext
				.getServiceRegistry().getService("OperatorRuleService");

	}

	public void contextDestroyed(ServletContextEvent sce) {

	}

	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();

		JSONRPCBridge jsonBridge = null;
		jsonBridge = (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
		if (jsonBridge == null) {
			jsonBridge = new JSONRPCBridge();
			session.setAttribute("JSONRPCBridge", jsonBridge);
		}

		jsonBridge.registerObject("ManagerService", managerService);
		
		jsonBridge.registerObject("PersonalInfoService", personalService);
																
		jsonBridge.registerObject("AlarmInfoService", alarmInfoService);
		jsonBridge.registerObject("AlarmRuleService", operatorRuleService);
	}

	public void sessionDestroyed(HttpSessionEvent se) {

	}

}
