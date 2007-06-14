package org.opengoss.probe.internal.trap;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.esper.client.EPServiceProvider;
import net.esper.client.EPStatement;
import net.esper.client.UpdateListener;
import net.esper.event.EventBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.probe.trap.EventMessage;
import org.opengoss.probe.trap.IEngineConfiguration;
import org.opengoss.probe.trap.ITrapParseEngine;
import org.opengoss.probe.trap.ITrapProbe;

public class TrapParseEngine implements ITrapParseEngine, UpdateListener {

	static final String PARSE_TRAP_FUNC = "parseTrap";

	static final Log logger = LogFactory.getLog(TrapParseEngine.class);

	private ScriptEngine engine;

	private IEngineConfiguration engineConfig;

	private EPServiceProvider esper;

	private EPStatement epStatement;

	public TrapParseEngine(ITrapProbe probe, IEngineConfiguration config) {
		this.engineConfig = config;
		ScriptEngineManager manager = new ScriptEngineManager();
		this.engine = manager.getEngineByName("JavaScript");
		this.esper = probe.getEsper();
	}

	public void start() throws Exception {
		URL[] preloadRules = engineConfig.getPreloadRules();
		for (URL preloadRule : preloadRules) {
			engine.eval(new InputStreamReader(preloadRule.openStream()));
		}
		URL[] parseRules = engineConfig.getParseRules();
		for (URL parseRule : parseRules) {
			engine.eval(new InputStreamReader(parseRule.openStream()));
		}

		this.epStatement = esper.getEPAdministrator().createEQL(
				"select * from TrapMessage.win:time(1 sec)");
		epStatement.addListener(this);
//		epStatement.start();
	}

	public void stop() throws Exception {
		epStatement.removeListener(this);
//		epStatement.stop();
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents == null) {
			return;
		}
		for (EventBean bean : newEvents) {
			try {
				Map<String, Object> trapMsg = (Map<String, Object>) bean
						.getUnderlying();
				Map<String, Object> eventMsg = parseMsg(trapMsg);
				if (eventMsg != null) {
					EventMessage event = new EventMessage();
					event.setValueMap(eventMsg);
					esper.getEPRuntime().route(event);
				}
			} catch (Exception e) {
				logger.warn("Unexpected exception happened!", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> parseMsg(Map<String, Object> trapMsg)
			throws Exception {
		String trapOid = (String) trapMsg.get("trap_oid");
		String parseConfig = engineConfig.getMapping().getProperty(trapOid);
		if (parseConfig == null) {
			logger.warn("No parser config found for trapOid " + trapOid);
			return null;
		}
		String[] configs = parseConfig.split(":");
		if (configs.length != 2) {
			logger.warn("Not valid config for trapoid " + trapOid);
			return null;
		}
		Invocable invoc = (Invocable) engine;
		Object trapConfig = engine.get(configs[0]);
		Object parseFunc = engine.get(configs[1]);
		return (Map<String, Object>) invoc.invokeFunction(PARSE_TRAP_FUNC,
				trapMsg, trapConfig, parseFunc);
	}

}
