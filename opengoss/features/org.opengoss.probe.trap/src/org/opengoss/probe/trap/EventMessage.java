package org.opengoss.probe.trap;

import java.util.Map;

public class EventMessage {

	private Map<String, Object> valueMap;

	public EventMessage() {
	}

	public void setValueMap(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
	}

	public Map<String, Object> getValueMap() {
		return valueMap;
	}

}
