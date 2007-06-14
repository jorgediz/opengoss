package org.opengoss.probe.trap;

import java.net.URL;
import java.util.Properties;

/**
 * Configuration of trap parser engine.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 20061130
 * @since 1.0
 */
public interface IEngineConfiguration {
	
	String MAPPING_FILE = "mapping.properties";
	
	/**
	 * Get the mapping properties.
	 * 
	 * @return the mapping properties
	 */
	Properties getMapping();

	/**
	 * Get preload rules.
	 * 
	 * @return preload rules.
	 */
	URL[] getPreloadRules();

	/**
	 * Get parse rules.
	 * 
	 * @return parse rules.
	 */
	URL[] getParseRules();

}
