package org.opengoss.alarm.manager.internal.core;

import java.util.List;

import org.opengoss.alarm.core.Alarm;
import org.opengoss.alarm.core.IAlarmDao;
import org.opengoss.alarm.manager.core.IAlarmInfoService;
import org.opengoss.dao.core.DaoException;

/**
 * @author hurr
 *
 */
public class AlarmInfoService implements IAlarmInfoService {

	private IAlarmDao alarmDao;
	
	public void setAlarmDao(IAlarmDao alarmDao) {
		this.alarmDao = alarmDao;
	}
	
	//pageNum from 0 to ...,
	public List<Alarm> getAlarmInfo(){
		List<Alarm> allAlarm = null;
		try {
			allAlarm = alarmDao.listAll(Alarm.class);
			System.out.println(allAlarm.size());
			return allAlarm;
		} catch (DaoException e) {
			
			e.printStackTrace();
		}
		if (allAlarm.size() == 0) {
			// 没有数据
			System.out.println("no data");
		} 
		return null;
	}

	//以后要修改，增加查询结果的总页数
	public int pageCountSum(int itemCount) {
		int pageSum = 0;
		Class clazz = Alarm.class;
		List<Alarm> result=null;
		try {
			result = alarmDao.listAll(clazz);
		} catch (DaoException e) {
			e.printStackTrace();
		} 
		int itemSum = result.size();
		if(itemSum != 0) {
			pageSum = itemSum/itemCount;
			int mod = itemSum % itemCount;
			pageSum = (mod == 0?pageSum:pageSum + 1);
		}
		return pageSum;
	}

	public boolean deleteAlarm(Long[] alarmIds) {
		try {
			for(int i = 0; i < alarmIds.length; i++) {
				Alarm _alarm = alarmDao.getById(alarmIds[i]);
				if (_alarm != null) {
					alarmDao.delete(_alarm);
				}
			}
			return true;
		}catch (DaoException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateAlarmMemo(Long alarmId,String memo) {
		try {
			Alarm _alarm = alarmDao.getById(alarmId);
			if (_alarm != null) {
				_alarm.setMemo(memo);
				alarmDao.update(_alarm);
				return true;
			}
			return false;
		}catch (DaoException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Alarm> queryAlarmByType(String alarmType) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("from Alarm alarm ");
		sbuffer.append("where alarm.alarmType = '" + alarmType + "'");
		System.out.println(sbuffer.toString());
		try {
			List<Alarm> queryList = alarmDao.query(sbuffer.toString());
			return queryList;
		}catch (DaoException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean setAckUserId(Long[] alarmIds) {
		try {
			for (int i = 0; i < alarmIds.length; i++) {
				Alarm _alarm = alarmDao.getById(alarmIds[i]);
				if (_alarm != null) {
					_alarm.setAckUserId(888);//以后修改
					alarmDao.update(_alarm);
				}
			}
			return true;
		}catch (DaoException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setAckUserIdToDefault(Long[] alarmIds) {
		try {
			for (int i = 0; i < alarmIds.length; i++) {
				Alarm _alarm = alarmDao.getById(alarmIds[i]);
				if (_alarm != null) {
					_alarm.setAckUserId(0);
					alarmDao.update(_alarm);
				}
			}
			return true;
		}catch (DaoException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * to show the details of the alarm by alarmId
	 * @ return org.opengoss.alarm.core.Alarm
	 * @ parameter alarmId Long
	 */ 
	
	public Alarm getAlarmDetail(Long alarmId) {
		try {
			Alarm _alarm = alarmDao.getById(alarmId);
			if (_alarm != null) {
				return _alarm;
			}
			return null;
		}catch (DaoException e) {
			e.printStackTrace();
			return null;
		}
	}
}
