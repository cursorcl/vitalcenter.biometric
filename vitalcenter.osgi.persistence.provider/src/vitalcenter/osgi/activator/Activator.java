package vitalcenter.osgi.activator;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import vitalcenter.osgi.persistence.ClientServiceFactory;
import vitalcenter.osgi.persistence.models.IClientService;

public class Activator implements BundleActivator {
	private ServiceRegistration<?> registration = null;
	private IClientService clientService;

	@Override
	public void start(BundleContext context) throws Exception {

		clientService = ClientServiceFactory.getInstance();
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put("type", "IClientService");
		registration = context.registerService(IClientService.class.getName(), clientService, props);
	}	

	@Override
	public void stop(BundleContext arg0) throws Exception {
		if (registration != null)
			registration.unregister();

		if (clientService != null) {

		}

	}

}
