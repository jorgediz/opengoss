package org.opengoss.petstore.dao.ibatis;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.CategoryDao;
import org.opengoss.petstore.dao.domain.Category;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

public class SqlMapCategoryDao extends SqlMapDaoTemplate implements CategoryDao {

  public SqlMapCategoryDao(DaoManager daoManager) {
		super(daoManager);
	}

public List getCategoryList() throws DaoException {
    return queryForList("getCategoryList", null);
  }

  public Category getCategory(String categoryId) throws DaoException {
    return (Category)queryForObject("getCategory", categoryId);
  }

}
