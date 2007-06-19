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

import org.opengoss.petstore.dao.ProductDao;
import org.opengoss.petstore.dao.domain.Product;

import au.com.gworks.gwt.petstore.client.service.ProductInfo;
import au.com.gworks.gwt.petstore.client.service.StoreSearchRpcController;

public class StoreSearchRpcControllerImpl extends BaseServlet implements StoreSearchRpcController {

	public ProductInfo[] searchForProducts(String keywords) {
		List prodList = getDaoFacade().getDao(ProductDao.class).searchProductList(
				keywords);
		List/* <ProductInfo> */prodInfoList = new ArrayList/* <ProductInfo> */();
		for (Iterator it = prodList.iterator(); it.hasNext();) {
			Product dto = (Product) it.next();
			String[] urlDesc = new String[2];
			ShoppingRpcControllerImpl.parseProductDescription(dto
					.getDescription(), urlDesc);
			ProductInfo info = new ProductInfo(dto.getProductId(), dto
					.getName(), urlDesc[1], urlDesc[0]);
			prodInfoList.add(info);
		}
		ProductInfo[] ret = new ProductInfo[prodInfoList.size()];
		prodInfoList.toArray(ret);
		return ret;
	}
}
