/**
 * 
 */
package org.opengoss.alarm.manager.core;

import java.util.List;

import org.opengoss.alarm.core.Alarm;


/**
 * @author hurr
 *
 */
public interface IAlarmInfoService {
	
	public int pageCountSum(int itemCount);
	
	public List<Alarm> getAlarmInfo();
	
	public boolean updateAlarmMemo(Long alarmId,String memo);
	
	public boolean deleteAlarm(Long[] alarmIds);
	
	public List<Alarm> queryAlarmByType(String alarmType);
	
	public boolean setAckUserIdToDefault(Long[] alarmIds);
	
	public boolean setAckUserId(Long[] alarmIds);
	
	public Alarm getAlarmDetail(Long alarmId);
}
