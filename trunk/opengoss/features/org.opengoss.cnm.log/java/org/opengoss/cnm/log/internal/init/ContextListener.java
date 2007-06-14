package org.opengoss.cnm.log.internal.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.opengoss.cnm.log.core.service.ILogService;
import org.opengoss.core.IPluginContext;
import org.opengoss.web.core.IWebPlugin;
import org.opengoss.web.service.IWSContainer;

import com.metaparadigm.jsonrpc.JSONRPCBridge;

public class ContextListener implements ServletContextListener,HttpSessionListener {

	private ILogService logService;

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		//这几行代码干吗的？
		IWebPlugin webPlugin = (IWebPlugin)context.getAttribute("WebPlugin");
		IPluginContext pluginContext = webPlugin.getContext();
		IWSContainer wsContainer = (IWSContainer)pluginContext
			.getServiceRegistry().getService("WebCore:WSContainer");
		context.setAttribute("WSContainer", wsContainer);
		logService = (ILogService)pluginContext.getServiceRegistry().getService("LogService");
	}
	public void contextDestroyed(ServletContextEvent sce) {
		//maybe close server class

	}
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
//		ServletContext sContext = session.getServletContext();
/*		System.out.println("ServletContext:"+sContext);
		IWebPlugin webPlugin = (IWebPlugin)sContext.getAttribute("WebPlugin"); //不能得到WebPlugin
		IPluginContext pluginContext = webPlugin.getContext();
		System.out.println("pluginContext:"+pluginContext);
*/		JSONRPCBridge jsonBridge = null;
		jsonBridge = (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
		if(jsonBridge == null){
			jsonBridge = new JSONRPCBridge();
			session.setAttribute("JSONRPCBridge",jsonBridge);
		}
		jsonBridge.registerObject("logService",logService);//register service
	}
	public void sessionDestroyed(HttpSessionEvent se) {
	
		
	}
}
