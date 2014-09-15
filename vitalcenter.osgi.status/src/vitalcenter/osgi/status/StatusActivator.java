package vitalcenter.osgi.status;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import kernel.osgi.MonitoringActivator;
import kernel.osgi.api.space.IContainer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import cl.eos.interfaces.status.api.IStatusValidatorManager;
import vitalcenter.osgi.status.validator.StatusValidatorFactory;

public class StatusActivator extends MonitoringActivator {

	private ServiceRegistration<?> registration = null;
	private Map<String, Properties> serviceRequirements = null;
	private IStatusValidatorManager statusService = null;

	@Override
	protected Map<String, Properties> getServiceRequirements() {
		if (serviceRequirements == null) {
			serviceRequirements = new HashMap<String, Properties>();
			serviceRequirements.put(IContainer.class.getName(), null);
		}
		return serviceRequirements;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		statusService = StatusValidatorFactory.getInstance();
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put("type", "IStatusValidatorManager");
		registration = context.registerService(IStatusValidatorManager.class.getName(), statusService, props);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		if (registration != null)
			registration.unregister();

		if (statusService != null) {
			statusService.stop();
		}

	}
	
	@Override
	protected void serviceRegistered(String name, ServiceReference<?> sref) {
		String rType = (String) sref.getProperty("type");
		if (rType == null) {
			return;
		}
	}
	
	protected void serviceUnregistered(String name, ServiceReference<?> sref) {
	}



}
