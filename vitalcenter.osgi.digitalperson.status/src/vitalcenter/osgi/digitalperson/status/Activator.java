package vitalcenter.osgi.digitalperson.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kernel.osgi.MonitoringActivator;

import org.osgi.framework.ServiceReference;

import vitalcenter.osgi.digitalperson.status.validate.DPValidator;
import cl.eos.interfaces.status.api.IStatusValidatorManager;


public class Activator extends MonitoringActivator {

	private Map<String, Properties> serviceRequirements = null;
	private ServiceReference<?> reference = null;
	private IStatusValidatorManager statusValidatorMan;
	private DPValidator validator =  new DPValidator();
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
		if (rType.equalsIgnoreCase("IStatusValidatorManager"))
		{
			statusValidatorMan.removeValidator(validator);
		}
	}
}
