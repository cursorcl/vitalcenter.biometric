package cl.eos.enroll.controller;

import vitalcenter.osgi.persistence.ClientServiceFactory;
import vitalcenter.osgi.persistence.models.Client;
import vitalcenter.osgi.persistence.models.TemplateInfo;
import vitalcenter.osgi.status.api.IStatusEventListener;
import vitalcenter.osgi.status.api.StatusEvent;
import cl.eos.enroll.model.ClientModel;
import cl.eos.enroll.validator.DPValidator;
import cl.eos.imp.controller.AController;

public class EnrollController extends AController implements IStatusEventListener {

	private Object oldStatus = null;

	public EnrollController() {
		model = new ClientModel();
	}

	public void findRut(String rut) {
		if (rut != null) {
			Client client = ClientServiceFactory.getInstance().getClient(rut);
			if (client != null) {

				selectedEntity = client;
			}
			notifyFound(client);
		}
	}

	public TemplateInfo findTemplate(String rut) {
		TemplateInfo result = null;
		return result;
	}

	@Override
	public void onStatusEvent(StatusEvent event) {
		if (event.getSource() instanceof DPValidator) {
			if (event.getStatus() != oldStatus) {
				notifyChangeStatus(event);
				oldStatus = event.getStatus();
			}
		}

	}

}
