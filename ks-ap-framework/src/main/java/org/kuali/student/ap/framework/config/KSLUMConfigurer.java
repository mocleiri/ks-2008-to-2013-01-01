package org.kuali.student.ap.framework.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;

/**
 * Provides default ks-lum service behavior as a local Rice module, suitable
 * for bundling with ks-ap when another provider is not available in the
 * environment.
 * 
 * @author Mark Fyffe <mwfyffe@indiana.edu>
 * @version ks-ap-0.1.1
 */
public class KSLUMConfigurer extends ModuleConfigurer {

	public KSLUMConfigurer() {
		super(KSAPConstants.KSLUM_MODULE_NAME);
		setValidRunModes(Arrays.asList(RunMode.LOCAL));
	}

	@Override
	protected String getDefaultConfigPackagePath() {
		return KSAPConstants.KSAP_PACKAGE_CONFIG_PATH;
	}

	@Override
	public List<String> getPrimarySpringFiles() {
		List<String> springFileLocations = new ArrayList<String>();
		springFileLocations.add(getDefaultConfigPackagePath() + "ks-lum-"
				+ getRunMode().name().toLowerCase() + ".xml");
		return springFileLocations;
	}

}