package org.opengoss.probe.trap;

import net.esper.client.EPServiceProvider;

/**
 * Receive and parse trap message, produce event msg.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * 
 */
public interface ITrapProbe {

	EPServiceProvider getEsper();

}
