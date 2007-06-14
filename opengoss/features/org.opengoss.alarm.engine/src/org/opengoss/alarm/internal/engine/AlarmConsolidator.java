package org.opengoss.alarm.internal.engine;

import java.util.Map;

import net.esper.client.UpdateListener;
import net.esper.event.EventBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.alarm.core.Alarm;
import org.opengoss.alarm.core.HandlePhase;
import org.opengoss.alarm.engine.IAlarmConsolidator;
import org.opengoss.alarm.engine.IAlarmEngine;
import org.opengoss.core.IInitializable;
import org.opengoss.platform.esper.EsperSupport;

public class AlarmConsolidator extends EsperSupport implements
		IAlarmConsolidator, UpdateListener, IInitializable {
	
	static final Log logger = LogFactory.getLog(AlarmConsolidator.class);
	
	public AlarmConsolidator(IAlarmEngine engine) {
		super(engine.getEsper());
	}

	public void init() throws Exception {
		this.epStatement = esper.getEPAdministrator().createEQL(
				"select * from RawEvent.win:time(1 sec)");
		epStatement.addListener(this);
//		epStatement.start();
	}

	public void destory() throws Exception {
//		epStatement.stop();
		epStatement.removeListener(this);
	}
	
	public Alarm consolidate(Map<String, Object> eventMsg) {
		Alarm alarm = new Alarm();
		alarm.setAlarmKey((String) eventMsg.get("alarm_key"));
		alarm.setPerceivedSeverity(((Double)eventMsg.get("alarm_severity")).intValue());
		alarm.setFirstOccurence((Long)eventMsg.get("time_stamp"));
		alarm.setAlarmRaisedTime((Long)eventMsg.get("received_time"));
		alarm.setPhrase(HandlePhase.CONSOLIDED);
		alarm.setAlarmSource((String)eventMsg.get("node_addr"));
		return alarm;
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if(newEvents == null) {
			return;
		}
		for (EventBean eventBean : newEvents) {
			Alarm alarm = consolidate((Map<String, Object>)eventBean.getUnderlying());
			esper.getEPRuntime().route(alarm);
		}
	}

}
