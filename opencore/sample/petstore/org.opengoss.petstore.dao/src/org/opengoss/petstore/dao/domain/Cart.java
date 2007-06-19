package org.opengoss.petstore.dao.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibatis.common.util.PaginatedArrayList;
import com.ibatis.common.util.PaginatedList;

public class Cart implements Serializable {

	/* Private Fields */

	private final Map itemMap = Collections.synchronizedMap(new HashMap());

	private final PaginatedList itemList = new PaginatedArrayList(4);

	/* JavaBeans Properties */

	public Cart() {
	}

	public Iterator getAllCartItems() {
		List allItems = new ArrayList();
		itemList.gotoPage(0);
		allItems.addAll(itemList);
		while (itemList.nextPage()) {
			allItems.addAll(itemList);
		}
		return allItems.iterator();
	}

	public PaginatedList getCartItemList() {
		return itemList;
	}

	public int getNumberOfItems() {
		return itemList.size();
	}

	/* Public Methods */

	public boolean containsItemId(String itemId) {
		return itemMap.containsKey(itemId);
	}

	public void addItem(Item item, boolean isInStock) {
		CartItem cartItem = (CartItem) itemMap.get(item.getItemId());
		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setItem(item);
			cartItem.setQuantity(0);
			cartItem.setInStock(isInStock);
			itemMap.put(item.getItemId(), cartItem);
			itemList.add(cartItem);
		}
		cartItem.incrementQuantity();
	}

	public Item removeItemById(String itemId) {
		CartItem cartItem = (CartItem) itemMap.remove(itemId);
		if (cartItem == null) {
			return null;
		} else {
			itemList.remove(cartItem);
			return cartItem.getItem();
		}
	}

	public void incrementQuantityByItemId(String itemId) {
		CartItem cartItem = (CartItem) itemMap.get(itemId);
		cartItem.incrementQuantity();
	}

	public void setQuantityByItemId(String itemId, int quantity) {
		CartItem cartItem = (CartItem) itemMap.get(itemId);
		cartItem.setQuantity(quantity);
	}

	public double getSubTotal() {
		double subTotal = 0;
		Iterator items = getAllCartItems();
		while (items.hasNext()) {
			CartItem cartItem = (CartItem) items.next();
			Item item = cartItem.getItem();
			double listPrice = item.getListPrice();
			int quantity = cartItem.getQuantity();
			subTotal += listPrice * quantity;
		}
		return subTotal;
	}

}
