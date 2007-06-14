package org.opengoss.platform.esper;

import net.esper.client.Configuration;
import net.esper.client.EPServiceProvider;

public interface IEsperService {

	EPServiceProvider getEPServiceProvider(String name, Configuration config);
	
}
