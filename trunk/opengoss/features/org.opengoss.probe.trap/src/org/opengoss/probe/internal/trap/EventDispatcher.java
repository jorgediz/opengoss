package org.opengoss.probe.internal.trap;

import java.util.ArrayList;
import java.util.List;

import net.esper.client.EPAdministrator;
import net.esper.client.EPServiceProvider;
import net.esper.client.EPStatement;
import net.esper.client.UpdateListener;
import net.esper.event.EventBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.alarm.engine.IEventSender;
import org.opengoss.core.IInitializable;
import org.opengoss.probe.trap.EventMessage;
import org.opengoss.probe.trap.IEventDispatcher;
import org.opengoss.probe.trap.ITrapProbe;

public class EventDispatcher implements IEventDispatcher, IInitializable,
		UpdateListener {

	static final Log logger = LogFactory.getLog(EventDispatcher.class);

	private EPServiceProvider esper;

	private EPStatement statement;

	private List<IEventSender> eventList = new ArrayList<IEventSender>(3);

	public EventDispatcher(ITrapProbe probe) {
		this.esper = probe.getEsper();
	}

	public void init() throws Exception {
		EPAdministrator admin = esper.getEPAdministrator();
		statement = admin
				.createEQL("select * from EventMessage.win:time(1 sec)");
		statement.addListener(this);
//		statement.start();
	}

	public void destory() throws Exception {
		statement.removeListener(this);
//		statement.stop();
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents == null) {
			return;
		}
		for (EventBean bean : newEvents) {
			EventMessage event = (EventMessage) bean.getUnderlying();
			dispatchEvent(event);
			logger.info("EventMessage Dispatched:" + event.getValueMap().toString());
		}
	}

	private void dispatchEvent(EventMessage event) {
		IEventSender[] senders = eventList.toArray(new IEventSender[eventList.size()]);
		for (int i = 0; i < senders.length; i++) {
			senders[i].sendEvent(event.getValueMap());
		}
	}

	public void add(IEventSender eventSender) {
		eventList.add(eventSender);
	}

	public void remove(IEventSender eventSender) {
		eventList.remove(eventSender);
	}

}
