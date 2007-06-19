package org.opengoss.petstore.dao;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.domain.Item;
import org.opengoss.petstore.dao.domain.Order;

public interface ItemDao {

  public void updateQuantity(Order order) throws DaoException;

  boolean isItemInStock(String itemId) throws DaoException;

  List getItemListByProduct(String productId) throws DaoException;

  Item getItem(String itemId) throws DaoException;

}
