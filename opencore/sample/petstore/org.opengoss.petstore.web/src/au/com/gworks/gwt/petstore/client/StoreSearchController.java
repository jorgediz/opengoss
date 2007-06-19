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

import org.javaongems.gwk.client.AbstractPageController;
import org.javaongems.gwk.client.SearchBar;
import org.javaongems.std.client.HistoryUtils;

import au.com.gworks.gwt.petstore.client.service.ProductInfo;
import au.com.gworks.gwt.petstore.client.service.StoreSearchRpcController;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

public class StoreSearchController 
	extends AbstractPageController 
	implements 
			TableListener
			, SearchBar.Listener
{
	private StoreSearchView view;
	private ProductInfo[] model;
	
	public void setUp(StoreCoordinator coord) {
		super.setUp(coord);
		view = new StoreSearchView();
		view.setUp(this);
		setPagePanel(coordinator.getPagePanel());
		coordinator.registerPageController(view, this);
		((StoreCoordinator)coordinator).getStoreSearchBar().setSearchListener(this);
	}
	
	protected String getPageName() {
		return "search";
	}

	protected Widget getPageView() {
		return view;
	}
	
	protected void openPage() {
		Widget w = ((StoreCoordinator)coordinator).getCartController().getSummaryView();
		coordinator.getPagePanel().setPageContext(w);
		super.openPage();
	}
	
	public void mountPageView() {
		coordinator.getPagePanel().add(view, null, "Search results", false);
	}

	protected void onControllerCallSuccess(ControllerCall call, Object result) {
		model = (ProductInfo[]) result;
		view.setModel(model);
	}

	public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
		if (row > -1) {
			String id = model[row].id;
			History.newItem("shopping" + HistoryUtils.QUERY_TOK + ShoppingController.PROD_ID + "=" + id);
		}
	}

	public void onSearch(String srcText, int sndrBtnIdx) {
		ControllerCall call = new ControllerCall();
		StoreSearchRpcController.Util.getInstance().searchForProducts(srcText, call);
		coordinator.getPagePanel().selectPage(view);
	}
}
