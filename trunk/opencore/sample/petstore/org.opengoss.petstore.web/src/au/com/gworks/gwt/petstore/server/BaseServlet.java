package au.com.gworks.gwt.petstore.server;

import javax.servlet.ServletException;

import org.opengoss.petstore.dao.DaoFacade;
import org.opengoss.web.core.IWebPlugin;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BaseServlet extends RemoteServiceServlet {

	private DaoFacade daoFacade;

	@Override
	public void init() throws ServletException {
		super.init();
		IWebPlugin webPlugin = (IWebPlugin) getServletContext().getAttribute(
				"WebPlugin");
		this.daoFacade = (DaoFacade) webPlugin.getContext()
				.getServiceRegistry().getService("PetStoreDao:DaoFacade");
	}

	protected final DaoFacade getDaoFacade() {
		if (daoFacade == null) {
			throw new IllegalStateException("No DaoFacade Found!");
		}
		return daoFacade;
	}

}
