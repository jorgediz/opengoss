package org.opengoss.cnm.log.internal.dao;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.opengoss.cnm.log.core.LoggerItem;
import org.opengoss.cnm.log.core.LoggerItemList;
import org.opengoss.cnm.log.core.OperationLog;
import org.opengoss.cnm.log.core.SysLog;
import org.opengoss.dao.core.DaoException;

public class LmDaoTest extends TestCase {
	private LmDao lmDao;

	public void setUp() throws Exception {
		lmDao = new LmDao(HibernateUtil.sessionFactory);
	}

	public void tearDown() throws Exception {
		lmDao = null;
	}

	private void addLog() throws DaoException{
		OperationLog opLog = new OperationLog();
		opLog.setDescription("opLog1");
		opLog.setUserName("userName1");
		Date data = new Date();
		opLog.setTime(data.getTime());
		SysLog sLog = new SysLog(SysLog.Level.ERROR,"sys1");
		lmDao.save(opLog);
		lmDao.save(sLog);
	}
	
	//********************************************
	/*public void testSave() throws DaoException{
		List<DomainObject> log = lmDao.findLogRecordByHql("from OperationLog opLog where opLog.userName = 'userName' ");
		assertEquals("opLog1",((OperationLog)log.get(0)).getDescription() );
	}*/
	
	
	/*public void testFindLogRecord() throws DaoException{
		DomainObject opLog = lmDao.findLogRecord(OperationLog.class, new Long(1));
		assertEquals("userName", ((OperationLog)opLog).getUserName());
		DomainObject sysLog = lmDao.findLogRecord(SysLog.class, new Long(1));
		assertEquals("sys1"	, ((SysLog)sysLog).getDescription());
	}*/
	
	
/*	public void testLoadOperationRecord() throws DaoException{
		List<OperationLog> oRecords = lmDao.loadOperationRecord();
		assertEquals(2, oRecords.size());
		
	}
	
	public void testLoadSysRecord() throws DaoException{
		List<SysLog> oRecords = lmDao.loadSysRecord();
		assertEquals(2, oRecords.size());
		assertEquals("sys2", oRecords.get(1).getDescription());
		
	}*/
	
	private void addLoggerItemList() throws DaoException{
		LoggerItemList loggerItemList = new LoggerItemList();
		LoggerItem loggerItem = new LoggerItem();
		loggerItem.setLogInstance(LoggerItem.LogInstance.OPERATIONLOG);
		loggerItem.setCurrentLogSize(3000);
		loggerItem.setLogRecordNumber(199);
		loggerItem.setMaxLogSize(2000);
		loggerItem.setMaxBufferSize(1);
		loggerItem.setAvailabilityStatus(LoggerItem.AvailabilityStatus.LOGFULL);
		loggerItemList.addLoggerItem(LoggerItem.LogInstance.OPERATIONLOG, loggerItem);
		
		LoggerItem sysLoggerItem = new LoggerItem();
		sysLoggerItem.setLogInstance(LoggerItem.LogInstance.SYSTEMLOG);
		sysLoggerItem.setCurrentLogSize(1000);
		sysLoggerItem.setLogRecordNumber(200);
		sysLoggerItem.setMaxLogSize(2000);
		sysLoggerItem.setMaxBufferSize(1);
		sysLoggerItem.setAvailabilityStatus(LoggerItem.AvailabilityStatus.LOGFULL);
		loggerItemList.addLoggerItem(LoggerItem.LogInstance.SYSTEMLOG, sysLoggerItem);
		lmDao.saveLoggerItemList(loggerItemList);
	}
	/*public void  testLoggerItemList() throws DaoException{
		addLoggerItemList();
		LoggerItemList loggerItemList = lmDao.loadLoggerItemList();
		assertEquals(3000, loggerItemList.getLoggerItem(LoggerItem.LogInstance.OPERATIONLOG).getCurrentLogSize());
		assertEquals(1000, loggerItemList.getLoggerItem(LoggerItem.LogInstance.SYSTEMLOG).getCurrentLogSize());
	}*/
	
	public void testLog() throws DaoException{
		addLog();
		List<OperationLog> opList = lmDao.loadOperationRecord();
		assertEquals(1, opList.size());
		List<SysLog> sysList = lmDao.loadSysRecord();
		assertEquals(1, sysList.size());
	}
}
