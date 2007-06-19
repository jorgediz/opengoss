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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.javaongems.gwk.client.AbstractPageController;

import au.com.gworks.gwt.petstore.client.service.CartInfo;
import au.com.gworks.gwt.petstore.client.service.CartItemRefInfo;
import au.com.gworks.gwt.petstore.client.service.ItemInfo;
import au.com.gworks.gwt.petstore.client.service.ProductInfo;

import com.google.gwt.user.client.ui.Widget;

public class CartController extends AbstractPageController {
	static public interface CartListener {
		public void onCartUpdate(CartInfo cartInfo);
	}
	
	private CartView detailView;
	private CartMonitorView summaryView;

	private CartInfo cart = new CartInfo();
	private Map/*<CartItemRefInfo,ProductInfo>*/ cartItemProductMap = new HashMap();
	private Map/*<CartItemRefInfo,ItemInfo>*/ cartItemItemDetailMap = new HashMap();
	private Map/*<String,CartItemRefInfo>*/ cartItemMap = new HashMap();
	
	public void setUp(StoreCoordinator coord) {
		super.setUp(coord);
		detailView = new CartView();
		summaryView = new CartMonitorView();
		setPagePanel(coordinator.getPagePanel());
		coordinator.registerPageController(detailView, this);
		detailView.setUp(this);
	}
		
	public void add(ProductInfo product, ItemInfo item) {
		CartItemRefInfo cartItem = getCartItemRefInfoAndCreateIfRequired(product, item);
		cartItem.quantity++;
		
		recalculateAndPublish();
	}
	
	public void remove(List items) {
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			CartItemRefInfo item = (CartItemRefInfo) iter.next();
			cart.items.remove(item);
			cartItemProductMap.remove(item);
			cartItemItemDetailMap.remove(item);
			cartItemMap.remove(item.id);
		}
		recalculateAndPublish();
	}
	
	public void recalculateAndPublish() {
		recalculate();
		publish();
	}
	
	public CartMonitorView getSummaryView() {
		return summaryView;
	}
	
	public float getSubTotal() {
		return cart.subTotal;
	}

	public ProductInfo getProductInfo(CartItemRefInfo item) {
		return (ProductInfo) cartItemProductMap.get(item);
	}
	
	public ItemInfo getItemInfo(CartItemRefInfo item) {
		return (ItemInfo) cartItemItemDetailMap.get(item);
	}
	
	public CartItemRefInfo getCartItemRefInfo(String itemId) {
		return (CartItemRefInfo) cartItemMap.get(itemId);
	}

	private void recalculate() {
		cart.subTotal = 0;
		cart.itemsCount = 0;
		for (Iterator iter = cart.items.iterator(); iter.hasNext();) {
			CartItemRefInfo item = (CartItemRefInfo) iter.next();
			double itemTotal = item.quantity * item.listPrice;
			cart.subTotal += itemTotal;
			cart.itemsCount += item.quantity;
		}
	}
	
	private void publish() {
		detailView.onCartUpdate(cart);
		summaryView.onCartUpdate(cart);
	}

	private CartItemRefInfo getCartItemRefInfoAndCreateIfRequired(ProductInfo product, ItemInfo item) {
		CartItemRefInfo ret = getCartItemRefInfo(item.id);
		if (ret == null) {
			ret = new CartItemRefInfo();
			ret.assign(item);
			ret.stockLevel = item.stockLevel;
			ret.quantity = 0;
			cart.items.add(ret);
			cartItemMap.put(item.id, ret);
			cartItemProductMap.put(ret, product);
		}
		cartItemItemDetailMap.put(ret, item);	// always update with the latest item info
		return ret;
	}
	
	// template methods
	protected Widget getPageView() {
		return detailView;
	}
	
	public void mountPageView() {
		pagePanel.add(detailView, null, "Cart/Check out", false);
	}
	
	protected String getPageName() {
		return "cart";
	}
	
	protected void openPage() {
		pagePanel.setPageContext(null);
		super.openPage();
	}

	protected void onControllerCallSuccess(ControllerCall call, Object result) {
	}
}
