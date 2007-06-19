package org.opengoss.petstore.dao;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.domain.Product;

public interface ProductDao {

	List getProductListByCategory(String categoryId) throws DaoException;

	List searchProductList(String keywords) throws DaoException;

	Product getProduct(String productId) throws DaoException;

}
