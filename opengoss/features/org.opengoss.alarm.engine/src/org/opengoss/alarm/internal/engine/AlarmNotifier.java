package org.opengoss.alarm.internal.engine;

import net.esper.client.UpdateListener;
import net.esper.event.EventBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.alarm.core.Alarm;
import org.opengoss.alarm.core.HandlePhase;
import org.opengoss.alarm.engine.IAlarmEngine;
import org.opengoss.core.IInitializable;
import org.opengoss.platform.esper.EsperSupport;

public class AlarmNotifier extends EsperSupport 
	implements IInitializable, UpdateListener {

	static final Log logger = LogFactory.getLog(AlarmNotifier.class);

	public AlarmNotifier(IAlarmEngine engine) {
		super(engine.getEsper());
	}

	public void init() throws Exception {
		epStatement = esper.getEPAdministrator().createEQL(
				"select * from AlarmEvent.win:time(2 sec) where phrase=" + HandlePhase.PERSISTED);
		epStatement.addListener(this);
//		epStatement.start();
	}

	public void destory() throws Exception {
		epStatement.removeListener(this);
//		epStatement.start();
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents == null) {
			return;
		}
		for (EventBean eventBean : newEvents) {
			Alarm alarm = (Alarm) eventBean.getUnderlying();
			logger.info(alarm.toString());
		}
	}

}
