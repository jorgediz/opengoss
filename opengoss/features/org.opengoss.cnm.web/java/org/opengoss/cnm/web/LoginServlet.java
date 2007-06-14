package org.opengoss.cnm.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.cnm.security.core.Navigator;
import org.opengoss.cnm.security.core.service.IAclEntryService;
import org.opengoss.core.IPluginContext;
import org.opengoss.web.core.IWebPlugin;

/**
 * 这个类负责调用后端业务进行用户登录，如果用户成功登录，则进入mian.html，并传入相应的用户信息
 * ，否则回到登录页面，并在其上显示错误信息。
 */
public class LoginServlet extends HttpServlet {
	
	private Log log = LogFactory.getLog(LoginServlet.class);
	private  IAclEntryService aclEntryService; //用户管理服务
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		IWebPlugin webPlugin = (IWebPlugin) getServletContext().getAttribute("WebPlugin");
		 IPluginContext pluginContext = webPlugin.getContext();
		 aclEntryService= (IAclEntryService) pluginContext.getServiceRegistry().getService("Security:AclEntryService");
		 if (aclEntryService == null){
			 log.error("no userService");
		 }
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doProcess(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doProcess(request,response);
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException {
        /** todo：添加用户的操作目前没有想清楚，暂时没有做
         * 1.设置用户名称
         * 2.根据用户类型查找用户菜单项，并存入session
         * 执行页面跳转
         */
    	
    	
    	String name = request.getParameter("name");
    	String pwd = request.getParameter("password");
    	Navigator navigator = aclEntryService.authenticate(name, pwd);
    	if (navigator  == null){
    		request.getRequestDispatcher("index.html").forward(request,response);
    	}else {
    		request.getSession().setAttribute("navigator",navigator);
            request.getSession().setAttribute("modifyProfileUrl","http://www.google.com");
            request.getRequestDispatcher("main.html").forward(request,response);
    	} 	
    
     }

}
