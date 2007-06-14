package org.opengoss.alarm.core;

public interface HandlePhase {
	
	int INITIALIZED = -1;

	int CONSOLIDED = 1;
	
	int CORRELATED = 2;
	
	int PERSISTED = 3;
	
	int ANALYZED = 4;
	
	int NOTIFIED = 5;
}
