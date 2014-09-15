package vitalcenter.osgi.enroll;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kernel.osgi.MonitoringActivator;
import kernel.osgi.api.space.IContainer;
import kernel.osgi.api.space.IMainContainer;

import org.osgi.framework.ServiceReference;

import vitalcenter.osgi.persistence.models.IClientService;
import cl.eos.enroll.controller.EnrollController;
import cl.eos.enroll.validator.DPValidator;
import cl.eos.enroll.view.PnlEnroll;
import cl.eos.interfaces.status.api.IStatusValidatorManager;

public class Activator extends MonitoringActivator {

	private ServiceReference<?> containerRef = null;
	private ServiceReference<?> clientServiceRef = null;
	private ServiceReference<?> statusValidatorManagerServiceRef = null;
	private Map<String, Properties> serviceRequirements = null;
	private PnlEnroll viewContainer;
	private IClientService clientService;
	private IContainer container;
	private IStatusValidatorManager statusValidatorManager = null;
	private boolean initialized = false;
	private EnrollController controller;

	@Override
	protected Map<String, Properties> getServiceRequirements() {
		if (serviceRequirements == null) {
			serviceRequirements = new HashMap<String, Properties>();
			serviceRequirements.put(IContainer.class.getName(), null);
			serviceRequirements.put(IClientService.class.getName(), null);
			serviceRequirements.put(IStatusValidatorManager.class.getName(), null);
			
		}
		return serviceRequirements;
	}

	@Override
	protected void serviceRegistered(String name, ServiceReference<?> sref) {
		String rType = (String) sref.getProperty("type");
		if (rType == null) {
			return;
		}
		if (rType.equalsIgnoreCase("IContainer")) {
			if (containerRef != null) {
			} else {
				containerRef = sref;
				container = (IContainer) getContext().getService(containerRef);

			}
		} else if (rType.equalsIgnoreCase("IClientService")) {
			clientServiceRef = sref;
			clientService = (IClientService) getContext().getService(clientServiceRef);
		} else if (rType.equalsIgnoreCase("IStatusValidatorManager")) {
			statusValidatorManagerServiceRef = sref;
			statusValidatorManager = (IStatusValidatorManager) getContext().getService(statusValidatorManagerServiceRef);
			statusValidatorManager.addValidator(new DPValidator());
		} else {
		}
		if (!initialized && clientService != null && container != null && statusValidatorManager != null) {
			controller = new EnrollController();
			statusValidatorManager.addStatusEventListener(controller);
			viewContainer = new PnlEnroll();
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
