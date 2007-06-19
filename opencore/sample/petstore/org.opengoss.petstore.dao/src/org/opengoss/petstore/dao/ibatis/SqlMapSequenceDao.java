package org.opengoss.petstore.dao.ibatis;

import org.opengoss.dao.core.DaoException;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

public class SqlMapSequenceDao extends SqlMapDaoTemplate {

	public SqlMapSequenceDao(DaoManager daoManager) {
		super(daoManager);
	}

	/**
	 * This is a generic sequence ID generator that is based on a database table
	 * called 'SEQUENCE', which contains two columns (NAME, NEXTID). This
	 * approach should work with any database.
	 * 
	 * @param name
	 *            the name of the sequence
	 * @return the next ID
	 */
	public int getNextId(String name) throws DaoException {
		Sequence sequence = new Sequence(name, -1);
		sequence = (Sequence) queryForObject("getSequence", sequence);
		if (sequence == null) {
			throw new DaoException(
					"Could not get next value of sequence '" + name
							+ "': sequence does not exist");
		}
		Object parameterObject = new Sequence(name, sequence.getNextId() + 1);
		update("updateSequence", parameterObject);
		return sequence.getNextId();
	}

}
