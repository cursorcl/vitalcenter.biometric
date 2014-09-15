package cl.eos.validate.controller;

import java.util.List;

import vitalcenter.osgi.digitalperson.status.validate.DPValidator;
import vitalcenter.osgi.persistence.ClientServiceFactory;
import vitalcenter.osgi.persistence.models.Client;
import vitalcenter.osgi.persistence.models.TemplateInfo;
import vitalcenter.osgi.rs232.status.validator.Rs232Validator;
import cl.eos.imp.controller.AController;
import cl.eos.interfaces.status.api.EStatus;
import cl.eos.interfaces.status.api.IStatusEventListener;
import cl.eos.interfaces.status.api.StatusEvent;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;

public class RegisterBikeController extends AController implements IStatusEventListener{

	private List<Client> listTemplates;
	private EStatus oldDPStatus = null;
	private EStatus oldRs232Status;
	

	public RegisterBikeController() {
		listTemplates = ClientServiceFactory.getInstance().getTemplates();
		
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

	/**
	 * Valida contra la base de datos de clientes.
	 * 
	 * @param featureSet
	 *            La huella en si misma.
	 */
	public boolean validate(DPFPFeatureSet featureSet) {
		Client cliente = null;
		boolean isValid = false;
		final DPFPVerification verification = DPFPGlobal.getVerificationFactory().createVerification(
				DPFPVerification.MEDIUM_SECURITY_FAR);
		DPFPVerificationResult result = null;
		for (Client lClient : listTemplates) {
			DPFPTemplate template = DPFPGlobal.getTemplateFactory().createTemplate();
			template.deserialize(lClient.getTemplate().getTemplate());
			result = verification.verify(featureSet, template);
			if (result.isVerified()) {
				cliente = ClientServiceFactory.getInstance().getClient(lClient.getRut());
				cliente.setTemplate(lClient.getTemplate());
				isValid = true;
				break;
			}
		}
		notifyFound(cliente);
		return isValid;
	}
	
	@Override
	public void onStatusEvent(StatusEvent event) {
		if (event.getSource() instanceof DPValidator) {
			if (event.getStatus() != oldDPStatus) {
				notifyChangeStatus(event);
				oldDPStatus = event.getStatus();
			}
		}
		else if(event.getSource() instanceof Rs232Validator)
		{
			if (event.getStatus() != oldRs232Status) {
				notifyChangeStatus(event);
				oldRs232Status = event.getStatus();
			}
		}

	}

}
