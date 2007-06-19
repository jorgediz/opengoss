package org.opengoss.orm.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ������DAO�ӿ�,�־û�����
 * <p>
 * Create at 2005-11-29
 * <p>
 * @version 1.0
 * @author shitianyu_kf
 */
public interface IDomainDao {

    /**
     * ����һ�������
     * @param <T>
     * @param object
     * @return
     */
    <T extends DomainObject> T save(T object) throws DaoException;

    /**
     * һ���Ա����������
     * @param <T>
     * @param objects
     * @throws DaoException
     */
    <T extends DomainObject> void save(List<T> objects) throws DaoException;

    /**
     * �޸�һ�������
     * @param <T>
     * @param object
     * @return
     */
    <T extends DomainObject> T update(T object) throws DaoException;

    /**
     * �޸�һ�������
     * @param <T>
     * @param object
     * @return
     */
    <T extends DomainObject> void update(List<T> objects) throws DaoException;

    /**
     * ɾ��һ�������
     * @param <T>
     * @param object
     * @return
     */
    <T extends DomainObject> T delete(T object) throws DaoException;

    /**
     * ���Idȥɾ�����
     * @param <T>
     * @param id
     * @return
     */
    <T extends DomainObject> T delete(Class clazz, Serializable id) throws DaoException;

    /**
     * ɾ��һ�������
     * @param <T>
     * @param objectList
     */
    <T extends DomainObject> void delete(List<T> objectList) throws DaoException;

    /**
     * ��ݱ�ʶȡ�������
     * @param <T>
     * @param clazz
     * @param id
     * @return
     */
    <T extends DomainObject> T get(Class clazz, Serializable id) throws DaoException;

    /**
     * ȡ��һ��ʵ������������
     * @param <T>
     * @param clazz
     * @return
     */
    <T extends DomainObject> List<T> listAll(Class clazz) throws DaoException;

    /**
     * ��ȡһ��ʵ����Ķ�����
     * @param <T>
     * @param clazz
     * @return
     */
    long countAll(Class clazz) throws DaoException;

    /**
     * ɾ��һ��ʵ�������е����
     * @param clazz
     * @return
     * @throws DaoException
     */
    long deleteAll(Class clazz) throws DaoException;

    /**
     * ������õĲ�ѯ��ƽ��в�ѯ�����ض�����
     * @param queryName
     * @param params
     * @return
     */
    List listByNamedQuery(String queryName, Object... params) throws DaoException;

    /**
     * ���HQL���в�ѯ�����ض�����
     * @param hql
     * @param params
     * @return
     */
    List list(String hql, Object... params) throws DaoException;

    /**
     * ������õĲ�ѯ��ƽ��в�ѯ�����ص������
     * @param queryName
     * @param params
     * @return
     */
    Object uniqueResultByNamedQuery(String queryName, Object... params) throws DaoException;

    /**
     * ���HQL���в�ѯ�����ص������
     * @param hql
     * @param params
     * @return
     */
    Object uniqueResult(String hql, Object... params) throws DaoException;

    /**
     * ������õĲ�ѯ��ƽ��и��²���
     * @param updateQueryName
     * @param params
     * @return
     */
    int executeUpdateByNamedQuery(String updateQueryName, Object... params) throws DaoException;

    /**
     * ���HQL���и��²���
     * @param hql
     * @param params
     * @return
     */
    int executeUpdate(String hql, Object... params) throws DaoException;

    /**
     * ������õĲ�ѯ��ƽ���Hibernate��ʽ�������ѯ�����ض�����
     * @param queryName
     * @param name
     * @param params
     * @return
     */
    List listByNamedQuery(String queryName, String[] name, Object[] params) throws DaoException;

    /**
     * ���HQL����Hibernate��ʽ�������ѯ�����ض�����
     * @param hql
     * @param name
     * @param params
     * @return
     */
    List list(String hql, String[] name, Object[] params) throws DaoException;

    /**
     * ������õĲ�ѯ��ƽ���Hibernate��ʽ�������ѯ�����ص������
     * @param queryName
     * @param name
     * @param params
     * @return
     */
    Object uniqueResultByNamedQuery(String queryName, String[] name, Object[] params) throws DaoException;

    /**
     * ���HQL����Hibernate��ʽ�������ѯ�����ص������
     * @param hql
     * @param name
     * @param params
     * @return
     */
    Object uniqueResult(String hql, String[] name, Object[] params) throws DaoException;

    /**
     * ������õĲ�ѯ��ƽ���Hibernate��ʽ��������²���
     * @param updateQueryName
     * @param name
     * @param params
     * @return
     */
    int executeUpdateByNamedQuery(String updateQueryName, String[] name, Object[] params) throws DaoException;

    /**
     * ���HQL����Hibernate��ʽ��������²���
     * @param hql
     * @param name
     * @param params
     * @return
     */
    int executeUpdate(String hql, String[] name, Object[] params) throws DaoException;

    /**
     * ���clazz���ʹ���ݿ���ȡ�ÿ�ʼ����offset������������maximum�����
     * @param queryMap
     * @param offset
     * @param max
     * @return
     * @throws DaoException
     */
    public Page getObjectPage(Map queryMap, Integer offset, Integer max) throws DaoException;
    
    /**
     * 
     * @param hql
     * @param offset
     * @param max
     * @return Page
     * @throws DaoException
     */
    public Page getObjectPage(String hql, Integer offset, Integer max) throws DaoException;
}
