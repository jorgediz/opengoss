package org.opengoss.probe.trap;

import java.util.Map;

import org.opengoss.core.IStartable;

public interface ITrapParseEngine extends IStartable {
	
	/**
	 * Parse trap message and return a event message.
	 * 
	 * @param trapMsg trap message
	 * @return event message
	 */
	Map<String, Object> parseMsg(Map<String, Object> trapMsg) throws Exception;
	
}
