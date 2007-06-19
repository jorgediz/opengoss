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

import org.javaongems.std.client.DivPanel;
import org.javaongems.std.client.EastWestHorizPanels;

import au.com.gworks.gwt.petstore.client.service.ProductInfo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;

public class StoreSearchView extends Composite {
	private EastWestHorizPanels groupBarContainer;
	private FlexTable items = new FlexTable();
	private DivPanel container = new DivPanel();
	private Label emptyCartLbl = new Label("No seach results");
	
	private ProductInfo[] searchModel;
//	private StoreSearchController controller;
	
	public StoreSearchView() {
		initWidget(container);
		
		setStyleName(getElement(), "ps-CartView", true);
		buildFrame();
	}
	
	public void setUp(StoreSearchController searchController) {
//		controller = searchController;
		items.addTableListener(searchController);
	}
	
	public void setModel(ProductInfo[] pi) {
		searchModel = pi;
		while (items.getRowCount() > 0)
			items.removeRow(0);
		if (searchModel == null || searchModel.length == 0) {
			emptyCartLbl.setVisible(true);
			return;
		}
		
		RowFormatter fmtr = items.getRowFormatter();
		for (int i = 0; i < searchModel.length; i++) {
			int row = items.insertRow(i);
			for (int j = 0; j < 2; j++)
				items.insertCell(row, j);
			items.setText(row, 0, searchModel[i].name);
			items.setText(row, 1, searchModel[i].description);
			fmtr.addStyleName(row, "ps-GridRow ps-GridRow-sel");
		}			
		emptyCartLbl.setVisible(false);
	}

	private void buildFrame() {
		groupBarContainer = new EastWestHorizPanels();
		container.add(groupBarContainer);
		groupBarContainer.setStyleName("ps-ewp-mainGroupSections");

		Label title = new Label("Store search");
		groupBarContainer.getWest().add(title);
		title.setStyleName("ps-ewp-mainGroupSections-txt");

		container.add(items, "std-client-width");
		container.add(emptyCartLbl);
	}
}
