package org.opengoss.example.log.consumer.internal;

import org.opengoss.core.IPluginContext;
import org.opengoss.core.PluginActivator;
import org.opengoss.example.log.consumer.ILogConsumer;

public class Activator extends PluginActivator {

	@Override
	protected void startPlugin(IPluginContext pluginContext) throws Exception {
		ILogConsumer consumer = (ILogConsumer)pluginContext.getServiceRegistry().getService("Consumer");
		consumer.consume();
		consumer.consume();
		consumer.consume();
	}

	@Override
	protected void stopPlugin(IPluginContext pluginContext) throws Exception {
		//nothing.
	}

}
