package org.opengoss.cnm.security.internal.init;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.opengoss.cnm.security.core.service.IUserDataService;
import org.opengoss.cnm.security.internal.service.UserService;
import org.opengoss.core.IPluginContext;
import org.opengoss.web.core.IWebPlugin;
import org.opengoss.web.service.IWSContainer;

import com.metaparadigm.jsonrpc.JSONRPCBridge;

public class ContextListener implements ServletContextListener,HttpSessionListener{

	private IUserDataService userDataService;
	
	private Logger log = Logger.getLogger("security ContextListener");
	private UserService userService;
//	private ICnmLog cnmLogger;
	
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		//这几行代码干吗的？
		IWebPlugin webPlugin = (IWebPlugin)context.getAttribute("WebPlugin");
		IPluginContext pluginContext = webPlugin.getContext();
		IWSContainer wsContainer = (IWSContainer)pluginContext
			.getServiceRegistry().getService("WebCore:WSContainer");
		context.setAttribute("WSContainer", wsContainer);
		
		userDataService = (IUserDataService) pluginContext.getServiceRegistry().getService("UserDataService");
		userService = (UserService) pluginContext.getServiceRegistry().getService("UserService");
		context.setAttribute("UserService", userService);
		if (userService == null){
			log.warning("no userService");
		}else{
			log.info("set Service");
		}
//		cnmLogger = (ICnmLog) pluginContext.getServiceRegistry().getService("log:CnmLogger");
//		System.out.println("catch cnmlogger");
	}

	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		JSONRPCBridge jsonBridge = null;
		jsonBridge = (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
		if(jsonBridge == null){
			jsonBridge = new JSONRPCBridge();
			session.setAttribute("JSONRPCBridge",jsonBridge);
		}
		jsonBridge.registerObject("userService",userDataService);
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		
	}

}
