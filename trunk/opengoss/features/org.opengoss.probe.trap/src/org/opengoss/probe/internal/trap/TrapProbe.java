package org.opengoss.probe.internal.trap;

import java.io.File;

import net.esper.client.Configuration;
import net.esper.client.EPServiceProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.platform.esper.IEsperService;
import org.opengoss.probe.trap.ITrapProbe;
//TODO
public class TrapProbe implements ITrapProbe {
		
	static final Log logger = LogFactory.getLog(TrapProbe.class);
	
	private IEsperService esperSerevice;
	
	private EPServiceProvider epServiceProvider = null;

	public TrapProbe(IEsperService esperService) {
		this.esperSerevice = esperService;
	}
	
	public EPServiceProvider getEsper() {
		if(epServiceProvider == null) {
			Configuration config = new Configuration();
			config.configure(new File("./etc/org.opengoss.probe.trap/esper.cfg.xml"));
			epServiceProvider = esperSerevice.getEPServiceProvider("TrapProbe", config);
		}
		return epServiceProvider;
	}

}
