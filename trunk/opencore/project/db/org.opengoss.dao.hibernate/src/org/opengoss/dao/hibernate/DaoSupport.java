/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opengoss.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.opengoss.orm.core.DaoException;
import org.opengoss.orm.core.DomainObject;
import org.opengoss.orm.core.IDomainDao;
import org.opengoss.orm.core.Page;

public class DaoSupport implements IDomainDao {

	private DataAccessor dbAccessor;

	public DaoSupport(SessionFactory sessionFactory) {
		this.dbAccessor = new DataAccessor(sessionFactory);
	}

	protected final DataAccessor getDbAccessor() {
		return dbAccessor;
	}

	protected final SessionFactory getSessionFactory() {
		return dbAccessor.getSessionFatory();
	}

	public <T extends DomainObject> T save(T object) throws DaoException {
		getDbAccessor().save(object);
		return object;
	}

	public <T extends DomainObject> void save(List<T> objects)
			throws DaoException {
		getDbAccessor().saveOrUpdateAll(objects);
	}

	public <T extends DomainObject> T update(T object) throws DaoException {
		getDbAccessor().update(object);
		return object;
	}

	public <T extends DomainObject> void update(List<T> objects)
			throws DaoException {
		// TODO: change later
		getDbAccessor().saveOrUpdateAll(objects);
	}

	public <T extends DomainObject> T delete(T object) throws DaoException {
		getDbAccessor().delete(object);
		return object;
	}

	@SuppressWarnings("unchecked")
	public <T extends DomainObject> T delete(Class clazz, Serializable id)
			throws DaoException {
		Object object = getDbAccessor().get(clazz, id);
		if (object != null) {
			getDbAccessor().delete(object);
		}
		return (T) object;
	}

	public <T extends DomainObject> void delete(List<T> objectList)
			throws DaoException {
		getDbAccessor().deleteAll(objectList);
	}

	@SuppressWarnings("unchecked")
	public <T extends DomainObject> T get(Class clazz, Serializable id)
			throws DaoException {
		return (T) getDbAccessor().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public <T extends DomainObject> List<T> listAll(Class clazz)
			throws DaoException {
		return list(" from " + clazz.getName());
	}

	public long countAll(Class clazz) throws DaoException {
		List list = list(" select count(*) from " + clazz.getName());
		return new Long((Integer) list.get(0));
	}

	public long deleteAll(Class clazz) throws DaoException {
		return executeUpdate(" delete from " + clazz.getName());
	}

	public List listByNamedQuery(final String queryName, final Object... params)
			throws DaoException {
		return (List) getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByName(queryName), params).list();
			}
		});
	}

	public List list(final String hql, final Object... params)
			throws DaoException {
		return (List) getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByHQL(hql), params).list();
			}
		});
	}

	public Object uniqueResultByNamedQuery(final String queryName,
			final Object... params) throws DaoException {
		return getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByName(queryName), params)
						.uniqueResult();
			}
		});
	}

	public Object uniqueResult(final String hql, final Object... params)
			throws DaoException {
		return getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByHQL(hql), params).uniqueResult();
			}
		});
	}

	public int executeUpdateByNamedQuery(final String updateQueryName,
			final Object... params) throws DaoException {
		return (Integer) getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByName(updateQueryName), params)
						.executeUpdate();
			}
		});
	}

	public int executeUpdate(final String hql, final Object... params)
			throws DaoException {
		return (Integer) getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByHQL(hql), params)
						.executeUpdate();
			}
		});
	}

	public List listByNamedQuery(final String queryName, final String[] name,
			final Object[] params) throws DaoException {
		return (List) getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByName(queryName), name, params)
						.list();
			}
		});
	}

	public List list(final String hql, final String[] name,
			final Object[] params) throws DaoException {
		return (List) getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByHQL(hql), name, params).list();
			}
		});
	}

	public Object uniqueResultByNamedQuery(final String queryName,
			final String[] name, final Object[] params) throws DaoException {
		return getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByName(queryName), name, params)
						.uniqueResult();
			}
		});
	}

	public Object uniqueResult(final String hql, final String[] name,
			final Object[] params) throws DaoException {
		return getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByHQL(hql), name, params)
						.uniqueResult();
			}
		});
	}

	public int executeUpdateByNamedQuery(final String updateQueryName,
			final String[] name, final Object[] params) throws DaoException {
		return (Integer) getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByName(updateQueryName), name,
						params).executeUpdate();
			}
		});
	}

	public int executeUpdate(final String hql, final String[] name,
			final Object[] params) throws DaoException {
		return (Integer) getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException,
					SQLException {
				return setParameters(getQueryByHQL(hql), name, params)
						.executeUpdate();
			}

		});
	}

	private Query getQueryByName(String queryName) {
		return getCurrentSession().getNamedQuery(queryName);
	}

	private Query getQueryByHQL(String hql) {
		return getCurrentSession().createQuery(hql);
	}

	private Query setParameters(Query query, Object... params) {
		if (params == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < params.length; i++) {
			if (params[i] == null) {
				throw new IllegalArgumentException();
			}
			query.setParameter(i, params[i]);
		}
		return query;
	}

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	private Query setParameters(Query query, String[] name, Object[] params) {

		if (name.length != params.length) {
			throw new RuntimeException(
					"The length of parameter names and parameters are not equal!");
		}
		for (int i = 0; i < params.length; i++) {
			if (params[i] == null) {
				throw new IllegalArgumentException();
			}
			if (params[i] instanceof Object[]) {
				if (((Object[]) params[i]).length == 0) {
					throw new IllegalArgumentException();
				}
				query.setParameterList(name[i], (Object[]) params[i]);
			} else if (params[i] instanceof Collection) {
				if (((Collection) params[i]).size() == 0) {
					throw new IllegalArgumentException();
				}
				query.setParameterList(name[i], (Collection) params[i]);
			} else {
				query.setParameter(name[i], params[i]);
			}
		}
		return query;
	}

	
	@SuppressWarnings("unchecked")
	public Page getObjectPage(Map queryMap, Integer offset, Integer max) throws DaoException{
		Class className = (Class)queryMap.get("class");
		List expressionList = (List)queryMap.get("list");
		
		Session session = getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Criteria criteria = session.createCriteria(className); 
		if(expressionList==null){
			
			expressionList = new ArrayList();
		}
		Iterator i = expressionList.iterator();
		while(i.hasNext()){
			Criterion ex = (Criterion)i.next();
			criteria.add(ex);
		}
		List list = criteria.list();
		
		int num = list.size();
		criteria.setFirstResult(offset);
		criteria.setMaxResults(max);

		list = criteria.list();
		transaction.commit();

		Page page = new Page(num, list);
		return page;
	}

	public Page getObjectPage(String hql, Integer offset, Integer max) throws DaoException {
		Session session = getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Query q = session.createQuery(hql);
		List list = q.list();
		int num = list.size();
		
		q.setFirstResult(offset);
		q.setMaxResults(max);
		
		list = q.list();
		transaction.commit();
		
		Page page = new Page(num, list);
		
		return page;
	}
	
	

}
