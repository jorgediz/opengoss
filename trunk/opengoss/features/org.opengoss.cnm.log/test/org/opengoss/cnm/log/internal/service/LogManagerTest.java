package org.opengoss.cnm.log.internal.service;

import org.opengoss.cnm.log.core.LogType;
import org.opengoss.cnm.log.core.OperationLog;
import org.opengoss.cnm.log.core.OperationSearchData;

import junit.framework.TestCase;

public class LogManagerTest extends TestCase{
	LogManager mgr;
	OperationSearchData data ;
	public void setUp() throws Exception{
	  mgr = new LogManager();
	  data = new OperationSearchData();
	  data.setType(0);
	  data.setAction("action");
	  data.setDesc("desc");
	  data.setResult("result");
	  data.setUserIP("userIP");
	  data.setUserName("userName");
	}
	
	public void tearDown()throws Exception{
		mgr = null;
		data = null ;
	}
	
	public void testParse2Sql(){
		
//		mgr.parse2Sql( data);
	}
}
