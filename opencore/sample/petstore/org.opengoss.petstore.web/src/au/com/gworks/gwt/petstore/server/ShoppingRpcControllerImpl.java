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
package au.com.gworks.gwt.petstore.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opengoss.petstore.dao.CategoryDao;
import org.opengoss.petstore.dao.ItemDao;
import org.opengoss.petstore.dao.ProductDao;
import org.opengoss.petstore.dao.domain.Category;
import org.opengoss.petstore.dao.domain.Item;
import org.opengoss.petstore.dao.domain.Product;

import au.com.gworks.gwt.petstore.client.service.AisleInfo;
import au.com.gworks.gwt.petstore.client.service.ItemInfo;
import au.com.gworks.gwt.petstore.client.service.ItemRefInfo;
import au.com.gworks.gwt.petstore.client.service.ProductInfo;
import au.com.gworks.gwt.petstore.client.service.ShoppingRpcController;
import au.com.gworks.gwt.petstore.client.service.UrlBaseRequestInfo;
import au.com.gworks.gwt.petstore.client.service.UrlItemRequestInfo;
import au.com.gworks.gwt.petstore.client.service.UrlProductRequestInfo;

public class ShoppingRpcControllerImpl extends BaseServlet implements
		ShoppingRpcController {
	final static private String SRC_PREFIX = "src=\"";
	final static private String SRC_SUFFIX = "\">";
	final static private int SRC_PREFIX_LEN = SRC_PREFIX.length();
	final static private int SRC_SUFFIX_LEN = SRC_SUFFIX.length();

	private ItemDao getItemDao() {
		return getDaoFacade().getDao(ItemDao.class);
	}
	
	private ProductDao getProductDao() {
		return getDaoFacade().getDao(ProductDao.class);
	}
	
	public UrlItemRequestInfo listCompleteDetailsForUrlItemRequest(String itemId, boolean inclAisles) {
		Item itemDto = getItemDao().getItem(itemId);
		if (itemDto == null)
			return null;
		Product prodDto = itemDto.getProduct();
		UrlItemRequestInfo ret = new UrlItemRequestInfo();
		listCompleteDetailsBase(prodDto, inclAisles, ret);
		ret.items = listProductShelf(itemDto.getProductId());
		ret.nominatedItem = listShelfItemDetails(itemId);
		return ret;
	}

	public UrlProductRequestInfo listCompleteDetailsForUrlProductRequest(String productId, boolean inclAisles) {
		Product prodDto = getProductDao().getProduct(productId);
		if (prodDto == null)
			return null;
		UrlProductRequestInfo ret = new UrlProductRequestInfo();
		listCompleteDetailsBase(prodDto, inclAisles, ret);
		for (int i = 0; i < ret.products.length; i++) {
			if (ret.products[i].id.equals(productId)) {
				ret.nominatedProduct = ret.products[i];
				break;
			}
		}
		return ret;
	}
	
	private void listCompleteDetailsBase(Product prodDto, boolean inclAisles, UrlBaseRequestInfo ret) {
		ret.aisleId = prodDto.getCategoryId();
		ret.productId = prodDto.getProductId();
		if (inclAisles)
			ret.aisles = listStoreAisles();
		ret.products = listAislesProducts(ret.aisleId);
	}

	public ProductInfo[] listAislesProducts(String catId) {
		List prodList = getProductDao().getProductListByCategory(catId);
		List/*<ProductInfo>*/ prodInfoList = new ArrayList/*<ProductInfo>*/();
		for (Iterator it = prodList.iterator(); it.hasNext();) {
			Product dto = (Product) it.next();
			String[] urlDesc = new String[2];
			parseProductDescription(dto.getDescription(), urlDesc);
			ProductInfo info = new ProductInfo(dto.getProductId(), dto.getName(), urlDesc[1], urlDesc[0]);
			prodInfoList.add(info);
		}
		ProductInfo[] ret = new ProductInfo[prodInfoList.size()];
		prodInfoList.toArray(ret);
		return ret;
	}

	public ItemRefInfo[] listProductShelf(String productId) {
		List itemList = getItemDao().getItemListByProduct(productId);
		List/*<ItemRefInfo>*/ itemInfoList = new ArrayList/*<ItemRefInfo>*/();
		for (Iterator it = itemList.iterator(); it.hasNext();) {
			Item dto = (Item) it.next();
			ItemRefInfo info = new ItemRefInfo(dto.getItemId(), dto.getAttribute1(), dto.getListPrice());
			itemInfoList.add(info);
		}
		ItemRefInfo[] ret = new ItemRefInfo[itemInfoList.size()];
		itemInfoList.toArray(ret);
		return ret;
	}

	public ItemInfo listShelfItemDetails(String itemId) {
		Item dto = getItemDao().getItem(itemId);
		return new ItemInfo(dto.getItemId(), dto.getAttribute1(), dto.getListPrice(), dto.getQuantity());
	}
	
	private CategoryDao getCategoryDao() {
		return getDaoFacade().getDao(CategoryDao.class);
	}

	public AisleInfo[] listStoreAisles() {
		List list = getCategoryDao().getCategoryList();
		List/*<AisleInfo>*/ catInfoList = new ArrayList/*<AisleInfo>*/();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Category dto = (Category) it.next();
			AisleInfo info = new AisleInfo(dto.getCategoryId(), dto.getName());
			catInfoList.add(info);
		}
		moveFavouriteToFront(catInfoList, "CATS");
		AisleInfo[] ret = new AisleInfo[catInfoList.size()];
		catInfoList.toArray(ret);
		return ret;
	}

	static private void moveFavouriteToFront(List/*<AisleInfo>*/ list, String fav) {
		for (int i = 0; i < list.size(); i++) {
			if (fav.equals(((AisleInfo)list.get(i)).id)) {
				AisleInfo favAisle = (AisleInfo) list.remove(i);
				list.add(0, favAisle);
				return;
			}
		}
	}

	static public void parseProductDescription(String desc,
			String[] storeUrlDesc) {
		int pos = desc.indexOf(SRC_PREFIX);
		int end = desc.lastIndexOf(SRC_SUFFIX);
		storeUrlDesc[0] = desc.substring(pos + SRC_PREFIX_LEN, end);
		storeUrlDesc[1] = desc.substring(end + SRC_SUFFIX_LEN);
		if (storeUrlDesc[0].startsWith("../"))
			storeUrlDesc[0] = storeUrlDesc[0].substring(3);
	}
}
