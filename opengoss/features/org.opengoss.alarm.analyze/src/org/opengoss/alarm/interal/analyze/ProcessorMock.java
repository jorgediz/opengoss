package org.opengoss.alarm.interal.analyze;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.alarm.analyze.IAnalyzeEngine;
import org.opengoss.alarm.analyze.IAnalyzeProcessor;
import org.opengoss.alarm.core.Alarm;
import org.opengoss.core.IStartable;

public class ProcessorMock implements IAnalyzeProcessor, IStartable {
	
	private static Log log = LogFactory.getLog(ProcessorMock.class);
	
	IAnalyzeEngine engine;

	String filter;

	public ProcessorMock(IAnalyzeEngine engine) {
		this.engine = engine;
		this.engine.addProcessor(this);
	}

	public void execute(Alarm alarm) {
		if(log.isDebugEnabled()){
			
			StringBuffer sb = new StringBuffer();
			sb.append("===============================================");
			sb.append(" Name : " + alarm.getName() + "/r/n");
			sb.append(" Vendor Alarm Id : " + alarm.getVendorAlarmId() + "/r/n");
			sb.append(" Vendor Alarm Name : " + alarm.getVendorAlarmName() + "/r/n");
			sb.append(" Alarm Type : " + alarm.getAlarmType() + "/r/n");
			sb.append(" ProbableCause : " + alarm.getProbableCause() + "/r/n");
			sb.append(" Special Problem : " + alarm.getSpecialProblem() + "/r/n");
			sb.append(" Perceived Severity : " + alarm.getPerceivedSeverity()
					+ "/r/n");
			sb.append(" Alarm Raised Time : " + alarm.getAlarmRaisedTime() + "/r/n");
			sb.append(" Additional Information : " + alarm.getAdditionalInfo() + "/r/n");
			sb.append(" Location : " + alarm.getLocation() + "/r/n");
			sb.append("=================================================");
			
			log.debug(sb.toString());
		}

	}

	public String getFilter() {
		return this.filter;
	}

	public void start() throws Exception {

	}

	public void stop() throws Exception {
		engine.removeProcessor(this);
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

}
