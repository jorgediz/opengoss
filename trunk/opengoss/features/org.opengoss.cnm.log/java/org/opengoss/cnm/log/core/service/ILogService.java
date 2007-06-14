package org.opengoss.cnm.log.core.service;

import java.util.List;

import org.opengoss.cnm.log.core.OperationLog;
import org.opengoss.cnm.log.core.OperationSearchData;
import org.opengoss.cnm.log.core.SysLog;
import org.opengoss.cnm.log.core.SysSearchData;
import org.opengoss.cnm.log.internal.exception.CnmException;
import org.opengoss.dao.core.DomainObject;

/**
 * 鎻愪緵log妯″潡鐨勫惎鍔ㄥ叧闂姛鑳斤紝骞朵笖鎻愪緵鏌ユ壘鏃ュ織鍔熻兘銆�
 * @author zouxc
 * @date 2006-12-17
 */
public interface ILogService {

	public void startUp( ) throws CnmException;
	
	public void stop();
//
//    /**
//     * return all loggers from default config
//     *
//     * @return list
//     */
//    List<LoggerItem> findAllLoggerItem ();
    
    /**
     * @param <T>
     * @param className
     * @param condition 瀵逛簬鏌ユ壘OperationLog鏉ヨ锛岄粯璁ゅ搴旀槸type,userIP,result,userName,action,desc
     * @return
     * @throws CNMException
     */
    public  List<DomainObject> findLogRecord(String className,OperationSearchData data) throws CnmException;
    
    public List<SysLog> findSysLogRecord(SysSearchData data) throws CnmException;
    
    public List<OperationLog> loadOperationLogRecord() throws CnmException;
   
    public List<SysLog> loadSysLogRecord() throws CnmException;
    
    public String hello();
}
