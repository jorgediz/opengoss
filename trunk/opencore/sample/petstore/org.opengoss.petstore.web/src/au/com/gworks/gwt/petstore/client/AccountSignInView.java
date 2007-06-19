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

import org.javaongems.gwk.client.SimpleFormPanel;

import au.com.gworks.gwt.petstore.client.service.AccountInfo;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AccountSignInView extends DialogBox implements ClickListener {
  public interface Listener extends SimpleFormPanel.Listener {

  }

  SimpleFormPanel form = new SimpleFormPanel();
  VerticalPanel container = new VerticalPanel();

  public AccountSignInView() {
    setText("Sign In");
    setSize("300", "125");
    setStyleName("ps-SignIn-DialogBox");

    form.addInputText("Username", 20, 15);
    form.addPasswordInputText("Password", 15);
    form.addButton("Sign In");
    form.addButton("Register");
    container.add(form);

    Button btnCLose = new Button("Close", this);
    container.add(btnCLose);

    setWidget(container);
  }

  public void setFormListener(SimpleFormPanel.Listener listener) {
    form.setListener(listener);
  }
  
  public void clearForm() {
    form.clearForm();
  }

  public void onClick(Widget sender) {
    hide();
  }

  public Object getInfo() {
    AccountInfo info = new AccountInfo(
        form.getTextBox("Username").getText(), 
        form.getPasswordTextBox("Password").getText(), 
        null, null, null);
    return info;
  }

}
