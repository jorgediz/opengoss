package org.opengoss.alarm.internal.engine;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.esper.client.EPServiceProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.alarm.engine.IAlarmEngine;
import org.opengoss.alarm.engine.IEventCollector;
import org.opengoss.core.IStartable;

public class RawEventCollector implements Runnable, IStartable, IEventCollector {

	final static Log logger = LogFactory.getLog(RawEventCollector.class);

	private Thread thread;

	private boolean stop;

	private EPServiceProvider esper;

	private ArrayBlockingQueue<Map<String, Object>> queue = new ArrayBlockingQueue<Map<String, Object>>(
			10000);

	public RawEventCollector(IAlarmEngine engine) {
		this.esper = engine.getEsper();
	}

	public void collectEvent(Map<String, Object> eventMap) {
		try {
			if (stop) {
				return;
			}
			boolean isOk = queue.offer(eventMap, 1, TimeUnit.SECONDS);
			if (!isOk) {
				logger.warn("No space in trap queue!");
			}
		} catch (InterruptedException e) {
			logger.warn("Exception happened when putting trap msg to queue!", e);
		} catch (Exception e) {
			logger.warn("Unexpected exception happened!", e);
		}
	}

	public void start() throws Exception {
		stop = false;
		thread = new Thread(this);
		thread.start();
	}

	public void stop() throws Exception {
		this.stop = true;
		thread.join();
		thread = null;
	}

	public void run() {
		while (!stop) {
			try {
				Map<String, Object> eventMap = queue.poll(1, TimeUnit.SECONDS);
				if (eventMap != null) {
					esper.getEPRuntime().sendEvent(eventMap, "RawEvent");
				}
			} catch (InterruptedException e) {
				logger.warn("Exception happened when taking trap msg from queue!", e);
			} catch (Exception e) {
				logger.warn("Unexpected exception happened!", e);
			}
		}
		Object[] leftObjs = queue.toArray();
		for (Object obj : leftObjs) {
			esper.getEPRuntime().sendEvent(((Map<String, Object>) obj),
					"RawEvent");
		}
		queue.clear();
	}

}
