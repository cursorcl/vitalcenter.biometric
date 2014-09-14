package vitalcenter.osgi.rs232.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kernel.osgi.MonitoringActivator;

import org.osgi.framework.ServiceReference;

import vitalcenter.osgi.rs232.status.validator.Rs232Validator;
import vitalcenter.osgi.status.api.IStatusValidatorManager;

public class Rs232Activator extends MonitoringActivator {

	private Map<String, Properties> serviceRequirements = null;
	private ServiceReference<?> reference = null;
	private IStatusValidatorManager statusValidatorMan;
	private Rs232Validator validator = null;

	public Rs232Activator() {
		// TODO Auto-generated constructor stub
		try {
			validator = new Rs232Validator();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected Map<String, Properties> getServiceRequirements() {
		if (serviceRequirements == null) {
			serviceRequirements = new HashMap<String, Properties>();
			serviceRequirements.put(IStatusValidatorManager.class.getName(), null);
		}
		return serviceRequirements;
	}

	protected void serviceRegistered(String name, ServiceReference<?> sref) {
		String rType = (String) sref.getProperty("type");
		if (rType == null) {
			// log.log(Level.WARNING,
			// "ERROR: arrived service with no space type");
			return;
		}
		if (rType.equalsIgnoreCase("IStatusValidatorManager")) {
			if (reference != null) {
				// log.info("ERROR: more than one IContainer active. Ignore.");
			} else {
				reference = sref;
				// log.info("[Location]: adding app IContainer");

				statusValidatorMan = (IStatusValidatorManager) getContext().getService(reference);
				statusValidatorMan.addValidator(validator);
			}
		}
	}

	protected void serviceUnregistered(String name, ServiceReference<?> sref) {
		String rType = (String) sref.getProperty("type");
		if (rType.equalsIgnoreCase("IStatusValidatorManager")) {
			statusValidatorMan.removeValidator(validator);
		}
	}

}
