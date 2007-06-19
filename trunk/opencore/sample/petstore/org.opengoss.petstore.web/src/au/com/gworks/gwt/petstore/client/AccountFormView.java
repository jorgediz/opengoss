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
import org.javaongems.gwk.client.SimpleFormPanel;
import org.javaongems.std.client.EastWestHorizPanels;

import au.com.gworks.gwt.petstore.client.service.AccountInfo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AccountFormView extends Composite {
  public interface Listener extends SimpleFormPanel.Listener {}
  
  private VerticalPanel container = new VerticalPanel();

  private Label titleLabel = new Label("Account");
  private EastWestHorizPanels titleBar = new EastWestHorizPanels();
  private SimpleFormPanel form = new SimpleFormPanel();

  public AccountFormView(AbstractPageController controller) {
    
    initWidget(container);

    titleLabel.setStyleName("ps-ewp-mainGroupSections-txt");
    titleBar.setStyleName("ps-ewp-mainGroupSections");
    titleBar.getWest().add(titleLabel);
    container.add(titleBar);

    form.addInputText("User ID", 15, 12);
    form.addInputText("Password", 15, 12);
    form.addInputText("First name", 40, 30);
    form.addInputText("Last name", 40, 30);
    form.addInputText("Email", 40, 30);
    form.addButton("Submit");
    container.add(form);
  }

  public void setFormListener(SimpleFormPanel.Listener listener) {
    form.setListener(listener);
  }
  
  public void setTitleBarLabel(String title) {
    this.titleLabel.setText(title);
  }
  
  public void clearForm() {
    form.clearForm();
  }

  public Object getInfo() {
    AccountInfo res = new AccountInfo(
        form.getTextBox("User ID").getText(),
        form.getTextBox("Password").getText(),
        form.getTextBox("Email").getText(),
        form.getTextBox("First name").getText(),
        form.getTextBox("Last name").getText()
    );
    return res;
  }
  
  public void setInfo(Object model) {
    AccountInfo accInfo = (AccountInfo) model;
    form.getTextBox("User ID").setText(accInfo.username);
    form.getTextBox("Password").setText(accInfo.password);
    form.getTextBox("First name").setText(accInfo.firstName);
    form.getTextBox("Last name").setText(accInfo.lastName);
    form.getTextBox("Email").setText(accInfo.email);
  }

}
