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
 * @author Dimax Pradi
 */
package au.com.gworks.gwt.petstore.client;

import org.javaongems.gwk.client.AbstractPageController;

import au.com.gworks.gwt.petstore.client.service.AccountInfo;
import au.com.gworks.gwt.petstore.client.service.AccountRpcController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class AccountController extends AbstractPageController 
    implements AccountFormView.Listener {
  
  AccountFormView view;
  AccountInfo accountInfo;
  boolean newAccount = true;
  
  public void setUp(StoreCoordinator coord) {
	super.setUp(coord);
    view = new AccountFormView(this);
    setPagePanel(coordinator.getPagePanel());
    coordinator.registerPageController(view, this);
    view.setFormListener(this);
  }

  protected String getPageName() {
    return "account";
  }

  protected Widget getPageView() {
    return view;
  }

  public void mountPageView() {
    pagePanel.add(view, null, "Account", false);
  }

  protected void onControllerCallSuccess(ControllerCall call, Object result) {
    // TODO Auto-generated method stub
  }

  public void openPage() {
    if (isNewAccount()) {
      view.setTitleBarLabel("New Account");
      view.clearForm();
    } else {
      view.setTitleBarLabel("Account");
      retrieveAccount();
    }
    Widget w = ((StoreCoordinator)coordinator).getCartController().getSummaryView();
    pagePanel.setPageContext(w);
    super.openPage();
  }

  // ================================================================= Properties.

  public boolean isNewAccount() { return newAccount; }
  public void setNewAccount(boolean newAccount) { this.newAccount = newAccount; }

  public AccountInfo getAccountInfo() { return accountInfo; }
  public void setAccountInfo(AccountInfo accountInfo) { this.accountInfo = accountInfo; }
  
  // ================================================================= Business Method.

  private void createAccount() {
    GWT.log("[TRACE] AccountController.createAccount", null);
    AccountInfo info = (AccountInfo)view.getInfo();
    ControllerCall cb = new ControllerCall() {
      public void onFailure(Throwable caught) {
        Window.alert("Account exist");
      }
      public void onSuccess(Object result) {
        AccountInfo infoResult = (AccountInfo) result;
        if (infoResult != null) {
          GWT.log("[DEBUG] Account created and login", null);
          ((StoreCoordinator)coordinator).updateLoginStatus(true, infoResult);
          History.newItem("shopping");
        } else {
          History.newItem("welcome");
        }
      }
    };
    AccountRpcController.Util.getInstance().createAccount(info, cb);
  }
  
  private void retrieveAccount() {
    GWT.log("[TRACE] AccountController.retrieveAccount", null);
    if (accountInfo != null) {
      ControllerCall cb = new ControllerCall() {
        public void onSuccess(Object result) {
          GWT.log("[DEBUG] Account retrieved.", null);
          accountInfo = (AccountInfo) result;
          view.setInfo(accountInfo);
        }
      };
      GWT.log("[DEBUG] Retrieving=" + accountInfo.username, null);
      AccountRpcController.Util.getInstance().retrieveAccount(
          accountInfo.username, cb);
    } else { 
      Window.alert("Please Sign In first.");
    }
  }
  
  private void updateAccount() {
    GWT.log("[TRACE] AccountController.updateAccount", null);
    AccountInfo info = (AccountInfo)view.getInfo();
    ControllerCall cb = new ControllerCall() {
      public void onFailure(Throwable caught) {
        Window.alert("Update failed.");
      }
      public void onSuccess(Object result) {
        AccountInfo infoResult = (AccountInfo) result;
        if (infoResult != null) {
          GWT.log("[DEBUG] Account updated and login.", null);
          ((StoreCoordinator)coordinator).updateLoginStatus(true, infoResult);
          History.newItem("shopping");
        } else {
          History.newItem("welcome");
        }
      }
    };
    AccountRpcController.Util.getInstance().updateAccount(info, cb);
  }
  // ================================================================= Listener Method.

  public void onCancel() {
    Window.alert("onCancel");
  }
  public void onSubmit(String btnTitle) {
    if ("Submit".equals(btnTitle)) {
      if (accountInfo == null) createAccount();
      else updateAccount();
    }
  }
  
}
