package cl.eos.validate.controller;

import java.util.List;

import vitalcenter.osgi.persistence.ClientServiceFactory;
import vitalcenter.osgi.persistence.models.Client;
import vitalcenter.osgi.persistence.models.TemplateInfo;
import cl.eos.imp.controller.AController;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;

public class ValidateController extends AController {

	private List<Client> listTemplates;

	public ValidateController() {
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

}
