package org.opengoss.petstore.dao;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.domain.Category;

public interface CategoryDao {

	List getCategoryList() throws DaoException;

	Category getCategory(String categoryId) throws DaoException;

}
