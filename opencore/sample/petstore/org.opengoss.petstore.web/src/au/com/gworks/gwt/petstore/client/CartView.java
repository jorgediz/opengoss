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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.javaongems.std.client.DivPanel;
import org.javaongems.std.client.EastWestHorizPanels;

import au.com.gworks.gwt.petstore.client.service.CartInfo;
import au.com.gworks.gwt.petstore.client.service.CartItemRefInfo;
import au.com.gworks.gwt.petstore.client.service.ItemInfo;
import au.com.gworks.gwt.petstore.client.service.ProductInfo;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;

public class CartView extends Composite 
		implements 
			ClickListener
			, CartController.CartListener 
			, TableListener
			, KeyboardListener
{
	private EastWestHorizPanels groupBarContainer;
	private DivPanel optionsBar = new DivPanel();
	private FlexTable items = new FlexTable();
	private DivPanel container = new DivPanel();
	private Button removeBtn = new Button("Remove");
	private Button checkOutBtn = new Button("Check out");
	private Label emptyCartLbl = new Label("Empty cart");
	
	private Label selAllLbl = new Label("All");
	private Label selNoneLbl = new Label("None");
	private Label selOutOfSockLbl = new Label("Out of stock");
	private Label subTotalAmtLbl = new Label("0");
	
	private CartInfo cartModel;
	private CartController controller;
	private int selectedRow;
	
	public CartView() {
		initWidget(container);
		items.addTableListener(this);
		selAllLbl.addClickListener(this);
		selNoneLbl.addClickListener(this);
		selOutOfSockLbl.addClickListener(this);
		removeBtn.addClickListener(this);
		
		setStyleName(getElement(), "ps-CartView", true);
		buildFrame();
	}
	
	public void setUp(CartController cartController) {
		controller = cartController;
	}
	
	public void setCartModel(CartInfo cartModel) {
		this.cartModel = cartModel;
	}
	
	public void onClick(Widget sender) {
		if (sender == removeBtn) {
			removeSelection();
		} else if (sender instanceof Label) {
			int type = -1;
			if (sender == selAllLbl)
				type = 0;
			else if (sender == selNoneLbl)
				type = 1;
			else if (sender == selOutOfSockLbl)
				type = 2;
			if (type > -1)
				selectCriteria(type);
		}
	}

	public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
		selectedRow = row;
	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		if (keyCode == KEY_TAB || keyCode == KEY_ENTER) {
			CartItemRefInfo itm = (CartItemRefInfo) cartModel.items.get(selectedRow);
			TextBox tb = (TextBox) items.getWidget(selectedRow, 5);
			int newVal = Integer.parseInt(tb.getText());
			itm.quantity = newVal;
			controller.recalculateAndPublish();
		}
	}
	
	public void onCartUpdate(CartInfo cartInfo) {
		setCartModel(cartInfo);
		syncWithModel();
	}
	
	private void removeSelection() {
		ArrayList candidates = new ArrayList();
		int rows = items.getRowCount();
		for (int i = 0; i < rows; i++) {
			CheckBox selCkb = (CheckBox) items.getWidget(i, 0);
			if (selCkb.isChecked()) {
				String itemId = items.getText(i, 1);
				CartItemRefInfo item = controller.getCartItemRefInfo(itemId);
				candidates.add(item);
			}
		}
		if (candidates.size() > 0)
			controller.remove(candidates);
	}
	
	private void syncWithModel() {
		subTotalAmtLbl.setText("" + cartModel.subTotal);
		ArrayList remainders = new ArrayList(cartModel.items);
		ArrayList deleteRows = new ArrayList();
		RowFormatter fmtr = items.getRowFormatter();
		int rows = items.getRowCount();
		for (int i = 0; i < rows; i++) {
			String itemId = items.getText(i, 1);
			CartItemRefInfo item = controller.getCartItemRefInfo(itemId);
			if (item != null) {
				prepareCartItemEntryRow(i, fmtr, item);
				remainders.remove(item);
			} else
				deleteRows.add(new Integer(i));
		}
		prepareDeltaItems(deleteRows, remainders);
		rows = items.getRowCount();
		emptyCartLbl.setVisible(rows == 0);
	}
	
	private void prepareDeltaItems(List deletedRows, List remainders) {
		int delDelta = 0;
		for (Iterator iter = deletedRows.iterator(); iter.hasNext();) {
			Integer row = (Integer) iter.next();
			int rowToDel = row.intValue() + delDelta--;
			items.removeRow(rowToDel);
		}
		
		RowFormatter fmtr = items.getRowFormatter();
		int rowCount = items.getRowCount();
		for (Iterator iter = remainders.iterator(); iter.hasNext();) {
			CartItemRefInfo item = (CartItemRefInfo) iter.next();
			int row = items.insertRow(rowCount);
			for (int i = 0; i < 7; i++)
				items.insertCell(row, i);
			prepareCartItemEntryRow(row, fmtr, item);
		}
	}
	
	private void prepareCartItemEntryRow(int row, RowFormatter fmtr, CartItemRefInfo item) {
		fmtr.addStyleName(row, "ps-GridRow");
		boolean oss = item.stockLevel <= 0 || item.stockLevel < item.quantity;
		if (oss) 
			fmtr.addStyleName(0, "ps-CartView-oos");
		else
			fmtr.removeStyleName(0, "ps-CartView-oos");

		ProductInfo product = controller.getProductInfo(item);
		ItemInfo itemDetail = controller.getItemInfo(item);
		CheckBox selCkb = (CheckBox) items.getWidget(row, 0);
		if (selCkb == null) {
			selCkb = new CheckBox();
			items.setWidget(row, 0, selCkb);
		}
		items.setText(row, 1, item.id);
		items.setText(row, 2, product.id);
		items.setText(row, 3, product.name + " " + itemDetail.description);
		String oosMsg = (oss ? "N/A" :""); 
		items.setText(row, 4, oosMsg);
		TextBox qtySle = (TextBox) items.getWidget(row, 5);
		if (qtySle == null) {
			qtySle = new TextBox();
			qtySle.addKeyboardListener(this);
			qtySle.addStyleName("ps-CartView-qtySle");
			items.setWidget(row, 5, qtySle);
		}
		qtySle.setText("" + item.quantity);
		items.setText(row, 6, "$" + item.listPrice);
		items.setText(row, 7, "$" + (item.listPrice* item.quantity));
	}
	
	private void buildFrame() {
		groupBarContainer = new EastWestHorizPanels();
		container.add(groupBarContainer);
		groupBarContainer.setStyleName("ps-ewp-mainGroupSections");

		Label title = new Label("Cart/Check out");
		groupBarContainer.getWest().add(title);
		title.setStyleName("ps-ewp-mainGroupSections-txt");

		container.add(optionsBar);
		container.add(items, "std-client-width");
		container.add(emptyCartLbl);

		optionsBar.setStyleName("std-client-width");
		optionsBar.add(new Label("Select:"), "std-align-west std-padding");
		optionsBar.add(selAllLbl, "std-align-west std-padding");
		optionsBar.add(selNoneLbl, "std-align-west std-padding");
		optionsBar.add(selOutOfSockLbl, "std-align-west std-padding");
		optionsBar.add(removeBtn, "std-align-west");
		
		optionsBar.add(checkOutBtn, "std-align-east");
		optionsBar.add(subTotalAmtLbl, "std-align-east std-padding");
		optionsBar.add(new Label("Sub total: $"), "std-align-east std-padding" );
	}
	
	private void selectCriteria(int type) {
		int count = items.getRowCount();
		for (int i = 0; i < count; i++) {
			CheckBox cb = (CheckBox) items.getWidget(i, 0);
			if (type == 0)
				cb.setChecked(true);
			else if (type == 1)
				cb.setChecked(false);
			else {
				String oss = items.getText(i, 4);
				cb.setChecked(oss.length() > 0);
			}
		}
	}
}
