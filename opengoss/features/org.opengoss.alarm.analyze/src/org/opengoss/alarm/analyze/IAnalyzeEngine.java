package org.opengoss.alarm.analyze;

public interface IAnalyzeEngine {

	public void addProcessor(IAnalyzeProcessor processor);

	public void removeProcessor(IAnalyzeProcessor processor);
}
