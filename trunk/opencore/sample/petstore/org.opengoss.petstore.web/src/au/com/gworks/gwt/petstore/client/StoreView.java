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

import org.javaongems.gwk.client.ApplicationLinkBar;
import org.javaongems.gwk.client.PagePanel;
import org.javaongems.std.client.DivPanel;
import org.javaongems.std.client.HistoryUtils;

import au.com.gworks.gwt.petstore.client.service.AccountInfo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StoreView extends Composite {
	private PagePanel storeView;
	private ApplicationLinkBar storeLinkBar;
	private DivPanel container;
	private StoreSearchBar storeSearchBar;
	
	public StoreView() {
		container = new DivPanel();
		initWidget(container);

		storeLinkBar = new ApplicationLinkBar();
		container.add(storeLinkBar);

		Image logoImg = new Image("gworks_logo2.gif");
		storeLinkBar.getWestSide().add(logoImg);

		updateLoginStatus(false, null);
    
		storeView = new PagePanel();
		container.add(storeView);
		
		storeSearchBar = new StoreSearchBar();
		storeView.setSearchPanel(storeSearchBar);
		
		setSize("100%", "100%");
	}
	
	public void setPageContextWidget(Widget w) {
		storeView.setPageContext(w);
	}

	public PagePanel getPagePanel() {
		return storeView;
	}
	
	public void setPagePanelListener(PagePanel.Listener lsnr) {
		storeView.setListener(lsnr);
	}
	
	public StoreSearchBar getStoreSearchBar() {
		return storeSearchBar;
	}
	
	public void setPixelSize(int width, int height) {
		super.setPixelSize(width, height);
		int linkBarHt = storeLinkBar.getOffsetHeight();
		int diff = height - linkBarHt;
		storeView.setPixelSize(width, diff);
	}
  
  public void updateLoginStatus(boolean isLogin, AccountInfo info) {
    storeLinkBar.getEastLinks().clear();
    if (!isLogin) {
      Hyperlink hl1 = new Hyperlink("Help", "shopping" + HistoryUtils.QUERY_TOK + "item-id=EST-27&test=one");
      Hyperlink hl2 = new Hyperlink("Sign in", "sign-in");
      storeLinkBar.getEastLinks().addHyperLink(hl1);
      storeLinkBar.getEastLinks().addHyperLink(hl2);
    } else {
      Label l1 = new Label(info.email);
      storeLinkBar.getEastLinks().addLabel(l1);
      Hyperlink hl1 = new Hyperlink("Help", "shopping" + HistoryUtils.QUERY_TOK + "item-id=EST-27&test=one");
      Hyperlink hl2 = new Hyperlink("Account", "account");
      Hyperlink hl3 = new Hyperlink("Sign Out", "sign-in" + HistoryUtils.QUERY_TOK + "out=ok");
      storeLinkBar.getEastLinks().addHyperLink(hl1);
      storeLinkBar.getEastLinks().addHyperLink(hl2);
      storeLinkBar.getEastLinks().addHyperLink(hl3);
    }
  }
}
