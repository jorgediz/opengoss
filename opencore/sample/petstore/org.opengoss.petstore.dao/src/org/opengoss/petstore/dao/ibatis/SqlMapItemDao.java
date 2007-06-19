package org.opengoss.petstore.dao.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.ItemDao;
import org.opengoss.petstore.dao.domain.Item;
import org.opengoss.petstore.dao.domain.LineItem;
import org.opengoss.petstore.dao.domain.Order;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

public class SqlMapItemDao extends SqlMapDaoTemplate implements ItemDao {

  public SqlMapItemDao(DaoManager daoManager) {
		super(daoManager);
	}

public void updateQuantity(Order order) throws DaoException {
    for (int i = 0; i < order.getLineItems().size(); i++) {
      LineItem lineItem = (LineItem) order.getLineItems().get(i);
      String itemId = lineItem.getItemId();
      Integer increment = new Integer(lineItem.getQuantity());
      Map param = new HashMap(2);
      param.put("itemId", itemId);
      param.put("increment", increment);
      update("updateInventoryQuantity", param);
    }
  }

  public boolean isItemInStock(String itemId) throws DaoException {
    Integer i = (Integer) queryForObject("getInventoryQuantity", itemId);
    return (i != null && i.intValue() > 0);
  }

  public List getItemListByProduct(String productId) throws DaoException {
    return queryForList("getItemListByProduct", productId);
  }

  public Item getItem(String itemId) throws DaoException {
    Item item = (Item) queryForObject("getItem", itemId);
		if (item != null) {
			Integer qty = (Integer) queryForObject("getInventoryQuantity", itemId);
			item.setQuantity(qty.intValue());
		}
    return item;
  }

}
