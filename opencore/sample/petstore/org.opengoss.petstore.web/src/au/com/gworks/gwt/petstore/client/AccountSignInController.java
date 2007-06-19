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

import java.util.Map;

import org.javaongems.gwk.client.AbstractPageController;

import au.com.gworks.gwt.petstore.client.service.AccountInfo;
import au.com.gworks.gwt.petstore.client.service.AccountRpcController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class AccountSignInController extends AbstractPageController
  implements AccountSignInView.Listener {
  
  private AccountSignInView view;
  
  public void setUp(StoreCoordinator coord) {
	  super.setUp(coord);
    view = new AccountSignInView();
    setPagePanel(coordinator.getPagePanel());
    coordinator.registerPageController(view, this);
    view.setFormListener(this);
  }
  
  public void showView() {
    int xPos = (Window.getClientWidth()/3);
    int yPos = (Window.getClientHeight()/3);
    
    view.setPopupPosition(xPos, yPos);
    view.clearForm();
    view.show();
  }
  
  protected String getPageName() {
    return "sign-in";
  }

  protected Widget getPageView() {
    return view;
  }

  public void mountPageView() {
    // This view is not mounted on the PagePanel.
  }

  protected void onControllerCallSuccess(ControllerCall call, Object result) {
    
  }
  
  public void onHistoryChange(String historyToken, Map queryMap) {
    if (queryMap.size() > 0) signOut();
    else showView();
    
  }

  // ================================================================= Business Method.
  
  private void signIn() {
    GWT.log("[TRACE] AccountSignInController.signIn", null);
    AccountInfo info = (AccountInfo)view.getInfo();
    view.hide();
    ControllerCall cb = new ControllerCall() {
      public void onSuccess(Object result) {
        AccountInfo infoResult = (AccountInfo) result;
        if (infoResult != null) {
          GWT.log("[DEBUG] Login success.", null);
          ((StoreCoordinator)coordinator).updateLoginStatus(true, infoResult);
          History.newItem("shopping");
        } else {
          GWT.log("[DEBUG] Login failed.", null);
          Window.alert("Wrong username and password");
          History.newItem("welcome");
        }
      }
    };
    AccountRpcController.Util.getInstance().signIn(info, cb);
  }

  private void registerAccount() {
    view.hide();
    ((StoreCoordinator)coordinator).getAccountController().setNewAccount(true);
    History.newItem("account");
  }
  
  private void signOut() {
    GWT.log("[TRACE] AccountSignInController.signOut", null);
    ControllerCall cb = new ControllerCall() {
      public void onSuccess(Object result) {
        GWT.log("[DEBUG] Logout success.", null);
        History.newItem("welcome");
      }
    };
    AccountRpcController.Util.getInstance().signOut(cb);
    ((StoreCoordinator)coordinator).updateLoginStatus(false, null);
  }

  // ================================================================= Listener Method.

  public void onCancel() {
    // TODO Auto-generated method stub
  }
  public void onSubmit(String btnTitle) {
    if ("Sign In".equals(btnTitle)) {
      signIn();
    } else if ("Register".equals(btnTitle)) {
      registerAccount();
    }
  }

}
