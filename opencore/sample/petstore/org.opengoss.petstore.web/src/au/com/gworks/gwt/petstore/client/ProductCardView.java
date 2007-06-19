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

import org.javaongems.gwk.client.Card;
import org.javaongems.std.client.EastWestHorizPanels;
import org.javaongems.std.client.HistoryUtils;

import au.com.gworks.gwt.petstore.client.service.ItemInfo;
import au.com.gworks.gwt.petstore.client.service.ItemRefInfo;
import au.com.gworks.gwt.petstore.client.service.ProductInfo;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

public class ProductCardView extends Card implements ClickListener {
	private ItemRefInfo itemRefInfo;
	private ProductInfo productInfo;
	
	private boolean builtBody = false;
	
	private EastWestHorizPanels titleBar = new EastWestHorizPanels();
	private Grid productDetails = new Grid();
	
	// title bar
	private Label itemIdLbl = new Label();
	private Label descLbl = new Label();
	private Label listPriceLbl = new Label();
	private Button addToCartBtn = new Button("Add to cart");
	
	// card body 
	private Image itemImg = new Image();
	
	public ProductCardView() {
		setTitleWidget(titleBar);
		setBodyWidget(productDetails);
		buildTitleFrame();
		addStyleName("ps-ProductCardView");
	}
	
	public ItemRefInfo getShelfRefInfo() {
		return itemRefInfo;
	}

	public void setProductInfo(ProductInfo productInfo) {
		this.productInfo = productInfo;
		itemImg.setUrl(productInfo.url);
	}

	public void setShelfRefInfo(ItemRefInfo shelfRefInfo) {
		this.itemRefInfo = shelfRefInfo;
		itemIdLbl.setText(shelfRefInfo.id);
		descLbl.setText(shelfRefInfo.description);
		String lp = "$" + shelfRefInfo.listPrice;
		listPriceLbl.setText(lp);
		prepareCardBody(shelfRefInfo, lp);
	}

	private void prepareCardBody(ItemRefInfo shelfRefInfo, String lp) {
		if (shelfRefInfo instanceof ItemInfo) {
			ItemInfo si = (ItemInfo) shelfRefInfo;
			descLbl.setText(productInfo.name + " - " + si.description);
			buildBodyFrameIfRequired();
			productDetails.setText(0, 1, productInfo.description);
			Hyperlink link = (Hyperlink) productDetails.getWidget(1, 1);
			if (link == null) {
				link = new Hyperlink();
				productDetails.setWidget(1, 1, link);
				link.setTitle("click here to link to this product item");
			}
			link.setText(si.id);
			link.setTargetHistoryToken(HistoryUtils.toHistoryToken("shopping", "item-id", si.id));
//			productDetails.setText(1, 1, si.id);
			productDetails.setText(2, 1, si.stockLevel + "");
			productDetails.setText(3, 1, lp);
		}
	}
	
	protected void internalOpen() {
		itemIdLbl.setVisible(false);
		listPriceLbl.setVisible(false);
		addToCartBtn.setVisible(true);
		buildBodyFrameIfRequired();
	}

	protected void internalClose() {
		itemIdLbl.setVisible(true);
		listPriceLbl.setVisible(true);
		addToCartBtn.setVisible(false);
		descLbl.setText(itemRefInfo.description);
	}
	
	private void buildTitleFrame() {
		HorizontalPanel hp = titleBar.getWest();
		hp.add(itemIdLbl);
		hp.add(descLbl);
		Element hpe = hp.getElement();
		DOM.setAttribute(hpe, "id", "productCardTitleInfo");
		
		hp = titleBar.getEast();
		hpe = hp.getElement();
		hp.add(listPriceLbl);
		addToCartBtn.setVisible(false);
		hp.add(addToCartBtn);
		DOM.setAttribute(hpe, "id", "productCardListPrice");
		
		addToCartBtn.addClickListener(this);
	}
	
	private void buildBodyFrameIfRequired() {
		if (builtBody)
			return;
		productDetails.resize(4, 2);
		
		productDetails.setWidget(0, 0, itemImg);
		productDetails.setText(1, 0, "Item No.");
		productDetails.setText(2, 0, "Stock level");
		productDetails.setText(3, 0, "List price");
		
		CellFormatter formatter = productDetails.getCellFormatter();
		for (int i = 0; i < 4; i++) {
			formatter.addStyleName(i, 0, "ps-ProductCardView-fldNms");
			String spec = i == 0 ? "ps-ProductCardView-img": "ps-ProductCardView-fldNm"; 
			formatter.addStyleName(i, 0, spec);
		}
		for (int i = 0; i < 4; i++) 
			formatter.setStyleName(i, 1, "ps-ProductCardView-val");
		builtBody = true;
	}

	public void onClick(Widget sender) {
		if (sender.equals(addToCartBtn)) 
			StoreCoordinator.getInstance().getCartController().add(productInfo, (ItemInfo) itemRefInfo);
	}
}
