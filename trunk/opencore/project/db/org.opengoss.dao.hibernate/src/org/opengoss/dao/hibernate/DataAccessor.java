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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.opengoss.orm.core.DaoException;

public class DataAccessor {

	private SessionFactory sessionFactory;

	public DataAccessor(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFatory() {
		return sessionFactory;
	}

	public Object execute(IAccessorCallback action) throws DaoException {
		Session session = sessionFactory.getCurrentSession();
		Transaction t = session.beginTransaction();
		Object obj = null;
		try {
			obj = action.call(session);
			t.commit();
		} catch (Exception e) {
			t.rollback();
			throw new DaoException(e);
		}
		return obj;
	}

	public Object get(Class entityClass, Serializable id) throws DaoException {
		return get(entityClass, id, null);
	}

	public Object get(final Class entityClass, final Serializable id, final LockMode lockMode)
			throws DaoException {
		return execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.get(entityClass, id, lockMode);
				}
				else {
					return session.get(entityClass, id);
				}
			}
		});
	}

	public Object get(String entityName, Serializable id) throws DaoException {
		return get(entityName, id, null);
	}

	public Object get(final String entityName, final Serializable id, final LockMode lockMode)
			throws DaoException {
		return execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.get(entityName, id, lockMode);
				}
				else {
					return session.get(entityName, id);
				}
			}
		});
	}

	public Object load(Class entityClass, Serializable id) throws DaoException {
		return load(entityClass, id, null);
	}

	public Object load(final Class entityClass, final Serializable id, final LockMode lockMode)
			throws DaoException {

		return execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.load(entityClass, id, lockMode);
				}
				else {
					return session.load(entityClass, id);
				}
			}
		});
	}

	public Object load(String entityName, Serializable id) throws DaoException {
		return load(entityName, id, null);
	}

	public Object load(final String entityName, final Serializable id, final LockMode lockMode)
			throws DaoException {

		return execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.load(entityName, id, lockMode);
				}
				else {
					return session.load(entityName, id);
				}
			}
		});
	}

	public List loadAll(final Class entityClass) throws DaoException {
		return (List) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Criteria criteria = session.createCriteria(entityClass);
				return criteria.list();
			}
		});
	}

	public void load(final Object entity, final Serializable id) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.load(entity, id);
				return null;
			}
		});
	}

	public void refresh(final Object entity) throws DaoException {
		refresh(entity, null);
	}

	public void refresh(final Object entity, final LockMode lockMode) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				if (lockMode != null) {
					session.refresh(entity, lockMode);
				}
				else {
					session.refresh(entity);
				}
				return null;
			}
		});
	}

	public boolean contains(final Object entity) throws DaoException {
		Boolean result = (Boolean) execute(new IAccessorCallback() {
			public Object call(Session session) {
				return (session.contains(entity) ? Boolean.TRUE : Boolean.FALSE);
			}
		});
		return result.booleanValue();
	}

	public void evict(final Object entity) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.evict(entity);
				return null;
			}
		});
	}

	//-------------------------------------------------------------------------
	// Convenience methods for storing individual objects
	//-------------------------------------------------------------------------

	public void lock(final Object entity, final LockMode lockMode) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.lock(entity, lockMode);
				return null;
			}
		});
	}

	public void lock(final String entityName, final Object entity, final LockMode lockMode)
			throws DaoException {

		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.lock(entityName, entity, lockMode);
				return null;
			}
		});
	}

	public Serializable save(final Object entity) throws DaoException {
		return (Serializable) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				return session.save(entity);
			}
		});
	}

	public Serializable save(final String entityName, final Object entity) throws DaoException {
		return (Serializable) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				return session.save(entityName, entity);
			}
		});
	}

	public void update(Object entity) throws DaoException {
		update(entity, null);
	}

	public void update(final Object entity, final LockMode lockMode) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.update(entity);
				if (lockMode != null) {
					session.lock(entity, lockMode);
				}
				return null;
			}
		});
	}

	public void update(String entityName, Object entity) throws DaoException {
		update(entityName, entity, null);
	}

	public void update(final String entityName, final Object entity, final LockMode lockMode)
			throws DaoException {

		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.update(entityName, entity);
				if (lockMode != null) {
					session.lock(entity, lockMode);
				}
				return null;
			}
		});
	}

	public void saveOrUpdate(final Object entity) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.saveOrUpdate(entity);
				return null;
			}
		});
	}

	public void saveOrUpdate(final String entityName, final Object entity) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.saveOrUpdate(entityName, entity);
				return null;
			}
		});
	}

	public void saveOrUpdateAll(final Collection entities) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				for (Iterator it = entities.iterator(); it.hasNext();) {
					session.saveOrUpdate(it.next());
				}
				return null;
			}
		});
	}

	public void replicate(final Object entity, final ReplicationMode replicationMode)
			throws DaoException {

		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.replicate(entity, replicationMode);
				return null;
			}
		});
	}

	public void replicate(final String entityName, final Object entity, final ReplicationMode replicationMode)
			throws DaoException {

		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.replicate(entityName, entity, replicationMode);
				return null;
			}
		});
	}

	public void persist(final Object entity) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.persist(entity);
				return null;
			}
		});
	}

	public void persist(final String entityName, final Object entity) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.persist(entityName, entity);
				return null;
			}
		});
	}

	public Object merge(final Object entity) throws DaoException {
		return execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				return session.merge(entity);
			}
		});
	}

	public Object merge(final String entityName, final Object entity) throws DaoException {
		return execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				return session.merge(entityName, entity);
			}
		});
	}

	public void delete(Object entity) throws DaoException {
		delete(entity, null);
	}

	public void delete(final Object entity, final LockMode lockMode) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				if (lockMode != null) {
					session.lock(entity, lockMode);
				}
				session.delete(entity);
				return null;
			}
		});
	}

	public void deleteAll(final Collection entities) throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				for (Iterator it = entities.iterator(); it.hasNext();) {
					session.delete(it.next());
				}
				return null;
			}
		});
	}

	public void flush() throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				session.flush();
				return null;
			}
		});
	}

	public void clear() throws DaoException {
		execute(new IAccessorCallback() {
			public Object call(Session session) {
				session.clear();
				return null;
			}
		});
	}

	public List find(String queryString) throws DaoException {
		return find(queryString, (Object[]) null);
	}

	public List find(String queryString, Object value) throws DaoException {
		return find(queryString, new Object[] {value});
	}

	public List find(final String queryString, final Object[] values) throws DaoException {
		return (List) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		});
	}

	public List findByNamedParam(String queryString, String paramName, Object value)
			throws DaoException {

		return findByNamedParam(queryString, new String[] {paramName}, new Object[] {value});
	}

	public List findByNamedParam(final String queryString, final String[] paramNames, final Object[] values)
			throws DaoException {

		if (paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return (List) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				return queryObject.list();
			}
		});
	}

	public List findByValueBean(final String queryString, final Object valueBean)
			throws DaoException {

		return (List) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				queryObject.setProperties(valueBean);
				return queryObject.list();
			}
		});
	}


	//-------------------------------------------------------------------------
	// Convenience finder methods for named queries
	//-------------------------------------------------------------------------

	public List findByNamedQuery(String queryName) throws DaoException {
		return findByNamedQuery(queryName, (Object[]) null);
	}

	public List findByNamedQuery(String queryName, Object value) throws DaoException {
		return findByNamedQuery(queryName, new Object[] {value});
	}

	public List findByNamedQuery(final String queryName, final Object[] values) throws DaoException {
		return (List) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.list();
			}
		});
	}

	public List findByNamedQueryAndNamedParam(String queryName, String paramName, Object value)
			throws DaoException {

		return findByNamedQueryAndNamedParam(queryName, new String[] {paramName}, new Object[] {value});
	}

	public List findByNamedQueryAndNamedParam(
			final String queryName, final String[] paramNames, final Object[] values)
			throws DaoException {

		if (paramNames != null && values != null && paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		return (List) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				return queryObject.list();
			}
		});
	}

	public List findByNamedQueryAndValueBean(final String queryName, final Object valueBean)
			throws DaoException {

		return (List) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				queryObject.setProperties(valueBean);
				return queryObject.list();
			}
		});
	}


	//-------------------------------------------------------------------------
	// Convenience finder methods for detached criteria
	//-------------------------------------------------------------------------

	public List findByCriteria(DetachedCriteria criteria) throws DaoException {
		return findByCriteria(criteria, -1, -1);
	}

	public List findByCriteria(final DetachedCriteria criteria, final int firstResult, final int maxResults)
			throws DaoException {
		return (List) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				if (firstResult >= 0) {
					executableCriteria.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					executableCriteria.setMaxResults(maxResults);
				}
				return executableCriteria.list();
			}
		});
	}

	public List findByExample(Object exampleEntity) throws DaoException {
		return findByExample(exampleEntity, -1, -1);
	}

	public List findByExample(final Object exampleEntity, final int firstResult, final int maxResults)
			throws DaoException {
		return (List) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Criteria executableCriteria = session.createCriteria(exampleEntity.getClass());
				executableCriteria.add(Example.create(exampleEntity));
				if (firstResult >= 0) {
					executableCriteria.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					executableCriteria.setMaxResults(maxResults);
				}
				return executableCriteria.list();
			}
		});
	}


	//-------------------------------------------------------------------------
	// Convenience query methods for iteration and bulk updates/deletes
	//-------------------------------------------------------------------------

	public Iterator iterate(String queryString) throws DaoException {
		return iterate(queryString, (Object[]) null);
	}

	public Iterator iterate(String queryString, Object value) throws DaoException {
		return iterate(queryString, new Object[] {value});
	}

	public Iterator iterate(final String queryString, final Object[] values) throws DaoException {
		return (Iterator) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.iterate();
			}
		});
	}

	public void closeIterator(Iterator it) throws DaoException {
		try {
			Hibernate.close(it);
		}
		catch (HibernateException ex) {
			throw new DaoException(ex);
		}
	}

	public int bulkUpdate(String queryString) throws DaoException {
		return bulkUpdate(queryString, (Object[]) null);
	}

	public int bulkUpdate(String queryString, Object value) throws DaoException {
		return bulkUpdate(queryString, new Object[] {value});
	}

	public int bulkUpdate(final String queryString, final Object[] values) throws DaoException {
		Integer updateCount = (Integer) execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return new Integer(queryObject.executeUpdate());
			}
		});
		return updateCount.intValue();
	}

}
