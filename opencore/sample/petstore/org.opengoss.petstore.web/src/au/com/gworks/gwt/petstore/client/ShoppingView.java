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

import org.javaongems.gwk.client.CardDeck;
import org.javaongems.win.client.OutlookBar;
import org.javaongems.win.client.OutlookBarItem;
import org.javaongems.win.client.OutlookBarList;

import au.com.gworks.gwt.petstore.client.service.AisleInfo;
import au.com.gworks.gwt.petstore.client.service.ItemRefInfo;
import au.com.gworks.gwt.petstore.client.service.ProductInfo;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ShoppingView extends Composite {
	final static public String AISLE_INFO = "aisle-info";
	final static public String PRODUCT_INFO = "product-info";

	private HorizontalPanel horizontalPanel;
	private OutlookBar productAisle;
	private CardDeck productShelf;

	public ShoppingView() {
		horizontalPanel = new HorizontalPanel();
		initWidget(horizontalPanel);

		productAisle = new OutlookBar();
		horizontalPanel.add(productAisle);

		productShelf = new CardDeck();
		horizontalPanel.add(productShelf);

		setStyleName("ps-ProductAisleView");
	}

	public void setUp(OutlookBar.Listener obLsnr, CardDeck.Listener cdLsnr) {
		productAisle.setListener(obLsnr);
		productShelf.setListener(cdLsnr);

		Element hpElem = horizontalPanel.getElement();
		DOM.setAttribute(hpElem, "id", "productAisleHp");
		Element paElm = productAisle.getElement();
		Element paPrntElem = DOM.getParent(paElm);
		DOM.setAttribute(paElm, "id", "productAisle");
		DOM.setAttribute(paPrntElem, "id", "productAisleTd");
//		resizeProductAisle();
	}

	public void loadAisles(AisleInfo[] aisles) {
		productAisle.clear();
		for (int i = 0; i < aisles.length; i++) {
			OutlookBarList list = productAisle.add(aisles[i].name, false);
			list.getCargo().put(AISLE_INFO, aisles[i]);
		}
	}

	public void loadProducts(int listIdx, ProductInfo[] products) {
		OutlookBarList list = productAisle.getCategoryList(listIdx);
		list.clear();
		for (int i = 0; i < products.length; i++) {
			OutlookBarItem itm = new OutlookBarItem();
			itm.setItem(products[i].name, products[i].url, products[i].description);
			itm.getCargo().put(PRODUCT_INFO, products[i]);
			list.add(itm);
		}
		productShelf.clear();
	}

	public void loadShelf(ProductInfo productInfo, ItemRefInfo[] shelfRefs) {
		productShelf.clear();
		for (int i = 0; i < shelfRefs.length; i++) {
			ProductCardView card = new ProductCardView();
			card.setProductInfo(productInfo);
			card.setShelfRefInfo(shelfRefs[i]);
			productShelf.add(card);
		}
	}
	
	public ProductCardView findAndOpenProductCard(String itemId) {
		for (Iterator iter = productShelf.iterator(); iter.hasNext();) {
			ProductCardView card = (ProductCardView) iter.next();
			if (card.getShelfRefInfo().id.equals(itemId)) {
				card.open();
				return card;
			}
		}
		return null;
	}
	
	public void openAisle(int idx) {
		productAisle.selectCategory(idx);
	}

	public int getSelectedAisle() {
		return productAisle.getSelectedCategory();
	}

	public OutlookBar getProductAisle() {
		return productAisle;
	}

	public CardDeck getProductShelf() {
		return productShelf;
	}

	public void refresh() {
		int h = getOffsetHeight();
		productAisle.setPixelSize(-1, h);
	}

//	public void setPixelSize(int width, int height) {
//		super.setPixelSize(width, height);
//		productAisle.setPixelSize(-1, height);
//		productShelf.setPixelSize(-1, height);
//  }


//	private void resizeProductAisle() {
//		int h = horizontalPanel.getOffsetHeight();
//		System.out.print("hp.height=" + h);
//	}
}
