package org.opengoss.probe.internal.trap;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.esper.client.EPServiceProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.core.IStartable;
import org.opengoss.probe.trap.ITrapProbe;
import org.opengoss.probe.trap.ITrapReceiver;
import org.opengoss.snmphibernate.api.ISnmpTrapD;
import org.opengoss.snmphibernate.api.ISnmpTrapMsg;

public class TrapReceiver implements Runnable, IStartable, ITrapReceiver {

	final static Log logger = LogFactory.getLog(TrapReceiver.class);

	private EPServiceProvider esper;
	
	private Thread thread;
	
	private boolean stop;
	
	private ISnmpTrapD trapDaemon;

	private ArrayBlockingQueue<ISnmpTrapMsg> queue 
		= new ArrayBlockingQueue<ISnmpTrapMsg>(10000);

	public TrapReceiver(ITrapProbe probe, ISnmpTrapD trapDaemon) {
		this.esper = probe.getEsper();
		this.trapDaemon = trapDaemon;
	}

	public void start() throws Exception {
		stop = false;
		thread = new Thread(this);
		thread.start();
		trapDaemon.addTrapHandler(this);
	}

	public void stop() throws Exception {
		this.stop = true;
		thread.join();
		thread = null;
		trapDaemon.removeTrapHandler(this);
	}

	public boolean onTrapMsg(ISnmpTrapMsg trapMsg) {
		try {
			if (stop) {
				return false;
			}
			boolean isOk = queue.offer(trapMsg, 1, TimeUnit.SECONDS);
			if (!isOk) {
				logger.warn("No space in trap queue!");
			}
		} catch (InterruptedException e) {
			logger.warn(
					"Exception happened when putting trap msg to queue!", e);
		} catch (Exception e) {
			logger.warn("Unexpected exception happened!", e);
		}
		return true;
	}

	public void run() {
		while (!stop) {
			try {
				ISnmpTrapMsg msg = queue.poll(1, TimeUnit.SECONDS);
				if (msg != null) {
					esper.getEPRuntime().sendEvent(msg.toMap(), "TrapMessage");
				}
			} catch (InterruptedException e) {
				logger.warn(
						"Exception happened when taking trap msg from queue!",
						e);
			} catch (Exception e) {
				logger.warn("Unexpected exception happened!", e);
			}
		}
		Object[] leftObjs = queue.toArray();
		for (Object obj : leftObjs) {
			esper.getEPRuntime().sendEvent(((ISnmpTrapMsg) obj).toMap(),
					"TrapMessage");
		}
		queue.clear();
	}

}
