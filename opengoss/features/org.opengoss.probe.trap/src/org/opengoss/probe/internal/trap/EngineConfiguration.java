package org.opengoss.probe.internal.trap;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.probe.trap.IEngineConfiguration;

public class EngineConfiguration implements IEngineConfiguration {
	
	Log logger = LogFactory.getLog(EngineConfiguration.class);
	
	Properties mapping;
	
	URL[] preloadRules;
	
	URL[] parseRules;
	
	public EngineConfiguration() throws Exception {
//		URL home = configMgr.getConfigHome();
//		if(home == null) {
//			throw new Exception("Cannot find configuration home!");
//		}
		URL rulesRepos = new File("./etc/org.opengoss.probe.trap/rules").toURI().toURL();//new URL(home, "rules");
		init(new File(rulesRepos.getFile()));
	}

	public Properties getMapping() {
		
		return mapping;
	}

	public URL[] getParseRules() {
		return parseRules;
	}

	public URL[] getPreloadRules() {
		return preloadRules;
	}

	private void init(File repos) throws Exception {
		//load mapping properites first.
		File mappingFile = new File(repos, MAPPING_FILE);
		mapping = new Properties();
		mapping.load(new FileReader(mappingFile));
		//load preload rules.
		String config = mapping.getProperty("preloadRules");
		String[] ruleNames = config.split(",");
		preloadRules = new URL[ruleNames.length];
		for (int i = 0; i < ruleNames.length; i++) {
			preloadRules[i] = toURL(new File(repos, ruleNames[i]));
		}
		//load parse rules.
		File[] ruleFiles = repos.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if( !isPreloadRule(pathname, preloadRules) && isJsFile(pathname) ) {
					return true;
				}
				return false;
			}

			private boolean isPreloadRule(File pathname, URL[] preloadRules) {
				URL rule = toURL(pathname);
				for (URL preloadRule : preloadRules) {
					if(rule.equals(preloadRule)) {
						return true;
					}
				}
				return false;
			}
		});
		parseRules = new URL[ruleFiles.length];
		for (int i = 0; i < ruleFiles.length; i++) {
			parseRules[i] = toURL(ruleFiles[i]);
		}
	}

	private boolean isJsFile(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".js");
	}

	private URL toURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			logger.warn("Invalid file.", e);
			return null;
		}
	}
}
