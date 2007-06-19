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
package au.com.gworks.gwt.petstore.remoteclient;

import org.javaongems.core.jclient.Gwt;

import au.com.gworks.gwt.petstore.client.service.AisleInfo;
import au.com.gworks.gwt.petstore.client.service.ItemInfo;
import au.com.gworks.gwt.petstore.client.service.ItemRefInfo;
import au.com.gworks.gwt.petstore.client.service.ProductInfo;
import au.com.gworks.gwt.petstore.client.service.ShoppingRpcController;
import au.com.gworks.gwt.petstore.client.service.UrlItemRequestInfo;
import au.com.gworks.gwt.petstore.client.service.UrlProductRequestInfo;

import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class JavaRpcClient {
	@SuppressWarnings("unused")
	static public void main(String[] args) throws Exception {
		ShoppingRpcController rpc = (ShoppingRpcController) Gwt.create(ShoppingRpcController.class);
		((ServiceDefTarget) rpc).setServiceEntryPoint("http://localhost:8888/shoppingRpcController.rpc");
		AisleInfo[] ret = rpc.listStoreAisles();
		ProductInfo[] ret2 = rpc.listAislesProducts(ret[0].id);
		ItemRefInfo[] ret3 = rpc.listProductShelf(ret2[0].id);
		ItemInfo ret4 = rpc.listShelfItemDetails(ret3[0].id);
		UrlItemRequestInfo ret5 = rpc.listCompleteDetailsForUrlItemRequest(ret4.id, true);
		UrlProductRequestInfo ret6 = rpc.listCompleteDetailsForUrlProductRequest(ret2[0].id, true);
	}
}
