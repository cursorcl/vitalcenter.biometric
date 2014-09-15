package vitalcenter.osgi.validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kernel.osgi.MonitoringActivator;
import kernel.osgi.api.space.IContainer;
import kernel.osgi.api.space.IMainContainer;

import org.osgi.framework.ServiceReference;

import vitalcenter.osgi.digitalperson.status.validate.DPValidator;
import vitalcenter.osgi.persistence.models.IClientService;
import vitalcenter.osgi.rs232.IRS232Service;
import cl.eos.interfaces.status.api.IStatusValidatorManager;
import cl.eos.validate.controller.RegisterBikeController;
import cl.eos.validate.view.PnlRegisterBikes;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends MonitoringActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "vitalcenter.osgi.validate"; //$NON-NLS-1$

	private ServiceReference<?> containerRef = null;
	private ServiceReference<?> clientServiceRef = null;
	private ServiceReference<?> rs232ServiceRef = null;
	private ServiceReference<?> statusValidatorManagerServiceRef = null;
	private Map<String, Properties> serviceRequirements = null;
	private PnlRegisterBikes viewContainer;
	private IStatusValidatorManager statusValidatorManager = null;
	private IClientService clientService;
	private IContainer container;
	private IRS232Service rs232Service;
	private boolean initialized = false;
	private RegisterBikeController controller;

	@Override
	protected Map<String, Properties> getServiceRequirements() {
		if (serviceRequirements == null) {
			serviceRequirements = new HashMap<String, Properties>();
			serviceRequirements.put(IContainer.class.getName(), null);
			serviceRequirements.put(IClientService.class.getName(), null);
			serviceRequirements.put(IRS232Service.class.getName(), null);
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
		} else if (rType.equalsIgnoreCase("IRS232Service")) {
			rs232ServiceRef = sref;
			rs232Service = (IRS232Service) getContext().getService(rs232ServiceRef);
		} else if (rType.equalsIgnoreCase("IStatusValidatorManager")) {
			statusValidatorManagerServiceRef = sref;
			statusValidatorManager = (IStatusValidatorManager) getContext().getService(statusValidatorManagerServiceRef);
			statusValidatorManager.addValidator(new DPValidator());
		}
		if (!initialized && clientService != null && container != null && rs232Service != null) {
			controller = new RegisterBikeController();
			statusValidatorManager.addStatusEventListener(controller);
			viewContainer = new PnlRegisterBikes();
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
				viewContainer.setVisible(false);
				IMainContainer mainContainer = (IMainContainer) getContext().getService(containerRef);
				mainContainer.remove(viewContainer);
				containerRef = null;
			} else if (containerRef != null) {
			} else {
			}
		} else if (rType.equalsIgnoreCase("IClientService")) {
		} else {
		}
	}

}
