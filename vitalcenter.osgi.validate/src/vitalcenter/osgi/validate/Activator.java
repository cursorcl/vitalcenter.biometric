package vitalcenter.osgi.validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kernel.osgi.MonitoringActivator;
import kernel.osgi.api.space.IContainer;
import kernel.osgi.api.space.IMainContainer;

import org.osgi.framework.ServiceReference;

import vitalcenter.osgi.persistence.models.IClientService;
import vitalcenter.osgi.rs232.IRS232Service;
import cl.eos.validate.controller.ValidateController;
import cl.eos.validate.view.PnlValidate;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends MonitoringActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "vitalcenter.osgi.validate"; //$NON-NLS-1$

	private ServiceReference<?> containerRef = null;
	private ServiceReference<?> clientServiceRef = null;
	private ServiceReference<?> rs232ServiceRef = null;
	private Map<String, Properties> serviceRequirements = null;
	private PnlValidate viewContainer;
	private IClientService clientService;
	private IContainer container;
	private IRS232Service rs232Service;
	private boolean initialized = false;
	private ValidateController controller;

	@Override
	protected Map<String, Properties> getServiceRequirements() {
		if (serviceRequirements == null) {
			serviceRequirements = new HashMap<String, Properties>();
			serviceRequirements.put(IContainer.class.getName(), null);
			serviceRequirements.put(IClientService.class.getName(), null);
			serviceRequirements.put(IRS232Service.class.getName(), null);
		}
		return serviceRequirements;
	}

	@Override
	protected void serviceRegistered(String name, ServiceReference<?> sref) {
		String rType = (String) sref.getProperty("type");
		if (rType == null) {
			// log.log(Level.WARNING,
			// "ERROR: arrived service with no space type");
			return;
		}
		if (rType.equalsIgnoreCase("IContainer")) {
			if (containerRef != null) {
				// log.info("ERROR: more than one IContainer active. Ignore.");
			} else {
				containerRef = sref;
				// log.info("[Location]: adding app IContainer");
				container = (IContainer) getContext().getService(containerRef);
			}

		} else if (rType.equalsIgnoreCase("IClientService")) {
			clientServiceRef = sref;
			clientService = (IClientService) getContext().getService(clientServiceRef);
		} else if (rType.equalsIgnoreCase("IRS232Service")) {
			rs232ServiceRef = sref;
			rs232Service = (IRS232Service) getContext().getService(rs232ServiceRef);
		} else {
			// log.info(String.format(
			// "Unknown space service registering of type %s\n",
			// spaceType == null ? "unknown" : spaceType));
		}
		if (!initialized && clientService != null && container != null && rs232Service != null) {
			controller = new ValidateController();
			viewContainer = new PnlValidate();
			controller.addView(viewContainer);
			viewContainer.setService(clientService);
			container.getMainContainer().add(viewContainer);
		}
	}

	@Override
	protected void serviceUnregistered(String name, ServiceReference<?> sref) {
		String rType = (String) sref.getProperty("type");
		if (rType.equalsIgnoreCase("IContainer")) {
			if (sref.compareTo(containerRef) == 0) {
				// log.info("Location: GeographicSpace unregister detected, removing Location from space");
				viewContainer.setVisible(false);
				IMainContainer mainContainer = (IMainContainer) getContext().getService(containerRef);
				mainContainer.remove(viewContainer);
				containerRef = null;
			} else if (containerRef != null) {
				// log.info("ERROR: unregistered geographic space was gone. Ignore.");
			} else {
				// not current service
				// log.info("ERROR: another geographic space was gone. Ignore.");
			}
		} else if (rType.equalsIgnoreCase("IClientService")) {
			// Probablemente debo hacer read/only el panel.
		} else {
			// log.info(String.format(
			// "Unknown space service unregistering of type %s\n",
			// spaceType == null ? "unknown" : spaceType));
		}
	}

}
