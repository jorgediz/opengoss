package org.opengoss.petstore.dao.ibatis;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.opengoss.dao.core.DaoException;
import org.opengoss.petstore.dao.ProductDao;
import org.opengoss.petstore.dao.domain.Product;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

public class SqlMapProductDao extends SqlMapDaoTemplate implements ProductDao {

	public SqlMapProductDao(DaoManager daoManager) {
		super(daoManager);
	}

	public List getProductListByCategory(String categoryId)
			throws DaoException {
		return queryForList("getProductListByCategory", categoryId);
	}

	public Product getProduct(String productId) throws DaoException {
		return (Product) queryForObject("getProduct", productId);
	}

	public List searchProductList(String keywords) throws DaoException {
		Object parameterObject = new ProductSearch(keywords);
		return queryForList("searchProductList", parameterObject);
	}

	/* Inner Classes */
	public static class ProductSearch {

		private List keywordList = new ArrayList();

		public ProductSearch(String keywords) {
			StringTokenizer splitter = new StringTokenizer(keywords, " ", false);
			while (splitter.hasMoreTokens()) {
				this.keywordList.add("%" + splitter.nextToken() + "%");
			}
		}

		public List getKeywordList() {
			return keywordList;
		}
	}

}
