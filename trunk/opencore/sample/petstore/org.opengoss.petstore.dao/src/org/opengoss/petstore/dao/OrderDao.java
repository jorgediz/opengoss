package org.opengoss.petstore.dao;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.domain.Order;

public interface OrderDao {

  List getOrdersByUsername(String username) throws DaoException;

  Order getOrder(int orderId) throws DaoException;

  void insertOrder(Order order) throws DaoException;

}
