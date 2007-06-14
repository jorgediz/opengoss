package org.opengoss.platform.esper;

import net.esper.client.EPServiceProvider;
import net.esper.client.EPStatement;

public class EsperSupport {

	protected final EPServiceProvider esper;

	protected EPStatement epStatement;
	
	public EsperSupport(EPServiceProvider esper) {
		this.esper = esper;
	}
	
}
