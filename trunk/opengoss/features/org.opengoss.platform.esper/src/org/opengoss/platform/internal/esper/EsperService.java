package org.opengoss.platform.internal.esper;

import net.esper.client.Configuration;
import net.esper.client.EPServiceProvider;
import net.esper.client.EPServiceProviderManager;

import org.opengoss.platform.esper.IEsperService;

public class EsperService implements IEsperService {

	public EPServiceProvider getEPServiceProvider(String name,
			Configuration config) {
		return EPServiceProviderManager.getProvider(name, config);
	}

}
