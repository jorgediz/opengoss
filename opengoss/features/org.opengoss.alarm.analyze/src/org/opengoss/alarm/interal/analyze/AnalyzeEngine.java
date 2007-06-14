package org.opengoss.alarm.interal.analyze;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.alarm.analyze.IAnalyzeEngine;
import org.opengoss.alarm.analyze.IAnalyzeProcessor;
import org.opengoss.alarm.core.Alarm;
import org.opengoss.alarm.engine.IAlarmEngine;
import org.opengoss.core.IStartable;

public class AnalyzeEngine implements IAnalyzeEngine, IStartable {

	private static Log log = LogFactory.getLog(AnalyzeEngine.class);

	List<IAnalyzeProcessor> processors = new ArrayList<IAnalyzeProcessor>();

	ScheduledExecutorService schedule = new ScheduledThreadPoolExecutor(1);

	private long period = 30;

	private long initialDelay = 30;

	private Long lastTime = Long.valueOf(0);

	private IAlarmEngine engine;
	
	private String defaultFilter = "from Alarm as alarm where ( alarm.alarmRaisedTime > %s and alarm.alarmRaisedTime < %s )";

	public void addProcessor(IAnalyzeProcessor processor) {
		processors.add(processor);
	}

	public void removeProcessor(IAnalyzeProcessor processor) {
		processors.remove(processor);
	}

	public void start() throws Exception {

		schedule.scheduleAtFixedRate(new Runner(), initialDelay, period,
				TimeUnit.SECONDS);
	}

	public void stop() throws Exception {
		schedule.shutdownNow();
	}

	class Runner implements Runnable {

		public void run() {

			for (IAnalyzeProcessor processor : processors) {
				
				try {
					doProcessor(processor);
				} catch (Exception e) {
					log.error(processor.getFilter(), e);
				}
			}
		}

		public void doProcessor(IAnalyzeProcessor processor) {
			String filter = buildFilter( processor.getFilter());
			List<Alarm> list = engine.getEvents( filter );
			
			for (Alarm alarm : list) {
				processor.execute(alarm);
			}
		}

	}

	private String buildFilter(String condition) {
		String filter ;
		Long currentTime = System.currentTimeMillis();

		if (condition == null && "".equals(condition)) {
			filter=defaultFilter;
		}else{
			filter = defaultFilter + " and ( %s )";
		}
		filter = String.format(filter,lastTime,currentTime,condition);
		lastTime = currentTime;
		return filter;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public IAlarmEngine getEngine() {
		return engine;
	}

	public void setEngine(IAlarmEngine engine) {
		this.engine = engine;
	}

	
	public static void main(String[] args){
		 Long lastTime = Long.valueOf(0);
		String filter="from Alarm as alarm where %s alarm.alarmRaisedTime > %s and alarm.alarmRaisedTime < %s %s";
		System.out.println(String.format(filter, "",lastTime,12,"",null));

		
	}

	public String getDefaultFilter() {
		return defaultFilter;
	}

	public void setDefaultFilter(String defaultFilter) {
		this.defaultFilter = defaultFilter;
	}
}
