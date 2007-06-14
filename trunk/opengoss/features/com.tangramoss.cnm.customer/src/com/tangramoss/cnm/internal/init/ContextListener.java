package com.tangramoss.cnm.internal.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.opengoss.core.IPluginContext;
import org.opengoss.web.core.IWebPlugin;
import org.opengoss.web.service.IWSContainer;

import com.metaparadigm.jsonrpc.JSONRPCBridge;
import com.tangramoss.cnm.customer.service.ICustomerMgr;

public class ContextListener implements ServletContextListener,HttpSessionListener{

	private ICustomerMgr customerMgr;
	
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		IWebPlugin webPlugin = (IWebPlugin)context.getAttribute("WebPlugin");
		IPluginContext pluginContext = webPlugin.getContext();
		IWSContainer wsContainer = (IWSContainer)pluginContext
			.getServiceRegistry().getService("WebCore:WSContainer");
		context.setAttribute("WSContainer", wsContainer);
		customerMgr = (ICustomerMgr) pluginContext.getServiceRegistry().getService("CustomerMgr");
	}

	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		JSONRPCBridge jsonBridge = null;
		jsonBridge = (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
		if(jsonBridge == null){
			jsonBridge = new JSONRPCBridge();
			session.setAttribute("JSONRPCBridge",jsonBridge);
		}
		jsonBridge.registerObject("customerMgr", customerMgr);
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		
	}

}