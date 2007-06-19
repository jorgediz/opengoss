package org.opengoss.petstore.dao.ibatis;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.OrderDao;
import org.opengoss.petstore.dao.domain.LineItem;
import org.opengoss.petstore.dao.domain.Order;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

public class SqlMapOrderDao extends SqlMapDaoTemplate implements OrderDao {

	public SqlMapOrderDao(DaoManager daoManager) {
		super(daoManager);
	}

	private SqlMapSequenceDao sequenceDao;

	public void setSequenceDao(SqlMapSequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}

	public List getOrdersByUsername(String username) throws DaoException {
		return queryForList("getOrdersByUsername", username);
	}

	public Order getOrder(int orderId) throws DaoException {
		Object parameterObject = new Integer(orderId);
		Order order = (Order) queryForObject("getOrder", parameterObject);
		if (order != null) {
			order.setLineItems(queryForList("getLineItemsByOrderId",
					new Integer(order.getOrderId())));
		}
		return order;
	}

	public void insertOrder(Order order) throws DaoException {
		order.setOrderId(this.sequenceDao.getNextId("ordernum"));
		insert("insertOrder", order);
		insert("insertOrderStatus", order);
		for (int i = 0; i < order.getLineItems().size(); i++) {
			LineItem lineItem = (LineItem) order.getLineItems().get(i);
			lineItem.setOrderId(order.getOrderId());
			insert("insertLineItem", lineItem);
		}
	}

}
