/**
 * Copyright 2006 gworks.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. (http://www.apache.org/licenses/LICENSE-2.0)
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed 
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
 * the specific language governing permissions and limitations under the License.
 *
 * <dhamma-message src="Atisha (11th century Tibetan Buddhist master)">
 *  The greatest achievement is selflessness. The greatest worth is self-mastery.
 *  The greatest quality is seeking to serve others. The greatest precept is continual awareness.
 *  The greatest medicine is the emptiness of everything. The greatest action is not conforming with the worlds ways.
 *  The greatest magic is transmuting the passions. The greatest generosity is non-attachment.
 *  The greatest goodness is a peaceful mind. The greatest patience is humility.
 *  The greatest effort is not concerned with results. The greatest meditation is a mind that lets go.
 *  The greatest wisdom is seeing through appearances. 
 * </dhamma-message>
 * 
 * @author Ashin Wimalajeewa (ash)
 */
package au.com.gworks.gwt.petstore.client;

import java.util.Iterator;

import org.javaongems.gwk.client.AbstractPageController;
import org.javaongems.gwk.client.AbstractPageCoordinator;
import org.javaongems.gwk.client.LogOutputController;
import org.javaongems.gwk.client.PagePanel;
import org.javaongems.std.client.Gem;
import org.javaongems.std.client.io.StringPrintStream;
import org.javaongems.std.client.service.EndpointUtils;

import au.com.gworks.gwt.petstore.client.service.AccountInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class StoreCoordinator extends AbstractPageCoordinator implements
		EndpointUtils.Adapter {

	static private StoreCoordinator instance;
	
	private StoreView view;
	private WelcomeController welcomeController = new WelcomeController();
	private ShoppingController shoppingController = new ShoppingController();
	private CartController cartController = new CartController();
	private AccountController accountController = new AccountController();
	private AccountSignInController signinController = new AccountSignInController();
	private StoreSearchController storeSearchController = new StoreSearchController();
	private LogOutputController logOutputController = new LogOutputController();

	public StoreCoordinator() {
		instance = this;
	}

	public void setUp(RootPanel mountPoint) {
		super.setUp(mountPoint);
		Gem.stone = new SysGem();
		if (GWT.isScript())
			System.setErr(StringPrintStream.ERROR_BUFFER);
		EndpointUtils.setInstance(this);
		welcomeController.setUp(this);
		shoppingController.setUp(this);
		cartController.setUp(this);
		accountController.setUp(this);
		storeSearchController.setUp(this);
		signinController.setUp(this);
		if (Gem.stone.isDebugEnabled())
			logOutputController.setUp(this);
		for (Iterator iter = controllers.iterator(); iter.hasNext();) {
			AbstractPageController ctrl = (AbstractPageController) iter.next();
			ctrl.mountPageView();
		}
	}

	public Widget getCoordinatedView() {
		if (view == null)
			view = new StoreView();
		return view;
	}

	public PagePanel getPagePanel() {
		getCoordinatedView();
		return view.getPagePanel();
	}

	public WelcomeController getWelcomeController() {
		return welcomeController;
	}

	public CartController getCartController() {
		return cartController;
	}

	public AccountController getAccountController() {
		return accountController;
	}

	public void updateLoginStatus(boolean isUserLogin, AccountInfo accInfo) {
		getAccountController().setNewAccount(!isUserLogin);
		getAccountController().setAccountInfo(accInfo);
		view.updateLoginStatus(isUserLogin, accInfo);
	}

	public StoreSearchBar getStoreSearchBar() {
		return view.getStoreSearchBar();
	}

	/**
	 * The purpose of this endpoint adaptation is:
	 * <ul>
	 * <li> [hosted mode] shell installed under "/shell" subcontext
	 * <li> [hosted mode] rpc available under root context
	 * <li> [web mode] no shell
	 * <li> [web mode] rpc avaialble under context of applcation
	 * </ul>
	 */
	public String adapt(String service) {
		String base = service;
		boolean isShellServer = "true".equals(Gem.getProperty("isShellServer"));
		if (GWT.isScript() && !isShellServer)
			base = GWT.getModuleBaseURL() + service;
		return base;
	}

	public class SysGem extends Gem {
		private boolean debug;
		
		public SysGem() {
			String val = getProperty("isDebugEnabled");
			debug = ("true".equals(val));
		}
		
		public boolean isDebugEnabled() {
			return debug;
		}
		
		protected void internalLog(String caller, String msg, Throwable e) {
			GWT.log(msg, e);
			if (Gem.stone.isDebugEnabled()) {
				if (caller != null)
					msg = caller + ":" + msg;
				logOutputController.log(msg, e);
			}
		}
	}

	static public StoreCoordinator getInstance() {
		return (StoreCoordinator) instance;
	}
}
